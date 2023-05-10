package read;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableRabbit
@SpringBootApplication
public class PaymentReadServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaymentReadServiceApplication.class, args);
    }
}