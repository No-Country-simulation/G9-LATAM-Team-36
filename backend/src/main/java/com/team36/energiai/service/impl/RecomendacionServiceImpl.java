package com.team36.energiai.service.impl;

import com.team36.energiai.dto.AnalisisRequest;
import com.team36.energiai.model.Categoria;
import com.team36.energiai.service.RecomendacionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecomendacionServiceImpl implements RecomendacionService {
    @Override
    public List<String> generar(AnalisisRequest request, Categoria categoria) {
        return List.of("Recomendacion pendiente de implementar (bloque H)", "Reduce el uso de electrodomésticos en horas pico", "Optimiza el rendimiento térmico de tu inmueble");
    }
}
