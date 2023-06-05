package transport.write.services;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import transport.common.BaseEvent;

@Service
@RequiredArgsConstructor
public class MessageService {

  private final Logger log = LogManager.getLogger(this.getClass());

  private final RabbitTemplate rabbitTemplate;
  private final String DEFAULT_EXCHANGE_NAME = "";

  @Value("${rabbitmq.event.queueName}")
  private String transportEventQueueName;

  @Value("${rabbitmq.reservation.queueName}")
  private String reservationQueueName;

  public void sendEvent(BaseEvent event) {
    log.info("Sending transport event to bus");
    rabbitTemplate.convertAndSend(DEFAULT_EXCHANGE_NAME, transportEventQueueName, event);
    log.info("Sending transport event to bus succeeded");
  }

  public void sendResponse(String response) {
    log.info("Sending transport reservation status to bus");
    rabbitTemplate.convertAndSend(DEFAULT_EXCHANGE_NAME, reservationQueueName, response,  r -> {
      r.getMessageProperties().getHeaders().put("content_type", "application/json");
      r.getMessageProperties().setContentEncoding("application/json");
      r.getMessageProperties().setContentType("application/json");
      return r;
    });
    log.info("Sending transport reservation status to bus succeeded");
  }
}
