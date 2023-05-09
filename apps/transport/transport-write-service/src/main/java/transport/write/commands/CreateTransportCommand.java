package transport.write.commands;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class CreateTransportCommand {
  private String sourcePlace;
  private String destinationPlace;
  private LocalDate date;
  private Integer availableSeats;
}
