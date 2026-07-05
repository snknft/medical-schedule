package com.ceiba.medisalud.infrastructure.rest.mapper;

import com.ceiba.medisalud.domain.model.Appointment;
import com.ceiba.medisalud.domain.model.AvailableSlot;
import com.ceiba.medisalud.domain.model.Doctor;
import com.ceiba.medisalud.domain.model.Patient;
import com.ceiba.medisalud.infrastructure.rest.dto.AppointmentResponse;
import com.ceiba.medisalud.infrastructure.rest.dto.AvailableSlotResponse;
import com.ceiba.medisalud.infrastructure.rest.dto.DoctorResponse;
import com.ceiba.medisalud.infrastructure.rest.dto.PatientResponse;

/**
 * Maps domain objects into REST response DTOs.
 */
public final class ApiResponseMapper {

    /**
     * Creates a new ApiResponseMapper instance.
     */
    private ApiResponseMapper() {
    }

    /**
     * Maps a domain object into a REST response.
     */
    public static DoctorResponse toResponse(Doctor doctor) {
        return new DoctorResponse(doctor.id(), doctor.fullName(), doctor.specialty(), doctor.phone(), doctor.email());
    }

    /**
     * Maps a domain object into a REST response.
     */
    public static PatientResponse toResponse(Patient patient) {
        return new PatientResponse(patient.id(), patient.fullName(), patient.documentNumber(), patient.phone(), patient.email(), patient.birthDate());
    }

    /**
     * Maps a domain object into a REST response.
     */
    public static AppointmentResponse toResponse(Appointment appointment) {
        return new AppointmentResponse(
                appointment.getId(),
                appointment.getPatientId(),
                appointment.getDoctorId(),
                appointment.getAppointmentDateTime(),
                appointment.getStatus(),
                appointment.getCancellationDateTime()
        );
    }

    /**
     * Maps a domain object into a REST response.
     */
    public static AvailableSlotResponse toResponse(AvailableSlot slot) {
        return new AvailableSlotResponse(slot.start(), slot.end());
    }
}
