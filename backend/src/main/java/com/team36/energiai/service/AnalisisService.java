package com.team36.energiai.service;

import com.team36.energiai.client.MlClient;
import com.team36.energiai.dto.AnalisisRequest;
import com.team36.energiai.dto.AnalisisResponse;
import com.team36.energiai.dto.PrediccionDto;
import com.team36.energiai.model.Analisis;
import com.team36.energiai.repository.AnalisisRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;


@Service
public class AnalisisService {

    private final MlClient mlClient;
    private final CalculoFinancieroService calculoFinancieroService;
    private final RecomendacionService recomendacionService;

    public AnalisisService(MlClient mlClient, CalculoFinancieroService calculoFinancieroService, RecomendacionService recomendacionService) {
        this.mlClient = mlClient;
        this.calculoFinancieroService = calculoFinancieroService;
        this.recomendacionService = recomendacionService;
    }

    public AnalisisResponse analizar(AnalisisRequest request) {
        PrediccionDto prediccion = mlClient.predecir(request);

        BigDecimal costoEstimado = calculoFinancieroService.calcularCostoEstimado(request.consumoKwh());

        var recomendaciones = recomendacionService.generar(request, prediccion.categoria());

        AnalisisResponse response = new AnalisisResponse(
                prediccion.categoria(),
                prediccion.probabilidad(),
                recomendaciones,
                costoEstimado
        );

        //analisisRepository.save(Analisis.desde(request, response));

        return response;
    }
}
