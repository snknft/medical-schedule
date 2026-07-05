package com.ceiba.medisalud.infrastructure.rest;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class PatientApiIntegrationTest extends RestApiTestSupport {

    @Test
    void createsListsAndGetsPatient() throws Exception {
        Long patientId = createPatient();

        mockMvc.perform(get("/api/patients/{id}", patientId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(patientId))
                .andExpect(jsonPath("$.email").exists());

        mockMvc.perform(get("/api/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", not(hasSize(0))));
    }

    @Test
    void rejectsInvalidPatientRequest() throws Exception {
        mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "fullName": "AB",
                                  "documentNumber": "123",
                                  "phone": "123",
                                  "email": "invalid-email",
                                  "birthDate": "2099-01-01"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void rejectsDuplicatedPatientDocument() throws Exception {
        String documentNumber = "DOC" + uniqueSuffix();
        String payload = """
                {
                  "fullName": "Paciente Duplicado",
                  "documentNumber": "%s",
                  "phone": "3001234567",
                  "email": "duplicated.%s@example.com",
                  "birthDate": "1995-05-20"
                }
                """.formatted(documentNumber, uniqueSuffix());

        mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }


    @Test
    void rejectsPatientPhoneWithoutSevenDigits() throws Exception {
        mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "fullName": "Paciente Teléfono Inválido",
                                  "documentNumber": "DOC%s",
                                  "phone": "--- --- ---",
                                  "email": "invalid.phone.%s@example.com",
                                  "birthDate": "1995-05-20"
                                }
                                """.formatted(uniqueSuffix(), uniqueSuffix())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void returnsNotFoundWhenPatientDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/patients/{id}", 999999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }
}
