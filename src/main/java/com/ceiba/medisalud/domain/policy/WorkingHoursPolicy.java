package com.ceiba.medisalud.domain.policy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.ceiba.medisalud.domain.model.AvailableSlot;

/**
 * Defines the contract for validating appointment start times and generating available slots.
 */
public interface WorkingHoursPolicy {

    /**
     * Determines whether the provided date-time is a valid appointment start.
     *
     * @param dateTime date-time to validate
     * @return {@code true} when the date-time matches an allowed slot start
     */
    boolean isValidAppointmentStart(LocalDateTime dateTime);

    /**
     * Generates valid appointment slots for the provided date.
     *
     * @param date date used to generate slots
     * @return ordered list of serviceable slots for the date
     */
    List<AvailableSlot> generateSlots(LocalDate date);
}
