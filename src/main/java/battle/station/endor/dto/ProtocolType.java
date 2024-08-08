package battle.station.endor.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Comparator;

public enum ProtocolType {
    //prioritize closest enemy point.
    CLOSEST_ENEMIES("closest-enemies", Comparator.comparingDouble(ScanProbeDto::getDistance)),
    //prioritize furthest enemy point.
    FURTHEST_ENEMIES("furthest-enemies", (o1, o2) -> Double.compare(o2.getDistance(), o1.getDistance())),
    //prioritize enemy points with allies.
    ASSIST_ALLIES("assist-allies", (o1, o2) -> Integer.compare(o2.getAllies(), o1.getAllies())),
    //do not attack enemy points with allies.
    AVOID_CROSSFIRE("avoid-crossfire", (o1, o2) -> Integer.compare(o1.getAllies(), o2.getAllies())),
    //attack mech enemies if found. Otherwise, any other enemy type is valid.
    PRIORITIZE_MECH("prioritize-mech", (o1, o2) -> EnemyType.compare(o1.getEnemies().getType(), o2.getEnemies().getType())),
    //do not attack any mech enemies.
    AVOID_MECH("avoid-mech", (o1, o2) -> EnemyType.compare(o2.getEnemies().getType(), o1.getEnemies().getType())),
    ;

    private final String value;
    @Getter
    private final Comparator<ScanProbeDto> comparator;

    ProtocolType(String value, Comparator<ScanProbeDto> comparator) {
        this.value = value;
        this.comparator = comparator;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static ProtocolType fromValue(String value) {
        for (ProtocolType type : values()) {
            String typeValue = type.getValue();
            if (typeValue.equals(value)) {
                return type;
            }
        }

        throw new IllegalArgumentException("Invalid value for ProtocolType Enum: " + value);
    }
}
