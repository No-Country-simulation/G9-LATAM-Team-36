package com.team36.energiai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.team36.energiai.model.Categoria;

import java.math.BigDecimal;
import java.util.List;

public record AnalisisResponse(
    Categoria categoria,

    Double probabilidad,

    List<String> recomendaciones,

    @JsonProperty("costo_estimado_mensual")
    BigDecimal costoEstimadoMensual
) { }
