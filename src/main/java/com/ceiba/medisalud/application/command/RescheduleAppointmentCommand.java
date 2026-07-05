package com.ceiba.medisalud.application.command;

import java.time.LocalDateTime;

/**
 * Carries the data required to reschedule an existing appointment.
 *
 * @param appointmentId identifier of the appointment to reschedule
 * @param newDateTime new appointment start date-time
 */
public record RescheduleAppointmentCommand(
        Long appointmentId,
        LocalDateTime newDateTime
) {
}
