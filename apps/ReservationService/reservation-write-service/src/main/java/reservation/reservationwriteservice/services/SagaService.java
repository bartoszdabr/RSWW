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

    public void processSagaStep(SagaResponse sagaResponse) throws EventNotFoundException,
            RollbackException, StatusNotKnownException, RuntimeException {

        var events = getAllReservationEventsSorted(sagaResponse.getReservationId());
        if (events.isEmpty()) {
            throw new RuntimeException("Reservation not found");
        }
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
                updateAfterHotelConfirmation(originalEvent);
                initTransportStep(originalEvent);
            }
            case HOTEL_CONFIRMED -> {
                updateAfterTransportConfirmation(originalEvent);
            }
            case TRANSPORT_CONFIRMED -> updateAfterPaymentConfirmation(originalEvent);
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
                .numOfPeople(reservationEvent.getNumOfPeople())
                .type(ReservationType.ADD.getText())
                .build();
        messageService.sendEventToHotel(reserveHotelEvent);
    }

    private void updateAfterPaymentConfirmation(ReservationEvent reservationEvent) {
        var event = insertAndCreateEventWithIdAndStatus(reservationEvent, ReservationStatuses.RESERVED.getText());
        messageService.sendEventToReservationRead(event);
    }

    private void updateAfterTransportConfirmation(ReservationEvent reservationEvent) {
        var event = insertAndCreateEventWithIdAndStatus(reservationEvent, ReservationStatuses.TRANSPORT_CONFIRMED.getText());
        messageService.sendEventToReservationRead(event);
    }

    private void updateAfterHotelConfirmation(ReservationEvent reservationEvent) {
        var event = insertAndCreateEventWithIdAndStatus(reservationEvent, ReservationStatuses.HOTEL_CONFIRMED.getText());
        messageService.sendEventToReservationRead(event);
    }

    private ReservationEvent insertAndCreateEventWithIdAndStatus(ReservationEvent reservationEvent, String status) {
        reservationEvent.setStatus(status);
        reservationEvent.setEventId(UUID.randomUUID().toString());
        reservationEvent.setTimestamp(Instant.now());
        eventRepository.insert(reservationEvent);

        return reservationEvent;
    }

    private void validateSagaEvents(List<ReservationEvent> reservationEventList, String reservationId, String sagaResponseStatus)
            throws EventNotFoundException, RollbackException {

        if (reservationEventList.isEmpty()) {
            throw new EventNotFoundException("No reservation events for id: " + reservationId);
        }

        var shouldRollback = reservationEventList.stream().anyMatch(event -> event.getStatus().equals(ReservationStatuses.REMOVED.getText()))
                || sagaResponseStatus.equals(SagaResponseStatuses.FAILURE.getText());
        if (shouldRollback) {
            throw new RollbackException("Rollback started for id: " + reservationId);
        }

    }

    private List<ReservationEvent> getAllReservationEventsSorted(String reservationId) {
        return eventRepository.findReservationEventsByReservationIdOrderByTimestamp(
                reservationId, Sort.by(Sort.Direction.ASC, "timestamp"));
    }

    public void processPaymentForReservation(String reservationId) {
        var event = eventRepository.findFirstByReservationIdOrderByTimestampAsc(reservationId);
        initPaymentStep(event
                .orElseThrow(() -> new RuntimeException("No reservation with id: " + reservationId)));
    }
}
