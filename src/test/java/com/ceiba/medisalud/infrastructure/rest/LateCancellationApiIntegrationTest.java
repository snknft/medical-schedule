package com.ceiba.medisalud.infrastructure.rest;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;

import com.ceiba.medisalud.infrastructure.persistence.springdata.SpringDataPenaltyJpaRepository;

/**
 * Verifies the late cancellation penalty business flow through the public REST API.
 */
@TestPropertySource(properties = "medisalud.clock.fixed-now=2026-07-06T07:30:00")
class LateCancellationApiIntegrationTest extends RestApiTestSupport {

    private static final LocalDateTime FIXED_NOW = LocalDateTime.of(2026, 7, 6, 7, 30);
    private static final LocalDateTime LATE_APPOINTMENT_DATE_TIME = LocalDateTime.of(2026, 7, 6, 8, 0);
    private static final LocalDateTime NEXT_APPOINTMENT_DATE_TIME = LocalDateTime.of(2026, 7, 6, 8, 30);

    @Autowired
    private SpringDataPenaltyJpaRepository penaltyRepository;

    /**
     * Ensures that three late cancellations create three penalties and block new scheduling.
     *
     * @throws Exception when the REST request execution fails
     */
    @Test
    void lateCancellationCreatesPenaltyAndBlocksPatientAfterThreePenalties() throws Exception {
        Long doctorId = createDoctor();
        Long patientId = createPatient();

        Long firstAppointmentId = createAppointment(patientId, doctorId, LATE_APPOINTMENT_DATE_TIME);
        cancelAppointment(firstAppointmentId);
        assertPenaltyCount(patientId, 1);

        Long secondAppointmentId = createAppointment(patientId, doctorId, LATE_APPOINTMENT_DATE_TIME);
        cancelAppointment(secondAppointmentId);
        assertPenaltyCount(patientId, 2);

        Long thirdAppointmentId = createAppointment(patientId, doctorId, LATE_APPOINTMENT_DATE_TIME);
        cancelAppointment(thirdAppointmentId);
        assertPenaltyCount(patientId, 3);

        mockMvc.perform(post("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "patientId": %d,
                                  "doctorId": %d,
                                  "appointmentDateTime": "%s"
                                }
                                """.formatted(patientId, doctorId, NEXT_APPOINTMENT_DATE_TIME)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message", containsString("penalizaciones")));
    }

    /**
     * Cancels the provided appointment through the REST API and validates the response.
     *
     * @param appointmentId appointment identifier to cancel
     * @throws Exception when the REST request execution fails
     */
    private void cancelAppointment(Long appointmentId) throws Exception {
        mockMvc.perform(patch("/api/appointments/{id}/cancel", appointmentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(appointmentId))
                .andExpect(jsonPath("$.status").value("CANCELADA"))
                .andExpect(jsonPath("$.cancellationDateTime").exists());
    }

    /**
     * Asserts the number of recent penalties registered for the provided patient.
     *
     * @param patientId patient identifier
     * @param expectedCount expected penalty count in the last thirty days
     */
    private void assertPenaltyCount(Long patientId, long expectedCount) {
        long penaltyCount = penaltyRepository.countByPatientIdAndPenaltyDateTimeGreaterThanEqual(
                patientId,
                FIXED_NOW.minusDays(30)
        );
        assertEquals(expectedCount, penaltyCount);
    }
}
