package reservation.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ReserveTransportEvent {

    @JsonProperty("id")
    private String reservationId;

    @JsonProperty("userName")
    private String username;

    @JsonProperty("numberOfPeople")
    private Long numOfPeople;

    @JsonProperty("type")
    private String type;

    @JsonProperty("transportId")
    private String transportId;
}
