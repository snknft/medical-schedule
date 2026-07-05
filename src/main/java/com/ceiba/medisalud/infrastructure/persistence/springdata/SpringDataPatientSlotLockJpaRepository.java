package com.ceiba.medisalud.infrastructure.persistence.springdata;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ceiba.medisalud.infrastructure.persistence.entity.PatientSlotLockJpaEntity;

public interface SpringDataPatientSlotLockJpaRepository extends JpaRepository<PatientSlotLockJpaEntity, Long> {

    void deleteByPatientIdAndAppointmentDateTime(Long patientId, LocalDateTime appointmentDateTime);
}
