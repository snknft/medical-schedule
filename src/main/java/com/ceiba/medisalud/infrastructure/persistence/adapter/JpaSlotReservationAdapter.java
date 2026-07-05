package com.ceiba.medisalud.infrastructure.persistence.adapter;

import java.time.LocalDateTime;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import com.ceiba.medisalud.domain.exception.ConflictException;
import com.ceiba.medisalud.domain.repository.SlotReservationPort;
import com.ceiba.medisalud.infrastructure.persistence.entity.DoctorSlotLockJpaEntity;
import com.ceiba.medisalud.infrastructure.persistence.entity.PatientSlotLockJpaEntity;
import com.ceiba.medisalud.infrastructure.persistence.springdata.SpringDataDoctorSlotLockJpaRepository;
import com.ceiba.medisalud.infrastructure.persistence.springdata.SpringDataPatientSlotLockJpaRepository;

/**
 * Implements guarded slot reservations using doctor and patient lock tables.
 */
@Repository
public class JpaSlotReservationAdapter implements SlotReservationPort {

    private final SpringDataDoctorSlotLockJpaRepository doctorSlotLockRepository;
    private final SpringDataPatientSlotLockJpaRepository patientSlotLockRepository;

    /**
     * Creates the slot reservation adapter with doctor and patient lock repositories.
     *
     * @param doctorSlotLockRepository repository used to persist doctor slot locks
     * @param patientSlotLockRepository repository used to persist patient slot locks
     */
    public JpaSlotReservationAdapter(
            SpringDataDoctorSlotLockJpaRepository doctorSlotLockRepository,
            SpringDataPatientSlotLockJpaRepository patientSlotLockRepository
    ) {
        this.doctorSlotLockRepository = doctorSlotLockRepository;
        this.patientSlotLockRepository = patientSlotLockRepository;
    }

    /**
     * Reserves an appointment slot for the doctor and patient.
     *
     * @param doctorId doctor identifier
     * @param patientId patient identifier
     * @param appointmentDateTime appointment slot date-time
     */
    @Override
    public void reserve(Long doctorId, Long patientId, LocalDateTime appointmentDateTime) {
        reserveDoctorSlot(doctorId, appointmentDateTime);
        reservePatientSlot(patientId, appointmentDateTime);
    }

    /**
     * Releases the appointment slot reservation for the doctor and patient.
     *
     * @param doctorId doctor identifier
     * @param patientId patient identifier
     * @param appointmentDateTime appointment slot date-time
     */
    @Override
    public void release(Long doctorId, Long patientId, LocalDateTime appointmentDateTime) {
        doctorSlotLockRepository.deleteByDoctorIdAndAppointmentDateTime(doctorId, appointmentDateTime);
        patientSlotLockRepository.deleteByPatientIdAndAppointmentDateTime(patientId, appointmentDateTime);
    }

    /**
     * Persists the guarded slot reservation for a doctor.
     *
     * @param doctorId doctor identifier
     * @param appointmentDateTime appointment slot date-time
     */
    private void reserveDoctorSlot(Long doctorId, LocalDateTime appointmentDateTime) {
        try {
            doctorSlotLockRepository.saveAndFlush(new DoctorSlotLockJpaEntity(doctorId, appointmentDateTime));
        } catch (DataIntegrityViolationException exception) {
            throw new ConflictException("La franja ya fue tomada por otro proceso para el médico seleccionado");
        }
    }

    /**
     * Persists the guarded slot reservation for a patient.
     *
     * @param patientId patient identifier
     * @param appointmentDateTime appointment slot date-time
     */
    private void reservePatientSlot(Long patientId, LocalDateTime appointmentDateTime) {
        try {
            patientSlotLockRepository.saveAndFlush(new PatientSlotLockJpaEntity(patientId, appointmentDateTime));
        } catch (DataIntegrityViolationException exception) {
            throw new ConflictException("La franja ya fue tomada por otro proceso para el paciente seleccionado");
        }
    }
}
