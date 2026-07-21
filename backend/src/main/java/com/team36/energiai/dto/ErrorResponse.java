package com.team36.energiai.dto;

import java.util.Map;

public record ErrorResponse(
        int status,

        String message,

        Map<String, String> errors
) {
    public ErrorResponse {
        if (errors == null) {
            errors = Map.of();
        }
    }
}
