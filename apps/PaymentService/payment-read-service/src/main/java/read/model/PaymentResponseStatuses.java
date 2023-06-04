package read.model;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum PaymentResponseStatuses {

    SUCCEEDED("SUCCEEDED"),
    FAILUER("FAILURE");

    @JsonValue
    private final String text;

    public static PaymentResponseStatuses fromText(String text) {
        return Arrays.stream(PaymentResponseStatuses.values())
                .filter(status -> status.text.equals(text))
                .findFirst()
                .orElseThrow();
    }
}

