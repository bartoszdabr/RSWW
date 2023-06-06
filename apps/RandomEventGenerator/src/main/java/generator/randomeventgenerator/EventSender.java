package generator.randomeventgenerator;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import  java.util.Random;

import java.util.List;
import java.util.UUID;

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
    private List<String> transportIds = List.of("02050f21-7561-4e12-ac59-e3c9b80cbed5","03906933-2368-4578-a5b4-f17830a441c1","0443c8ee-569b-49b7-a46b-9859aa9bf433","04ce1554-196c-4b50-87ed-5279c9cf7c79","058433cf-754e-4f41-ba48-8fc23d37fbb6","05b94dfb-1208-46c9-82b7-bca01ae7095a","0637d516-844a-49ce-b765-64be17dde3ec","068a1c27-666c-4012-8260-e19d787daa47","07e79d20-c650-4aa8-9037-42e76a43c91a","09d1b7cd-52db-49b1-8234-eb28709e06db","0be04cf8-83d4-4aae-8aee-d99530d2f1da","0be173a7-b212-4c46-87a7-e1db76173c5f","0c0065aa-3a86-4b97-9519-2e5894979482","0cfb52e9-6737-4e48-8990-cef80e2aa80d","0d6d3fed-3941-41a7-b867-51ef4cd26d88","0e238145-d7a7-4216-aa74-7d3abfe9612b","0eaca053-df20-486b-a457-281ebcf37dc9","0ef993f8-fc30-42f3-a7f9-1ea19bbd3981","10ae9056-906b-42fd-8900-ef9881da7f60","113fb7d9-94c8-4cf8-9ff8-7e6e59501d6b");
    private List<String> hotelIds = List.of("647e68d4c00fc1b8961a4f3c","647e68d4c00fc1b8961a4f42","647e68d4c00fc1b8961a4fa2","647e68d4c00fc1b8961a4f90","647e68d4c00fc1b8961a4fa8","647e68d4c00fc1b8961a4faa","647e68d4c00fc1b8961a4fac","647e68d4c00fc1b8961a4fae","647e68d4c00fc1b8961a4fb0","647e68d4c00fc1b8961a4f9e","647e68d4c00fc1b8961a4fb2","647e68d4c00fc1b8961a4f8a","647e68d4c00fc1b8961a4fb4","647e68d4c00fc1b8961a4fb8","647e68d4c00fc1b8961a4fba","647e68d4c00fc1b8961a4f96","647e68d4c00fc1b8961a4f58","647e68d4c00fc1b8961a4f52","647e68d4c00fc1b8961a4fc6","647e68d4c00fc1b8961a4fc4");
    EventPayload eventPayload = new EventPayload();
    public void sendEvents() throws InterruptedException {
        while (true) {
            int randomNum = (int) (Math.random() * 2) + 1;

            switch (randomNum) {
                case 1:
                    String random1 = "647e68d4c00fc1b8961a4f58"; //= hotelIds.get(new Random().nextInt(hotelIds.size()));
                    rabbitTemplate.convertAndSend(hotelQueueName, eventPayload.addHotelReservation(UUID.randomUUID().toString(),
                            "aaa",
                            random1,
                            4));
                    break;
                case 2:
                    String random2 = "166ff249-18cc-416b-8490-1ca2d5e14d26";//= transportIds.get(new Random().nextInt(transportIds.size()));
                    rabbitTemplate.convertAndSend(transportQueueName, eventPayload.makeTransportReservation(UUID.randomUUID().toString(),
                            random2,
                            "aaa",
                            2));
                    break;
                default:
                    throw new IllegalStateException("Invalid random number: " + randomNum);
            }
            Thread.sleep(1000);
        }
    }
}