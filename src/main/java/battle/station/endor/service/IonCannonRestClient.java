package battle.station.endor.service;

import battle.station.endor.config.properties.CannonGenerationProperties;
import battle.station.endor.dto.FireIonCannonDto;
import battle.station.endor.dto.FireIonCannonResponseDto;
import battle.station.endor.dto.IonCannonStatusDto;
import battle.station.endor.exception.RestTemplateException;
import org.jetbrains.annotations.NotNull;

public interface IonCannonRestClient {
    @NotNull
    IonCannonStatusDto checkStatus(@NotNull CannonGenerationProperties cannonProperties) throws RestTemplateException;

    @NotNull
    FireIonCannonResponseDto fire(@NotNull CannonGenerationProperties cannonProperties, @NotNull FireIonCannonDto fireIonCannonDto) throws RestTemplateException;
}
