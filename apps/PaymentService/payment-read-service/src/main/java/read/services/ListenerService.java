package read.services;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@Component
public class ListenerService {

    @RabbitListener(queues = "paymentQueue")
    public void receiveMessage(String message) {
        if (message != null) {
            Random rand = new Random();
            int myRandInt = rand.nextInt(100);

            if(myRandInt < 10)
                System.out.println("Błąd przetwarzania płatności!" + message);
            else
                System.out.println("Płatność przebiegła pomyślna!" + message);
        }
    }
}
