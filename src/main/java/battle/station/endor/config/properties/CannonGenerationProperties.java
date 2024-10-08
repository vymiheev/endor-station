package battle.station.endor.config.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
@ToString
public class CannonGenerationProperties {
    private String serviceUrl;
    //in microseconds
    private long fireTime;
    private int generation;
}