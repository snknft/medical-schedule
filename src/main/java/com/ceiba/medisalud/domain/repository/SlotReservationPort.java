package com.ceiba.medisalud.domain.repository;

import java.time.LocalDateTime;

public interface SlotReservationPort {

    void reserve(Long doctorId, Long patientId, LocalDateTime appointmentDateTime);

    void release(Long doctorId, Long patientId, LocalDateTime appointmentDateTime);
}
