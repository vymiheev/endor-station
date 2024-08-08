package battle.station.endor.config;

import battle.station.endor.config.properties.MainEndorProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RestConfig {

    @Bean(name = "rest")
    public RestTemplate IonCannonRestTemplate(final MainEndorProperties properties) {
        return new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofMillis(properties.getRest().getConnectTimeoutMs()))
                .setReadTimeout(Duration.ofMillis(properties.getRest().getReadTimeoutMs()))
                .errorHandler(new battle.station.endor.exception.AdviceExceptionHandler())
                .build();
    }
}
