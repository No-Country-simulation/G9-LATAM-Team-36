package com.team36.energiai.client;

import com.team36.energiai.dto.AnalisisRequest;
import com.team36.energiai.dto.PrediccionDto;
import com.team36.energiai.exception.MlServiceUnavailableException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
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
                .requestFactory(new SimpleClientHttpRequestFactory())
                .build();
    }

    @Override
    public PrediccionDto predecir(AnalisisRequest request) {
        try {
            return restClient.post()
                    .uri("/predict")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .body(PrediccionDto.class);
        } catch (RestClientException ex) {
            throw new MlServiceUnavailableException(
                    "No se pudo obtener la prediccion del servicio ML", ex
            );
        }
    }
}