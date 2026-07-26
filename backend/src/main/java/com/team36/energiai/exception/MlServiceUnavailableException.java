package com.team36.energiai.exception;

public class MlServiceUnavailableException extends RuntimeException {
    public MlServiceUnavailableException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}