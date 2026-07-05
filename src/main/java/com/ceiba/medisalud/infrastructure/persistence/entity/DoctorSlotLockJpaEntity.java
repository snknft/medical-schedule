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
 * Represents the JPA persistence entity that protects active doctor slot reservations.
 */
@Entity
@Table(
        name = "doctor_slot_locks",
        uniqueConstraints = @UniqueConstraint(name = "uk_doctor_slot_lock", columnNames = {"doctor_id", "appointment_date_time"})
)
public class DoctorSlotLockJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "doctor_id", nullable = false)
    private Long doctorId;

    @Column(name = "appointment_date_time", nullable = false)
    private LocalDateTime appointmentDateTime;

    /**
     * Creates an empty doctor slot lock entity required by JPA.
     */
    protected DoctorSlotLockJpaEntity() {
    }

    /**
     * Creates a doctor slot lock entity.
     *
     * @param doctorId doctor identifier
     * @param appointmentDateTime locked appointment slot date-time
     */
    public DoctorSlotLockJpaEntity(Long doctorId, LocalDateTime appointmentDateTime) {
        this.doctorId = doctorId;
        this.appointmentDateTime = appointmentDateTime;
    }

    /**
     * Returns the generated database identifier.
     */
    public Long getId() {
        return id;
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
}
