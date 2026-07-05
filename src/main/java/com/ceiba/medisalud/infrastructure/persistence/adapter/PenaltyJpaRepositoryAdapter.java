package com.ceiba.medisalud.infrastructure.persistence.adapter;

import java.time.LocalDateTime;

import org.springframework.stereotype.Repository;

import com.ceiba.medisalud.domain.model.Penalty;
import com.ceiba.medisalud.domain.repository.PenaltyRepositoryPort;
import com.ceiba.medisalud.infrastructure.persistence.mapper.PenaltyJpaMapper;
import com.ceiba.medisalud.infrastructure.persistence.springdata.SpringDataPenaltyJpaRepository;

/**
 * Implements a domain port using Spring Data JPA for penalty persistence.
 */
@Repository
public class PenaltyJpaRepositoryAdapter implements PenaltyRepositoryPort {

    private final SpringDataPenaltyJpaRepository repository;

    /**
     * Creates a new PenaltyJpaRepositoryAdapter instance.
     */
    public PenaltyJpaRepositoryAdapter(SpringDataPenaltyJpaRepository repository) {
        this.repository = repository;
    }

    /**
     * Persists the provided domain object and returns the saved instance.
     */
    @Override
    public Penalty save(Penalty penalty) {
        return PenaltyJpaMapper.toDomain(repository.save(PenaltyJpaMapper.toEntity(penalty)));
    }

    /**
     * Counts patient penalties registered from the provided date-time.
     */
    @Override
    public long countByPatientIdSince(Long patientId, LocalDateTime since) {
        return repository.countByPatientIdAndPenaltyDateTimeGreaterThanEqual(patientId, since);
    }
}
