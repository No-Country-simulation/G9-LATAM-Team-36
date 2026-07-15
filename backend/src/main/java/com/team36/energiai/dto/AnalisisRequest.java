package com.team36.energiai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

/**
 * Entrada de POST /analisis-energetico según el Contrato 1.
 * Dueño de la estructura: Bloque E. Dueño de las anotaciones @Valid: Bloque F.
 */
public record AnalisisRequest(

        @JsonProperty("consumo_kwh")
        @NotNull @Positive(message = "consumo_kwh debe ser mayor a 0")
        Double consumoKwh,

        @JsonProperty("uso_horario_pico")
        @NotNull
        Boolean usoHorarioPico,

        @JsonProperty("cantidad_equipos")
        @NotNull @Min(value = 1, message = "cantidad_equipos debe ser al menos 1")
        Integer cantidadEquipos,

        @JsonProperty("tipo_inmueble")
        @NotNull
        @Pattern(regexp = "Casa|Departamento|Local", message = "tipo_inmueble debe ser Casa, Departamento o Local")
        String tipoInmueble,

        @JsonProperty("horas_alto_consumo")
        @NotNull @Min(0) @Max(24)
        Integer horasAltoConsumo
) {}
