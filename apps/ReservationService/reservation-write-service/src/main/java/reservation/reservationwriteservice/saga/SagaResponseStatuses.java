package reservation.reservationwriteservice.saga;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum SagaResponseStatuses {

    SUCCEEDED("SUCCEEDED"),
    FAILURE("FAILURE");

    @JsonValue
    private final String text;

    public static SagaResponseStatuses fromText(String text) {
        return Arrays.stream(SagaResponseStatuses.values())
                .filter(status -> status.text.equals(text))
                .findFirst()
                .orElseThrow();
    }
}
