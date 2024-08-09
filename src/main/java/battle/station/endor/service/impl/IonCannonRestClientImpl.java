package battle.station.endor.service.impl;

import battle.station.endor.config.properties.CannonGenerationProperties;
import battle.station.endor.config.properties.MainEndorProperties;
import battle.station.endor.dto.FireIonCannonDto;
import battle.station.endor.dto.FireIonCannonResponseDto;
import battle.station.endor.dto.IonCannonStatusDto;
import battle.station.endor.exception.RestTemplateException;
import battle.station.endor.service.IonCannonRestClient;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Objects;

@Service
@Slf4j
public class IonCannonRestClientImpl implements IonCannonRestClient {
    @Autowired
    @Qualifier("rest")
    private RestTemplate restTemplate;
    @Autowired
    private MainEndorProperties appProperties;

    @NotNull
    public IonCannonStatusDto checkStatus(@NotNull CannonGenerationProperties cannonProperties) throws RestTemplateException {
        String url = String.format("http://%s/%s", cannonProperties.getServiceUrl(), appProperties.getCannon().getStatusUrl());
        URI uri = URI.create(url);
        log.debug("Check Ion Cannon Status up {}", url);
        IonCannonStatusDto result;
        try {
            result = restTemplate.getForObject(uri, IonCannonStatusDto.class);
        } catch (ResourceAccessException e) {
            throw new RestTemplateException(e.getMessage());
        }
        if (Objects.isNull(result)) {
            throw new RestTemplateException("CheckStatus return empty response");
        }
        log.debug("Status of ion gun {} is {}", cannonProperties.getGeneration(), result);
        return result;
    }

    @NotNull
    public FireIonCannonResponseDto fire(@NotNull CannonGenerationProperties cannonProperties, @NotNull FireIonCannonDto fireIonCannonDto) throws RestTemplateException {
        String url = String.format("http://%s/%s",  cannonProperties.getServiceUrl(), appProperties.getCannon().getFireUrl());
        URI uri = URI.create(url);
        log.debug("Fire Ion Cannon {}\n{}", url, fireIonCannonDto);
        FireIonCannonResponseDto result;
        try {
            result = restTemplate.postForObject(uri, fireIonCannonDto, FireIonCannonResponseDto.class);
        } catch (ResourceAccessException e) {
            throw new RestTemplateException(e.getMessage());
        }
        if (Objects.isNull(result)) {
            throw new RestTemplateException("Fire return empty response");
        }
        log.debug("Fire response of ion gun {} is {}", cannonProperties.getGeneration(), result);
        return result;
    }
}
