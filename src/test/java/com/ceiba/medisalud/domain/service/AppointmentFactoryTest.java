package com.ceiba.medisalud.domain.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.ceiba.medisalud.domain.model.Appointment;
import com.ceiba.medisalud.domain.model.AppointmentStatus;

class AppointmentFactoryTest {

    private final AppointmentFactory factory = new AppointmentFactory();

    @Test
    void createsScheduledAppointment() {
        LocalDateTime appointmentDateTime = LocalDateTime.of(2026, 7, 6, 8, 0);

        Appointment appointment = factory.createScheduled(1L, 2L, appointmentDateTime);

        assertThat(appointment.getId()).isNull();
        assertThat(appointment.getPatientId()).isEqualTo(1L);
        assertThat(appointment.getDoctorId()).isEqualTo(2L);
        assertThat(appointment.getAppointmentDateTime()).isEqualTo(appointmentDateTime);
        assertThat(appointment.getStatus()).isEqualTo(AppointmentStatus.PROGRAMADA);
        assertThat(appointment.getCancellationDateTime()).isNull();
    }
}
