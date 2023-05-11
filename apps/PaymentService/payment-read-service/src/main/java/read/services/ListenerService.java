package read.services;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class ListenerService {

    private final RabbitTemplate rabbitTemplate;
    private final String DEFAULT_EXCHANGE_NAME = "";

    @Value("${rabbitmq.reservation.queueName}")
    private String reservationQueueName;

    @RabbitListener(queues = "${rabbitmq.payment.queueName}")
    public void receiveMessage(String message) {
        if (message != null) {
            Random rand = new Random();
            int myRandInt = rand.nextInt(100);

            if(myRandInt < 10)
                System.out.println("Błąd przetwarzania płatności!" + message);
            else
                System.out.println("Płatność przebiegła pomyślnie!" + message);

            rabbitTemplate.convertAndSend(DEFAULT_EXCHANGE_NAME, reservationQueueName, "Succeed");
        }
    }
}
