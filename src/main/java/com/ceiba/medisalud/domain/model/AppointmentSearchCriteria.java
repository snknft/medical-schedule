package com.ceiba.medisalud.domain.model;

import java.time.LocalDateTime;


/**
 * Represents optional filters used to search appointments.
 */
public record AppointmentSearchCriteria(
        Long doctorId,
        Long patientId,
        AppointmentStatus status,
        LocalDateTime fechaInicio,
        LocalDateTime fechaFin
) {
}
