package reservation.reservationwriteservice.services;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reservation.events.*;
import reservation.reservationwriteservice.exceptions.EventNotFoundException;
import reservation.reservationwriteservice.exceptions.RollbackException;
import reservation.reservationwriteservice.exceptions.StatusNotKnownException;
import reservation.reservationwriteservice.models.SagaResponse;
import reservation.reservationwriteservice.repositories.EventRepository;
import reservation.reservationwriteservice.saga.SagaResponseStatuses;
import reservation.reservationwriteservice.saga.TimeoutTask;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class SagaService {

    private final Logger log = LogManager.getLogger(SagaService.class);

    private final int SAGA_TIMEOUT_MINUTES = 1;

    private final TimeoutTask timeoutTask;

    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    private final MessageService messageService;

    private final EventRepository eventRepository;

    private final SagaRollbackService sagaRollbackService;

    public void startSaga(ReservationEvent reservationEvent) {
        log.info("Starting saga for id: " + reservationEvent.getReservationId());
        initHotelStep(reservationEvent);
        addSagaTimeout(reservationEvent.getReservationId());
    }

    private void addSagaTimeout(String eventId) {
        executorService.schedule(() -> timeoutTask.checkIfReservationTimeouted(eventId),
                SAGA_TIMEOUT_MINUTES, TimeUnit.MINUTES);
    }

    public void processSagaStep(SagaResponse sagaResponse) throws EventNotFoundException, RollbackException, StatusNotKnownException {
        var events = getAllReservationEventsSorted(sagaResponse.getReservationId());
        var originalEvent = getOriginalEvent(events);
        var latestEvent = events.get(events.size() -1);

        try {
            validateSagaEvents(events, sagaResponse.getReservationId(), sagaResponse.getStatus());
        } catch (RollbackException rollbackException) {
            sagaRollbackService.rollbackSaga(originalEvent.getReservationId(), events);
            throw rollbackException;
        }


        switch (ReservationStatuses.fromText(latestEvent.getStatus())) {
            case NEW -> {
                updateAfterHotelConfirmation(sagaResponse.getReservationId());
                initTransportStep(originalEvent);
            }
            case HOTEL_CONFIRMED -> {
                updateAfterTransportConfirmation(sagaResponse.getReservationId());
                initPaymentStep(originalEvent);
            }
            case TRANSPORT_CONFIRMED -> {
                updateAfterPaymentConfirmation(sagaResponse.getReservationId());
            }
            default -> throw new StatusNotKnownException("Not known status for event");
        }

    }

    private ReservationEvent getOriginalEvent(List<ReservationEvent> events) {
        return events.stream()
                .filter(event -> event.getStatus().equals(ReservationStatuses.NEW.getText()))
                .findFirst()
                .orElseThrow();
    }

    private void initPaymentStep(ReservationEvent originalEvent) {
        var processPaymentEvent = ProcessPaymentEvent.builder()
                .reservationId(originalEvent.getReservationId())
                .cost(originalEvent.getCost())
                .build();

        messageService.sendEventToPayment(processPaymentEvent);
    }

    private void initTransportStep(ReservationEvent reservationEvent) {
        var reserveTransportEvent = ReserveTransportEvent.builder()
                .reservationId(reservationEvent.getReservationId())
                .username(reservationEvent.getUsername())
                .transportId(reservationEvent.getTransportId())
                .numOfPeople(reservationEvent.getNumOfPeople())
                .type(ReservationType.ADD.getText())
                .build();

        messageService.sendEventToTransport(reserveTransportEvent);
    }

    private void initHotelStep(ReservationEvent reservationEvent) {
        var reserveHotelEvent = ReserveHotelEvent
                .builder()
                .eventId(reservationEvent.getReservationId())
                .username(reservationEvent.getUsername())
                .roomReservationId(reservationEvent.getRoomReservationId())
                .ageGroups(reservationEvent.getAgeGroupsSize())
                .type(ReservationType.ADD.getText())
                .build();
        messageService.sendEventToHotel(reserveHotelEvent);
    }

    private void updateAfterPaymentConfirmation(String reservationId) {
        var event = insertAndCreateEventWithIdAndStatus(reservationId, ReservationStatuses.RESERVED.getText());
        messageService.sendEventToReservationRead(event);
    }

    private void updateAfterTransportConfirmation(String reservationId) {
        insertAndCreateEventWithIdAndStatus(reservationId, ReservationStatuses.TRANSPORT_CONFIRMED.getText());
    }

    private void updateAfterHotelConfirmation(String reservationId) {
        insertAndCreateEventWithIdAndStatus(reservationId, ReservationStatuses.HOTEL_CONFIRMED.getText());
    }

    private ReservationEvent insertAndCreateEventWithIdAndStatus(String reservationId, String status) {
        var event = ReservationEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .timestamp(Instant.now())
                .status(status)
                .reservationId(reservationId)
                .build();
        eventRepository.insert(event);

        return event;
    }

    private void validateSagaEvents(List<ReservationEvent> reservationEventList, String reservationId, String sagaResponseStatus)
            throws EventNotFoundException, RollbackException {

        if (reservationEventList.isEmpty()) {
            throw new EventNotFoundException("No reservation events for id: " + reservationId);
        }

        var shouldRollback = reservationEventList.stream().anyMatch(event -> event.getStatus().equals(ReservationStatuses.REMOVED.name()))
                || sagaResponseStatus.equals(SagaResponseStatuses.FAILURE.getText());
        if (shouldRollback) {
            throw new RollbackException("Rollback started for id: " + reservationId);
        }

    }

    private List<ReservationEvent> getAllReservationEventsSorted(String reservationId) {
        return eventRepository.findReservationEventsByReservationIdOrderByTimestamp(
                reservationId, Sort.by(Sort.Direction.ASC, "timestamp"));
    }
}
