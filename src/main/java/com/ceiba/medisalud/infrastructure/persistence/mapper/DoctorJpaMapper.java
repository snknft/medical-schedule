package com.ceiba.medisalud.infrastructure.persistence.mapper;

import com.ceiba.medisalud.domain.model.Doctor;
import com.ceiba.medisalud.infrastructure.persistence.entity.DoctorJpaEntity;

public final class DoctorJpaMapper {

    private DoctorJpaMapper() {
    }

    public static Doctor toDomain(DoctorJpaEntity entity) {
        return new Doctor(entity.getId(), entity.getFullName(), entity.getSpecialty(), entity.getPhone(), entity.getEmail());
    }

    public static DoctorJpaEntity toEntity(Doctor doctor) {
        return new DoctorJpaEntity(doctor.id(), doctor.fullName(), doctor.specialty(), doctor.phone(), doctor.email());
    }
}
