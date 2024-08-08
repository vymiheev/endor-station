package battle.station.endor.config.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "cannon")
@Getter
@Setter
@ToString
public class CommonCannonProperties {
    private CannonGenerationProperties ionGeneration1;
    private CannonGenerationProperties ionGeneration2;
    private CannonGenerationProperties ionGeneration3;
    private int fireRetry;
    private String statusUrl;
    private String fireUrl;
}
