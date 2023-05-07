package reservation.reservationwriteservice;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableRabbit
@SpringBootApplication
public class ReservationWriteServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReservationWriteServiceApplication.class, args);
    }

}
