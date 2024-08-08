package battle.station.endor.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.PositiveOrZero;
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
@JsonDeserialize(builder = FireIonCannonDto.Builder.class)
@Builder(builderClassName = "Builder", toBuilder = true)
public class FireIonCannonDto {
    private CoordinatesProbeDto target;
    @PositiveOrZero
    private int enemies;
}
