package com.ceiba.medisalud.infrastructure.rest.mapper;

import com.ceiba.medisalud.domain.model.Appointment;
import com.ceiba.medisalud.domain.model.AvailableSlot;
import com.ceiba.medisalud.domain.model.Doctor;
import com.ceiba.medisalud.domain.model.Patient;
import com.ceiba.medisalud.infrastructure.rest.dto.AppointmentResponse;
import com.ceiba.medisalud.infrastructure.rest.dto.AvailableSlotResponse;
import com.ceiba.medisalud.infrastructure.rest.dto.DoctorResponse;
import com.ceiba.medisalud.infrastructure.rest.dto.PatientResponse;

public final class ApiResponseMapper {

    private ApiResponseMapper() {
    }

    public static DoctorResponse toResponse(Doctor doctor) {
        return new DoctorResponse(doctor.id(), doctor.fullName(), doctor.specialty(), doctor.phone(), doctor.email());
    }

    public static PatientResponse toResponse(Patient patient) {
        return new PatientResponse(patient.id(), patient.fullName(), patient.documentNumber(), patient.phone(), patient.email(), patient.birthDate());
    }

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

    public static AvailableSlotResponse toResponse(AvailableSlot slot) {
        return new AvailableSlotResponse(slot.start(), slot.end());
    }
}
