package com.ceiba.medisalud.infrastructure.persistence.springdata;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ceiba.medisalud.infrastructure.persistence.entity.DoctorSlotLockJpaEntity;

public interface SpringDataDoctorSlotLockJpaRepository extends JpaRepository<DoctorSlotLockJpaEntity, Long> {

    void deleteByDoctorIdAndAppointmentDateTime(Long doctorId, LocalDateTime appointmentDateTime);
}
