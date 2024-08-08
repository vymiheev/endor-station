package battle.station.endor.config.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "station-ender")
@Getter
@Setter
@ToString
public class MainEndorProperties {
    private RestProperties rest;
    private CommonCannonProperties cannon;
    private int radiusOfSight;
}
