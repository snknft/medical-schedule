package com.ceiba.medisalud.infrastructure.rest;

import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.JsonNode;

class AvailableSlotsApiIntegrationTest extends RestApiTestSupport {

    @Test
    void returnsAvailableSlotsForDoctorAndDateRange() throws Exception {
        LocalDate testDate = uniqueFutureBusinessDate();

        mockMvc.perform(get("/api/doctors/{doctorId}/available-slots", 1L)
                        .param("fechaInicio", testDate.toString())
                        .param("fechaFin", testDate.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].start").value(startsWith(testDate.atTime(8, 0).toString())))
                .andExpect(jsonPath("$[0].end").value(startsWith(testDate.atTime(8, 30).toString())));
    }

    @Test
    void excludesOccupiedSlotsFromAvailability() throws Exception {
        Long patientId = createPatient();
        LocalDate testDate = uniqueFutureBusinessDate();
        LocalDateTime occupiedSlot = slot(testDate, 0);
        createAppointment(patientId, 1L, occupiedSlot);

        MvcResult result = mockMvc.perform(get("/api/doctors/{doctorId}/available-slots", 1L)
                        .param("fechaInicio", testDate.toString())
                        .param("fechaFin", testDate.toString()))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode slots = json(result);
        boolean containsOccupiedSlot = false;
        for (JsonNode availableSlot : slots) {
            if (availableSlot.get("start").asText().startsWith(occupiedSlot.toString())) {
                containsOccupiedSlot = true;
                break;
            }
        }
        assertFalse(containsOccupiedSlot, "Occupied slot must not be returned as available");
    }

    @Test
    void rejectsInvalidDateRangeForAvailableSlots() throws Exception {
        LocalDate testDate = uniqueFutureBusinessDate();

        mockMvc.perform(get("/api/doctors/{doctorId}/available-slots", 1L)
                        .param("fechaInicio", testDate.plusDays(1).toString())
                        .param("fechaFin", testDate.toString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void returnsNotFoundWhenDoctorDoesNotExistForAvailableSlots() throws Exception {
        LocalDate testDate = uniqueFutureBusinessDate();

        mockMvc.perform(get("/api/doctors/{doctorId}/available-slots", 999999L)
                        .param("fechaInicio", testDate.toString())
                        .param("fechaFin", testDate.toString()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }
}
