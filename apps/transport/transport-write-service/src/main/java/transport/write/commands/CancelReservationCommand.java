package transport.write.commands;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CancelReservationCommand {
  private String id;
  private String transportId;
  private String userName;
  private Long numberOfPeople;
}
