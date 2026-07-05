package com.ceiba.medisalud.infrastructure.persistence.springdata;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ceiba.medisalud.domain.model.AppointmentStatus;
import com.ceiba.medisalud.infrastructure.persistence.entity.AppointmentJpaEntity;

/**
 * Provides the Spring Data JPA repository contract for appointment entities.
 */
public interface SpringDataAppointmentJpaRepository extends JpaRepository<AppointmentJpaEntity, Long>, JpaSpecificationExecutor<AppointmentJpaEntity> {

    /**
     * Finds appointment entities by doctor, status, and appointment date-time range.
     */
    List<AppointmentJpaEntity> findByDoctorIdAndStatusAndAppointmentDateTimeBetween(
            Long doctorId,
            AppointmentStatus status,
            LocalDateTime from,
            LocalDateTime to
    );
}
