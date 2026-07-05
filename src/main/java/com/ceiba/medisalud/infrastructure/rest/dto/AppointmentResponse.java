package com.ceiba.medisalud.infrastructure.rest.dto;

import java.time.LocalDateTime;

import com.ceiba.medisalud.domain.model.AppointmentStatus;

/**
 * Represents the REST response returned for an appointment.
 */
public record AppointmentResponse(
        Long id,
        Long patientId,
        Long doctorId,
        LocalDateTime appointmentDateTime,
        AppointmentStatus status,
        LocalDateTime cancellationDateTime
) {
}
