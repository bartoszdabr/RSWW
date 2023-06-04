package transport.write.messaging;

import jakarta.annotation.PostConstruct;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class QueuesInitializer {
  @Value("${rabbitmq.event.queueName}")
  private String eventQueueName;

  @Value("${rabbitmq.reservation.queueName}")
  private String reservationQueueName;

  @Value("${rabbitmq.transport.queueName}")
  private String transportQueueName;

  private final AmqpAdmin amqpAdmin;

  public QueuesInitializer(AmqpAdmin amqpAdmin) {
    this.amqpAdmin = amqpAdmin;
  }

  @PostConstruct
  public void createQueues() {
    boolean isDurable = true;
    amqpAdmin.declareQueue(new Queue(eventQueueName, isDurable));
    amqpAdmin.declareQueue(new Queue(reservationQueueName, isDurable));
    amqpAdmin.declareQueue(new Queue(transportQueueName, isDurable));
  }

  @Bean
  public MessageConverter jsonMessageConverter(Jackson2ObjectMapperBuilder builder) {
    var objectMapper = builder.createXmlMapper(false).build();
    objectMapper.findAndRegisterModules();
    return new Jackson2JsonMessageConverter(objectMapper);
  }
}
