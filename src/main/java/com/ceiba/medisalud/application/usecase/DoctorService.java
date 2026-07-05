package com.ceiba.medisalud.application.usecase;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ceiba.medisalud.application.command.RegisterDoctorCommand;
import com.ceiba.medisalud.domain.exception.NotFoundException;
import com.ceiba.medisalud.domain.model.Doctor;
import com.ceiba.medisalud.domain.repository.DoctorRepositoryPort;

/**
 * Coordinates doctor registration and query use cases.
 */
@Service
@Transactional
public class DoctorService {

    private final DoctorRepositoryPort doctorRepository;

    /**
     * Creates a new DoctorService instance.
     */
    public DoctorService(DoctorRepositoryPort doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    /**
     * Registers a new domain resource after validating its business constraints.
     */
    public Doctor register(RegisterDoctorCommand command) {
        Doctor doctor = new Doctor(
                null,
                command.fullName(),
                command.specialty(),
                command.phone(),
                command.email()
        );
        return doctorRepository.save(doctor);
    }

    /**
     * Returns the byId value.
     */
    @Transactional(readOnly = true)
    public Doctor getById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Médico no encontrado con id " + id));
    }

    /**
     * Returns all persisted resources of the current type.
     */
    @Transactional(readOnly = true)
    public List<Doctor> findAll() {
        return doctorRepository.findAll();
    }
}
