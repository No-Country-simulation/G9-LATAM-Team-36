package com.team36.energiai.service;

import com.team36.energiai.client.MlClient;
import com.team36.energiai.dto.AnalisisRequest;
import com.team36.energiai.dto.PrediccionDto;
import com.team36.energiai.model.Categoria;
import com.team36.energiai.model.TipoInmueble;
import com.team36.energiai.repository.AnalisisRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AnalisisServiceTest {

    private AnalisisService analisisService;
    private AnalisisRequest request;
    private AtomicInteger guardados;

    @BeforeEach
    void setUp() {
        guardados = new AtomicInteger();
        AnalisisRepository analisisRepository = (AnalisisRepository) Proxy.newProxyInstance(
                AnalisisRepository.class.getClassLoader(),
                new Class<?>[]{AnalisisRepository.class},
                (proxy, method, args) -> {
                    if (method.getName().equals("save")) {
                        guardados.incrementAndGet();
                        return args[0];
                    }
                    return null;
                }
        );
        MlClient mlClient = ignored -> new PrediccionDto(Categoria.INEFICIENTE, 0.97);
        CalculoFinancieroService calculoFinancieroService =
                new CalculoFinancieroService(new java.math.BigDecimal("0.75"));
        RecomendacionService recomendacionService =
                (ignored, categoria) -> List.of("Reducir el consumo en horario pico");

        analisisService = new AnalisisService(
                mlClient,
                calculoFinancieroService,
                recomendacionService,
                analisisRepository
        );
        request = new AnalisisRequest(420.0, true, 10, TipoInmueble.CASA, 8);
    }

    @Test
    void guardaElAnalisisCuandoPersistirEsTrue() {
        analisisService.analizar(request, true);

        assertEquals(1, guardados.get());
    }

    @Test
    void noGuardaElAnalisisCuandoPersistirEsFalse() {
        analisisService.analizar(request, false);

        assertEquals(0, guardados.get());
    }
}
