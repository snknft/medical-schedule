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

@Repository
public class JpaSlotReservationAdapter implements SlotReservationPort {

    private final SpringDataDoctorSlotLockJpaRepository doctorSlotLockRepository;
    private final SpringDataPatientSlotLockJpaRepository patientSlotLockRepository;

    public JpaSlotReservationAdapter(
            SpringDataDoctorSlotLockJpaRepository doctorSlotLockRepository,
            SpringDataPatientSlotLockJpaRepository patientSlotLockRepository
    ) {
        this.doctorSlotLockRepository = doctorSlotLockRepository;
        this.patientSlotLockRepository = patientSlotLockRepository;
    }

    @Override
    public void reserve(Long doctorId, Long patientId, LocalDateTime appointmentDateTime) {
        reserveDoctorSlot(doctorId, appointmentDateTime);
        reservePatientSlot(patientId, appointmentDateTime);
    }

    @Override
    public void release(Long doctorId, Long patientId, LocalDateTime appointmentDateTime) {
        doctorSlotLockRepository.deleteByDoctorIdAndAppointmentDateTime(doctorId, appointmentDateTime);
        patientSlotLockRepository.deleteByPatientIdAndAppointmentDateTime(patientId, appointmentDateTime);
    }

    private void reserveDoctorSlot(Long doctorId, LocalDateTime appointmentDateTime) {
        try {
            doctorSlotLockRepository.saveAndFlush(new DoctorSlotLockJpaEntity(doctorId, appointmentDateTime));
        } catch (DataIntegrityViolationException exception) {
            throw new ConflictException("La franja ya fue tomada por otro proceso para el médico seleccionado");
        }
    }

    private void reservePatientSlot(Long patientId, LocalDateTime appointmentDateTime) {
        try {
            patientSlotLockRepository.saveAndFlush(new PatientSlotLockJpaEntity(patientId, appointmentDateTime));
        } catch (DataIntegrityViolationException exception) {
            throw new ConflictException("La franja ya fue tomada por otro proceso para el paciente seleccionado");
        }
    }
}
