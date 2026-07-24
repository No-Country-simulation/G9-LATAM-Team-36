package com.team36.energiai.service;

import com.team36.energiai.dto.AnalisisResponse;
import com.team36.energiai.model.Analisis;
import com.team36.energiai.model.Categoria;
import com.team36.energiai.repository.AnalisisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@Transactional
public class ConsultaService {
    @Autowired
    private AnalisisRepository repository;

    public AnalisisResponse obtenerPorId(Long id) {
        // cambiar excepcion
        Analisis analisis=repository.findById(id).orElseThrow(() -> new NoSuchElementException("Analisis no encontrado. ID: "+id));
        return toResponse(analisis);
    }

    public Page<AnalisisResponse> obtenerTodos(Pageable pageable) {
        return repository.findAll(pageable).map(this::toResponse);
    }

    private AnalisisResponse toResponse(Analisis analisis) {
        return new AnalisisResponse(
                Categoria.valueOf(analisis.getCategoria()),
                analisis.getProbabilidad().doubleValue(),
                analisis.getRecomendaciones(),
                analisis.getCostoEstimadoMensual()
        );
    }
}
