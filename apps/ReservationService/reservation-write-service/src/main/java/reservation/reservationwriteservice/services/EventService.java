package reservation.reservationwriteservice.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import reservation.AddReservationEvent;
import reservation.BaseEvent;
import reservation.RemoveReservationEvent;
import reservation.costcalculator.CostCalculator;
import reservation.costcalculator.CostCalculatorImpl;
import reservation.reservationwriteservice.models.AddReservation;
import reservation.reservationwriteservice.models.RemoveReservation;
import reservation.reservationwriteservice.repositories.EventRepository;

import java.time.Instant;
import java.util.UUID;

@Service
public class EventService {

    Logger log = LogManager.getLogger(EventService.class);

    private final EventRepository eventRepository;

    private final CostCalculator costCalculator = new CostCalculatorImpl();

    private final MessageService messageService;

    public EventService(EventRepository eventRepository, MessageService messageService) {
        this.eventRepository = eventRepository;
        this.messageService = messageService;
    }

    public void addNewReservation(AddReservation addReservation) {
        log.info("New reservation from user: " + addReservation.username());
        var cost = costCalculator.calculateOfferCost(null, null, null);
        var addReservationEvent = AddReservationEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .ageGroupsSize(addReservation.ageGroupsSize())
                .cost(cost)
                .transportId(addReservation.transportId())
                .username(addReservation.username())
                .roomReservationId(addReservation.roomReservationId())
                .timestamp(Instant.now())
                .build();

        saveEventToNoSql(addReservationEvent);

        sendEvent(addReservationEvent);
    }

    public void removeReservation(RemoveReservation removeReservation) {
        log.info("New remove reservation id: " + removeReservation.removedReservationId());
        var removeReservationEvent = RemoveReservationEvent.builder()
                .removedReservationId(removeReservation.removedReservationId())
                .username(removeReservation.username())
                .timestamp(Instant.now())
                .eventId(UUID.randomUUID().toString())
                .build();

        saveEventToNoSql(removeReservationEvent);

        sendEvent(removeReservationEvent);
    }

    private void saveEventToNoSql(BaseEvent baseEvent) {
        eventRepository.insert(baseEvent);
        log.info("Event id: " + baseEvent.getEventId() + " inserted to db");
    }

    private void sendEvent(BaseEvent baseEvent) {
        messageService.sendEvent(baseEvent);
    }
}
