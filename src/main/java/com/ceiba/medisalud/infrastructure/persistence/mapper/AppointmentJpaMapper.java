package com.ceiba.medisalud.infrastructure.persistence.mapper;

import com.ceiba.medisalud.domain.model.Appointment;
import com.ceiba.medisalud.infrastructure.persistence.entity.AppointmentJpaEntity;

/**
 * Maps appointment data between domain models and JPA entities.
 */
public final class AppointmentJpaMapper {

    /**
     * Creates a new AppointmentJpaMapper instance.
     */
    private AppointmentJpaMapper() {
    }

    /**
     * Maps a persistence representation into a domain object.
     */
    public static Appointment toDomain(AppointmentJpaEntity entity) {
        return new Appointment(
                entity.getId(),
                entity.getPatientId(),
                entity.getDoctorId(),
                entity.getAppointmentDateTime(),
                entity.getStatus(),
                entity.getCancellationDateTime()
        );
    }

    /**
     * Maps a domain object into a persistence representation.
     */
    public static AppointmentJpaEntity toEntity(Appointment appointment) {
        return new AppointmentJpaEntity(
                appointment.getId(),
                appointment.getPatientId(),
                appointment.getDoctorId(),
                appointment.getAppointmentDateTime(),
                appointment.getStatus(),
                appointment.getCancellationDateTime()
        );
    }
}
