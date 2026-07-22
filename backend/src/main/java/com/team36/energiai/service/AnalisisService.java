package com.team36.energiai.service;

import com.team36.energiai.client.MlClient;
import com.team36.energiai.dto.AnalisisRequest;
import com.team36.energiai.dto.AnalisisResponse;
import com.team36.energiai.dto.PrediccionDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;


@Service
public class AnalisisService {

    private static final BigDecimal TARIFA_KWH = new BigDecimal("0.75");

    private final MlClient mlClient;
    /* private final RecomendacionService recomendacionService; */

    public AnalisisService(MlClient mlClient /*, RecomendacionService recomendacionService */) {
        this.mlClient = mlClient;
        /* this.recomendacionService = recomendacionService; */
    }

    public AnalisisResponse analizar(AnalisisRequest request) {
        PrediccionDto prediccion = mlClient.predecir(request);

        BigDecimal costoEstimado = BigDecimal.valueOf(request.consumoKwh())
                .multiply(TARIFA_KWH)
                .setScale(2, RoundingMode.HALF_UP);

        // var recomendaciones = recomendacionService.generar(request, prediccion.categoria());
        var recomendaciones = List.of("Reduce el uso de electrodomésticos en horas pico.", "Optimiza el rendimiento térmico de tu inmueble.");

        AnalisisResponse response = new AnalisisResponse(
                prediccion.categoria(),
                prediccion.probabilidad(),
                recomendaciones,
                costoEstimado
        );

        // analisisRepository.save(Analisis.desde(request, response));

        return response;
    }
}
