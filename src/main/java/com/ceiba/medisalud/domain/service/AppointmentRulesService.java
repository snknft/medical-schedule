package com.ceiba.medisalud.domain.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.ceiba.medisalud.domain.exception.BadRequestException;
import com.ceiba.medisalud.domain.model.Patient;
import com.ceiba.medisalud.domain.policy.WorkingHoursPolicy;

/**
 * Centralizes appointment business rule validation.
 */
public class AppointmentRulesService {

    private static final int LATE_CANCELLATION_HOURS = 2;

    private final WorkingHoursPolicy workingHoursPolicy;

    /**
     * Creates a new AppointmentRulesService instance.
     */
    public AppointmentRulesService(WorkingHoursPolicy workingHoursPolicy) {
        this.workingHoursPolicy = workingHoursPolicy;
    }

    /**
     * Validates that an appointment date-time can be scheduled.
     */
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

    /**
     * Validates patient age constraints at scheduling time.
     */
    public void validatePatientAgeAtScheduling(Patient patient, LocalDate currentDate) {
        if (patient.birthDate() != null && patient.birthDate().isAfter(currentDate)) {
            throw new BadRequestException("La fecha de nacimiento del paciente no puede ser futura");
        }
    }

    /**
     * Determines whether a cancellation is considered late.
     */
    public boolean isLateCancellation(LocalDateTime appointmentDateTime, LocalDateTime cancellationDateTime) {
        Duration anticipation = Duration.between(cancellationDateTime, appointmentDateTime);
        return !anticipation.isNegative() && anticipation.compareTo(Duration.ofHours(LATE_CANCELLATION_HOURS)) < 0;
    }

    /**
     * Validates whether an appointment can be cancelled at the provided time.
     */
    public void validateCancelable(LocalDateTime appointmentDateTime, LocalDateTime now) {
        if (!appointmentDateTime.isAfter(now)) {
            throw new BadRequestException("No se pueden cancelar citas vencidas");
        }
    }
}
