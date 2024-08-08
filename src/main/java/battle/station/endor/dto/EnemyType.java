package battle.station.endor.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

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
        for (EnemyType type : values()) {
            String typeValue = type.getValue();
            if (typeValue.equals(value)) {
                return type;
            }
        }

        throw new IllegalArgumentException("Invalid value for EnemyType Enum: " + value);
    }
}
