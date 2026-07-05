package com.ceiba.medisalud.infrastructure.persistence.mapper;

import com.ceiba.medisalud.domain.model.Doctor;
import com.ceiba.medisalud.infrastructure.persistence.entity.DoctorJpaEntity;

/**
 * Maps doctor data between domain models and JPA entities.
 */
public final class DoctorJpaMapper {

    /**
     * Prevents instantiation of the DoctorJpaMapper utility class.
     */
    private DoctorJpaMapper() {
    }

    /**
     * Maps a doctor JPA entity into a domain object.
     *
     * @param entity doctor JPA entity
     * @return doctor domain object
     */
    public static Doctor toDomain(DoctorJpaEntity entity) {
        return new Doctor(entity.getId(), entity.getFullName(), entity.getSpecialty(), entity.getPhone(), entity.getEmail());
    }

    /**
     * Maps a doctor domain object into a JPA entity.
     *
     * @param doctor doctor domain object
     * @return doctor JPA entity
     */
    public static DoctorJpaEntity toEntity(Doctor doctor) {
        return new DoctorJpaEntity(doctor.id(), doctor.fullName(), doctor.specialty(), doctor.phone(), doctor.email());
    }
}
