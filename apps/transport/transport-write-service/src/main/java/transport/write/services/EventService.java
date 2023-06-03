package transport.write.services;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import transport.write.events.TransportEvent;
import transport.write.model.TransportEventResponseModel;
import transport.write.repositories.TransportEventRepository;


@Service
@RequiredArgsConstructor
public class EventService {
  private final Logger log = LogManager.getLogger(this.getClass());
  private final TransportEventRepository eventRepository;

  public TransportEventResponseModel findAll() {
    return new TransportEventResponseModel(eventRepository.findAll());
  }

  public TransportEventResponseModel find(String transportId) {
    return new TransportEventResponseModel(eventRepository.findByTransportId(transportId));
  }
}
