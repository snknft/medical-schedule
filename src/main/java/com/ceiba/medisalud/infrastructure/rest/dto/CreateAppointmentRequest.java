package com.ceiba.medisalud.infrastructure.rest.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonAlias;

import jakarta.validation.constraints.NotNull;

/**
 * Represents the REST request used to create an appointment.
 *
 * @param patientId patient identifier; also accepts the Spanish alias {@code pacienteId}
 * @param doctorId doctor identifier; also accepts the Spanish alias {@code medicoId}
 * @param appointmentDateTime appointment start date-time in ISO-8601 format
 */
public record CreateAppointmentRequest(
        @JsonAlias("pacienteId")
        @NotNull(message = "patientId es obligatorio")
        Long patientId,

        @JsonAlias("medicoId")
        @NotNull(message = "doctorId es obligatorio")
        Long doctorId,

        @NotNull(message = "appointmentDateTime es obligatorio")
        LocalDateTime appointmentDateTime
) {
}
