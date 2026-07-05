package com.ceiba.medisalud.domain.model;

import java.time.LocalDateTime;

import com.ceiba.medisalud.domain.exception.ConflictException;

/**
 * Represents the appointment aggregate and enforces state transitions for cancellations and attendance.
 */
public class Appointment {

    private final Long id;
    private final Long patientId;
    private final Long doctorId;
    private final LocalDateTime appointmentDateTime;
    private AppointmentStatus status;
    private LocalDateTime cancellationDateTime;

    /**
     * Creates a new Appointment instance.
     */
    public Appointment(
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

    /**
     * Changes the appointment status to cancelled when the current state allows it.
     */
    public void cancel(LocalDateTime cancellationDateTime) {
        if (status != AppointmentStatus.PROGRAMADA) {
            throw new ConflictException("Solo se pueden cancelar citas en estado PROGRAMADA");
        }
        this.status = AppointmentStatus.CANCELADA;
        this.cancellationDateTime = cancellationDateTime;
    }

    /**
     * Changes the appointment status to attended when the current state allows it.
     */
    public void attend() {
        if (status != AppointmentStatus.PROGRAMADA) {
            throw new ConflictException("Solo se pueden marcar como atendidas citas en estado PROGRAMADA");
        }
        this.status = AppointmentStatus.ATENDIDA;
    }
}
