package com.team36.energiai.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * Bloque F — Manejo centralizado de errores.
 * Formato de error unificado: {timestamp, status, errores: [{campo, mensaje}]}
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        List<Map<String, String>> errores = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> Map.of("campo", e.getField(), "mensaje", e.getDefaultMessage()))
                .toList();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(cuerpoError(HttpStatus.BAD_REQUEST, errores));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleJsonMalformado(HttpMessageNotReadableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(cuerpoError(HttpStatus.BAD_REQUEST,
                        List.of(Map.of("campo", "body", "mensaje", "JSON malformado"))));
    }

    @ExceptionHandler(MlServiceUnavailableException.class)
    public ResponseEntity<Map<String, Object>> handleMlUnavailable(MlServiceUnavailableException ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(cuerpoError(HttpStatus.SERVICE_UNAVAILABLE,
                        List.of(Map.of("campo", "-", "mensaje", ex.getMessage()))));
    }

    // TODO (Bloque G): agregar handler de EntityNotFoundException -> 404
    // cuando se implementen los endpoints de consulta.

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenerico(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(cuerpoError(HttpStatus.INTERNAL_SERVER_ERROR,
                        List.of(Map.of("campo", "-", "mensaje", "Error interno del servidor"))));
    }

    private Map<String, Object> cuerpoError(HttpStatus status, List<Map<String, String>> errores) {
        return Map.of(
                "timestamp", Instant.now().toString(),
                "status", status.value(),
                "errores", errores
        );
    }
}
