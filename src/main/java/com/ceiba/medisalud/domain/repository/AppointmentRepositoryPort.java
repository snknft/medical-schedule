package com.ceiba.medisalud.domain.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.ceiba.medisalud.domain.model.AppointmentSearchCriteria;
import com.ceiba.medisalud.domain.model.Appointment;

/**
 * Defines the domain persistence port for appointment operations.
 */
public interface AppointmentRepositoryPort {

    /**
     * Persists the provided domain object and returns the saved instance.
     */
    Appointment save(Appointment appointment);

    /**
     * Finds a resource by its identifier.
     */
    Optional<Appointment> findById(Long id);

    /**
     * Determines whether a doctor already has an active appointment in the provided slot.
     */
    boolean existsScheduledForDoctorAt(Long doctorId, LocalDateTime dateTime, Long excludedAppointmentId);

    /**
     * Determines whether a patient already has an active appointment in the provided slot.
     */
    boolean existsScheduledForPatientAt(Long patientId, LocalDateTime dateTime, Long excludedAppointmentId);

    /**
     * Finds scheduled appointments for a doctor within the provided date-time range.
     */
    List<Appointment> findScheduledByDoctorBetween(Long doctorId, LocalDateTime from, LocalDateTime to);

    /**
     * Searches appointments using the provided optional filters.
     */
    List<Appointment> search(AppointmentSearchCriteria criteria);
}
