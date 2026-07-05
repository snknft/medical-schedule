package com.ceiba.medisalud.infrastructure.persistence.springdata;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ceiba.medisalud.infrastructure.persistence.entity.PatientJpaEntity;

public interface SpringDataPatientJpaRepository extends JpaRepository<PatientJpaEntity, Long> {

    boolean existsByDocumentNumber(String documentNumber);
}
