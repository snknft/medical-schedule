package com.ceiba.medisalud.infrastructure.persistence.mapper;

import com.ceiba.medisalud.domain.model.Patient;
import com.ceiba.medisalud.infrastructure.persistence.entity.PatientJpaEntity;

/**
 * Maps patient data between domain models and JPA entities.
 */
public final class PatientJpaMapper {

    /**
     * Creates a new PatientJpaMapper instance.
     */
    private PatientJpaMapper() {
    }

    /**
     * Maps a persistence representation into a domain object.
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
     * Maps a domain object into a persistence representation.
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
