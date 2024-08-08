package battle.station.endor.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
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
@JsonDeserialize(builder = AttackResponseDto.Builder.class)
@Builder(builderClassName = "Builder", toBuilder = true)
public class AttackResponseDto {
    private CoordinatesProbeDto target;
    private int casualties;
    private int generation;
}