package com.team36.energiai.service.impl;

import com.team36.energiai.dto.AnalisisRequest;
import com.team36.energiai.model.Categoria;
import com.team36.energiai.model.TipoInmueble;
import com.team36.energiai.service.RecomendacionService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RecomendacionServiceImplTest {

    private final RecomendacionService service = new RecomendacionServiceImpl();

    @Test
    void perfilIneficiente() {

        AnalisisRequest request = new AnalisisRequest(
                600.0,
                true,
                10,
                TipoInmueble.CASA,
                8
        );

        List<String> recomendaciones =
                service.generar(request, Categoria.INEFICIENTE);

        assertNotNull(recomendaciones);
        assertFalse(recomendaciones.isEmpty());
        assertTrue(recomendaciones.size() >= 2);
        assertTrue(recomendaciones.size() <= 5);
    }

    @Test
    void perfilModerado() {

        AnalisisRequest request = new AnalisisRequest(
                350.0,
                false,
                8,
                TipoInmueble.DEPARTAMENTO,
                5
        );

        List<String> recomendaciones =
                service.generar(request, Categoria.MODERADO);

        assertNotNull(recomendaciones);
        assertFalse(recomendaciones.isEmpty());
        assertTrue(recomendaciones.size() >= 2);
        assertTrue(recomendaciones.size() <= 5);
    }

    @Test
    void perfilEficiente() {

        AnalisisRequest request = new AnalisisRequest(
                180.0,
                false,
                4,
                TipoInmueble.LOCAL,
                2
        );

        List<String> recomendaciones =
                service.generar(request, Categoria.EFICIENTE);

        assertNotNull(recomendaciones);
        assertFalse(recomendaciones.isEmpty());
        assertTrue(recomendaciones.size() >= 2);
        assertTrue(recomendaciones.size() <= 5);
    }
}