package battle.station.endor.service;

import battle.station.endor.dto.FireIonCannonDto;
import battle.station.endor.dto.FireIonCannonResponseDto;
import org.jetbrains.annotations.NotNull;

public interface IonCannonService {

    @NotNull
    FireIonCannonResponseDto fireAvailableGun(@NotNull FireIonCannonDto fireIonCannonDto);
}
