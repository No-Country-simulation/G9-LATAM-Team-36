package com.team36.energiai.client;

import com.team36.energiai.dto.AnalisisRequest;
import com.team36.energiai.dto.PrediccionDto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
@Profile("prod")
public class MlClientHttp implements MlClient {

    private final RestClient restClient;

    public MlClientHttp(@Value("${ml.service.url}") String mlServiceUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(mlServiceUrl)
                .build();
    }

    @Override
    public PrediccionDto predecir(AnalisisRequest request) {

        return restClient.post()
                .uri("/predecir")
                .body(request)
                .retrieve()
                .body(PrediccionDto.class);

    }
}