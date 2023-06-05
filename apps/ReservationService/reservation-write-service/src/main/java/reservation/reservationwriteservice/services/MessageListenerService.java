package reservation.reservationwriteservice.services;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import reservation.reservationwriteservice.exceptions.EventNotFoundException;
import reservation.reservationwriteservice.exceptions.RollbackException;
import reservation.reservationwriteservice.exceptions.StatusNotKnownException;
import reservation.reservationwriteservice.models.SagaResponse;

@Service
@AllArgsConstructor
public class MessageListenerService {

    private final Logger log = LogManager.getLogger(MessageListenerService.class);

    private final SagaService sagaService;

    @RabbitListener(queues = "${rabbitmq.reservation.queueName}")
    public void receivedMessage(SagaResponse sagaResponse) {
        log.info("Received new message: %s".formatted(sagaResponse));
        try {
            sagaService.processSagaStep(sagaResponse);
        } catch (EventNotFoundException | RollbackException | StatusNotKnownException | RuntimeException exception) {
            log.error(exception.getMessage());
        }

    }
}
