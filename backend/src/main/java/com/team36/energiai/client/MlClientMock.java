package com.team36.energiai.client;

import com.team36.energiai.dto.AnalisisRequest;
import com.team36.energiai.dto.PrediccionDto;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Bloque E — Mock activo en perfil "dev". Permite construir y probar toda
 * la API sin que el Bloque D (Python) esté listo todavía.
 */
@Component
@Profile("dev")
public class MlClientMock implements MlClient {

    @Override
    public PrediccionDto predecir(AnalisisRequest req) {
        if (req.consumoKwh() > 400 || req.horasAltoConsumo() > 8) {
            return new PrediccionDto("Ineficiente", 0.81);
        }
        if (req.consumoKwh() > 250) {
            return new PrediccionDto("Moderado", 0.65);
        }
        return new PrediccionDto("Eficiente", 0.80);
    }
}
