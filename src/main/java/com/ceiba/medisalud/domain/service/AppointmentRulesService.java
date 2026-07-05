package com.ceiba.medisalud.domain.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.ceiba.medisalud.domain.exception.BadRequestException;
import com.ceiba.medisalud.domain.model.Patient;
import com.ceiba.medisalud.domain.policy.WorkingHoursPolicy;

public class AppointmentRulesService {

    private static final int LATE_CANCELLATION_HOURS = 2;

    private final WorkingHoursPolicy workingHoursPolicy;

    public AppointmentRulesService(WorkingHoursPolicy workingHoursPolicy) {
        this.workingHoursPolicy = workingHoursPolicy;
    }

    public void validateSchedulableDateTime(LocalDateTime appointmentDateTime, LocalDateTime now) {
        if (appointmentDateTime == null) {
            throw new BadRequestException("La fecha y hora de la cita es obligatoria");
        }
        if (!appointmentDateTime.isAfter(now)) {
            throw new BadRequestException("No se pueden agendar citas en el pasado");
        }
        if (!workingHoursPolicy.isValidAppointmentStart(appointmentDateTime)) {
            throw new BadRequestException("La cita debe estar dentro del horario laboral y en franjas exactas de 30 minutos");
        }
    }

    public void validatePatientAgeAtScheduling(Patient patient, LocalDate currentDate) {
        if (patient.birthDate() != null && patient.birthDate().isAfter(currentDate)) {
            throw new BadRequestException("La fecha de nacimiento del paciente no puede ser futura");
        }
    }

    public boolean isLateCancellation(LocalDateTime appointmentDateTime, LocalDateTime cancellationDateTime) {
        Duration anticipation = Duration.between(cancellationDateTime, appointmentDateTime);
        return !anticipation.isNegative() && anticipation.compareTo(Duration.ofHours(LATE_CANCELLATION_HOURS)) < 0;
    }

    public void validateCancelable(LocalDateTime appointmentDateTime, LocalDateTime now) {
        if (!appointmentDateTime.isAfter(now)) {
            throw new BadRequestException("No se pueden cancelar citas vencidas");
        }
    }
}
