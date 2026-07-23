package com.team36.energiai.repository;

import com.team36.energiai.dto.AnalisisRequest;
import com.team36.energiai.dto.AnalisisResponse;
import com.team36.energiai.model.Analisis;
import com.team36.energiai.model.Categoria;
import com.team36.energiai.model.TipoInmueble;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

@DataJpaTest
@ActiveProfiles("test")
class AnalisisRepositoryTest {
    @Autowired
    private AnalisisRepository repository;

    @Test
    void testGuardarYRecuperarAnalisis(){
        // crear el analisis
        AnalisisRequest request= new AnalisisRequest(
          450.5,
          true,
          8,
          TipoInmueble.CASA,
          4
        );

        // crea el response
        AnalisisResponse response= new AnalisisResponse(
                Categoria.INEFICIENTE,
                0.75,
                Arrays.asList(
                        "recomendacion 1",
                        "recomendacion 2",
                        "recomendacion 3"
                ),
                new BigDecimal("1230.5")
        );

        // crear la entidad y sar el metodo desde
        Analisis analisis = Analisis.desde(request, response);

        // persistir
        Analisis analisisGuardado = repository.save(analisis);

        // consultar
        Optional<Analisis> analisisRecuperadoOpt = repository.findById(analisisGuardado.getId());

        // verificar una respuesta
        assertTrue(analisisRecuperadoOpt.isPresent());
        Analisis analisisRecuperado = analisisRecuperadoOpt.get();

        // matchear los datos del request
        assertEquals(450.5, analisisRecuperado.getConsumoKwh());
        assertTrue(analisisRecuperado.getUsoHorarioPico());
        assertEquals(8, analisisRecuperado.getCantidadEquipos());
        assertEquals(TipoInmueble.CASA, analisisRecuperado.getTipoInmueble());
        assertEquals(4, analisisRecuperado.getHorasAltoConsumo());

        // matchear los datos del response
        assertEquals("INEFICIENTE", analisisRecuperado.getCategoria());
        assertEquals(new BigDecimal("0.75"), analisisRecuperado.getProbabilidad());
        assertEquals(new BigDecimal("1230.5"), analisisRecuperado.getCostoEstimadoMensual());
        assertEquals(3, analisisRecuperado.getRecomendaciones().size());
        assertEquals("recomendacion 1",
                analisisRecuperado.getRecomendaciones().get(0));

        // verificar que se creo el id y la fecha
        assertNotNull(analisisRecuperado.getId());
        assertNotNull(analisisRecuperado.getCreadoEn());

        System.out.println("  Analisis guardado:");
        System.out.println(analisisRecuperado);
    }
}