package com.ceiba.medisalud.domain.model;

import java.time.LocalDateTime;

import com.ceiba.medisalud.domain.exception.ConflictException;

/**
 * Represents the appointment aggregate and enforces valid state transitions for cancellation and attendance.
 */
public class Appointment {

    private final Long id;
    private final Long patientId;
    private final Long doctorId;
    private final LocalDateTime appointmentDateTime;
    private AppointmentStatus status;
    private LocalDateTime cancellationDateTime;

    /**
     * Creates an appointment aggregate.
     *
     * @param id appointment identifier, or {@code null} before persistence
     * @param patientId patient identifier associated with the appointment
     * @param doctorId doctor identifier associated with the appointment
     * @param appointmentDateTime scheduled appointment start date-time
     * @param status current lifecycle status
     * @param cancellationDateTime cancellation date-time, or {@code null} when not cancelled
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
     * Returns the appointment identifier.
     *
     * @return appointment identifier
     */
    public Long getId() {
        return id;
    }

    /**
     * Returns the patient identifier.
     *
     * @return patient identifier
     */
    public Long getPatientId() {
        return patientId;
    }

    /**
     * Returns the doctor identifier.
     *
     * @return doctor identifier
     */
    public Long getDoctorId() {
        return doctorId;
    }

    /**
     * Returns the scheduled appointment start date-time.
     *
     * @return appointment start date-time
     */
    public LocalDateTime getAppointmentDateTime() {
        return appointmentDateTime;
    }

    /**
     * Returns the current lifecycle status.
     *
     * @return appointment status
     */
    public AppointmentStatus getStatus() {
        return status;
    }

    /**
     * Returns the cancellation date-time when the appointment has been cancelled.
     *
     * @return cancellation date-time, or {@code null} when not cancelled
     */
    public LocalDateTime getCancellationDateTime() {
        return cancellationDateTime;
    }

    /**
     * Changes the appointment status to cancelled when the current state allows it.
     *
     * @param cancellationDateTime date-time when the cancellation is registered
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
