package battle.station.endor.service.impl;

import battle.station.endor.dto.*;
import battle.station.endor.exception.CommonEnderRuntimeException;
import battle.station.endor.service.TargetSelectorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class TargetSelectorServiceTest {
    @Autowired
    private TargetSelectorService cannonService;

    @Test
    void findNextTarget_closest() {
        var firstScan = ScanProbeDto.builder()
                .enemies(EnemiesProbeDto.builder().type(EnemyType.SOLDIER).number(10).build())
                .coordinates(CoordinatesProbeDto.builder().x(10).y(20).build())
                .build();
        var secondScan = ScanProbeDto.builder()
                .enemies(EnemiesProbeDto.builder().type(EnemyType.SOLDIER).number(10).build())
                .coordinates(CoordinatesProbeDto.builder().x(11).y(20).build())
                .build();
        var result = cannonService.findNextTarget(DroidProbeDto.builder()
                .scan(List.of(firstScan, secondScan))
                .protocols(List.of(ProtocolType.CLOSEST_ENEMIES))
                .build());
        assertEquals(firstScan, result);
    }

    @Test
    void findNextTarget_largeDistance() {
        var firstScan = ScanProbeDto.builder()
                .enemies(EnemiesProbeDto.builder().type(EnemyType.SOLDIER).number(10).build())
                .coordinates(CoordinatesProbeDto.builder().x(100).y(100).build())
                .allies(10)
                .build();
        var secondScan = ScanProbeDto.builder()
                .enemies(EnemiesProbeDto.builder().type(EnemyType.SOLDIER).number(10).build())
                .coordinates(CoordinatesProbeDto.builder().x(11).y(20).build())
                .build();
        var result = cannonService.findNextTarget(DroidProbeDto.builder()
                .scan(List.of(firstScan, secondScan))
                .protocols(List.of(ProtocolType.ASSIST_ALLIES))
                .build());
        assertEquals(secondScan, result);
    }

    @Test
    void findNextTarget_multipleProtocols() {
        var firstScan = ScanProbeDto.builder()
                .enemies(EnemiesProbeDto.builder().type(EnemyType.MECH).number(10).build())
                .coordinates(CoordinatesProbeDto.builder().x(10).y(10).build())
                .allies(10)
                .build();
        var secondScan = ScanProbeDto.builder()
                .enemies(EnemiesProbeDto.builder().type(EnemyType.SOLDIER).number(10).build())
                .coordinates(CoordinatesProbeDto.builder().x(11).y(20).build())
                .allies(20)
                .build();
        var thirdScan = ScanProbeDto.builder()
                .enemies(EnemiesProbeDto.builder().type(EnemyType.MECH).number(10).build())
                .coordinates(CoordinatesProbeDto.builder().x(11).y(20).build())
                .build();
        var result = cannonService.findNextTarget(DroidProbeDto.builder()
                .scan(List.of(firstScan, secondScan, thirdScan))
                .protocols(List.of(ProtocolType.ASSIST_ALLIES, ProtocolType.PRIORITIZE_MECH))
                .build());
        assertEquals(firstScan, result);
    }

    @Test
    void findNextTarget_filterReturnEmpty() {
        var firstScan = ScanProbeDto.builder()
                .enemies(EnemiesProbeDto.builder().type(EnemyType.MECH).number(10).build())
                .coordinates(CoordinatesProbeDto.builder().x(10).y(10).build())
                .allies(10)
                .build();

        CommonEnderRuntimeException exception = assertThrows(CommonEnderRuntimeException.class, () -> {
            cannonService.findNextTarget(DroidProbeDto.builder()
                    .scan(List.of(firstScan))
                    .protocols(List.of(ProtocolType.AVOID_CROSSFIRE))
                    .build());
        });

        assertEquals("Not found proper position for attack after filtering.", exception.getMessage());
    }

}