package com.ceiba.medisalud.infrastructure.persistence.specification;

import org.springframework.data.jpa.domain.Specification;

import com.ceiba.medisalud.domain.model.AppointmentSearchCriteria;
import com.ceiba.medisalud.infrastructure.persistence.entity.AppointmentJpaEntity;

public final class AppointmentJpaSpecifications {

    private AppointmentJpaSpecifications() {
    }

    public static Specification<AppointmentJpaEntity> byCriteria(AppointmentSearchCriteria criteria) {
        return Specification
                .where(hasDoctor(criteria.doctorId()))
                .and(hasPatient(criteria.patientId()))
                .and(hasStatus(criteria.status()))
                .and(dateGreaterOrEqual(criteria.fechaInicio()))
                .and(dateLessOrEqual(criteria.fechaFin()));
    }

    private static Specification<AppointmentJpaEntity> hasDoctor(Long doctorId) {
        return (root, query, builder) -> doctorId == null ? null : builder.equal(root.get("doctorId"), doctorId);
    }

    private static Specification<AppointmentJpaEntity> hasPatient(Long patientId) {
        return (root, query, builder) -> patientId == null ? null : builder.equal(root.get("patientId"), patientId);
    }

    private static Specification<AppointmentJpaEntity> hasStatus(Object status) {
        return (root, query, builder) -> status == null ? null : builder.equal(root.get("status"), status);
    }

    private static Specification<AppointmentJpaEntity> dateGreaterOrEqual(java.time.LocalDateTime fechaInicio) {
        return (root, query, builder) -> fechaInicio == null ? null : builder.greaterThanOrEqualTo(root.get("appointmentDateTime"), fechaInicio);
    }

    private static Specification<AppointmentJpaEntity> dateLessOrEqual(java.time.LocalDateTime fechaFin) {
        return (root, query, builder) -> fechaFin == null ? null : builder.lessThanOrEqualTo(root.get("appointmentDateTime"), fechaFin);
    }
}
