package com.ceiba.medisalud.domain.repository;

import java.util.List;
import java.util.Optional;

import com.ceiba.medisalud.domain.model.Doctor;

/**
 * Defines the domain persistence port for doctor operations.
 */
public interface DoctorRepositoryPort {

    /**
     * Persists the provided doctor and returns the saved instance.
     *
     * @param doctor doctor to persist
     * @return persisted doctor
     */
    Doctor save(Doctor doctor);

    /**
     * Finds a doctor by its identifier.
     *
     * @param id doctor identifier
     * @return optional doctor
     */
    Optional<Doctor> findById(Long id);

    /**
     * Returns all persisted doctors.
     *
     * @return all persisted doctors
     */
    List<Doctor> findAll();

    /**
     * Determines whether a doctor exists for the provided identifier.
     *
     * @param id doctor identifier
     * @return {@code true} when the doctor exists
     */
    boolean existsById(Long id);

    /**
     * Counts persisted doctors.
     *
     * @return number of persisted doctors
     */
    long count();
}
