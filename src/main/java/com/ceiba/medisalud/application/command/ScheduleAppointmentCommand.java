package com.ceiba.medisalud.application.command;

import java.time.LocalDateTime;

/**
 * Carries the data required to schedule a new appointment.
 *
 * @param patientId patient identifier
 * @param doctorId doctor identifier
 * @param appointmentDateTime requested appointment start date-time
 */
public record ScheduleAppointmentCommand(
        Long patientId,
        Long doctorId,
        LocalDateTime appointmentDateTime
) {
}
