package battle.station.endor.service;

import battle.station.endor.config.properties.CannonGenerationProperties;
import battle.station.endor.config.properties.MainEndorProperties;
import battle.station.endor.dto.*;
import battle.station.endor.exception.CommonEnderRuntimeException;
import battle.station.endor.exception.RestTemplateException;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class IonCannonService {
    @Autowired
    @Qualifier("rest")
    private RestTemplate restTemplate;
    @Autowired
    private MainEndorProperties appProperties;
    private static final int MAX_GUN_AMOUNT = 3;
    private final PriorityBlockingQueue<CannonGenerationProperties> gunAvailableQueue = new PriorityBlockingQueue<>(MAX_GUN_AMOUNT);
    private final ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(MAX_GUN_AMOUNT);

    @PostConstruct
    public void init() {
        gunAvailableQueue.add(appProperties.getCannon().getIonGeneration1());
        gunAvailableQueue.add(appProperties.getCannon().getIonGeneration2());
        gunAvailableQueue.add(appProperties.getCannon().getIonGeneration3());
    }

    @NotNull
    public ScanProbeDto findNextTarget(@NotNull DroidProbeDto droidProbeDto) {
        log.debug("Get Probe: {}\n", droidProbeDto);
        List<ScanProbeDto> closest = new ArrayList<>(droidProbeDto.getScan().stream()
                .filter(it -> it.getDistance() < appProperties.getRadiusOfSight()).toList());
        if (closest.isEmpty()) {
            throw new CommonEnderRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR, "Not found proper position for attack");
        }
        for (ProtocolType protocolType : droidProbeDto.getProtocols()) {
            closest.sort(protocolType.getComparator());
        }
        return closest.getFirst();
    }

    @NotNull
    public FireIonCannonResponseDto fireAvailableGun(@NotNull FireIonCannonDto fireIonCannonDto) {
        int attempt = 0;
        FireIonCannonResponseDto fireResponse = null;
        while (attempt < appProperties.getCannon().getFireRetry() && fireResponse == null) {
            ++attempt;

            CannonGenerationProperties gun = null;
            try {
                gun = gunAvailableQueue.take();
                log.debug("Get ion cannon {} from queue", gun);
                var status = checkStatus(gun);
                if (status.isAvailable()) {
                    fireResponse = fire(gun, fireIonCannonDto);
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
                throw new CommonEnderRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            } catch (RestTemplateException e) {
                rechargeIonCannon(gun);
                log.error("Rest IonCannon communication exception", e);
                throw new CommonEnderRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            }
        }
        if (Objects.isNull(fireResponse)) {
            throw new CommonEnderRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR, "Attempt amount of searching available exceeded");
        }
        return fireResponse;
    }

    private void rechargeIonCannon(@NotNull CannonGenerationProperties gun) {
        scheduledExecutor.schedule(() -> {
            log.debug("Ion cannon {} is recharged to {}", gun.getGeneration(), gun.getFireTime());
            gunAvailableQueue.offer(gun);
        }, gun.getFireTime(), TimeUnit.MICROSECONDS);
    }

    @NotNull
    private IonCannonStatusDto checkStatus(@NotNull CannonGenerationProperties cannonProperties) throws RestTemplateException {
        String url = String.format("http://%s:%d/%s", cannonProperties.getHost(), cannonProperties.getPort(), appProperties.getCannon().getStatusUrl());
        log.debug("Check Ion Cannon Status up {}", url);
        IonCannonStatusDto result;
        try {
            result = restTemplate.getForObject(url, IonCannonStatusDto.class);
        } catch (ResourceAccessException e) {
            throw new RestTemplateException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        if (Objects.isNull(result)) {
            throw new RestTemplateException(HttpStatus.INTERNAL_SERVER_ERROR, "checkStatus return empty response");
        }
        log.debug("Status of ion gun {} is {}", cannonProperties.getGeneration(), result);
        return result;
    }

    @NotNull
    private FireIonCannonResponseDto fire(@NotNull CannonGenerationProperties cannonProperties, @NotNull FireIonCannonDto fireIonCannonDto) throws RestTemplateException {
        String url = String.format("http://%s:%d/%s", cannonProperties.getHost(), cannonProperties.getPort(), appProperties.getCannon().getFireUrl());
        log.debug("Fire Ion Cannon {}\n{}", url, fireIonCannonDto);
        FireIonCannonResponseDto result;
        try {
            result = restTemplate.postForObject(url, fireIonCannonDto, FireIonCannonResponseDto.class);
        } catch (ResourceAccessException e) {
            throw new RestTemplateException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        if (Objects.isNull(result)) {
            throw new RestTemplateException(HttpStatus.INTERNAL_SERVER_ERROR, "fire return empty response");
        }
        log.debug("Fire response of ion gun {} is {}", cannonProperties.getGeneration(), result);
        return result;
    }

    @PreDestroy
    public void destroy() {
        scheduledExecutor.shutdown();
    }
}
