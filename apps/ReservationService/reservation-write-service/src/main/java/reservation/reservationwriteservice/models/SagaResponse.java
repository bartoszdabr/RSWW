package reservation.reservationwriteservice.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class SagaResponse {

    @JsonProperty("id")
    private final String reservationId;

    @JsonProperty("status")
    private final String status;
}
