package reservation.events;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum ReservationStatuses {
    NEW("NEW"),
    HOTEL_CONFIRMED("HOTEL_CONFIRMED"),
    TRANSPORT_CONFIRMED("TRANSPORT_CONFIRMED"),
    PAYMENT_CONFIRMED("PAYMENT_CONFIRMED"),
    RESERVED("RESERVED"),
    REMOVED("REMOVED");

    @JsonValue
    private final String text;

    public static ReservationStatuses fromText(String text) {
        return Arrays.stream(ReservationStatuses.values())
                .filter(status -> status.text.equals(text))
                .findFirst()
                .orElseThrow();
    }
}
