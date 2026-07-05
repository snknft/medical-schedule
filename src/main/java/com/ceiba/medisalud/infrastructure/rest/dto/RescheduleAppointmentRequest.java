package com.ceiba.medisalud.infrastructure.rest.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;

/**
 * Represents the REST request used to reschedule an appointment.
 */
public record RescheduleAppointmentRequest(
        @NotNull(message = "newDateTime es obligatorio")
        LocalDateTime newDateTime
) {
}
