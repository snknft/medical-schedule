package com.ceiba.medisalud.domain.policy;

import java.time.LocalDate;

/**
 * Defines the contract used to determine whether a date is a holiday.
 */
public interface HolidayProvider {

    /**
     * Determines whether the provided date is configured as a holiday.
     */
    boolean isHoliday(LocalDate date);
}
