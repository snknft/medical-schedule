package com.ceiba.medisalud.infrastructure.rest;

import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class AppointmentApiIntegrationTest extends RestApiTestSupport {

    @Test
    void createsListsAndGetsAppointment() throws Exception {
        Long doctorId = createDoctor();
        Long patientId = createPatient();
        LocalDate testDate = uniqueFutureBusinessDate();
        LocalDateTime appointmentDateTime = slot(testDate, 0);

        Long appointmentId = createAppointment(patientId, doctorId, appointmentDateTime);

        mockMvc.perform(get("/api/appointments/{id}", appointmentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(appointmentId))
                .andExpect(jsonPath("$.status").value("PROGRAMADA"));

        mockMvc.perform(get("/api/appointments")
                        .param("doctorId", doctorId.toString())
                        .param("patientId", patientId.toString())
                        .param("status", "PROGRAMADA")
                        .param("fechaInicio", testDate.atStartOfDay().toString())
                        .param("fechaFin", testDate.atTime(23, 59, 59).toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(appointmentId));
    }

    @Test
    void rejectsDuplicatedDoctorSlot() throws Exception {
        Long doctorId = createDoctor();
        Long firstPatientId = createPatient();
        Long secondPatientId = createPatient();
        LocalDateTime appointmentDateTime = slot(uniqueFutureBusinessDate(), 0);

        createAppointment(firstPatientId, doctorId, appointmentDateTime);

        mockMvc.perform(post("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "patientId": %d,
                                  "doctorId": %d,
                                  "appointmentDateTime": "%s"
                                }
                                """.formatted(secondPatientId, doctorId, appointmentDateTime)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    void rejectsAppointmentOutsideWorkingHours() throws Exception {
        Long doctorId = createDoctor();
        Long patientId = createPatient();
        LocalDateTime dateTime = uniqueFutureBusinessDate().atTime(19, 0);

        mockMvc.perform(post("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "patientId": %d,
                                  "doctorId": %d,
                                  "appointmentDateTime": "%s"
                                }
                                """.formatted(patientId, doctorId, dateTime)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void rejectsAppointmentWithInvalidSlotStart() throws Exception {
        Long doctorId = createDoctor();
        Long patientId = createPatient();
        LocalDateTime dateTime = uniqueFutureBusinessDate().atTime(8, 15);

        mockMvc.perform(post("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "patientId": %d,
                                  "doctorId": %d,
                                  "appointmentDateTime": "%s"
                                }
                                """.formatted(patientId, doctorId, dateTime)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void cancelsAppointment() throws Exception {
        Long doctorId = createDoctor();
        Long patientId = createPatient();
        LocalDateTime appointmentDateTime = slot(uniqueFutureBusinessDate(), 0);
        Long appointmentId = createAppointment(patientId, doctorId, appointmentDateTime);

        mockMvc.perform(patch("/api/appointments/{id}/cancel", appointmentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(appointmentId))
                .andExpect(jsonPath("$.status").value("CANCELADA"))
                .andExpect(jsonPath("$.cancellationDateTime").exists());
    }

    @Test
    void reschedulesAppointment() throws Exception {
        Long doctorId = createDoctor();
        Long patientId = createPatient();
        LocalDate testDate = uniqueFutureBusinessDate();
        LocalDateTime originalDateTime = slot(testDate, 0);
        LocalDateTime newDateTime = slot(testDate, 1);
        Long appointmentId = createAppointment(patientId, doctorId, originalDateTime);

        mockMvc.perform(patch("/api/appointments/{id}/reschedule", appointmentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "newDateTime": "%s"
                                }
                                """.formatted(newDateTime)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PROGRAMADA"))
                .andExpect(jsonPath("$.appointmentDateTime").value(startsWith(newDateTime.toString())));
    }

    @Test
    void marksAppointmentAsAttended() throws Exception {
        Long doctorId = createDoctor();
        Long patientId = createPatient();
        LocalDateTime appointmentDateTime = slot(uniqueFutureBusinessDate(), 0);
        Long appointmentId = createAppointment(patientId, doctorId, appointmentDateTime);

        mockMvc.perform(patch("/api/appointments/{id}/attend", appointmentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(appointmentId))
                .andExpect(jsonPath("$.status").value("ATENDIDA"));
    }

    @Test
    void returnsNotFoundWhenAppointmentDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/appointments/{id}", 999999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }
}
