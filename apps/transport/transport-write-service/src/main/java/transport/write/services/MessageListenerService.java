package transport.write.services;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import transport.write.commands.AddReservationCommand;
import transport.write.commands.CancelReservationCommand;
import transport.write.messaging.MessageType;
import transport.write.messaging.ResponseMessage;
import transport.write.messaging.ResponseMessageStatus;
import transport.write.model.SagaMessage;

@Component
@AllArgsConstructor
public class MessageListenerService {
  private final TransportService transportService;
  private final MessageService messageService;
  private final Logger log = LogManager.getLogger(this.getClass());

  @RabbitListener(queues = "${rabbitmq.transport.queueName}")
  public void receivedMessage(SagaMessage message) {
    log.info("Received new message: %s".formatted(message));
    MessageType type = MessageType.valueOf(message.getType());

    switch (type) {
      case ADD -> handleAddReservationMessage(message);
      case CANCEL -> handleCancelReservationMessage(message);
    }
  }

  private void handleAddReservationMessage(SagaMessage message) {
    var command = AddReservationCommand
            .builder()
            .id(message.getReservationId())
            .transportId(message.getTransportId())
            .username(message.getUsername())
            .numOfPeople(message.getNumOfPeople())
            .build();
    var response = ResponseMessage.builder()
            .id(command.getId())
            .status(ResponseMessageStatus.SUCCEEDED)
            .build();

    try {
      transportService.makeReservation(command);
    } catch (Exception e) {
      log.error(e);
      response.setStatus(ResponseMessageStatus.FAILURE);
    } finally {
      messageService.sendResponse(response);
    }
  }

  private void handleCancelReservationMessage(SagaMessage message) {
    var command = CancelReservationCommand
            .builder()
            .id(message.getReservationId())
            .transportId(message.getTransportId())
            .userName(message.getUsername())
            .numberOfPeople(message.getNumOfPeople())
            .build();
    try {
      transportService.cancelReservation(command);
    } catch (Exception e) {
      log.error(e);
    }
  }
}
