package com.team36.energiai.service;

import com.team36.energiai.dto.AnalisisRequest;

import java.util.List;

/**
 * Puerto de recomendaciones (Bloque E lo define, Bloque H lo implementa
 * en service/impl/RecomendacionServiceImpl.java sin tocar el resto del código).
 */
public interface RecomendacionService {
    List<String> generar(AnalisisRequest request, String categoria);
}
