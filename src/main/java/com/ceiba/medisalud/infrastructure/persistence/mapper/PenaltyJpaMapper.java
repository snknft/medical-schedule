package com.ceiba.medisalud.infrastructure.persistence.mapper;

import com.ceiba.medisalud.domain.model.Penalty;
import com.ceiba.medisalud.infrastructure.persistence.entity.PenaltyJpaEntity;

/**
 * Maps penalty data between domain models and JPA entities.
 */
public final class PenaltyJpaMapper {

    /**
     * Creates a new PenaltyJpaMapper instance.
     */
    private PenaltyJpaMapper() {
    }

    /**
     * Maps a persistence representation into a domain object.
     */
    public static Penalty toDomain(PenaltyJpaEntity entity) {
        return new Penalty(entity.getId(), entity.getPatientId(), entity.getAppointmentId(), entity.getPenaltyDateTime(), entity.getReason());
    }

    /**
     * Maps a domain object into a persistence representation.
     */
    public static PenaltyJpaEntity toEntity(Penalty penalty) {
        return new PenaltyJpaEntity(penalty.id(), penalty.patientId(), penalty.appointmentId(), penalty.penaltyDateTime(), penalty.reason());
    }
}
