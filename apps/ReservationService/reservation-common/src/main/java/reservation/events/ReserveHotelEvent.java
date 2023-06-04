package reservation.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import reservation.AgeRange;

import java.io.Serializable;
import java.util.HashMap;

@Builder
@Getter
public class ReserveHotelEvent implements Serializable {

    @JsonProperty("guid")
    private String eventId;

    @JsonProperty("username")
    private String username;

    @JsonProperty("roomReservationId")
    private String roomReservationId;

    @JsonProperty("ageGroups")
    private HashMap<AgeRange, Long> ageGroups;

    @JsonProperty("type")
    private String type;

}
