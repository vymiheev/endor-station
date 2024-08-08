package battle.station.endor.controller;

import battle.station.endor.dto.AttackResponseDto;
import battle.station.endor.dto.DroidProbeDto;
import battle.station.endor.dto.FireIonCannonDto;
import battle.station.endor.service.IonCannonService;
import battle.station.endor.service.TargetSelectorService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
@Slf4j
public class AttackController {
    @Autowired
    private IonCannonService fireService;
    @Autowired
    private TargetSelectorService targetSelectorService;

    @PostMapping(value = "/attack")
    @ResponseStatus(HttpStatus.OK)
    public AttackResponseDto attack(@RequestBody @Valid DroidProbeDto droidProbeDto) {
        var scanProbeDto = targetSelectorService.findNextTarget(droidProbeDto);
        log.info("Selected point {}", scanProbeDto);
        var fireIonCannonDto = FireIonCannonDto.builder()
                .target(scanProbeDto.getCoordinates())
                .enemies(scanProbeDto.getEnemies().getNumber()).build();

        var fireResult = fireService.fireAvailableGun(fireIonCannonDto);

        log.debug("Return fire result {}", fireResult);
        return AttackResponseDto.builder()
                .generation(fireResult.getGeneration())
                .casualties(fireResult.getCasualties())
                .target(fireIonCannonDto.getTarget())
                .build();
    }
}
