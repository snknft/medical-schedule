package com.ceiba.medisalud.domain.repository;

import java.util.List;
import java.util.Optional;

import com.ceiba.medisalud.domain.model.Patient;

/**
 * Defines the domain persistence port for patient operations.
 */
public interface PatientRepositoryPort {

    /**
     * Persists the provided domain object and returns the saved instance.
     */
    Patient save(Patient patient);

    /**
     * Finds a resource by its identifier.
     */
    Optional<Patient> findById(Long id);

    /**
     * Returns all persisted resources of the current type.
     */
    List<Patient> findAll();

    /**
     * Determines whether a patient already exists for the provided document number.
     */
    boolean existsByDocumentNumber(String documentNumber);
}
