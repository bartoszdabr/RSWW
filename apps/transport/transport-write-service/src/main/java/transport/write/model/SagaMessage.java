package transport.write.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class SagaMessage {

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
