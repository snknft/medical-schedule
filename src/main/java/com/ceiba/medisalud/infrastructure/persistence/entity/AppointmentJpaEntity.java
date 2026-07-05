package com.ceiba.medisalud.infrastructure.persistence.entity;

import java.time.LocalDateTime;

import com.ceiba.medisalud.domain.model.AppointmentStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/**
 * Represents the JPA persistence entity for appointment records.
 */
@Entity
@Table(name = "appointments")
public class AppointmentJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    @Column(name = "doctor_id", nullable = false)
    private Long doctorId;

    @Column(name = "appointment_date_time", nullable = false)
    private LocalDateTime appointmentDateTime;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "status", nullable = false, length = 20)
    private AppointmentStatus status;

    @Column(name = "cancellation_date_time")
    private LocalDateTime cancellationDateTime;

    /**
     * Creates a new AppointmentJpaEntity instance.
     */
    protected AppointmentJpaEntity() {
    }

    /**
     * Creates a new AppointmentJpaEntity instance.
     */
    public AppointmentJpaEntity(
            Long id,
            Long patientId,
            Long doctorId,
            LocalDateTime appointmentDateTime,
            AppointmentStatus status,
            LocalDateTime cancellationDateTime
    ) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentDateTime = appointmentDateTime;
        this.status = status;
        this.cancellationDateTime = cancellationDateTime;
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
     * Returns the doctorId value.
     */
    public Long getDoctorId() {
        return doctorId;
    }

    /**
     * Returns the appointmentDateTime value.
     */
    public LocalDateTime getAppointmentDateTime() {
        return appointmentDateTime;
    }

    /**
     * Returns the status value.
     */
    public AppointmentStatus getStatus() {
        return status;
    }

    /**
     * Returns the cancellationDateTime value.
     */
    public LocalDateTime getCancellationDateTime() {
        return cancellationDateTime;
    }
}
