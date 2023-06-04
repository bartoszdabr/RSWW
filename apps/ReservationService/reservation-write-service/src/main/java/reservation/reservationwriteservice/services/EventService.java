package reservation.reservationwriteservice.services;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import reservation.events.ReservationEvent;
import reservation.costcalculator.CostCalculator;
import reservation.costcalculator.CostCalculatorImpl;
import reservation.reservationwriteservice.models.AddReservation;
import reservation.reservationwriteservice.models.RemoveReservation;
import reservation.reservationwriteservice.repositories.EventRepository;
import reservation.events.ReservationStatuses;

import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
public class EventService {

    private final Logger log = LogManager.getLogger(EventService.class);

    private final EventRepository eventRepository;

    private final CostCalculator costCalculator = new CostCalculatorImpl();

    private final MessageService messageService;

    private final SagaService sagaService;

    public void addNewReservation(AddReservation addReservation) {
        log.info("New reservation from user: " + addReservation.username());
        var cost = costCalculator.calculateOfferCost(addReservation.ageGroupsSize(), addReservation.numOfDays());
        var reservationEvent = ReservationEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .ageGroupsSize(addReservation.ageGroupsSize())
                .cost(cost)
                .transportId(addReservation.transportId())
                .username(addReservation.username())
                .roomReservationId(addReservation.roomReservationId())
                .reservationId(UUID.randomUUID().toString())
                .timestamp(Instant.now())
                .status(ReservationStatuses.NEW.getText())
                .build();

        saveEventToNoSql(reservationEvent);

        sendEventToReservationRead(reservationEvent);

        sagaService.startSaga(reservationEvent);
    }

    private void saveEventToNoSql(ReservationEvent event) {
        eventRepository.insert(event);
        log.info("Event id: " + event.getEventId() + " inserted to db");
    }

    private void sendEventToReservationRead(ReservationEvent event) {
        messageService.sendEventToReservationRead(event);
    }
}
