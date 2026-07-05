package com.ceiba.medisalud.infrastructure.persistence.springdata;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ceiba.medisalud.infrastructure.persistence.entity.PenaltyJpaEntity;

/**
 * Provides the Spring Data JPA repository contract for penalty entities.
 */
public interface SpringDataPenaltyJpaRepository extends JpaRepository<PenaltyJpaEntity, Long> {

    /**
     * Counts data that matches the provided criteria.
     */
    long countByPatientIdAndPenaltyDateTimeGreaterThanEqual(Long patientId, LocalDateTime since);
}
