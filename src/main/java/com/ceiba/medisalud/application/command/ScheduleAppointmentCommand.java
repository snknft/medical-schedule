package com.ceiba.medisalud.application.command;

import java.time.LocalDateTime;

public record ScheduleAppointmentCommand(
        Long patientId,
        Long doctorId,
        LocalDateTime appointmentDateTime
) {
}
