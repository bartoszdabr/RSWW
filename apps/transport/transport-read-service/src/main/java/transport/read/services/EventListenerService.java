package transport.read.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import transport.common.AddReservationEvent;
import transport.common.BaseEvent;
import transport.common.CancelReservationEvent;
import transport.common.CreateTransportEvent;

@Service
public class EventListenerService {
  private final Logger log = LogManager.getLogger(EventListenerService.class);

  private final TransportService transportService;

  public EventListenerService(TransportService transportService) {
    this.transportService = transportService;
  }

  @RabbitListener(queues = "${rabbitmq.event.queueName}")
  public void receivedMessage(BaseEvent baseEvent) {
    // TODO: Refactor this method
    log.info("Received new transport event id:" + baseEvent.getEventId());
    if (baseEvent instanceof AddReservationEvent) {
      transportService.addNewReservation((AddReservationEvent) baseEvent);
    } else if (baseEvent instanceof CancelReservationEvent) {
      transportService.cancelReservation((CancelReservationEvent) baseEvent);
    } else if (baseEvent instanceof CreateTransportEvent) {
      transportService.addNewTransport((CreateTransportEvent) baseEvent);
    }
  }
}
