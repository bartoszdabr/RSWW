package transport.write.mappers;

import org.springframework.stereotype.Component;
import transport.common.AddReservationEvent;
import transport.common.CancelReservationEvent;
import transport.common.CreateTransportEvent;
import transport.write.commands.AddReservationCommand;
import transport.write.commands.CancelReservationCommand;
import transport.write.commands.CreateTransportCommand;

import java.time.Instant;
import java.util.UUID;

@Component
public class EventCommandMapper {

  public AddReservationEvent toEvent(AddReservationCommand command) {
    return AddReservationEvent.builder()
        .eventId(UUID.randomUUID().toString())
        .timeStamp(Instant.now())
        .transportId(command.getTransportId())
        .numberOfPeople(command.getNumOfPeople())
        .userName(command.getUsername())
        .build();
  }

  public CancelReservationEvent toEvent(CancelReservationCommand command) {
    return CancelReservationEvent.builder()
        .eventId(UUID.randomUUID().toString())
        .timeStamp(Instant.now())
        .transportId(command.getTransportId())
        .numberOfPeople(command.getNumberOfPeople())
        .userName(command.getUserName())
        .build();
  }

  public CreateTransportEvent toEvent(CreateTransportCommand command) {
    return CreateTransportEvent.builder()
        .eventId(UUID.randomUUID().toString())
        .timeStamp(Instant.now())
        .transportId(UUID.randomUUID().toString())
        .sourcePlace(command.getSourcePlace())
        .destinationPlace(command.getDestinationPlace())
        .date(command.getDate())
        .availableSeats(command.getAvailableSeats())
        .build();
  }
}
