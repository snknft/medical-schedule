package com.ceiba.medisalud.infrastructure.persistence.adapter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.ceiba.medisalud.domain.model.AppointmentSearchCriteria;
import com.ceiba.medisalud.domain.model.Appointment;
import com.ceiba.medisalud.domain.model.AppointmentStatus;
import com.ceiba.medisalud.domain.repository.AppointmentRepositoryPort;
import com.ceiba.medisalud.infrastructure.persistence.entity.AppointmentJpaEntity;
import com.ceiba.medisalud.infrastructure.persistence.mapper.AppointmentJpaMapper;
import com.ceiba.medisalud.infrastructure.persistence.specification.AppointmentJpaSpecifications;
import com.ceiba.medisalud.infrastructure.persistence.springdata.SpringDataAppointmentJpaRepository;

/**
 * Implements a domain port using Spring Data JPA for appointment persistence.
 */
@Repository
public class AppointmentJpaRepositoryAdapter implements AppointmentRepositoryPort {

    private final SpringDataAppointmentJpaRepository repository;

    /**
     * Creates the appointment adapter with the Spring Data repository.
     *
     * @param repository Spring Data repository used for appointment persistence
     */
    public AppointmentJpaRepositoryAdapter(SpringDataAppointmentJpaRepository repository) {
        this.repository = repository;
    }

    /**
     * Persists the provided domain object and returns the saved instance.
     *
     * @param appointment domain appointment to persist
     * @return persisted appointment
     */
    @Override
    public Appointment save(Appointment appointment) {
        return AppointmentJpaMapper.toDomain(repository.save(AppointmentJpaMapper.toEntity(appointment)));
    }

    /**
     * Finds a resource by its identifier.
     *
     * @param id resource identifier
     * @return optional domain object
     */
    @Override
    public Optional<Appointment> findById(Long id) {
        return repository.findById(id).map(AppointmentJpaMapper::toDomain);
    }

    /**
     * Determines whether a doctor already has an active appointment in the provided slot.
     *
     * @param doctorId doctor identifier
     * @param dateTime appointment slot date-time
     * @param excludedAppointmentId appointment identifier ignored by the check
     * @return {@code true} when a scheduled doctor conflict exists
     */
    @Override
    public boolean existsScheduledForDoctorAt(Long doctorId, LocalDateTime dateTime, Long excludedAppointmentId) {
        return repository.findAll((root, query, builder) -> builder.and(
                        builder.equal(root.get("doctorId"), doctorId),
                        builder.equal(root.get("appointmentDateTime"), dateTime),
                        builder.equal(root.get("status"), AppointmentStatus.PROGRAMADA)
                ))
                .stream()
                .anyMatch(entity -> !Objects.equals(entity.getId(), excludedAppointmentId));
    }

    /**
     * Determines whether a patient already has an active appointment in the provided slot.
     *
     * @param patientId patient identifier
     * @param dateTime appointment slot date-time
     * @param excludedAppointmentId appointment identifier ignored by the check
     * @return {@code true} when a scheduled patient conflict exists
     */
    @Override
    public boolean existsScheduledForPatientAt(Long patientId, LocalDateTime dateTime, Long excludedAppointmentId) {
        return repository.findAll((root, query, builder) -> builder.and(
                        builder.equal(root.get("patientId"), patientId),
                        builder.equal(root.get("appointmentDateTime"), dateTime),
                        builder.equal(root.get("status"), AppointmentStatus.PROGRAMADA)
                ))
                .stream()
                .anyMatch(entity -> !Objects.equals(entity.getId(), excludedAppointmentId));
    }

    /**
     * Finds scheduled appointments for a doctor within the provided date-time range.
     *
     * @param doctorId doctor identifier
     * @param from initial date-time included in the search
     * @param to final date-time included in the search
     * @return scheduled appointments for the doctor in the range
     */
    @Override
    public List<Appointment> findScheduledByDoctorBetween(Long doctorId, LocalDateTime from, LocalDateTime to) {
        return repository.findByDoctorIdAndStatusAndAppointmentDateTimeBetween(doctorId, AppointmentStatus.PROGRAMADA, from, to)
                .stream()
                .map(AppointmentJpaMapper::toDomain)
                .toList();
    }

    /**
     * Searches appointments using the provided optional filters.
     *
     * @param criteria optional appointment filters
     * @return appointments matching the criteria
     */
    @Override
    public List<Appointment> search(AppointmentSearchCriteria criteria) {
        return repository.findAll(
                        AppointmentJpaSpecifications.byCriteria(criteria),
                        Sort.by(Sort.Direction.ASC, "appointmentDateTime")
                )
                .stream()
                .map(AppointmentJpaMapper::toDomain)
                .toList();
    }
}
