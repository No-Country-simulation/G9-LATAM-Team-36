package com.team36.energiai.service;

import com.team36.energiai.client.MlClient;
import com.team36.energiai.dto.AnalisisRequest;
import com.team36.energiai.dto.AnalisisResponse;
import com.team36.energiai.dto.PrediccionDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Bloque E — Orquesta el flujo completo de POST /analisis-energetico:
 * 1) predicción vía MlClient (Bloque D)
 * 2) cálculo del costo estimado (tarifa fija $0.75/kWh)
 * 3) recomendaciones vía RecomendacionService (Bloque H)
 * 4) TODO (Bloque G): persistir el análisis antes de retornar
 */
@Service
public class AnalisisService {

    private static final BigDecimal TARIFA_KWH = new BigDecimal("0.75");

    private final MlClient mlClient;
    private final RecomendacionService recomendacionService;

    public AnalisisService(MlClient mlClient, RecomendacionService recomendacionService) {
        this.mlClient = mlClient;
        this.recomendacionService = recomendacionService;
    }

    public AnalisisResponse analizar(AnalisisRequest request) {
        PrediccionDto prediccion = mlClient.predecir(request);

        BigDecimal costoEstimado = BigDecimal.valueOf(request.consumoKwh())
                .multiply(TARIFA_KWH)
                .setScale(2, RoundingMode.HALF_UP);

        var recomendaciones = recomendacionService.generar(request, prediccion.categoria());

        // TODO (Bloque G): guardar `request` + resultado en el repositorio JPA aquí.

        return new AnalisisResponse(
                prediccion.categoria(),
                prediccion.probabilidad(),
                recomendaciones,
                costoEstimado
        );
    }
}
