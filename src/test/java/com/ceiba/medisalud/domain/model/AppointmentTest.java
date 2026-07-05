package com.ceiba.medisalud.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.ceiba.medisalud.domain.exception.ConflictException;

class AppointmentTest {

    @Test
    void cancelsScheduledAppointment() {
        LocalDateTime appointmentDateTime = LocalDateTime.of(2026, 7, 6, 8, 0);
        LocalDateTime cancellationDateTime = LocalDateTime.of(2026, 7, 5, 10, 0);
        Appointment appointment = new Appointment(
                1L,
                10L,
                20L,
                appointmentDateTime,
                AppointmentStatus.PROGRAMADA,
                null
        );

        appointment.cancel(cancellationDateTime);

        assertThat(appointment.getId()).isEqualTo(1L);
        assertThat(appointment.getPatientId()).isEqualTo(10L);
        assertThat(appointment.getDoctorId()).isEqualTo(20L);
        assertThat(appointment.getAppointmentDateTime()).isEqualTo(appointmentDateTime);
        assertThat(appointment.getStatus()).isEqualTo(AppointmentStatus.CANCELADA);
        assertThat(appointment.getCancellationDateTime()).isEqualTo(cancellationDateTime);
    }

    @Test
    void rejectsCancelWhenAppointmentIsNotScheduled() {
        Appointment appointment = new Appointment(
                1L,
                10L,
                20L,
                LocalDateTime.of(2026, 7, 6, 8, 0),
                AppointmentStatus.CANCELADA,
                LocalDateTime.of(2026, 7, 5, 10, 0)
        );

        assertThatThrownBy(() -> appointment.cancel(LocalDateTime.of(2026, 7, 5, 11, 0)))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("PROGRAMADA");
    }

    @Test
    void attendsScheduledAppointment() {
        Appointment appointment = new Appointment(
                1L,
                10L,
                20L,
                LocalDateTime.of(2026, 7, 6, 8, 0),
                AppointmentStatus.PROGRAMADA,
                null
        );

        appointment.attend();

        assertThat(appointment.getStatus()).isEqualTo(AppointmentStatus.ATENDIDA);
    }

    @Test
    void rejectsAttendWhenAppointmentIsNotScheduled() {
        Appointment appointment = new Appointment(
                1L,
                10L,
                20L,
                LocalDateTime.of(2026, 7, 6, 8, 0),
                AppointmentStatus.CANCELADA,
                LocalDateTime.of(2026, 7, 5, 10, 0)
        );

        assertThatThrownBy(appointment::attend)
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("PROGRAMADA");
    }
}
