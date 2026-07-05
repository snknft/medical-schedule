package com.ceiba.medisalud.infrastructure.rest.dto;

import java.time.LocalDateTime;

public record AvailableSlotResponse(
        LocalDateTime start,
        LocalDateTime end
) {
}
