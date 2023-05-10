package reservation;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.HashMap;

@SuperBuilder
@Getter
public class AddReservationEvent extends BaseEvent {

    private String username;

    private Long transportId;

    private Long roomReservationId;

    private HashMap<AgeRange, Long> ageGroupsSize;

    private Double cost;

}
