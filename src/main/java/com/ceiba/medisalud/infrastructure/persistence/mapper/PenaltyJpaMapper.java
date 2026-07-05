package com.ceiba.medisalud.infrastructure.persistence.mapper;

import com.ceiba.medisalud.domain.model.Penalty;
import com.ceiba.medisalud.infrastructure.persistence.entity.PenaltyJpaEntity;

/**
 * Maps penalty data between domain models and JPA entities.
 */
public final class PenaltyJpaMapper {

    /**
     * Prevents instantiation of the PenaltyJpaMapper utility class.
     */
    private PenaltyJpaMapper() {
    }

    /**
     * Maps a penalty JPA entity into a domain object.
     *
     * @param entity penalty JPA entity
     * @return penalty domain object
     */
    public static Penalty toDomain(PenaltyJpaEntity entity) {
        return new Penalty(entity.getId(), entity.getPatientId(), entity.getAppointmentId(), entity.getPenaltyDateTime(), entity.getReason());
    }

    /**
     * Maps a penalty domain object into a JPA entity.
     *
     * @param penalty penalty domain object
     * @return penalty JPA entity
     */
    public static PenaltyJpaEntity toEntity(Penalty penalty) {
        return new PenaltyJpaEntity(penalty.id(), penalty.patientId(), penalty.appointmentId(), penalty.penaltyDateTime(), penalty.reason());
    }
}
