package com.ceiba.medisalud.domain.repository;

import java.time.LocalDateTime;

import com.ceiba.medisalud.domain.model.Penalty;

/**
 * Defines the domain persistence port for penalty operations.
 */
public interface PenaltyRepositoryPort {

    /**
     * Persists the provided domain object and returns the saved instance.
     */
    Penalty save(Penalty penalty);

    /**
     * Counts patient penalties registered from the provided date-time.
     */
    long countByPatientIdSince(Long patientId, LocalDateTime since);
}
