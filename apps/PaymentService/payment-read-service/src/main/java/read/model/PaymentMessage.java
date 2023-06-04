package read.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class PaymentMessage implements Serializable {

    @JsonProperty("reservationId")
    private String reservationId;

    @JsonProperty("cost")
    private Double cost;
}
