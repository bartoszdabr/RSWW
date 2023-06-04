package reservation.reservationwriteservice.services;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reservation.events.*;
import reservation.reservationwriteservice.repositories.EventRepository;

import java.time.Instant;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class SagaRollbackService {

    private final Logger log = LoggerFactory.getLogger(SagaRollbackService.class);

    private final EventRepository eventRepository;

    private final MessageService messageService;

    public void rollbackSagaById(String reservationId) {
        rollbackSaga(reservationId, getAllReservationEventsSorted(reservationId));
    }

    public void rollbackSaga(String reservationId, List<ReservationEvent> events) {
        log.info("Starting rollback of reservation: " + reservationId);

        var latestSagaStep = events.stream()
                .sorted(Comparator.comparing(ReservationEvent::getTimestamp).reversed())
                .map(event -> ReservationStatuses.fromText(event.getStatus()))
                .filter(status -> Arrays.asList(ReservationStatuses.values()).contains(status))
                .findFirst()
                .orElseThrow();
        var originalReservation = getOriginalEvent(events);

        switch (latestSagaStep) {
            case NEW -> rollbackReservation(originalReservation);
            case HOTEL_CONFIRMED -> {
                rollbackReservation(originalReservation);
                rollbackHotel(originalReservation);
            }
            case TRANSPORT_CONFIRMED -> {
                rollbackReservation(originalReservation);
                rollbackHotel(originalReservation);
                rollbackTransport(originalReservation);
            }
        }
        log.info("Rollback finished for reservation: " + reservationId);
    }

    private void rollbackHotel(ReservationEvent reservationEvent) {
        log.info("Rolling back hotel reservation " + reservationEvent.getReservationId());
        var reserveHotelEvent = ReserveHotelEvent
                .builder()
                .eventId(reservationEvent.getReservationId())
                .username(reservationEvent.getUsername())
                .roomReservationId(reservationEvent.getRoomReservationId())
                .numOfPeople(reservationEvent.getNumOfPeople())
                .type(ReservationType.CANCEL.getText())
                .build();

        messageService.sendEventToHotel(reserveHotelEvent);
        log.info("Finished rolling back hotel reservation " + reservationEvent.getReservationId());
    }

    private void rollbackTransport(ReservationEvent reservationEvent) {
        log.info("Rolling back transport reservation " + reservationEvent.getReservationId());
        var removeTransportReservationEvent = ReserveTransportEvent.builder()
                .reservationId(reservationEvent.getReservationId())
                .transportId(reservationEvent.getTransportId())
                .username(reservationEvent.getUsername())
                .type(ReservationType.CANCEL.getText())
                .numOfPeople(reservationEvent.getNumOfPeople())
                .build();

        messageService.sendEventToTransport(removeTransportReservationEvent);
        log.info("Finished rolling back transport reservation " + reservationEvent.getReservationId());
    }

    private void rollbackReservation(ReservationEvent reservationEvent) {
        reservationEvent.setEventId(UUID.randomUUID().toString());
        reservationEvent.setTimestamp(Instant.now());
        reservationEvent.setStatus(ReservationStatuses.REMOVED.getText());

        eventRepository.insert(reservationEvent);
        messageService.sendEventToReservationRead(reservationEvent);
    }

    private ReservationEvent getOriginalEvent(List<ReservationEvent> events) {
        return events.stream()
                .filter(event -> event.getStatus().equals(ReservationStatuses.NEW.getText()))
                .findFirst()
                .orElseThrow();
    }

    private List<ReservationEvent> getAllReservationEventsSorted(String reservationId) {
        return eventRepository.findReservationEventsByReservationIdOrderByTimestamp(
                reservationId, Sort.by(Sort.Direction.ASC, "timestamp"));
    }
}
