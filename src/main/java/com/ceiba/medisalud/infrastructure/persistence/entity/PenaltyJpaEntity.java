package com.ceiba.medisalud.infrastructure.persistence.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Represents the JPA persistence entity for penalty records.
 */
@Entity
@Table(name = "penalties")
public class PenaltyJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    @Column(name = "appointment_id", nullable = false)
    private Long appointmentId;

    @Column(name = "penalty_date_time", nullable = false)
    private LocalDateTime penaltyDateTime;

    @Column(nullable = false, length = 200)
    private String reason;

    /**
     * Creates a new PenaltyJpaEntity instance.
     */
    protected PenaltyJpaEntity() {
    }

    /**
     * Creates a new PenaltyJpaEntity instance.
     */
    public PenaltyJpaEntity(Long id, Long patientId, Long appointmentId, LocalDateTime penaltyDateTime, String reason) {
        this.id = id;
        this.patientId = patientId;
        this.appointmentId = appointmentId;
        this.penaltyDateTime = penaltyDateTime;
        this.reason = reason;
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
     * Returns the appointmentId value.
     */
    public Long getAppointmentId() {
        return appointmentId;
    }

    /**
     * Returns the penaltyDateTime value.
     */
    public LocalDateTime getPenaltyDateTime() {
        return penaltyDateTime;
    }

    /**
     * Returns the reason value.
     */
    public String getReason() {
        return reason;
    }
}
