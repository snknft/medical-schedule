package com.ceiba.medisalud.infrastructure.rest.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;

public record RescheduleAppointmentRequest(
        @NotNull(message = "newDateTime es obligatorio")
        LocalDateTime newDateTime
) {
}
