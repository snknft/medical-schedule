package com.ceiba.medisalud.domain.repository;

import java.util.List;
import java.util.Optional;

import com.ceiba.medisalud.domain.model.Doctor;

/**
 * Defines the domain persistence port for doctor operations.
 */
public interface DoctorRepositoryPort {

    /**
     * Persists the provided domain object and returns the saved instance.
     */
    Doctor save(Doctor doctor);

    /**
     * Finds a resource by its identifier.
     */
    Optional<Doctor> findById(Long id);

    /**
     * Returns all persisted resources of the current type.
     */
    List<Doctor> findAll();

    /**
     * Determines whether a resource exists for the provided identifier.
     */
    boolean existsById(Long id);

    /**
     * Counts the number of persisted resources.
     */
    long count();
}
