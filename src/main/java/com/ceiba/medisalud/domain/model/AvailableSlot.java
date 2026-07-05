package com.ceiba.medisalud.domain.model;

import java.time.LocalDateTime;

/**
 * Represents a 30-minute appointment slot returned by the working hours policy.
 *
 * @param start slot start date-time
 * @param end slot end date-time
 */
public record AvailableSlot(
        LocalDateTime start,
        LocalDateTime end
) {
}
