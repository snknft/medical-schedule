package com.ceiba.medisalud.domain.policy;

import java.time.LocalDate;

public interface HolidayProvider {

    boolean isHoliday(LocalDate date);
}
