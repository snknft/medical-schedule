package com.ceiba.medisalud.domain.policy;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.ceiba.medisalud.domain.model.AvailableSlot;

class DefaultMedicalWorkingHoursPolicyTest {

    private final HolidayProvider holidayProvider = date -> Set.of(LocalDate.of(2026, 7, 20)).contains(date);
    private final DefaultMedicalWorkingHoursPolicy policy = new DefaultMedicalWorkingHoursPolicy(holidayProvider);

    @Test
    void generatesTwentyWeekdaySlots() {
        LocalDate monday = LocalDate.of(2026, 7, 6);

        assertThat(policy.generateSlots(monday))
                .hasSize(20)
                .first()
                .extracting(AvailableSlot::start, AvailableSlot::end)
                .containsExactly(monday.atTime(8, 0), monday.atTime(8, 30));

        assertThat(policy.generateSlots(monday))
                .last()
                .extracting(AvailableSlot::start, AvailableSlot::end)
                .containsExactly(monday.atTime(17, 30), monday.atTime(18, 0));
    }

    @Test
    void generatesTenSaturdaySlots() {
        LocalDate saturday = LocalDate.of(2026, 7, 11);

        assertThat(policy.generateSlots(saturday))
                .hasSize(10)
                .last()
                .extracting(AvailableSlot::start, AvailableSlot::end)
                .containsExactly(saturday.atTime(12, 30), saturday.atTime(13, 0));
    }

    @Test
    void doesNotGenerateSlotsForSundayHolidayOrNullDate() {
        assertThat(policy.generateSlots(LocalDate.of(2026, 7, 5))).isEmpty();
        assertThat(policy.generateSlots(LocalDate.of(2026, 7, 20))).isEmpty();
        assertThat(policy.generateSlots(null)).isEmpty();
    }

    @Test
    void validatesAppointmentStart() {
        assertThat(policy.isValidAppointmentStart(LocalDateTime.of(2026, 7, 6, 8, 0))).isTrue();
        assertThat(policy.isValidAppointmentStart(LocalDateTime.of(2026, 7, 6, 8, 30))).isTrue();
        assertThat(policy.isValidAppointmentStart(LocalDateTime.of(2026, 7, 11, 12, 30))).isTrue();
    }

    @Test
    void rejectsInvalidAppointmentStart() {
        assertThat(policy.isValidAppointmentStart(null)).isFalse();
        assertThat(policy.isValidAppointmentStart(LocalDateTime.of(2026, 7, 6, 8, 15))).isFalse();
        assertThat(policy.isValidAppointmentStart(LocalDateTime.of(2026, 7, 6, 8, 0, 1))).isFalse();
        assertThat(policy.isValidAppointmentStart(LocalDateTime.of(2026, 7, 6, 18, 0))).isFalse();
        assertThat(policy.isValidAppointmentStart(LocalDateTime.of(2026, 7, 5, 8, 0))).isFalse();
        assertThat(policy.isValidAppointmentStart(LocalDateTime.of(2026, 7, 20, 8, 0))).isFalse();
    }
}
