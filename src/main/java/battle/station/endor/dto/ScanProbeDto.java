package battle.station.endor.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Validated
@ToString
@JsonDeserialize(builder = ScanProbeDto.Builder.class)
@Builder(builderClassName = "Builder", toBuilder = true)
public class ScanProbeDto {
    @JsonProperty(required = true)
    @NotNull
    private CoordinatesProbeDto coordinates;
    @JsonProperty(required = true)
    @NotNull
    private EnemiesProbeDto enemies;
    @JsonProperty(value = "allies")
    private int allies;

    public double getDistance() {
        return Math.sqrt(Math.pow(coordinates.getX(), 2) + Math.pow(coordinates.getY(), 2));
    }
}
