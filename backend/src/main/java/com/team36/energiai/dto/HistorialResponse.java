package com.team36.energiai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.team36.energiai.model.Categoria;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record HistorialResponse(
        Categoria categoria,
        Double probabilidad,
        List<String> recomendaciones,
        @JsonProperty("costo_estimado_mensual")
        BigDecimal costoEstimadoMensual,
        @JsonProperty("creado_en")
        LocalDateTime creadoEn,
        @JsonProperty("consumo_kwh")
        Double consumoKwh,
        @JsonProperty("tipo_inmueble")
        String tipoInmueble
    ){}
