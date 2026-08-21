package com.team36.energiai.service;

import com.team36.energiai.dto.AnalisisResponse;
import com.team36.energiai.dto.HistorialResponse;
import com.team36.energiai.exception.RecursoNoEncontradoException;
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

    private final AnalisisRepository repository;

    public ConsultaService(AnalisisRepository repository) {
        this.repository = repository;
    }

    public AnalisisResponse obtenerPorId(Long id) {
        Analisis analisis = repository.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("Analisis no encontrado con id " + id));
        return toResponse(analisis);
    }

    public Page<HistorialResponse> obtenerTodos(Pageable pageable) {
        return repository.findAll(pageable).map(this::toHistorialResponse);
    }

    private AnalisisResponse toResponse(Analisis analisis) {
        return new AnalisisResponse(
                Categoria.valueOf(analisis.getCategoria()),
                analisis.getProbabilidad().doubleValue(),
                analisis.getRecomendaciones(),
                analisis.getCostoEstimadoMensual()
        );
    }

    private HistorialResponse toHistorialResponse(Analisis analisis) {
        return new HistorialResponse(
                Categoria.valueOf(analisis.getCategoria()),
                analisis.getProbabilidad().doubleValue(),
                analisis.getRecomendaciones(),
                analisis.getCostoEstimadoMensual(),
                analisis.getCreadoEn(),
                analisis.getConsumoKwh(),
                analisis.getTipoInmueble().name()
        );
    }
}
