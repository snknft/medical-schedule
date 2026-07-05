package com.ceiba.medisalud.infrastructure.persistence.mapper;

import com.ceiba.medisalud.domain.model.Doctor;
import com.ceiba.medisalud.infrastructure.persistence.entity.DoctorJpaEntity;

/**
 * Maps doctor data between domain models and JPA entities.
 */
public final class DoctorJpaMapper {

    /**
     * Creates a new DoctorJpaMapper instance.
     */
    private DoctorJpaMapper() {
    }

    /**
     * Maps a persistence representation into a domain object.
     */
    public static Doctor toDomain(DoctorJpaEntity entity) {
        return new Doctor(entity.getId(), entity.getFullName(), entity.getSpecialty(), entity.getPhone(), entity.getEmail());
    }

    /**
     * Maps a domain object into a persistence representation.
     */
    public static DoctorJpaEntity toEntity(Doctor doctor) {
        return new DoctorJpaEntity(doctor.id(), doctor.fullName(), doctor.specialty(), doctor.phone(), doctor.email());
    }
}
