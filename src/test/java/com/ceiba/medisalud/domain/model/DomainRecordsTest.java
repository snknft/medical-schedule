package com.ceiba.medisalud.domain.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class DomainRecordsTest {

    @Test
    void exposesDoctorRecordValues() {
        Doctor doctor = new Doctor(1L, "Dra. María González", "Cardiología", "5551001", "maria@example.com");

        assertThat(doctor.id()).isEqualTo(1L);
        assertThat(doctor.fullName()).isEqualTo("Dra. María González");
        assertThat(doctor.specialty()).isEqualTo("Cardiología");
        assertThat(doctor.phone()).isEqualTo("5551001");
        assertThat(doctor.email()).isEqualTo("maria@example.com");
    }

    @Test
    void exposesPatientRecordValues() {
        LocalDate birthDate = LocalDate.of(1995, 5, 20);
        Patient patient = new Patient(1L, "Juan Pérez", "1234567", "3001234567", "juan@example.com", birthDate);

        assertThat(patient.id()).isEqualTo(1L);
        assertThat(patient.fullName()).isEqualTo("Juan Pérez");
        assertThat(patient.documentNumber()).isEqualTo("1234567");
        assertThat(patient.phone()).isEqualTo("3001234567");
        assertThat(patient.email()).isEqualTo("juan@example.com");
        assertThat(patient.birthDate()).isEqualTo(birthDate);
    }

    @Test
    void exposesPenaltyRecordValues() {
        LocalDateTime penaltyDateTime = LocalDateTime.of(2026, 7, 5, 10, 0);
        Penalty penalty = new Penalty(1L, 2L, 3L, penaltyDateTime, "Late cancellation");

        assertThat(penalty.id()).isEqualTo(1L);
        assertThat(penalty.patientId()).isEqualTo(2L);
        assertThat(penalty.appointmentId()).isEqualTo(3L);
        assertThat(penalty.penaltyDateTime()).isEqualTo(penaltyDateTime);
        assertThat(penalty.reason()).isEqualTo("Late cancellation");
    }

    @Test
    void exposesAvailableSlotAndSearchCriteriaValues() {
        LocalDateTime start = LocalDateTime.of(2026, 7, 6, 8, 0);
        LocalDateTime end = LocalDateTime.of(2026, 7, 6, 8, 30);
        AvailableSlot slot = new AvailableSlot(start, end);
        AppointmentSearchCriteria criteria = new AppointmentSearchCriteria(
                1L,
                2L,
                AppointmentStatus.PROGRAMADA,
                start,
                end
        );

        assertThat(slot.start()).isEqualTo(start);
        assertThat(slot.end()).isEqualTo(end);
        assertThat(criteria.doctorId()).isEqualTo(1L);
        assertThat(criteria.patientId()).isEqualTo(2L);
        assertThat(criteria.status()).isEqualTo(AppointmentStatus.PROGRAMADA);
        assertThat(criteria.fechaInicio()).isEqualTo(start);
        assertThat(criteria.fechaFin()).isEqualTo(end);
    }
}
