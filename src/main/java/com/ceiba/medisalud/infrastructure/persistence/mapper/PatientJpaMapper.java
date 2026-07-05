package com.ceiba.medisalud.infrastructure.persistence.mapper;

import com.ceiba.medisalud.domain.model.Patient;
import com.ceiba.medisalud.infrastructure.persistence.entity.PatientJpaEntity;

public final class PatientJpaMapper {

    private PatientJpaMapper() {
    }

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
