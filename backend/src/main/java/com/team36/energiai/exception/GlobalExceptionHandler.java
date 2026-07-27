package com.team36.energiai.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.team36.energiai.dto.ErrorResponse;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import tools.jackson.databind.exc.MismatchedInputException;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;


@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new LinkedHashMap<>();
        Class<?> targetClass = ex.getBindingResult().getTarget().getClass();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            if (error instanceof FieldError fieldError) {
                String jsonFieldName = resolveJsonFieldName(fieldError, targetClass);
                errors.put(jsonFieldName, fieldError.getDefaultMessage());
            } else {
                errors.put(error.getObjectName(), error.getDefaultMessage());
            }
        });

        ErrorResponse errorPayload = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Error de validación en los datos enviados",
                errors
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorPayload);
    }

    private String resolveJsonFieldName(FieldError fieldError, Class<?> targetClass) {
        try {
            Field field = targetClass.getDeclaredField(fieldError.getField());
            JsonProperty jsonProperty = field.getAnnotation(JsonProperty.class);
            return jsonProperty != null ? jsonProperty.value() : fieldError.getField();
        } catch (NoSuchFieldException e) {
            return fieldError.getField();
        }
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleUnreadable(HttpMessageNotReadableException ex) {
        String detalle = "El cuerpo de la solicitud no es un JSON válido";
        Throwable cause = ex.getCause();

        if (cause instanceof MismatchedInputException mie && !mie.getPath().isEmpty()) {
            String campo = mie.getPath().getFirst().getPropertyName();

            if (mie.getTargetType() != null && mie.getTargetType().isEnum()) {
                String valoresValidos = Arrays.stream(mie.getTargetType().getEnumConstants())
                        .map(Object::toString)
                        .collect(Collectors.joining(", "));
                detalle = "El campo '" + campo + "' debe ser uno de los siguientes valores: " + valoresValidos;
            } else {
                detalle = "El campo '" + campo + "' tiene un valor inválido";
            }
        }

        ErrorResponse errorPayload = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                detalle,
                Map.of()
        );
        return ResponseEntity.badRequest().body(errorPayload);
    }

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleRecursoNoEncontrado(RecursoNoEncontradoException ex) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                Map.of()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ErrorResponse> handleParametrosInvalidos(HandlerMethodValidationException ex) {
        Map<String, String> errors = new LinkedHashMap<>();

        ex.getParameterValidationResults().forEach(result -> {
            String nombreParametro = result.getMethodParameter().getParameterName();
            result.getResolvableErrors().forEach(error ->
                    errors.put(nombreParametro, error.getDefaultMessage())
            );
        });

        ErrorResponse errorPayload = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Parametros de solicitud invalidos",
                errors
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorPayload);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTipoInvalido(MethodArgumentTypeMismatchException ex) {
        Map<String, String> errors = new LinkedHashMap<>();
        String tipoEsperado = ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "valor valido";
        errors.put(ex.getName(), "debe ser un " + tipoEsperado);

        ErrorResponse errorPayload = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Parametros de solicitud invalidos",
                errors
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorPayload);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(NoResourceFoundException ex) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Ruta no encontrada: /" + ex.getResourcePath(),
                Map.of()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(MlServiceUnavailableException.class)
    public ResponseEntity<ErrorResponse> handleMlUnavailable(MlServiceUnavailableException ex) {
        log.error("Fallo al comunicarse con el servicio ML: {}", ex.getMessage(), ex);

        ErrorResponse error = new ErrorResponse(
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                "El servicio de analisis no esta disponible en este momento",
                Map.of()
        );
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        log.error("Error inesperado no manejado especificamente", ex);

        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Ocurrio un error inesperado, intenta de nuevo mas tarde",
                Map.of()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

}
