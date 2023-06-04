package transport.write.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import transport.write.events.TransportEvent;

import java.util.List;

@Getter
@AllArgsConstructor
public class TransportEventResponseModel {
  private List<TransportEvent> transportEvents;
}
