package com.ceiba.medisalud.infrastructure.persistence.springdata;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ceiba.medisalud.infrastructure.persistence.entity.DoctorJpaEntity;

/**
 * Provides the Spring Data JPA repository contract for doctor entities.
 */
public interface SpringDataDoctorJpaRepository extends JpaRepository<DoctorJpaEntity, Long> {
}
