package com.ceiba.medisalud.infrastructure.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
abstract class RestApiTestSupport {

    private static final AtomicInteger WEEK_OFFSET = new AtomicInteger(0);

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    protected Long createDoctor() throws Exception {
        String suffix = uniqueSuffix();
        MvcResult result = mockMvc.perform(post("/api/doctors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "fullName": "Dr. Test %s",
                                  "specialty": "Medicina Interna",
                                  "phone": "5552001",
                                  "email": "doctor.%s@medisalud.com"
                                }
                                """.formatted(suffix, suffix)))
                .andExpect(status().isCreated())
                .andReturn();
        return json(result).get("id").asLong();
    }

    protected Long createPatient() throws Exception {
        String suffix = uniqueSuffix();
        MvcResult result = mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "fullName": "Paciente Test %s",
                                  "documentNumber": "DOC%s",
                                  "phone": "3001234567",
                                  "email": "patient.%s@example.com",
                                  "birthDate": "1995-05-20"
                                }
                                """.formatted(suffix, suffix, suffix)))
                .andExpect(status().isCreated())
                .andReturn();
        return json(result).get("id").asLong();
    }

    protected Long createAppointment(Long patientId, Long doctorId, LocalDateTime appointmentDateTime) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "patientId": %d,
                                  "doctorId": %d,
                                  "appointmentDateTime": "%s"
                                }
                                """.formatted(patientId, doctorId, appointmentDateTime)))
                .andExpect(status().isCreated())
                .andReturn();
        return json(result).get("id").asLong();
    }

    protected JsonNode json(MvcResult result) throws Exception {
        return objectMapper.readTree(result.getResponse().getContentAsString(StandardCharsets.UTF_8));
    }

    protected LocalDate uniqueFutureBusinessDate() {
        LocalDate base = LocalDate.now()
                .plusYears(1)
                .with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY))
                .plusWeeks(WEEK_OFFSET.getAndIncrement());
        while (base.getDayOfWeek() == DayOfWeek.SATURDAY || base.getDayOfWeek() == DayOfWeek.SUNDAY) {
            base = base.plusDays(1);
        }
        return base;
    }

    protected LocalDateTime slot(LocalDate date, int slotIndex) {
        return date.atTime(8, 0).plusMinutes(slotIndex * 30L);
    }

    protected String uniqueSuffix() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 12);
    }
}
