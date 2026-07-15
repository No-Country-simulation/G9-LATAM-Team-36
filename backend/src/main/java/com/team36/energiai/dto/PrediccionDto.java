package com.team36.energiai.dto;

/** Respuesta del microservicio ML (POST /predict) según el Contrato 2. */
public record PrediccionDto(String categoria, Double probabilidad) {}
