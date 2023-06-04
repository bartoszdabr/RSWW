package transport.common;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class CancelReservationEvent extends BaseEvent {
  private int numberOfPeople;
  private String userName;
}
