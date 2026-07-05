package com.ceiba.medisalud.domain.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.ceiba.medisalud.domain.model.AppointmentSearchCriteria;
import com.ceiba.medisalud.domain.model.Appointment;

public interface AppointmentRepositoryPort {

    Appointment save(Appointment appointment);

    Optional<Appointment> findById(Long id);

    boolean existsScheduledForDoctorAt(Long doctorId, LocalDateTime dateTime, Long excludedAppointmentId);

    boolean existsScheduledForPatientAt(Long patientId, LocalDateTime dateTime, Long excludedAppointmentId);

    List<Appointment> findScheduledByDoctorBetween(Long doctorId, LocalDateTime from, LocalDateTime to);

    List<Appointment> search(AppointmentSearchCriteria criteria);
}
