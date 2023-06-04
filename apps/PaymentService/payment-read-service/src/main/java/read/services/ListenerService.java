package read.services;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import read.model.PaymentMessage;
import read.model.PaymentResponseModel;
import read.model.PaymentResponseStatuses;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class ListenerService {

    private final Logger log = LogManager.getLogger(ListenerService.class);

    private final RabbitTemplate rabbitTemplate;
    private final String DEFAULT_EXCHANGE_NAME = "";

    private final long SLEEP_5_SECONDS = 5000L;

    @Value("${rabbitmq.reservation.queueName}")
    private String reservationQueueName;

    @RabbitListener(queues = "${rabbitmq.payment.queueName}")
    public void receiveMessage(PaymentMessage message) throws InterruptedException {
        var paymentStatus = processPayment(message.getReservationId());

        var paymentResponse = PaymentResponseModel.builder()
                .reservationId(message.getReservationId())
                .status(paymentStatus)
                .build();

        rabbitTemplate.convertAndSend(DEFAULT_EXCHANGE_NAME, reservationQueueName, paymentResponse);
    }

    private String processPayment(String reservationId) throws InterruptedException {
        log.info("Processing new payment for reservation: " + reservationId);
        Thread.sleep(SLEEP_5_SECONDS);
        boolean paymentPassed = new Random().nextInt(100) >= 10;
        log.info("Payment status for reservation: " + reservationId + " status: " + paymentPassed);

        return paymentPassed ? PaymentResponseStatuses.SUCCEEDED.getText() : PaymentResponseStatuses.FAILUER.getText();
    }
}
