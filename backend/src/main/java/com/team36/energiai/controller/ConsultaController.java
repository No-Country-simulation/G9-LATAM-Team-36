package com.team36.energiai.controller;

import com.team36.energiai.repository.AnalisisRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Bloque G — Endpoints de consulta (segundo endpoint obligatorio del brief).
 * Controller propio para no tocar AnalisisController (dueño: Bloque E).
 */
@RestController
public class ConsultaController {

    private final AnalisisRepository repository;

    public ConsultaController(AnalisisRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/analisis/{id}")
    public Object obtenerPorId(@PathVariable Long id) {
        // TODO (Bloque G): lanzar EntityNotFoundException si no existe (F la mapea a 404)
        return repository.findById(id).orElseThrow();
    }

    @GetMapping("/analisis")
    public Page<?> listar(Pageable pageable) {
        // TODO (Bloque G): mapear entidad -> DTO de respuesta, ordenar por creadoEn desc
        return repository.findAll(pageable);
    }
}
