package com.ceiba.medisalud.domain.model;

import java.time.LocalDateTime;

/**
 * Represents a 30-minute appointment slot returned by the working hours policy.
 */
public record AvailableSlot(
        LocalDateTime start,
        LocalDateTime end
) {
}
