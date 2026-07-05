package com.ceiba.medisalud.application.query;

import java.time.LocalDate;

public record AvailableSlotsQuery(
        Long doctorId,
        LocalDate fechaInicio,
        LocalDate fechaFin
) {
}
