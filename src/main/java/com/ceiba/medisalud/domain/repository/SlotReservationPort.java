package com.ceiba.medisalud.domain.repository;

import java.time.LocalDateTime;

/**
 * Defines the domain persistence port for slotreservation operations.
 */
public interface SlotReservationPort {

    /**
     * Reserves an appointment slot for the doctor and patient.
     */
    void reserve(Long doctorId, Long patientId, LocalDateTime appointmentDateTime);

    /**
     * Releases the appointment slot reservation for the doctor and patient.
     */
    void release(Long doctorId, Long patientId, LocalDateTime appointmentDateTime);
}
