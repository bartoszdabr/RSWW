package generator.randomeventgenerator;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class EventSender implements ApplicationRunner {

    @Value("${rabbitmq.hotel.queueName}")
    private String hotelQueueName;

    @Value("${rabbitmq.transport.queueName}")
    private String transportQueueName;

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    private EventSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

     @Override
    public void run(ApplicationArguments args) throws Exception {
        this.sendEvents();
    }

    EventPayload eventPayload = new EventPayload();
    public void sendEvents() throws InterruptedException {
        while (true) {
            int randomNum = (int) (Math.random() * 2) + 1;

            switch (randomNum) {
                case 1:
                    rabbitTemplate.convertAndSend(hotelQueueName, eventPayload.addHotelReservation("bd7418f1-5568-4bec-8f14-214da15383e9",
                            "aaa",
                            "647ca7631f72054807f3ad20",
                            "4"));
                    break;
                case 2:
                    rabbitTemplate.convertAndSend(transportQueueName, eventPayload.makeTransportReservation("02050f21-7561-4e12-ac59-e3c9b80cbed5",
                            "02050f21-7561-4e12-ac59-e3c9b80cbed5",
                            "aaa",
                            "2"));
                    break;
                default:
                    throw new IllegalStateException("Invalid random number: " + randomNum);
            }
            Thread.sleep(1000);
        }
    }
}