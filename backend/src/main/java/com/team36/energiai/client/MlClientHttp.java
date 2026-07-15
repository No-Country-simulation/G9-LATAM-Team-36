package com.team36.energiai.client;

import com.team36.energiai.dto.AnalisisRequest;
import com.team36.energiai.dto.PrediccionDto;
import com.team36.energiai.exception.MlServiceUnavailableException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/**
 * Bloque E — Implementación real, activa en perfil "prod".
 * Llama al Bloque D (FastAPI) según el Contrato 2.
 */
@Component
@Profile("prod")
public class MlClientHttp implements MlClient {

    private final RestClient restClient;

    public MlClientHttp(@Value("${ml.service.url}") String mlServiceUrl) {
        this.restClient = RestClient.builder().baseUrl(mlServiceUrl).build();
    }

    @Override
    public PrediccionDto predecir(AnalisisRequest req) {
        try {
            return restClient.post()
                    .uri("/predict")
                    .body(req)
                    .retrieve()
                    .body(PrediccionDto.class);
        } catch (Exception e) {
            throw new MlServiceUnavailableException("El servicio de análisis no está disponible", e);
        }
    }
}
