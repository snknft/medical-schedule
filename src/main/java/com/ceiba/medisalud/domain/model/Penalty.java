package com.ceiba.medisalud.domain.model;

import java.time.LocalDateTime;

/**
 * Represents a late cancellation penalty applied to a patient.
 */
public record Penalty(
        Long id,
        Long patientId,
        Long appointmentId,
        LocalDateTime penaltyDateTime,
        String reason
) {
}
