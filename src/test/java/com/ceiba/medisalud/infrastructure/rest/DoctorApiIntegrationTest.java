package com.ceiba.medisalud.infrastructure.rest;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class DoctorApiIntegrationTest extends RestApiTestSupport {

    @Test
    void createsListsAndGetsDoctor() throws Exception {
        Long doctorId = createDoctor();

        mockMvc.perform(get("/api/doctors/{id}", doctorId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(doctorId))
                .andExpect(jsonPath("$.specialty").value("Medicina Interna"));

        mockMvc.perform(get("/api/doctors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", not(hasSize(0))));
    }

    @Test
    void rejectsInvalidDoctorRequest() throws Exception {
        mockMvc.perform(post("/api/doctors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "fullName": "AB",
                                  "specialty": "",
                                  "phone": "123",
                                  "email": "invalid-email"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }


    @Test
    void rejectsDoctorPhoneWithoutSevenDigits() throws Exception {
        mockMvc.perform(post("/api/doctors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "fullName": "Dra. Teléfono Inválido",
                                  "specialty": "Medicina Interna",
                                  "phone": "--- --- ---",
                                  "email": "doctor.invalid.phone@medisalud.com"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void returnsNotFoundWhenDoctorDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/doctors/{id}", 999999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }
}
