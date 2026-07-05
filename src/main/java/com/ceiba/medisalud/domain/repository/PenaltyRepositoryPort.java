package com.ceiba.medisalud.domain.repository;

import java.time.LocalDateTime;

import com.ceiba.medisalud.domain.model.Penalty;

public interface PenaltyRepositoryPort {

    Penalty save(Penalty penalty);

    long countByPatientIdSince(Long patientId, LocalDateTime since);
}
