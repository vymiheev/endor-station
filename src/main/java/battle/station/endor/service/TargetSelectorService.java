package battle.station.endor.service;

import battle.station.endor.dto.DroidProbeDto;
import battle.station.endor.dto.ScanProbeDto;
import org.jetbrains.annotations.NotNull;

public interface TargetSelectorService {
    @NotNull
    ScanProbeDto findNextTarget(@NotNull DroidProbeDto droidProbeDto);
}
