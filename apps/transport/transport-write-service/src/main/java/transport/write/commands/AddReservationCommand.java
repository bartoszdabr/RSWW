package transport.write.commands;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddReservationCommand {
  private String id;
  private String transportId;
  private String username;
  private Long numOfPeople;
}
