package com.team36.energiai.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TipoInmueble {
    CASA("Casa"),
    DEPARTAMENTO("Departamento"),
    LOCAL("Local");

    private final String etiqueta;

    TipoInmueble(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    @JsonValue
    public String getEtiqueta() {
        return etiqueta;
    }

}
