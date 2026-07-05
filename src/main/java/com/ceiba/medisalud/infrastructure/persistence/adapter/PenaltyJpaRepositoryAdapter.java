package com.ceiba.medisalud.infrastructure.persistence.adapter;

import java.time.LocalDateTime;

import org.springframework.stereotype.Repository;

import com.ceiba.medisalud.domain.model.Penalty;
import com.ceiba.medisalud.domain.repository.PenaltyRepositoryPort;
import com.ceiba.medisalud.infrastructure.persistence.mapper.PenaltyJpaMapper;
import com.ceiba.medisalud.infrastructure.persistence.springdata.SpringDataPenaltyJpaRepository;

@Repository
public class PenaltyJpaRepositoryAdapter implements PenaltyRepositoryPort {

    private final SpringDataPenaltyJpaRepository repository;

    public PenaltyJpaRepositoryAdapter(SpringDataPenaltyJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Penalty save(Penalty penalty) {
        return PenaltyJpaMapper.toDomain(repository.save(PenaltyJpaMapper.toEntity(penalty)));
    }

    @Override
    public long countByPatientIdSince(Long patientId, LocalDateTime since) {
        return repository.countByPatientIdAndPenaltyDateTimeGreaterThanEqual(patientId, since);
    }
}
