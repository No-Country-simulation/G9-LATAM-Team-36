package com.team36.energiai.controller;

import com.team36.energiai.dto.AnalisisRequest;
import com.team36.energiai.dto.AnalisisResponse;
import com.team36.energiai.model.Categoria;
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
@RequestMapping("/api/analisis-energetico")
public class AnalisisController {

    @PostMapping
    public ResponseEntity<AnalisisResponse> analizar(@RequestBody @Valid AnalisisRequest request) {
        AnalisisResponse mockResponse = new AnalisisResponse(
                Categoria.INEFICIENTE,
                0.88,
                List.of("Reduce el uso de electrodomésticos en horas pico.", "Optimiza el rendimiento térmico de tu inmueble."),
                BigDecimal.valueOf(45.50)
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(mockResponse);
    }
}
