package com.ceiba.medisalud.infrastructure.persistence.mapper;

import com.ceiba.medisalud.domain.model.Appointment;
import com.ceiba.medisalud.infrastructure.persistence.entity.AppointmentJpaEntity;

/**
 * Maps appointment data between domain models and JPA entities.
 */
public final class AppointmentJpaMapper {

    /**
     * Prevents instantiation of the AppointmentJpaMapper utility class.
     */
    private AppointmentJpaMapper() {
    }

    /**
     * Maps an appointment JPA entity into a domain object.
     *
     * @param entity appointment JPA entity
     * @return appointment domain object
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
     * Maps an appointment domain object into a JPA entity.
     *
     * @param appointment appointment domain object
     * @return appointment JPA entity
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
