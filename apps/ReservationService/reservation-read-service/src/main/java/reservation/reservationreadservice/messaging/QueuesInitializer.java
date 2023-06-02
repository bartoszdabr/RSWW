package reservation.reservationreadservice.messaging;

import jakarta.annotation.PostConstruct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueuesInitializer {

    private final Logger log = LogManager.getLogger(QueuesInitializer.class);

    @Value("${rabbitmq.transport.queueName}")
    private String transportQueueName;

    @Value("${rabbitmq.hotel.queueName}")
    private String hotelQueueName;

    @Value("${rabbitmq.payment.queueName}")
    private String paymentQueueName;

    @Value("${rabbitmq.event.queueName}")
    private String eventsQueue;

    private final AmqpAdmin amqpAdmin;

    public QueuesInitializer(AmqpAdmin amqpAdmin) {
        this.amqpAdmin = amqpAdmin;
    }

    @PostConstruct
    public void createQueues() {
        var isDurable = true;
        log.info("Initializing queues.");
        log.info("Initializing " + transportQueueName);
        amqpAdmin.declareQueue(new Queue(transportQueueName, isDurable));
        log.info("Initializing " + hotelQueueName);
        amqpAdmin.declareQueue(new Queue(hotelQueueName, isDurable));
        log.info("Initializing " + paymentQueueName);
        amqpAdmin.declareQueue(new Queue(paymentQueueName, isDurable));
        log.info("Initializing " + eventsQueue);
        amqpAdmin.declareQueue(new Queue(eventsQueue, isDurable));
        log.info("Finished initializing queues.");
    }
}
