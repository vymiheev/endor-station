package battle.station.endor.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
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
@JsonDeserialize(builder = CoordinatesProbeDto.Builder.class)
@Builder(builderClassName = "Builder", toBuilder = true)
public class CoordinatesProbeDto {
    @JsonProperty(required = true)
    private int x;
    @JsonProperty(required = true)
    private int y;
}
