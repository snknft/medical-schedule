package com.ceiba.medisalud.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ceiba.medisalud.domain.exception.BadRequestException;
import com.ceiba.medisalud.domain.model.Patient;
import com.ceiba.medisalud.domain.policy.WorkingHoursPolicy;

class AppointmentRulesServiceTest {

    private AppointmentRulesService service;

    @BeforeEach
    void setUp() {
        WorkingHoursPolicy workingHoursPolicy = new WorkingHoursPolicy() {
            @Override
            public boolean isValidAppointmentStart(LocalDateTime dateTime) {
                return LocalDateTime.of(2026, 7, 6, 8, 0).equals(dateTime);
            }

            @Override
            public java.util.List<com.ceiba.medisalud.domain.model.AvailableSlot> generateSlots(LocalDate date) {
                return java.util.List.of();
            }
        };
        service = new AppointmentRulesService(workingHoursPolicy);
    }

    @Test
    void acceptsSchedulableFutureDateTime() {
        assertThatCode(() -> service.validateSchedulableDateTime(
                LocalDateTime.of(2026, 7, 6, 8, 0),
                LocalDateTime.of(2026, 7, 1, 8, 0)
        )).doesNotThrowAnyException();
    }

    @Test
    void rejectsNullPastAndInvalidDateTimes() {
        LocalDateTime now = LocalDateTime.of(2026, 7, 1, 8, 0);

        assertThatThrownBy(() -> service.validateSchedulableDateTime(null, now))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("obligatoria");

        assertThatThrownBy(() -> service.validateSchedulableDateTime(now.minusMinutes(1), now))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("pasado");

        assertThatThrownBy(() -> service.validateSchedulableDateTime(LocalDateTime.of(2026, 7, 6, 8, 30), now))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("horario laboral");
    }

    @Test
    void validatesPatientBirthDateAtScheduling() {
        LocalDate currentDate = LocalDate.of(2026, 7, 1);
        Patient withoutBirthDate = new Patient(1L, "Paciente", "1234567", "3001234567", "patient@example.com", null);
        Patient validPatient = new Patient(2L, "Paciente", "7654321", "3001234567", "patient2@example.com", currentDate);
        Patient futureBirthDate = new Patient(3L, "Paciente", "7777777", "3001234567", "patient3@example.com", currentDate.plusDays(1));

        assertThatCode(() -> service.validatePatientAgeAtScheduling(withoutBirthDate, currentDate)).doesNotThrowAnyException();
        assertThatCode(() -> service.validatePatientAgeAtScheduling(validPatient, currentDate)).doesNotThrowAnyException();
        assertThatThrownBy(() -> service.validatePatientAgeAtScheduling(futureBirthDate, currentDate))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("nacimiento");
    }

    @Test
    void identifiesLateCancellationOnlyWhenLessThanTwoHoursAhead() {
        LocalDateTime appointmentDateTime = LocalDateTime.of(2026, 7, 6, 10, 0);

        assertThat(service.isLateCancellation(appointmentDateTime, LocalDateTime.of(2026, 7, 6, 8, 1))).isTrue();
        assertThat(service.isLateCancellation(appointmentDateTime, LocalDateTime.of(2026, 7, 6, 8, 0))).isFalse();
        assertThat(service.isLateCancellation(appointmentDateTime, LocalDateTime.of(2026, 7, 6, 10, 1))).isFalse();
    }

    @Test
    void validatesCancelableAppointmentDateTime() {
        LocalDateTime now = LocalDateTime.of(2026, 7, 1, 8, 0);

        assertThatCode(() -> service.validateCancelable(now.plusHours(1), now)).doesNotThrowAnyException();
        assertThatThrownBy(() -> service.validateCancelable(now.minusMinutes(1), now))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("vencidas");
    }
}
