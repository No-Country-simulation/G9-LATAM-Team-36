package com.team36.energiai.client;

import com.team36.energiai.dto.AnalisisRequest;
import com.team36.energiai.dto.PrediccionDto;
import com.team36.energiai.model.Categoria;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class MlClientMock implements MlClient {

    @Override
    public PrediccionDto predecir(AnalisisRequest req) {
        if (req.consumoKwh() > 400 || req.horasAltoConsumo() > 8) {
            return new PrediccionDto(Categoria.INEFICIENTE, 0.81);
        }
        if (req.consumoKwh() > 250) {
            return new PrediccionDto(Categoria.MODERADO, 0.65);
        }
        return new PrediccionDto(Categoria.EFICIENTE, 0.80);
    }
}
