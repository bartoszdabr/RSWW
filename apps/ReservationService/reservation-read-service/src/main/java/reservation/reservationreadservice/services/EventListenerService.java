package reservation.reservationreadservice.services;


import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import reservation.events.ReservationEvent;

@Service
@RequiredArgsConstructor
public class EventListenerService {

    private final Logger log = LogManager.getLogger(EventListenerService.class);

    private final EventHandlerService eventHandlerService;

    @RabbitListener(queues = "${rabbitmq.event.queueName}")
    public void receivedMessage(ReservationEvent reservationEvent) {
        log.info("Received new event id:" + reservationEvent.getEventId() +
                " reservation: " + reservationEvent.getReservationId());
        eventHandlerService.handleAddReservationEvent(reservationEvent);
        log.info("Finished handling event id: " + reservationEvent.getEventId() +
                " for reservation: " + reservationEvent.getReservationId());
    }
}
