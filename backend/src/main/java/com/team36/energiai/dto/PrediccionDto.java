package com.team36.energiai.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.team36.energiai.model.Categoria;

public record PrediccionDto(

        @JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_VALUES)
        Categoria categoria,

        Double probabilidad
) {
    public PrediccionDto {
        if (categoria == null) {
            throw new IllegalArgumentException("categoria no puede ser null");
        }
    }
}
