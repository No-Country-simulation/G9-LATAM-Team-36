package com.team36.energiai.controller;

import com.team36.energiai.dto.AnalisisRequest;
import com.team36.energiai.dto.AnalisisResponse;
import com.team36.energiai.service.AnalisisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/** Bloque E — Endpoint principal del MVP (Contrato 1). */
@RestController
public class AnalisisController {

    private final AnalisisService analisisService;

    public AnalisisController(AnalisisService analisisService) {
        this.analisisService = analisisService;
    }

    @PostMapping("/analisis-energetico")
    @Operation(summary = "Analiza el consumo energético y devuelve clasificación, recomendaciones y costo")
    @ApiResponse(responseCode = "200", description = "Análisis realizado correctamente")
    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    @ApiResponse(responseCode = "503", description = "Servicio de análisis no disponible")
    public ResponseEntity<AnalisisResponse> analizar(@Valid @RequestBody AnalisisRequest request) {
        return ResponseEntity.ok(analisisService.analizar(request));
    }
}
