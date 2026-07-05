package com.ceiba.medisalud.domain.policy;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.ceiba.medisalud.domain.model.AvailableSlot;

/**
 * Implements the default medical working hours and slot generation policy.
 */
public class DefaultMedicalWorkingHoursPolicy implements WorkingHoursPolicy {

    /**
     * Executes the of operation.
     */
    private static final LocalTime WEEKDAY_OPEN = LocalTime.of(8, 0);
    /**
     * Executes the of operation.
     */
    private static final LocalTime WEEKDAY_CLOSE = LocalTime.of(18, 0);
    /**
     * Executes the of operation.
     */
    private static final LocalTime SATURDAY_OPEN = LocalTime.of(8, 0);
    /**
     * Executes the of operation.
     */
    private static final LocalTime SATURDAY_CLOSE = LocalTime.of(13, 0);
    private static final int SLOT_MINUTES = 30;

    private final HolidayProvider holidayProvider;

    /**
     * Creates a new DefaultMedicalWorkingHoursPolicy instance.
     */
    public DefaultMedicalWorkingHoursPolicy(HolidayProvider holidayProvider) {
        this.holidayProvider = holidayProvider;
    }

    /**
     * Determines whether the provided date-time is a valid appointment start.
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
