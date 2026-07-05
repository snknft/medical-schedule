package com.ceiba.medisalud.application.query;

import java.time.LocalDate;

/**
 * Carries the search criteria required to calculate available appointment slots.
 */
public record AvailableSlotsQuery(
        Long doctorId,
        LocalDate fechaInicio,
        LocalDate fechaFin
) {
}
