package reservation;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class RemoveReservationEvent extends BaseEvent {

    private String removedReservationId;

    private String username;
}
