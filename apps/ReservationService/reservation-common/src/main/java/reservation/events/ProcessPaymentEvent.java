package reservation.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProcessPaymentEvent {

    @JsonProperty("reservationId")
    private String reservationId;

    @JsonProperty("cost")
    private Double cost;

}
