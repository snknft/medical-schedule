package com.ceiba.medisalud.domain.model;

import java.time.LocalDateTime;

/**
 * Represents a late cancellation penalty applied to a patient.
 *
 * @param id penalty identifier
 * @param patientId penalized patient identifier
 * @param appointmentId appointment that originated the penalty
 * @param penaltyDateTime date-time when the penalty was registered
 * @param reason human-readable penalty reason
 */
public record Penalty(
        Long id,
        Long patientId,
        Long appointmentId,
        LocalDateTime penaltyDateTime,
        String reason
) {
}
