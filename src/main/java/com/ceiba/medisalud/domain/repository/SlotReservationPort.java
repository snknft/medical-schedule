package com.ceiba.medisalud.domain.repository;

import java.time.LocalDateTime;

/**
 * Defines the domain persistence port used to protect active appointment slot reservations.
 */
public interface SlotReservationPort {

    /**
     * Reserves an appointment slot for the doctor and patient.
     *
     * @param doctorId doctor identifier
     * @param patientId patient identifier
     * @param appointmentDateTime appointment slot date-time
     */
    void reserve(Long doctorId, Long patientId, LocalDateTime appointmentDateTime);

    /**
     * Releases the appointment slot reservation for the doctor and patient.
     *
     * @param doctorId doctor identifier
     * @param patientId patient identifier
     * @param appointmentDateTime appointment slot date-time
     */
    void release(Long doctorId, Long patientId, LocalDateTime appointmentDateTime);
}
