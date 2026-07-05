package com.ceiba.medisalud.infrastructure.persistence.springdata;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ceiba.medisalud.infrastructure.persistence.entity.PatientSlotLockJpaEntity;

/**
 * Provides the Spring Data JPA repository contract for patientslotlock entities.
 */
public interface SpringDataPatientSlotLockJpaRepository extends JpaRepository<PatientSlotLockJpaEntity, Long> {

    /**
     * Deletes persisted data that matches the provided criteria.
     */
    void deleteByPatientIdAndAppointmentDateTime(Long patientId, LocalDateTime appointmentDateTime);
}
