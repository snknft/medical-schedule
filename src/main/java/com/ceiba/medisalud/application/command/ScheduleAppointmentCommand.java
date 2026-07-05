package com.ceiba.medisalud.application.command;

import java.time.LocalDateTime;

/**
 * Carries the data required to schedule a new appointment.
 */
public record ScheduleAppointmentCommand(
        Long patientId,
        Long doctorId,
        LocalDateTime appointmentDateTime
) {
}
