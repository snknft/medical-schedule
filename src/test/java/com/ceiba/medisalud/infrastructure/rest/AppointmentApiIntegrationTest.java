package com.ceiba.medisalud.infrastructure.rest;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class AppointmentApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createsPatientAndAppointmentThroughRestApi() throws Exception {
        mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Correlation-Id", "it-patient-flow")
                        .content("""
                                {
                                  "fullName": "Paciente Integracion",
                                  "documentNumber": "900000001",
                                  "phone": "3001234567",
                                  "email": "integration@example.com",
                                  "birthDate": "1997-05-10"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string("X-Correlation-Id", "it-patient-flow"))
                .andExpect(jsonPath("$.id", is(1)));

        mockMvc.perform(post("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Correlation-Id", "it-appointment-flow")
                        .content("""
                                {
                                  "patientId": 1,
                                  "doctorId": 1,
                                  "appointmentDateTime": "2026-08-03T08:00:00"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string("X-Correlation-Id", "it-appointment-flow"))
                .andExpect(jsonPath("$.status", is("PROGRAMADA")));
    }

    @Test
    void exposesReadinessEndpoint() throws Exception {
        mockMvc.perform(get("/actuator/health/readiness"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("UP")));
    }
}
