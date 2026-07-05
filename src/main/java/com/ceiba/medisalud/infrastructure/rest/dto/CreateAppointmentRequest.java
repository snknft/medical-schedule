package com.ceiba.medisalud.infrastructure.rest.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;

public record CreateAppointmentRequest(
        @NotNull(message = "patientId es obligatorio")
        Long patientId,

        @NotNull(message = "doctorId es obligatorio")
        Long doctorId,

        @NotNull(message = "appointmentDateTime es obligatorio")
        LocalDateTime appointmentDateTime
) {
}
