package read.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public class PaymentResponseModel {

    @JsonProperty("id")
    private final String reservationId;

    @JsonProperty("status")
    private final String status;

}
