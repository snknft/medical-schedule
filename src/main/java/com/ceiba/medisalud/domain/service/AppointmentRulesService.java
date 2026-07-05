package com.ceiba.medisalud.domain.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.ceiba.medisalud.domain.exception.BadRequestException;
import com.ceiba.medisalud.domain.model.Patient;
import com.ceiba.medisalud.domain.policy.WorkingHoursPolicy;

/**
 * Centralizes appointment business rule validation that does not belong to a single aggregate instance.
 */
public class AppointmentRulesService {

    private static final int LATE_CANCELLATION_HOURS = 2;

    private final WorkingHoursPolicy workingHoursPolicy;

    /**
     * Creates the rule service with the working hours policy used to validate appointment slots.
     *
     * @param workingHoursPolicy policy that validates working hours and slot boundaries
     */
    public AppointmentRulesService(WorkingHoursPolicy workingHoursPolicy) {
        this.workingHoursPolicy = workingHoursPolicy;
    }

    /**
     * Validates that an appointment date-time can be scheduled.
     *
     * @param appointmentDateTime requested appointment start date-time
     * @param now current application date-time
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
     *
     * @param patient patient associated with the appointment
     * @param currentDate current application date
     */
    public void validatePatientAgeAtScheduling(Patient patient, LocalDate currentDate) {
        if (patient.birthDate() != null && patient.birthDate().isAfter(currentDate)) {
            throw new BadRequestException("La fecha de nacimiento del paciente no puede ser futura");
        }
    }

    /**
     * Determines whether a cancellation is considered late.
     *
     * @param appointmentDateTime scheduled appointment start date-time
     * @param cancellationDateTime date-time when the cancellation is requested
     * @return {@code true} when cancellation occurs with less than two hours of anticipation
     */
    public boolean isLateCancellation(LocalDateTime appointmentDateTime, LocalDateTime cancellationDateTime) {
        Duration anticipation = Duration.between(cancellationDateTime, appointmentDateTime);
        return !anticipation.isNegative() && anticipation.compareTo(Duration.ofHours(LATE_CANCELLATION_HOURS)) < 0;
    }

    /**
     * Validates whether an appointment can be cancelled at the provided time.
     *
     * @param appointmentDateTime scheduled appointment start date-time
     * @param now current application date-time
     */
    public void validateCancelable(LocalDateTime appointmentDateTime, LocalDateTime now) {
        if (!appointmentDateTime.isAfter(now)) {
            throw new BadRequestException("No se pueden cancelar citas vencidas");
        }
    }
}
