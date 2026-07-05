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
     * Creates an empty appointment entity required by JPA.
     */
    protected AppointmentJpaEntity() {
    }

    /**
     * Creates an appointment entity with persisted column values.
     *
     * @param id database identifier
     * @param patientId patient identifier
     * @param doctorId doctor identifier
     * @param appointmentDateTime appointment slot date-time
     * @param status appointment status
     * @param cancellationDateTime cancellation date-time when present
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
     * Returns the doctor identifier stored by this entity.
     */
    public Long getDoctorId() {
        return doctorId;
    }

    /**
     * Returns the appointment slot date-time stored by this entity.
     */
    public LocalDateTime getAppointmentDateTime() {
        return appointmentDateTime;
    }

    /**
     * Returns the persisted appointment status.
     */
    public AppointmentStatus getStatus() {
        return status;
    }

    /**
     * Returns the persisted cancellation date-time.
     */
    public LocalDateTime getCancellationDateTime() {
        return cancellationDateTime;
    }
}
