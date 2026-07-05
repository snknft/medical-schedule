package com.ceiba.medisalud.infrastructure.persistence.specification;

import org.springframework.data.jpa.domain.Specification;

import com.ceiba.medisalud.domain.model.AppointmentSearchCriteria;
import com.ceiba.medisalud.infrastructure.persistence.entity.AppointmentJpaEntity;

/**
 * Provides reusable JPA specifications for appointment queries.
 */
public final class AppointmentJpaSpecifications {

    /**
     * Creates a new AppointmentJpaSpecifications instance.
     */
    private AppointmentJpaSpecifications() {
    }

    /**
     * Builds a JPA specification from appointment search criteria.
     */
    public static Specification<AppointmentJpaEntity> byCriteria(AppointmentSearchCriteria criteria) {
        return Specification
                .where(hasDoctor(criteria.doctorId()))
                .and(hasPatient(criteria.patientId()))
                .and(hasStatus(criteria.status()))
                .and(dateGreaterOrEqual(criteria.fechaInicio()))
                .and(dateLessOrEqual(criteria.fechaFin()));
    }

    /**
     * Executes the hasDoctor operation.
     */
    private static Specification<AppointmentJpaEntity> hasDoctor(Long doctorId) {
        return (root, query, builder) -> doctorId == null ? null : builder.equal(root.get("doctorId"), doctorId);
    }

    /**
     * Executes the hasPatient operation.
     */
    private static Specification<AppointmentJpaEntity> hasPatient(Long patientId) {
        return (root, query, builder) -> patientId == null ? null : builder.equal(root.get("patientId"), patientId);
    }

    /**
     * Executes the hasStatus operation.
     */
    private static Specification<AppointmentJpaEntity> hasStatus(Object status) {
        return (root, query, builder) -> status == null ? null : builder.equal(root.get("status"), status);
    }

    /**
     * Builds a specification that filters appointments from a start date-time.
     */
    private static Specification<AppointmentJpaEntity> dateGreaterOrEqual(java.time.LocalDateTime fechaInicio) {
        return (root, query, builder) -> fechaInicio == null ? null : builder.greaterThanOrEqualTo(root.get("appointmentDateTime"), fechaInicio);
    }

    /**
     * Builds a specification that filters appointments up to an end date-time.
     */
    private static Specification<AppointmentJpaEntity> dateLessOrEqual(java.time.LocalDateTime fechaFin) {
        return (root, query, builder) -> fechaFin == null ? null : builder.lessThanOrEqualTo(root.get("appointmentDateTime"), fechaFin);
    }
}
