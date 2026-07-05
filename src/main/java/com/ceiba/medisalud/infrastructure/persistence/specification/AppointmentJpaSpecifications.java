package com.ceiba.medisalud.infrastructure.persistence.specification;

import org.springframework.data.jpa.domain.Specification;

import com.ceiba.medisalud.domain.model.AppointmentSearchCriteria;
import com.ceiba.medisalud.infrastructure.persistence.entity.AppointmentJpaEntity;

/**
 * Provides reusable JPA specifications for appointment queries.
 */
public final class AppointmentJpaSpecifications {

    /**
     * Prevents instantiation of the specification utility class.
     */
    private AppointmentJpaSpecifications() {
    }

    /**
     * Builds a JPA specification from appointment search criteria.
     *
     * @param criteria optional appointment filters
     * @return composed JPA specification
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
     * Builds a specification that filters appointments by doctor.
     *
     * @param doctorId optional doctor identifier
     * @return JPA specification for doctor filtering
     */
    private static Specification<AppointmentJpaEntity> hasDoctor(Long doctorId) {
        return (root, query, builder) -> doctorId == null ? null : builder.equal(root.get("doctorId"), doctorId);
    }

    /**
     * Builds a specification that filters appointments by patient.
     *
     * @param patientId optional patient identifier
     * @return JPA specification for patient filtering
     */
    private static Specification<AppointmentJpaEntity> hasPatient(Long patientId) {
        return (root, query, builder) -> patientId == null ? null : builder.equal(root.get("patientId"), patientId);
    }

    /**
     * Builds a specification that filters appointments by status.
     *
     * @param status optional appointment status
     * @return JPA specification for status filtering
     */
    private static Specification<AppointmentJpaEntity> hasStatus(Object status) {
        return (root, query, builder) -> status == null ? null : builder.equal(root.get("status"), status);
    }

    /**
     * Builds a specification that filters appointments from a start date-time.
     *
     * @param fechaInicio optional lower bound date-time
     * @return JPA specification for lower-bound date filtering
     */
    private static Specification<AppointmentJpaEntity> dateGreaterOrEqual(java.time.LocalDateTime fechaInicio) {
        return (root, query, builder) -> fechaInicio == null ? null : builder.greaterThanOrEqualTo(root.get("appointmentDateTime"), fechaInicio);
    }

    /**
     * Builds a specification that filters appointments up to an end date-time.
     *
     * @param fechaFin optional upper bound date-time
     * @return JPA specification for upper-bound date filtering
     */
    private static Specification<AppointmentJpaEntity> dateLessOrEqual(java.time.LocalDateTime fechaFin) {
        return (root, query, builder) -> fechaFin == null ? null : builder.lessThanOrEqualTo(root.get("appointmentDateTime"), fechaFin);
    }
}
