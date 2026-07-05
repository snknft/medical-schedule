package com.ceiba.medisalud.application.query;

import java.time.LocalDate;

/**
 * Carries the data required to calculate available appointment slots for a doctor.
 *
 * @param doctorId doctor identifier
 * @param fechaInicio first date included in the availability range
 * @param fechaFin last date included in the availability range
 */
public record AvailableSlotsQuery(
        Long doctorId,
        LocalDate fechaInicio,
        LocalDate fechaFin
) {
}
