package com.ceiba.medisalud.domain.model;

import java.time.LocalDateTime;

/**
 * Represents optional filters used to search appointments.
 *
 * @param doctorId optional doctor identifier filter
 * @param patientId optional patient identifier filter
 * @param status optional appointment status filter
 * @param fechaInicio optional initial date-time filter
 * @param fechaFin optional final date-time filter
 */
public record AppointmentSearchCriteria(
        Long doctorId,
        Long patientId,
        AppointmentStatus status,
        LocalDateTime fechaInicio,
        LocalDateTime fechaFin
) {
}
