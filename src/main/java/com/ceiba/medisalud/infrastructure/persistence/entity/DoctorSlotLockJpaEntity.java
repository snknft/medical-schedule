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

    protected DoctorSlotLockJpaEntity() {
    }

    public DoctorSlotLockJpaEntity(Long doctorId, LocalDateTime appointmentDateTime) {
        this.doctorId = doctorId;
        this.appointmentDateTime = appointmentDateTime;
    }

    public Long getId() {
        return id;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public LocalDateTime getAppointmentDateTime() {
        return appointmentDateTime;
    }
}
