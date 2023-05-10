package reservation.reservationreadservice;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableRabbit
@SpringBootApplication
public class ReservationReadServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReservationReadServiceApplication.class, args);
    }

}
