package com.ceiba.medisalud.infrastructure.persistence.mapper;

import com.ceiba.medisalud.domain.model.Penalty;
import com.ceiba.medisalud.infrastructure.persistence.entity.PenaltyJpaEntity;

public final class PenaltyJpaMapper {

    private PenaltyJpaMapper() {
    }

    public static Penalty toDomain(PenaltyJpaEntity entity) {
        return new Penalty(entity.getId(), entity.getPatientId(), entity.getAppointmentId(), entity.getPenaltyDateTime(), entity.getReason());
    }

    public static PenaltyJpaEntity toEntity(Penalty penalty) {
        return new PenaltyJpaEntity(penalty.id(), penalty.patientId(), penalty.appointmentId(), penalty.penaltyDateTime(), penalty.reason());
    }
}
