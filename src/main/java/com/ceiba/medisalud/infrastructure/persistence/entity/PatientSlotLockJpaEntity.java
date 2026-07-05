package com.ceiba.medisalud.infrastructure.persistence.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
        name = "patient_slot_locks",
        uniqueConstraints = @UniqueConstraint(name = "uk_patient_slot_lock", columnNames = {"patient_id", "appointment_date_time"})
)
/**
 * Represents the JPA persistence entity for patientslotlock records.
 */
public class PatientSlotLockJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    @Column(name = "appointment_date_time", nullable = false)
    private LocalDateTime appointmentDateTime;

    /**
     * Creates a new PatientSlotLockJpaEntity instance.
     */
    protected PatientSlotLockJpaEntity() {
    }

    /**
     * Creates a new PatientSlotLockJpaEntity instance.
     */
    public PatientSlotLockJpaEntity(Long patientId, LocalDateTime appointmentDateTime) {
        this.patientId = patientId;
        this.appointmentDateTime = appointmentDateTime;
    }

    /**
     * Returns the id value.
     */
    public Long getId() {
        return id;
    }

    /**
     * Returns the patientId value.
     */
    public Long getPatientId() {
        return patientId;
    }

    /**
     * Returns the appointmentDateTime value.
     */
    public LocalDateTime getAppointmentDateTime() {
        return appointmentDateTime;
    }
}
