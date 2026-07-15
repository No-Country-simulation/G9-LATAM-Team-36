package com.team36.energiai.service.impl;

import com.team36.energiai.dto.AnalisisRequest;
import com.team36.energiai.service.RecomendacionService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Bloque H — Motor de recomendaciones.
 * TODO (Bloque H): reemplazar esta implementación placeholder por el motor
 * de reglas real (ver guía completa del Bloque H en Notion: tabla de
 * condición -> texto, mínimo 10 reglas, ordenadas por prioridad).
 */
@Service
public class RecomendacionServiceImpl implements RecomendacionService {

    @Override
    public List<String> generar(AnalisisRequest req, String categoria) {
        // Placeholder: las 3 recomendaciones fijas del brief.
        // El Bloque H debe hacer que varíen según categoria y features.
        return List.of(
                "Reducir el uso de equipos durante horarios pico",
                "Evaluar aparatos con alto consumo energético",
                "Distribuir actividades de mayor consumo a lo largo del día"
        );
    }
}
