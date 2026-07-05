package com.ceiba.medisalud.domain.policy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.ceiba.medisalud.domain.model.AvailableSlot;

public interface WorkingHoursPolicy {

    boolean isValidAppointmentStart(LocalDateTime dateTime);

    List<AvailableSlot> generateSlots(LocalDate date);
}
