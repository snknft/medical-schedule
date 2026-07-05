package com.ceiba.medisalud.application.command;

import java.time.LocalDateTime;

/**
 * Carries the data required to reschedule an existing appointment.
 */
public record RescheduleAppointmentCommand(
        Long appointmentId,
        LocalDateTime newDateTime
) {
}
