package com.team36.energiai.controller;

import com.team36.energiai.dto.AnalisisRequest;
import com.team36.energiai.dto.AnalisisResponse;
import com.team36.energiai.dto.ErrorResponse;
import com.team36.energiai.model.Categoria;
import com.team36.energiai.service.AnalisisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/analisis-energetico")
public class AnalisisController {

    private final AnalisisService analisisService;

    public AnalisisController(AnalisisService analisisService) {
        this.analisisService = analisisService;
    }

    @PostMapping
    @Operation(summary = "Analiza el consumo energetico de una vivienda o establecimiento")
    @ApiResponse(
            responseCode = "201",
            description = "Analisis generado correctamente",
            content = @Content(schema = @Schema(implementation = AnalisisResponse.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "Datos de entrada invalidos o JSON malformado",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    @ApiResponse(
            responseCode = "503",
            description = "Servicio de ML no disponible",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<AnalisisResponse> analizar(@RequestBody @Valid AnalisisRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(analisisService.analizar(request));
    }
}
