package reservation.reservationwriteservice.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class MessageListenerService {

    private final Logger log = LogManager.getLogger(MessageListenerService.class);

    @RabbitListener(queues = "${rabbitmq.reservation.queueName}")
    public void receivedMessage(String message) {
        log.info("Received new message: %s".formatted(message));
    }
}
