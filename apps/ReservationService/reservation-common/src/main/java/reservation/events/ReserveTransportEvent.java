package reservation.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ReserveTransportEvent {

    @JsonProperty("id")
    private String reservationId;

    @JsonProperty("username")
    private String username;

    @JsonProperty("numOfPeople")
    private Long numOfPeople;

    @JsonProperty("type")
    private String type;

    @JsonProperty("transportId")
    private String transportId;
}
