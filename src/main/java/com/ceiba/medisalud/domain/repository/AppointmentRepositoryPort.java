package com.ceiba.medisalud.domain.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.ceiba.medisalud.domain.model.Appointment;
import com.ceiba.medisalud.domain.model.AppointmentSearchCriteria;

/**
 * Defines the domain persistence port for appointment operations.
 */
public interface AppointmentRepositoryPort {

    /**
     * Persists the provided appointment and returns the saved instance.
     *
     * @param appointment appointment to persist
     * @return persisted appointment
     */
    Appointment save(Appointment appointment);

    /**
     * Finds an appointment by its identifier.
     *
     * @param id appointment identifier
     * @return optional appointment
     */
    Optional<Appointment> findById(Long id);

    /**
     * Determines whether a doctor already has an active appointment in the provided slot.
     *
     * @param doctorId doctor identifier
     * @param dateTime appointment slot date-time
     * @param excludedAppointmentId appointment identifier ignored by the check
     * @return {@code true} when a scheduled doctor conflict exists
     */
    boolean existsScheduledForDoctorAt(Long doctorId, LocalDateTime dateTime, Long excludedAppointmentId);

    /**
     * Determines whether a patient already has an active appointment in the provided slot.
     *
     * @param patientId patient identifier
     * @param dateTime appointment slot date-time
     * @param excludedAppointmentId appointment identifier ignored by the check
     * @return {@code true} when a scheduled patient conflict exists
     */
    boolean existsScheduledForPatientAt(Long patientId, LocalDateTime dateTime, Long excludedAppointmentId);

    /**
     * Finds scheduled appointments for a doctor within the provided date-time range.
     *
     * @param doctorId doctor identifier
     * @param from initial date-time included in the search
     * @param to final date-time included in the search
     * @return scheduled appointments for the doctor in the range
     */
    List<Appointment> findScheduledByDoctorBetween(Long doctorId, LocalDateTime from, LocalDateTime to);

    /**
     * Searches appointments using optional filters.
     *
     * @param criteria search criteria
     * @return appointments matching the criteria
     */
    List<Appointment> search(AppointmentSearchCriteria criteria);
}
