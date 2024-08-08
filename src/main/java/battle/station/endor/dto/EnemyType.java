package battle.station.endor.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum EnemyType {
    MECH("mech"),
    SOLDIER("soldier");

    EnemyType(String value) {
        this.value = value;
    }

    private final String value;

    public static int compare(EnemyType type1, EnemyType type2) {
        return Integer.compare(type1.ordinal(), type2.ordinal());
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static EnemyType fromValue(String value) {
        return Arrays.stream(values())
                .filter(type -> type.getValue().equals(value))
                .findFirst().
                orElseThrow(() -> new IllegalArgumentException("Invalid value for EnemyType Enum: " + value));
    }
}
