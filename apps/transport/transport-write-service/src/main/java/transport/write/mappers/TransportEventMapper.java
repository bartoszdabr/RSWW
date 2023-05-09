package transport.write.mappers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import transport.common.*;
import transport.write.model.Transport;
import transport.write.events.TransportEvent;

import java.util.List;

@Component
public class TransportEventMapper {
  private final ObjectMapper objectMapper;

  public TransportEventMapper(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }
  AddReservationEvent e = new AddReservationEvent();
  //TODO: Refactor this
  public Transport map(List<TransportEvent> transportEvents) throws JsonProcessingException {
    Transport transport = new Transport();
    for (TransportEvent event : transportEvents) {
      switch (event.getType()) {
        case ADD -> transport.handle(
            objectMapper.readValue(event.getEventJson(), AddReservationEvent.class));
        case CANCEL -> transport.handle(
            objectMapper.readValue(event.getEventJson(), CancelReservationEvent.class));
        case CREATE -> transport.handle(
            objectMapper.readValue(event.getEventJson(), CreateTransportEvent.class));
      }
    }
    return transport;
  }
}
