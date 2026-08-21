package com.team36.energiai.client;

import com.team36.energiai.dto.AnalisisRequest;
import com.team36.energiai.dto.PrediccionDto;

public interface MlClient {
    PrediccionDto predecir(AnalisisRequest request);
}
