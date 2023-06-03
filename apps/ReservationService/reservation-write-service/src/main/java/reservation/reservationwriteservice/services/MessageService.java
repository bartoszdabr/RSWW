package reservation.reservationwriteservice.services;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reservation.events.ProcessPaymentEvent;
import reservation.events.ReservationEvent;
import reservation.events.ReserveHotelEvent;
import reservation.events.ReserveTransportEvent;

@Service
public class MessageService {

    Logger log = LogManager.getLogger(MessageService.class);

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.event.queueName}")
    private String eventQueueName;

    @Value("${rabbitmq.transport.queueName}")
    private String transportQueueName;

    @Value("${rabbitmq.hotel.queueName}")
    private String hotelQueueName;

    @Value("${rabbitmq.payment.queueName}")
    private String paymentQueueName;

    private final String DEFAULT_EXCHANGE_NAME = "";

    public MessageService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendEventToReservationRead(ReservationEvent event) {
        log.info("Sending message to " + eventQueueName);
        rabbitTemplate.convertAndSend(DEFAULT_EXCHANGE_NAME, eventQueueName, event);
        log.info("Sending message succeeded");
    }

    public void sendEventToHotel(ReserveHotelEvent event) {
        log.info("Sending message to " + hotelQueueName);
        rabbitTemplate.convertAndSend(DEFAULT_EXCHANGE_NAME, hotelQueueName, event);
        log.info("Sending message succeeded");
    }

    public void sendEventToTransport(ReserveTransportEvent event) {
        log.info("Sending message to " + transportQueueName);
        rabbitTemplate.convertAndSend(DEFAULT_EXCHANGE_NAME, transportQueueName, event);
        log.info("Sending message succeeded");
    }

    public void sendEventToPayment(ProcessPaymentEvent event) {
        log.info("Sending message to " + paymentQueueName);
        rabbitTemplate.convertAndSend(DEFAULT_EXCHANGE_NAME, paymentQueueName, event);
        log.info("Sending message succeeded");
    }
}
