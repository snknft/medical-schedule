package com.ceiba.medisalud.application.command;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.ceiba.medisalud.application.query.AvailableSlotsQuery;

class ApplicationRecordsTest {

    @Test
    void exposesRegisterDoctorCommandValues() {
        RegisterDoctorCommand command = new RegisterDoctorCommand(
                "Dra. Laura Pérez",
                "Cardiología",
                "5552001",
                "laura@example.com"
        );

        assertThat(command.fullName()).isEqualTo("Dra. Laura Pérez");
        assertThat(command.specialty()).isEqualTo("Cardiología");
        assertThat(command.phone()).isEqualTo("5552001");
        assertThat(command.email()).isEqualTo("laura@example.com");
    }

    @Test
    void exposesRegisterPatientCommandValues() {
        LocalDate birthDate = LocalDate.of(1995, 5, 20);
        RegisterPatientCommand command = new RegisterPatientCommand(
                "Juan Pérez",
                "123456789",
                "3001234567",
                "juan@example.com",
                birthDate
        );

        assertThat(command.fullName()).isEqualTo("Juan Pérez");
        assertThat(command.documentNumber()).isEqualTo("123456789");
        assertThat(command.phone()).isEqualTo("3001234567");
        assertThat(command.email()).isEqualTo("juan@example.com");
        assertThat(command.birthDate()).isEqualTo(birthDate);
    }

    @Test
    void exposesAppointmentCommandValues() {
        LocalDateTime appointmentDateTime = LocalDateTime.of(2026, 7, 6, 8, 0);
        ScheduleAppointmentCommand scheduleCommand = new ScheduleAppointmentCommand(1L, 2L, appointmentDateTime);
        RescheduleAppointmentCommand rescheduleCommand = new RescheduleAppointmentCommand(3L, appointmentDateTime.plusMinutes(30));

        assertThat(scheduleCommand.patientId()).isEqualTo(1L);
        assertThat(scheduleCommand.doctorId()).isEqualTo(2L);
        assertThat(scheduleCommand.appointmentDateTime()).isEqualTo(appointmentDateTime);
        assertThat(rescheduleCommand.appointmentId()).isEqualTo(3L);
        assertThat(rescheduleCommand.newDateTime()).isEqualTo(appointmentDateTime.plusMinutes(30));
    }

    @Test
    void exposesAvailableSlotsQueryValues() {
        LocalDate start = LocalDate.of(2026, 7, 6);
        LocalDate end = LocalDate.of(2026, 7, 10);
        AvailableSlotsQuery query = new AvailableSlotsQuery(1L, start, end);

        assertThat(query.doctorId()).isEqualTo(1L);
        assertThat(query.fechaInicio()).isEqualTo(start);
        assertThat(query.fechaFin()).isEqualTo(end);
    }
}
