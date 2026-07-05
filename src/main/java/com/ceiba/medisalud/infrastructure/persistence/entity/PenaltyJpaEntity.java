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
     * Creates an empty penalty entity required by JPA.
     */
    protected PenaltyJpaEntity() {
    }

    /**
     * Creates a penalty entity with persisted column values.
     *
     * @param id database identifier
     * @param patientId penalized patient identifier
     * @param appointmentId appointment that originated the penalty
     * @param penaltyDateTime date-time when the penalty was registered
     * @param reason penalty reason
     */
    public PenaltyJpaEntity(Long id, Long patientId, Long appointmentId, LocalDateTime penaltyDateTime, String reason) {
        this.id = id;
        this.patientId = patientId;
        this.appointmentId = appointmentId;
        this.penaltyDateTime = penaltyDateTime;
        this.reason = reason;
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
     * Returns the appointment identifier stored by this entity.
     */
    public Long getAppointmentId() {
        return appointmentId;
    }

    /**
     * Returns the penalty registration date-time.
     */
    public LocalDateTime getPenaltyDateTime() {
        return penaltyDateTime;
    }

    /**
     * Returns the persisted penalty reason.
     */
    public String getReason() {
        return reason;
    }
}
