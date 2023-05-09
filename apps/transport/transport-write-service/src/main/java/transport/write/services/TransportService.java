package transport.write.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import transport.common.*;
import transport.write.model.Transport;
import transport.write.mappers.EventCommandMapper;
import transport.write.events.TransportEvent;
import transport.write.mappers.TransportEventMapper;
import transport.write.repositories.TransportEventRepository;
import transport.write.commands.AddReservationCommand;
import transport.write.commands.CancelReservationCommand;
import transport.write.commands.CreateTransportCommand;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TransportService {
  private final TransportEventRepository eventRepository;
  private final MessageService messageService;
  private final ObjectMapper objectMapper;
  private final EventCommandMapper eventCommandMapper;
  private final TransportEventMapper transportEventMapper;
  private final Logger log = LogManager.getLogger(this.getClass());

  @Transactional
  public void makeReservation(AddReservationCommand command) {
    AddReservationEvent event = eventCommandMapper.toEvent(command);

    List<TransportEvent> transportEvents =
        eventRepository.findByTransportId(event.getTransportId());
    Transport current = null;
    try {
      current = transportEventMapper.map(transportEvents);
    } catch (JsonProcessingException e) {
      log.error("An error occurred during events deserialization");
      throw new RuntimeException(e);
    }
    if (current.getAvailableSeats() < event.getNumberOfPeople()) {
      throw new IllegalArgumentException(
          "The number of people is larger than the number of available seats");
    }
    TransportEvent newTransportEvent = getTransportEvent(TransportEventType.ADD, event);
    eventRepository.save(newTransportEvent);
    messageService.sendEvent(event);
  }

  public void addTransport(CreateTransportCommand command) throws JsonProcessingException {
    CreateTransportEvent event = eventCommandMapper.toEvent(command);
    TransportEvent newTransportEvent = getTransportEvent(TransportEventType.CREATE, event);
    eventRepository.save(newTransportEvent);
    messageService.sendEvent(event);
  }

  @Transactional
  public void cancelReservation(CancelReservationCommand command) {
    CancelReservationEvent event = eventCommandMapper.toEvent(command);

    List<TransportEvent> transportEvents =
        eventRepository.findByTransportId(event.getTransportId());
    if (transportEvents.isEmpty()) {
      throw new IllegalArgumentException(
          "An attempt to cancel reservation on non existing transport");
    }

    TransportEvent newTransportEvent = getTransportEvent(TransportEventType.CANCEL, event);

    eventRepository.save(newTransportEvent);
    messageService.sendEvent(event);
  }

  private TransportEvent getTransportEvent(TransportEventType type, BaseEvent event) {
    try {
      return TransportEvent.builder()
          .id(UUID.randomUUID().toString())
          .type(type)
          .transportId(event.getTransportId())
          .eventJson(objectMapper.writeValueAsString(event))
          .build();
    } catch (JsonProcessingException e) {
      log.error("An error occurred during event serialization");
      throw new RuntimeException(e);
    }
  }
}
