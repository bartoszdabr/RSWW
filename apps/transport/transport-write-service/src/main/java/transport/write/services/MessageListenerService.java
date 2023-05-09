package transport.write.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import transport.write.commands.AddReservationCommand;
import transport.write.commands.CancelReservationCommand;
import transport.write.messaging.MessageType;
import transport.write.messaging.ResponseMessage;

@Component
@AllArgsConstructor
public class MessageListenerService {
  private final ObjectMapper objectMapper;
  private final TransportService transportService;
  private final MessageService messageService;
  private final Logger log = LogManager.getLogger(this.getClass());

  // TODO: Refactor this
  @RabbitListener(queues = "${rabbitmq.transport.queueName}")
  public void receivedMessage(String message) {
    log.info("Received new message: %s".formatted(message));
    try {
      JsonNode jsonNode = objectMapper.readTree(message);
      MessageType type = getMessageType(message);

      switch (type) {
        case ADD -> handleAddReservationMessage(jsonNode);
        case CANCEL -> handleCancelReservationMessage(jsonNode);
      }
    } catch (JsonProcessingException e) {
      log.error("Error occurred while reading/converting message: ", e);
    }
  }

  private MessageType getMessageType(String message) throws JsonProcessingException {
    JsonNode jsonNode = objectMapper.readTree(message);
    return MessageType.valueOf(jsonNode.get("type").asText().toUpperCase());
  }

  private void handleAddReservationMessage(JsonNode jsonNode) throws JsonProcessingException {
    AddReservationCommand command = objectMapper.treeToValue(jsonNode, AddReservationCommand.class);
    ResponseMessage response =
        ResponseMessage.builder().id(command.getId()).status("succeeded").build();
    try {
      transportService.makeReservation(command);
    } catch (Exception e) {
      log.error(e);
      response.setStatus("failure");
    } finally {
      messageService.sendResponse(objectMapper.writeValueAsString(response));
    }
  }

  private void handleCancelReservationMessage(JsonNode jsonNode) throws JsonProcessingException {
    CancelReservationCommand command =
        objectMapper.treeToValue(jsonNode, CancelReservationCommand.class);
    try {
      transportService.cancelReservation(command);
    } catch (Exception e) {
      log.error(e);
    }
  }
}
