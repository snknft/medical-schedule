package com.ceiba.medisalud.infrastructure.persistence.mapper;

import com.ceiba.medisalud.domain.model.Appointment;
import com.ceiba.medisalud.infrastructure.persistence.entity.AppointmentJpaEntity;

public final class AppointmentJpaMapper {

    private AppointmentJpaMapper() {
    }

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
