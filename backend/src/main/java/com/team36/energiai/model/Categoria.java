package com.team36.energiai.model;


import com.fasterxml.jackson.annotation.JsonValue;

public enum Categoria {
    INEFICIENTE("Ineficiente"),
    MODERADO("Moderado"),
    EFICIENTE("Eficiente");

    private final String etiqueta;

    Categoria(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    @JsonValue
    public String getEtiqueta() {
        return etiqueta;
    }
}
