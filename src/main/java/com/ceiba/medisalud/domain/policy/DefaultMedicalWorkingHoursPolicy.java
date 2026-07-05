package com.ceiba.medisalud.domain.policy;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.ceiba.medisalud.domain.model.AvailableSlot;

/**
 * Implements the default medical working hours and 30-minute slot generation policy.
 */
public class DefaultMedicalWorkingHoursPolicy implements WorkingHoursPolicy {

    private static final LocalTime WEEKDAY_OPEN = LocalTime.of(8, 0);
    private static final LocalTime WEEKDAY_CLOSE = LocalTime.of(18, 0);
    private static final LocalTime SATURDAY_OPEN = LocalTime.of(8, 0);
    private static final LocalTime SATURDAY_CLOSE = LocalTime.of(13, 0);
    private static final int SLOT_MINUTES = 30;

    private final HolidayProvider holidayProvider;

    /**
     * Creates the policy with a holiday provider.
     *
     * @param holidayProvider provider used to exclude holidays from appointment scheduling
     */
    public DefaultMedicalWorkingHoursPolicy(HolidayProvider holidayProvider) {
        this.holidayProvider = holidayProvider;
    }

    /**
     * Determines whether the provided date-time is a valid appointment start.
     *
     * @param dateTime date-time to validate
     * @return {@code true} when the value starts at a valid 30-minute working slot
     */
    @Override
    public boolean isValidAppointmentStart(LocalDateTime dateTime) {
        if (dateTime == null) {
            return false;
        }
        if (dateTime.getSecond() != 0 || dateTime.getNano() != 0) {
            return false;
        }
        if (dateTime.getMinute() != 0 && dateTime.getMinute() != 30) {
            return false;
        }
        return generateSlots(dateTime.toLocalDate()).stream()
                .anyMatch(slot -> slot.start().equals(dateTime));
    }

    /**
     * Generates valid appointment slots for the provided date.
     *
     * @param date date for which slots must be generated
     * @return ordered list of valid 30-minute slots; empty when the date is not serviceable
     */
    @Override
    public List<AvailableSlot> generateSlots(LocalDate date) {
        List<AvailableSlot> slots = new ArrayList<>();
        if (date == null || holidayProvider.isHoliday(date) || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
            return slots;
        }

        LocalTime open;
        LocalTime close;
        if (date.getDayOfWeek() == DayOfWeek.SATURDAY) {
            open = SATURDAY_OPEN;
            close = SATURDAY_CLOSE;
        } else {
            open = WEEKDAY_OPEN;
            close = WEEKDAY_CLOSE;
        }

        LocalDateTime cursor = LocalDateTime.of(date, open);
        LocalDateTime end = LocalDateTime.of(date, close);
        while (!cursor.plusMinutes(SLOT_MINUTES).isAfter(end)) {
            slots.add(new AvailableSlot(cursor, cursor.plusMinutes(SLOT_MINUTES)));
            cursor = cursor.plusMinutes(SLOT_MINUTES);
        }
        return slots;
    }
}
