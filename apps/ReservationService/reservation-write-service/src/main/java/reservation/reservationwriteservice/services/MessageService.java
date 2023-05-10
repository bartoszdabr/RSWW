package reservation.reservationwriteservice.services;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reservation.BaseEvent;

@Service
public class MessageService {

    Logger log = LogManager.getLogger(MessageService.class);

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.event.queueName}")
    private String eventQueueName;

    private final String DEFAULT_EXCHANGE_NAME = "";

    public MessageService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendEvent(BaseEvent event) {
        log.info("Sending message to bus");
        rabbitTemplate.convertAndSend(DEFAULT_EXCHANGE_NAME, eventQueueName, event);
        log.info("Sending to message bus succeeded");
    }
}
