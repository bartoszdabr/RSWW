package reservation.reservationreadservice.services;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import reservation.AddReservationEvent;
import reservation.BaseEvent;
import reservation.RemoveReservationEvent;

@Service
public class EventListenerService {

    Logger log = LogManager.getLogger(EventListenerService.class);

    private final EventHandlerService eventHandlerService;

    public EventListenerService(EventHandlerService eventHandlerService) {
        this.eventHandlerService = eventHandlerService;
    }


    @RabbitListener(queues = "${rabbitmq.event.queueName}")
    public void receivedMessage(BaseEvent baseEvent) {
        log.info("Received new event id:" + baseEvent.getEventId());
        if (baseEvent instanceof AddReservationEvent) {
            eventHandlerService.handleAddReservationEvent((AddReservationEvent) baseEvent);
        } else if (baseEvent instanceof RemoveReservationEvent) {
            eventHandlerService.handleRemoveReservationEvent((RemoveReservationEvent) baseEvent);
        }
        log.info("Finished handling event id: " + baseEvent.getEventId());
    }
}
