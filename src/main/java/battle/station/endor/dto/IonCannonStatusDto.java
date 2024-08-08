package battle.station.endor.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
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
@JsonDeserialize(builder = IonCannonStatusDto.Builder.class)
@Builder(builderClassName = "Builder", toBuilder = true)
public class IonCannonStatusDto {
    @JsonProperty(required = true)
    private boolean available;
    @PositiveOrZero
    @JsonProperty(required = true)
    private int generation;
}
