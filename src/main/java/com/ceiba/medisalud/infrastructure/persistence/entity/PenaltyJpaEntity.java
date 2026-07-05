package com.ceiba.medisalud.infrastructure.persistence.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

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

    protected PenaltyJpaEntity() {
    }

    public PenaltyJpaEntity(Long id, Long patientId, Long appointmentId, LocalDateTime penaltyDateTime, String reason) {
        this.id = id;
        this.patientId = patientId;
        this.appointmentId = appointmentId;
        this.penaltyDateTime = penaltyDateTime;
        this.reason = reason;
    }

    public Long getId() {
        return id;
    }

    public Long getPatientId() {
        return patientId;
    }

    public Long getAppointmentId() {
        return appointmentId;
    }

    public LocalDateTime getPenaltyDateTime() {
        return penaltyDateTime;
    }

    public String getReason() {
        return reason;
    }
}
