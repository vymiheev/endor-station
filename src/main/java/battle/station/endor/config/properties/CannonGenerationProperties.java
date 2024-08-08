package battle.station.endor.config.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
@ToString
public class CannonGenerationProperties implements Comparable<CannonGenerationProperties> {
    private String host;
    private int port;
    //in microseconds
    private long fireTime;
    private int generation;

    @Override
    public int compareTo(@NotNull CannonGenerationProperties o) {
        return Integer.compare(this.generation, o.generation);
    }
}