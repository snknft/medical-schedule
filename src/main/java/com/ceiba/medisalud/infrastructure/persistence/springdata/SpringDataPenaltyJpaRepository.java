package com.ceiba.medisalud.infrastructure.persistence.springdata;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ceiba.medisalud.infrastructure.persistence.entity.PenaltyJpaEntity;

public interface SpringDataPenaltyJpaRepository extends JpaRepository<PenaltyJpaEntity, Long> {

    long countByPatientIdAndPenaltyDateTimeGreaterThanEqual(Long patientId, LocalDateTime since);
}
