package transport.write.messaging;

import jakarta.annotation.PostConstruct;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class QueuesInitializer {
  @Value("${rabbitmq.event.queueName}")
  private String eventQueueName;

  @Value("${rabbitmq.reservation.queueName}")
  private String reservationQueueName;

  private final AmqpAdmin amqpAdmin;

  public QueuesInitializer(AmqpAdmin amqpAdmin) {
    this.amqpAdmin = amqpAdmin;
  }

  @PostConstruct
  public void createQueues() {
    boolean isDurable = true;
    amqpAdmin.declareQueue(new Queue(eventQueueName, isDurable));
    amqpAdmin.declareQueue(new Queue(reservationQueueName, isDurable));
  }
}
