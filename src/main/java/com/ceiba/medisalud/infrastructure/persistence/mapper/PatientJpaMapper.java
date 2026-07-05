package com.ceiba.medisalud.infrastructure.persistence.mapper;

import com.ceiba.medisalud.domain.model.Patient;
import com.ceiba.medisalud.infrastructure.persistence.entity.PatientJpaEntity;

/**
 * Maps patient data between domain models and JPA entities.
 */
public final class PatientJpaMapper {

    /**
     * Prevents instantiation of the PatientJpaMapper utility class.
     */
    private PatientJpaMapper() {
    }

    /**
     * Maps a patient JPA entity into a domain object.
     *
     * @param entity patient JPA entity
     * @return patient domain object
     */
    public static Patient toDomain(PatientJpaEntity entity) {
        return new Patient(
                entity.getId(),
                entity.getFullName(),
                entity.getDocumentNumber(),
                entity.getPhone(),
                entity.getEmail(),
                entity.getBirthDate()
        );
    }

    /**
     * Maps a patient domain object into a JPA entity.
     *
     * @param patient patient domain object
     * @return patient JPA entity
     */
    public static PatientJpaEntity toEntity(Patient patient) {
        return new PatientJpaEntity(
                patient.id(),
                patient.fullName(),
                patient.documentNumber(),
                patient.phone(),
                patient.email(),
                patient.birthDate()
        );
    }
}
