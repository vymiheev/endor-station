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

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TargetSelectorServiceImpl implements TargetSelectorService {

    @Autowired
    private MainEndorProperties appProperties;

    @NotNull
    public ScanProbeDto findNextTarget(@NotNull DroidProbeDto droidProbeDto) {
        log.debug("Get Probe: {}\n", droidProbeDto);
        List<ScanProbeDto> available = droidProbeDto.getScan().stream()
                .filter(it -> it.getDistance() < appProperties.getRadiusOfSight())
                .collect(Collectors.toList());
        if (available.isEmpty()) {
            throw new CommonEnderRuntimeException("Not found proper position for attack");
        }

        available = filterDroidScans(droidProbeDto.getProtocols(), available);

        sortDroidScans(droidProbeDto.getProtocols(), available);

        return available.getFirst();
    }

    private static void sortDroidScans(@NotNull List<ProtocolType> protocolTypes, @NotNull List<ScanProbeDto> available) {
        Optional<Comparator<ScanProbeDto>> combinedComparator = protocolTypes.stream()
                .filter(it -> Objects.nonNull(it.getComparator()))
                .sorted(Comparator.comparingInt(ProtocolType::getPriorityOrder))
                .map(ProtocolType::getComparator)
                .reduce(Comparator::thenComparing);
        combinedComparator.ifPresent(available::sort);
    }

    private static @NotNull List<ScanProbeDto> filterDroidScans(@NotNull List<ProtocolType> protocolTypes, @NotNull List<ScanProbeDto> available) {
        Optional<Predicate<ScanProbeDto>> combinedPredicate = protocolTypes.stream()
                .map(ProtocolType::getPredicate)
                .filter(Objects::nonNull)
                .reduce(Predicate::and);
        if (combinedPredicate.isPresent()) {
            available = available.stream().filter(combinedPredicate.get()).collect(Collectors.toList());
        }
        if (available.isEmpty()) {
            throw new CommonEnderRuntimeException("Not found proper position for attack after filtering.");
        }
        return available;
    }
}
