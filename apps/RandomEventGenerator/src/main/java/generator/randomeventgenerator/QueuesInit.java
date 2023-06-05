package generator.randomeventgenerator;

import jakarta.annotation.PostConstruct;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueuesInit {

    @Value("${rabbitmq.payment.queueName}")
    private String paymentQueueName;

    @Value("${rabbitmq.reservation.queueName}")
    private String reservationQueueName;

    @Value("${rabbitmq.hotel.queueName}")
    private String hotelQueueName;

    @Value("${rabbitmq.transport.queueName}")
    private String transportQueueName;

    private final AmqpAdmin amqpAdmin;

    public QueuesInit(AmqpAdmin amqpAdmin) {
        this.amqpAdmin = amqpAdmin;
    }

    @PostConstruct
    public void createQueues() {
        var isDurable = true;
        amqpAdmin.declareQueue(new Queue(paymentQueueName, isDurable));
        amqpAdmin.declareQueue(new Queue(reservationQueueName, isDurable));
        amqpAdmin.declareQueue(new Queue(hotelQueueName, isDurable));
        amqpAdmin.declareQueue(new Queue(transportQueueName, isDurable));
    }
}