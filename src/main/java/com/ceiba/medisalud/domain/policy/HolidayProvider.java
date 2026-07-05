package com.ceiba.medisalud.domain.policy;

import java.time.LocalDate;

/**
 * Defines the contract used to determine whether a date is a holiday.
 */
public interface HolidayProvider {

    /**
     * Determines whether the provided date is configured as a holiday.
     *
     * @param date date to evaluate
     * @return {@code true} when the date is considered a holiday
     */
    boolean isHoliday(LocalDate date);
}
