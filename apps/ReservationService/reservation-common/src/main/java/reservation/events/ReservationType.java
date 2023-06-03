package reservation.events;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ReservationType {

    ADD("ADD"),
    CANCEL("CANCEL");

    @JsonValue
    private final String text;
}
