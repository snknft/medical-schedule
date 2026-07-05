package com.ceiba.medisalud.domain.model;

import java.time.LocalDateTime;

import com.ceiba.medisalud.domain.exception.ConflictException;

public class Appointment {

    private final Long id;
    private final Long patientId;
    private final Long doctorId;
    private final LocalDateTime appointmentDateTime;
    private AppointmentStatus status;
    private LocalDateTime cancellationDateTime;

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

    public Long getId() {
        return id;
    }

    public Long getPatientId() {
        return patientId;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public LocalDateTime getAppointmentDateTime() {
        return appointmentDateTime;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public LocalDateTime getCancellationDateTime() {
        return cancellationDateTime;
    }

    public void cancel(LocalDateTime cancellationDateTime) {
        if (status != AppointmentStatus.PROGRAMADA) {
            throw new ConflictException("Solo se pueden cancelar citas en estado PROGRAMADA");
        }
        this.status = AppointmentStatus.CANCELADA;
        this.cancellationDateTime = cancellationDateTime;
    }

    public void attend() {
        if (status != AppointmentStatus.PROGRAMADA) {
            throw new ConflictException("Solo se pueden marcar como atendidas citas en estado PROGRAMADA");
        }
        this.status = AppointmentStatus.ATENDIDA;
    }
}
