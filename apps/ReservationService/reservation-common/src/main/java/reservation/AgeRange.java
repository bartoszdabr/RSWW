package reservation;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum AgeRange {
    LESS_THAN_3_YEARS_OLD("lessThan3YearsOld"),
    LESS_THAN_10_YEARS_OLD("lessThan10YearsOld"),
    LESS_THAN_18_YEARS_OLD("lessThan18YearsOld"),
    ADULT("adult");

    @JsonValue
    private final String text;

    public static AgeRange fromText(String text) {
        return Arrays.stream(AgeRange.values())
                .filter(ageRange -> ageRange.text.equals(text))
                .findFirst()
                .orElseThrow();
    }
}
