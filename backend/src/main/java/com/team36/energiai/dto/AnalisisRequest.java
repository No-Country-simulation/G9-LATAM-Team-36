package com.team36.energiai.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.team36.energiai.model.TipoInmueble;
import jakarta.validation.constraints.*;

public record AnalisisRequest(
    @JsonProperty("consumo_kwh")
    @NotNull(message = "es obligatorio")
    @Positive(message = "debe ser mayor a 0")
    Double consumoKwh,

    @JsonProperty("uso_horario_pico")
    @NotNull(message = "es obligatorio")
    Boolean usoHorarioPico,

    @JsonProperty("cantidad_equipos")
    @NotNull(message = "es obligatorio")
    @Min(value = 1, message = "debe ser al menos 1")
    Integer cantidadEquipos,

    @JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_VALUES)
    @JsonProperty("tipo_inmueble")
    @NotNull(message = "es obligatorio")
    TipoInmueble tipoInmueble,

    @JsonProperty("horas_alto_consumo")
    @NotNull(message = "es obligatorio")
    @Min(value = 0, message = "no puede ser menor a 0")
    @Max(value = 24, message = "no puede ser mayor a 24")
    Integer horasAltoConsumo
) {}
