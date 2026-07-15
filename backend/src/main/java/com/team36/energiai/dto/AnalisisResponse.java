package com.team36.energiai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.List;

/** Salida compuesta de POST /analisis-energetico según el Contrato 1. */
public record AnalisisResponse(

        String categoria,

        Double probabilidad,

        List<String> recomendaciones,

        @JsonProperty("costo_estimado_mensual")
        BigDecimal costoEstimadoMensual
) {}
