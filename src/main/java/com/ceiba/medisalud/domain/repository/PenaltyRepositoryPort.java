package com.ceiba.medisalud.domain.repository;

import java.time.LocalDateTime;

import com.ceiba.medisalud.domain.model.Penalty;

/**
 * Defines the domain persistence port for penalty operations.
 */
public interface PenaltyRepositoryPort {

    /**
     * Persists the provided penalty and returns the saved instance.
     *
     * @param penalty penalty to persist
     * @return persisted penalty
     */
    Penalty save(Penalty penalty);

    /**
     * Counts patient penalties registered from the provided date-time.
     *
     * @param patientId patient identifier
     * @param since lower bound date-time for counting penalties
     * @return number of penalties registered since the provided date-time
     */
    long countByPatientIdSince(Long patientId, LocalDateTime since);
}
