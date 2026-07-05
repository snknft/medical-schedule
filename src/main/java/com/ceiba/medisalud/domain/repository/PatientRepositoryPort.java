package com.ceiba.medisalud.domain.repository;

import java.util.List;
import java.util.Optional;

import com.ceiba.medisalud.domain.model.Patient;

/**
 * Defines the domain persistence port for patient operations.
 */
public interface PatientRepositoryPort {

    /**
     * Persists the provided patient and returns the saved instance.
     *
     * @param patient patient to persist
     * @return persisted patient
     */
    Patient save(Patient patient);

    /**
     * Finds a patient by its identifier.
     *
     * @param id patient identifier
     * @return optional patient
     */
    Optional<Patient> findById(Long id);

    /**
     * Returns all persisted patients.
     *
     * @return all persisted patients
     */
    List<Patient> findAll();

    /**
     * Determines whether a patient already exists for the provided document number.
     *
     * @param documentNumber identity document number
     * @return {@code true} when the document number is already registered
     */
    boolean existsByDocumentNumber(String documentNumber);
}
