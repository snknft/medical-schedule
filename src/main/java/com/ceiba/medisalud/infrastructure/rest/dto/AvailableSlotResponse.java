package com.ceiba.medisalud.infrastructure.rest.dto;

import java.time.LocalDateTime;

/**
 * Represents the REST response returned for an available appointment slot.
 */
public record AvailableSlotResponse(
        LocalDateTime start,
        LocalDateTime end
) {
}
