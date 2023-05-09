package transport.write.model;

import lombok.Data;
import transport.common.AddReservationEvent;
import transport.common.CancelReservationEvent;
import transport.common.CreateTransportEvent;

import java.time.LocalDate;

@Data
public class Transport {
  private String id;
  private String destinationPlace;
  private String sourcePlace;
  private LocalDate date;
  private int availableSeats;

  public void handle(AddReservationEvent event) {
    availableSeats -= event.getNumberOfPeople();
  }

  public void handle(CancelReservationEvent event) {
    availableSeats += event.getNumberOfPeople();
  }

  public void handle(CreateTransportEvent event) {
    destinationPlace = event.getDestinationPlace();
    sourcePlace = event.getSourcePlace();
    date = event.getDate();
    availableSeats += event.getAvailableSeats();
  }
}
