package com.ceiba.medisalud.infrastructure.rest;

import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.JsonNode;

class FullEndToEndApiIntegrationTest extends RestApiTestSupport {

    @Test
    void completesMedicalSchedulingFlowEndToEnd() throws Exception {
        Long doctorId = createDoctor();
        Long patientId = createPatient();
        LocalDate testDate = uniqueFutureBusinessDate();
        LocalDateTime originalDateTime = slot(testDate, 0);
        LocalDateTime newDateTime = slot(testDate, 1);

        mockMvc.perform(get("/api/doctors/{doctorId}/available-slots", doctorId)
                        .param("fechaInicio", testDate.toString())
                        .param("fechaFin", testDate.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].start").value(startsWith(originalDateTime.toString())));

        Long originalAppointmentId = createAppointment(patientId, doctorId, originalDateTime);

        MvcResult searchResult = mockMvc.perform(get("/api/appointments")
                        .param("doctorId", doctorId.toString())
                        .param("patientId", patientId.toString())
                        .param("status", "PROGRAMADA")
                        .param("fechaInicio", testDate.atStartOfDay().toString())
                        .param("fechaFin", testDate.atTime(23, 59, 59).toString()))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode scheduledAppointments = json(searchResult);
        boolean originalAppointmentWasListed = false;
        for (JsonNode appointment : scheduledAppointments) {
            if (appointment.get("id").asLong() == originalAppointmentId) {
                originalAppointmentWasListed = true;
                break;
            }
        }
        assertTrue(originalAppointmentWasListed, "Scheduled appointment must be listed by search filters");

        MvcResult rescheduleResult = mockMvc.perform(patch("/api/appointments/{id}/reschedule", originalAppointmentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "newDateTime": "%s"
                                }
                                """.formatted(newDateTime)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PROGRAMADA"))
                .andExpect(jsonPath("$.appointmentDateTime").value(startsWith(newDateTime.toString())))
                .andReturn();

        Long newAppointmentId = json(rescheduleResult).get("id").asLong();

        mockMvc.perform(get("/api/appointments/{id}", originalAppointmentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELADA"));

        mockMvc.perform(patch("/api/appointments/{id}/attend", newAppointmentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ATENDIDA"));
    }
}
