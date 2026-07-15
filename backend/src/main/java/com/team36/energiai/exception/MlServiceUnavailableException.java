package com.team36.energiai.exception;

/** Bloque F — Lanzada por MlClientHttp cuando el Bloque D no responde. */
public class MlServiceUnavailableException extends RuntimeException {
    public MlServiceUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
