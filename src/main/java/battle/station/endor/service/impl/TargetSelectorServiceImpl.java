package battle.station.endor.service.impl;

import battle.station.endor.config.properties.MainEndorProperties;
import battle.station.endor.dto.DroidProbeDto;
import battle.station.endor.dto.ProtocolType;
import battle.station.endor.dto.ScanProbeDto;
import battle.station.endor.exception.CommonEnderRuntimeException;
import battle.station.endor.service.TargetSelectorService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TargetSelectorServiceImpl implements TargetSelectorService {

    @Autowired
    private MainEndorProperties appProperties;

    @NotNull
    public ScanProbeDto findNextTarget(@NotNull DroidProbeDto droidProbeDto) {
        log.debug("Get Probe: {}\n", droidProbeDto);
        List<ScanProbeDto> closest = droidProbeDto.getScan().stream()
                .filter(it -> it.getDistance() < appProperties.getRadiusOfSight()).collect(Collectors.toList());
        if (closest.isEmpty()) {
            throw new CommonEnderRuntimeException("Not found proper position for attack");
        }
        for (ProtocolType protocolType : droidProbeDto.getProtocols()) {
            closest.sort(protocolType.getComparator());
        }
        return closest.getFirst();
    }
}
