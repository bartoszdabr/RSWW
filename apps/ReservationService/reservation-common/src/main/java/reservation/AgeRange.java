package reservation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum AgeRange {
    LESS_THAN_3_YEARS_OLD("lessThan3YearsOld"),
    LESS_THAN_10_YEARS_OLD("lessThan10YearsOld"),
    LESS_THAN_18_YEARS_OLD("lessThan18YearsOld"),
    ADULT("adult");

    @JsonValue
    private final String text;

    AgeRange(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public static AgeRange fromText(String text) {
        return Arrays.stream(AgeRange.values()).filter(ageRange -> ageRange.text.equals(text)).findFirst().orElseThrow();
    }
}
