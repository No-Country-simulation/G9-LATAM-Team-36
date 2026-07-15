package com.team36.energiai.client;

import com.team36.energiai.dto.AnalisisRequest;
import com.team36.energiai.dto.PrediccionDto;

/**
 * Puerto hacia el microservicio ML (Bloque D).
 * Implementaciones: MlClientMock (perfil dev, no requiere que D esté listo)
 * y MlClientHttp (perfil prod, llama al servicio real vía Contrato 2).
 */
public interface MlClient {
    PrediccionDto predecir(AnalisisRequest request);
}
