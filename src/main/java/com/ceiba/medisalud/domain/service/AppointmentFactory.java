package com.ceiba.medisalud.domain.service;

import java.time.LocalDateTime;

import com.ceiba.medisalud.domain.model.Appointment;
import com.ceiba.medisalud.domain.model.AppointmentStatus;

/**
 * Creates appointment domain objects with consistent initial state.
 */
public class AppointmentFactory {

    /**
     * Creates a scheduled appointment domain object.
     */
    public Appointment createScheduled(Long patientId, Long doctorId, LocalDateTime appointmentDateTime) {
        return new Appointment(
                null,
                patientId,
                doctorId,
                appointmentDateTime,
                AppointmentStatus.PROGRAMADA,
                null
        );
    }
}
