package com.ceiba.medisalud.infrastructure.persistence.springdata;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ceiba.medisalud.infrastructure.persistence.entity.DoctorSlotLockJpaEntity;

/**
 * Provides the Spring Data JPA repository contract for doctorslotlock entities.
 */
public interface SpringDataDoctorSlotLockJpaRepository extends JpaRepository<DoctorSlotLockJpaEntity, Long> {

    /**
     * Deletes persisted data that matches the provided criteria.
     */
    void deleteByDoctorIdAndAppointmentDateTime(Long doctorId, LocalDateTime appointmentDateTime);
}
