package reservation.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Builder
@Getter
public class ReserveHotelEvent implements Serializable {

    @JsonProperty("guid")
    private String eventId;

    @JsonProperty("username")
    private String username;

    @JsonProperty("roomReservationId")
    private Long roomReservationId;

    @JsonProperty("numOfPeople")
    private Long numOfPeople;

    @JsonProperty("type")
    private String type;

}
