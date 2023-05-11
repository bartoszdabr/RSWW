package read.messaging;

import jakarta.annotation.PostConstruct;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueuesInitializer {

    @Value("${rabbitmq.payment.queueName}")
    private String paymentQueueName;

    @Value("${rabbitmq.reservation.queueName}")
    private String reservationQueueName;

    private final AmqpAdmin amqpAdmin;

    public QueuesInitializer(AmqpAdmin amqpAdmin) {
        this.amqpAdmin = amqpAdmin;
    }

    @PostConstruct
    public void createQueues() {
        var isDurable = true;
        amqpAdmin.declareQueue(new Queue(paymentQueueName, isDurable));
        amqpAdmin.declareQueue(new Queue(reservationQueueName, isDurable));
    }
}