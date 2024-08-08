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

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Validated
@ToString
@JsonDeserialize(builder = DroidProbeDto.Builder.class)
@Builder(builderClassName = "Builder", toBuilder = true)
public class DroidProbeDto {
    @JsonProperty(required = true)
    @NotNull
    private List<ProtocolType> protocols;
    @JsonProperty(required = true)
    @NotNull
    private List<ScanProbeDto> scan;
}