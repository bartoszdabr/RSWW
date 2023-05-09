package transport.read;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableRabbit
@SpringBootApplication
public class TransportReadService {
  public static void main(String[] args) {
    SpringApplication.run(TransportReadService.class, args);
  }
}
