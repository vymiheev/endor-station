package battle.station.endor.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Predicate;

@ToString
public enum ProtocolType {
    //prioritize closest enemy point.
    CLOSEST_ENEMIES("closest-enemies", Comparator.comparingDouble(ScanProbeDto::getDistance), Integer.MAX_VALUE),
    //prioritize furthest enemy point.
    FURTHEST_ENEMIES("furthest-enemies", (o1, o2) -> Double.compare(o2.getDistance(), o1.getDistance()), Integer.MAX_VALUE),
    //prioritize enemy points with allies.
    ASSIST_ALLIES("assist-allies", (o1, o2) -> Boolean.compare(o2.getAllies() > 0, o1.getAllies() > 0), Integer.MIN_VALUE),
    //do not attack enemy points with allies.
    AVOID_CROSSFIRE("avoid-crossfire", (o1) -> o1.getAllies() == 0),
    //attack mech enemies if found. Otherwise, any other enemy type is valid.
    PRIORITIZE_MECH("prioritize-mech", (o1, o2) -> EnemyType.compare(o1.getEnemies().getType(), o2.getEnemies().getType()), Integer.MIN_VALUE),
    //do not attack any mech enemies.
    AVOID_MECH("avoid-mech", (o1) -> o1.getEnemies().getType() == EnemyType.SOLDIER),
    ;

    private final String value;
    @Getter
    @Nullable
    private final Comparator<ScanProbeDto> comparator;
    @Getter
    @Nullable
    private final Predicate<ScanProbeDto> predicate;
    @Getter
    int priorityOrder;

    ProtocolType(@NotNull String value, @NotNull Comparator<ScanProbeDto> comparator, int priorityOrder) {
        this.value = value;
        this.comparator = comparator;
        this.predicate = null;
        this.priorityOrder = priorityOrder;
    }

    ProtocolType(@NotNull String value, @NotNull Predicate<ScanProbeDto> predicate) {
        this.value = value;
        this.predicate = predicate;
        this.comparator = null;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static ProtocolType fromValue(String value) {
        return Arrays.stream(values())
                .filter(type -> type.getValue().equals(value))
                .findFirst().
                orElseThrow(() -> new IllegalArgumentException("Invalid value for ProtocolType Enum: " + value));
    }
}
