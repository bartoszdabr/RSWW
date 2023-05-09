package transport.common;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
public class AddReservationEvent extends BaseEvent {
    private String userName;
    private int numberOfPeople;
}
