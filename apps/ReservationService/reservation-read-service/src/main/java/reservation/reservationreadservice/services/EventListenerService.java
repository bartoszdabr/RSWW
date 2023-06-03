package reservation.reservationreadservice.services;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import reservation.events.ReservationEvent;
import reservation.events.ReservationStatuses;

@Service
public class EventListenerService {

    Logger log = LogManager.getLogger(EventListenerService.class);

    private final EventHandlerService eventHandlerService;

    public EventListenerService(EventHandlerService eventHandlerService) {
        this.eventHandlerService = eventHandlerService;
    }


    @RabbitListener(queues = "${rabbitmq.event.queueName}")
    public void receivedMessage(ReservationEvent reservationEvent) {
        log.info("Received new event id:" + reservationEvent.getEventId());
        if (reservationEvent.getStatus().equals(ReservationStatuses.NEW.name())) {
            eventHandlerService.handleAddReservationEvent(reservationEvent);
        } else if (reservationEvent.getStatus().equals(ReservationStatuses.REMOVED.name())) {
            eventHandlerService.handleRemoveReservationEvent(reservationEvent);
        }
        log.info("Finished handling event id: " + reservationEvent.getEventId());
    }
}
