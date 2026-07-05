package com.ceiba.medisalud.infrastructure.persistence.springdata;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ceiba.medisalud.infrastructure.persistence.entity.DoctorJpaEntity;

public interface SpringDataDoctorJpaRepository extends JpaRepository<DoctorJpaEntity, Long> {
}
