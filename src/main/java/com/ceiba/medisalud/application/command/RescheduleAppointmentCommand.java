package com.ceiba.medisalud.application.command;

import java.time.LocalDateTime;

public record RescheduleAppointmentCommand(
        Long appointmentId,
        LocalDateTime newDateTime
) {
}
