package reservation.reservationwriteservice.controllers;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PresentationController {

    Logger log = LogManager.getLogger(PresentationController.class);

    private final String DEFAULT_EXCHANGE_NAME = "";

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.transport.queueName}")
    private String transportQueueName;

    @Value("${rabbitmq.hotel.queueName}")
    private String hotelQueueName;

    @Value("${rabbitmq.payment.queueName}")
    private String paymentQueueName;

    @GetMapping("test/transport")
    public ResponseEntity<Void> testTransportQueueCommunication() {
        log.info("Sending message to " + transportQueueName);
        rabbitTemplate.convertAndSend(DEFAULT_EXCHANGE_NAME, transportQueueName, "{\n" +
                "\"id\":\"reservationId\",\n" +
                "\"transportId\":\"transportId\",\n" +
                "\"userName\":\"abc\",\n" +
                "\"numberOfPeople\":2,\n" +
                "\"type\":\"add\"\n" +
                "}");
        log.info("Message to " + transportQueueName + " sent.");
        return ResponseEntity.ok().build();
    }

    @GetMapping("test/payment")
    public ResponseEntity<Void> testPaymentQueueCommunication() {
        log.info("Sending message to " + transportQueueName);
        rabbitTemplate.convertAndSend(DEFAULT_EXCHANGE_NAME, paymentQueueName, "{\n" +
                "\"Guid\":\"reservationId\",\n" +
                "\"Username\":\"abc\",\n" +
                "\"IdReservationRoom\":\"roomReservationId\",\n" +
                "\"Osoby\": {" +
                    "\"DoLat3\": 1," +
                    "\"DoLat10\": 2," +
                    "\"DoLat18\": 0," +
                    "\"Dorosli\": 2" +
                "}}");
        log.info("Message to " + paymentQueueName + " sent.");
        return ResponseEntity.ok().build();
    }

    @GetMapping("test/hotel")
    public ResponseEntity<Void> testHotelQueueCommunication() {
        log.info("Sending message to " + hotelQueueName);
        rabbitTemplate.convertAndSend(DEFAULT_EXCHANGE_NAME, hotelQueueName, "{\n" +
                "\"id\":\"reservationId\",\n" +
                "\"userName\":\"abc\",\n" +
                "\"cost\":2,\n" +
                "}");
        log.info("Message to " + hotelQueueName + " sent.");
        return ResponseEntity.ok().build();
    }

}
