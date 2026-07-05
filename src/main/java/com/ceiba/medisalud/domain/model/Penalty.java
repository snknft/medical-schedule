package com.ceiba.medisalud.domain.model;

import java.time.LocalDateTime;

public record Penalty(
        Long id,
        Long patientId,
        Long appointmentId,
        LocalDateTime penaltyDateTime,
        String reason
) {
}
