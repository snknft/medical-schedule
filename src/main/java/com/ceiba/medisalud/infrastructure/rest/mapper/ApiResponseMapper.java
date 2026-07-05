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
     * Prevents instantiation of the response mapper utility class.
     */
    private ApiResponseMapper() {
    }

    /**
     * Maps a doctor domain object into a REST response.
     *
     * @param doctor doctor domain object
     * @return doctor response DTO
     */
    public static DoctorResponse toResponse(Doctor doctor) {
        return new DoctorResponse(doctor.id(), doctor.fullName(), doctor.specialty(), doctor.phone(), doctor.email());
    }

    /**
     * Maps a patient domain object into a REST response.
     *
     * @param patient patient domain object
     * @return patient response DTO
     */
    public static PatientResponse toResponse(Patient patient) {
        return new PatientResponse(patient.id(), patient.fullName(), patient.documentNumber(), patient.phone(), patient.email(), patient.birthDate());
    }

    /**
     * Maps an appointment domain object into a REST response.
     *
     * @param appointment appointment domain object
     * @return appointment response DTO
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
     * Maps an available slot domain object into a REST response.
     *
     * @param slot available slot domain object
     * @return available slot response DTO
     */
    public static AvailableSlotResponse toResponse(AvailableSlot slot) {
        return new AvailableSlotResponse(slot.start(), slot.end());
    }
}
