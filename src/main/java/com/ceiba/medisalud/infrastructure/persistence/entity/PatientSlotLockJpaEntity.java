package com.ceiba.medisalud.infrastructure.persistence.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

/**
 * Represents the JPA persistence entity that protects active patient slot reservations.
 */
@Entity
@Table(
        name = "patient_slot_locks",
        uniqueConstraints = @UniqueConstraint(name = "uk_patient_slot_lock", columnNames = {"patient_id", "appointment_date_time"})
)
public class PatientSlotLockJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    @Column(name = "appointment_date_time", nullable = false)
    private LocalDateTime appointmentDateTime;

    /**
     * Creates an empty patient slot lock entity required by JPA.
     */
    protected PatientSlotLockJpaEntity() {
    }

    /**
     * Creates a patient slot lock entity.
     *
     * @param patientId patient identifier
     * @param appointmentDateTime locked appointment slot date-time
     */
    public PatientSlotLockJpaEntity(Long patientId, LocalDateTime appointmentDateTime) {
        this.patientId = patientId;
        this.appointmentDateTime = appointmentDateTime;
    }

    /**
     * Returns the generated database identifier.
     */
    public Long getId() {
        return id;
    }

    /**
     * Returns the patient identifier stored by this entity.
     */
    public Long getPatientId() {
        return patientId;
    }

    /**
     * Returns the appointment slot date-time stored by this entity.
     */
    public LocalDateTime getAppointmentDateTime() {
        return appointmentDateTime;
    }
}
