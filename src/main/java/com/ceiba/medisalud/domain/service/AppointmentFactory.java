package com.ceiba.medisalud.domain.service;

import java.time.LocalDateTime;

import com.ceiba.medisalud.domain.model.Appointment;
import com.ceiba.medisalud.domain.model.AppointmentStatus;

/**
 * Creates appointment domain objects with a consistent initial state.
 */
public class AppointmentFactory {

    /**
     * Creates a scheduled appointment domain object.
     *
     * @param patientId patient identifier
     * @param doctorId doctor identifier
     * @param appointmentDateTime scheduled appointment start date-time
     * @return appointment initialized with {@link AppointmentStatus#PROGRAMADA}
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
