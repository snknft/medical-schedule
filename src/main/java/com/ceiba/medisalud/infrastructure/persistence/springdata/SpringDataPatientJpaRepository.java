package com.ceiba.medisalud.infrastructure.persistence.springdata;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ceiba.medisalud.infrastructure.persistence.entity.PatientJpaEntity;

/**
 * Provides the Spring Data JPA repository contract for patient entities.
 */
public interface SpringDataPatientJpaRepository extends JpaRepository<PatientJpaEntity, Long> {

    /**
     * Determines whether a patient already exists for the provided document number.
     */
    boolean existsByDocumentNumber(String documentNumber);
}
