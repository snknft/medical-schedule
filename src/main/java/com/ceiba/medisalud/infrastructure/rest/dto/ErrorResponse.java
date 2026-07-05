package com.ceiba.medisalud.infrastructure.rest.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents the standardized REST error response.
 */
public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path,
        String correlationId,
        List<String> details
) {
}
