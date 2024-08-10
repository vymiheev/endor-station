package battle.station.endor.service.impl;

import battle.station.endor.config.properties.CannonGenerationProperties;
import battle.station.endor.config.properties.MainEndorProperties;
import battle.station.endor.dto.FireIonCannonDto;
import battle.station.endor.dto.FireIonCannonResponseDto;
import battle.station.endor.exception.CommonEnderRuntimeException;
import battle.station.endor.exception.RestTemplateException;
import battle.station.endor.service.IonCannonRestClient;
import battle.station.endor.service.IonCannonService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class IonCannonServiceImpl implements IonCannonService {
    @Autowired
    private IonCannonRestClient serviceRestClient;
    @Autowired
    private MainEndorProperties appProperties;
    private static final int MAX_GUN_AMOUNT = 3;
    private final PriorityBlockingQueue<CannonGenerationProperties> gunAvailableQueue = new PriorityBlockingQueue<>(MAX_GUN_AMOUNT,
            Comparator.comparingInt(CannonGenerationProperties::getGeneration));
    private final ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(MAX_GUN_AMOUNT);

    @PostConstruct
    public void init() {
        gunAvailableQueue.add(appProperties.getCannon().getIonGeneration1());
        gunAvailableQueue.add(appProperties.getCannon().getIonGeneration2());
        gunAvailableQueue.add(appProperties.getCannon().getIonGeneration3());
    }

    @NotNull
    public FireIonCannonResponseDto fireAvailableGun(@NotNull FireIonCannonDto fireIonCannonDto) {
        FireIonCannonResponseDto fireResponse = null;
        while (Objects.isNull(fireResponse)) {
            CannonGenerationProperties gun = null;
            try {
                gun = gunAvailableQueue.take();
                log.debug("Get ion cannon {} from queue", gun);
                var status = serviceRestClient.checkStatus(gun);
                if (status.isAvailable()) {
                    fireResponse = serviceRestClient.fire(gun, fireIonCannonDto);
                    rechargeIonCannon(gun);
                } else {
                    gunAvailableQueue.add(gun);
                    log.debug("Ion cannon {} is unavailable. Sleeping...", gun.getGeneration());
                    Thread.sleep(200);
                }
            } catch (InterruptedException e) {
                if (gunAvailableQueue.size() != MAX_GUN_AMOUNT) {
                    gunAvailableQueue.offer(gun);
                }
                log.error("Queue accessing interrupted", e);
                throw new CommonEnderRuntimeException(e.getMessage());
            } catch (RestTemplateException e) {
                rechargeIonCannon(gun);
                log.error("Rest IonCannon communication exception", e);
                throw new CommonEnderRuntimeException(e.getMessage());
            }
        }
        return fireResponse;
    }

    private void rechargeIonCannon(@NotNull CannonGenerationProperties gun) {
        scheduledExecutor.schedule(() -> {
            log.debug("Ion cannon {} is recharged to {}", gun.getGeneration(), gun.getFireTime());
            gunAvailableQueue.offer(gun);
        }, gun.getFireTime(), TimeUnit.MICROSECONDS);
    }

    @PreDestroy
    public void destroy() {
        scheduledExecutor.shutdown();
    }
}
