package com.team36.energiai.controller;

import com.team36.energiai.dto.AnalisisResponse;
import com.team36.energiai.dto.ErrorResponse;
import com.team36.energiai.service.ConsultaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/analisis-energetico")
public class ConsultaController {

    private final ConsultaService consultaService;

    public ConsultaController(ConsultaService consultaService) {
        this.consultaService = consultaService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtiene un analisis por su id")
    @ApiResponse(
            responseCode = "200",
            description = "Analisis encontrado",
            content = @Content(schema = @Schema(implementation = AnalisisResponse.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "No existe un analisis con ese id",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<AnalisisResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(consultaService.obtenerPorId(id));
    }

    @GetMapping
    @Operation(summary = "Lista todos los analisis, paginados")
    @ApiResponse(
            responseCode = "200",
            description = "Listado paginado de analisis",
            content = @Content(schema = @Schema(implementation = Page.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "Parametros de paginacion invalidos (page/size fuera de rango o no numericos)",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<Page<AnalisisResponse>> obtenerTodos(
            @RequestParam(defaultValue = "0") @Min(value = 0, message = "debe ser mayor o igual a 0") int page,
            @RequestParam(defaultValue = "10") @Min(value = 1, message = "debe ser mayor o igual a 1") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("creadoEn").descending());
        Page<AnalisisResponse> resultado = consultaService.obtenerTodos(pageable);
        return ResponseEntity.ok(resultado);
    }
}
