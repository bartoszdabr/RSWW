package transport.write;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableRabbit
@SpringBootApplication
public class TransportWriteService {
  public static void main(String[] args) {
    SpringApplication.run(TransportWriteService.class, args);
  }
}
