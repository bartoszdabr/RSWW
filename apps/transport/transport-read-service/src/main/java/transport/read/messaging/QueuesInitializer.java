package transport.read.messaging;

import jakarta.annotation.PostConstruct;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueuesInitializer {
  @Value("${rabbitmq.event.queueName}")
  private String eventQueueName;

  private final AmqpAdmin amqpAdmin;

  public QueuesInitializer(AmqpAdmin amqpAdmin) {
    this.amqpAdmin = amqpAdmin;
  }

  @PostConstruct
  public void createQueues() {
    boolean isDurable = true;
    amqpAdmin.declareQueue(new Queue(eventQueueName, isDurable));
  }
}
