package battle.station.endor.config.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "rest")
@Getter
@Setter
@ToString
public class RestProperties {
    private long connectTimeoutMs;
    private long readTimeoutMs;
}


