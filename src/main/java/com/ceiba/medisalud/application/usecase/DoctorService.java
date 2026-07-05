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
     * Creates the doctor service with the doctor persistence port.
     *
     * @param doctorRepository port used to persist and query doctors
     */
    public DoctorService(DoctorRepositoryPort doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    /**
     * Registers a new doctor.
     *
     * @param command registration command with doctor data
     * @return persisted doctor
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
     * Retrieves a doctor by identifier.
     *
     * @param id doctor identifier
     * @return doctor when found
     */
    @Transactional(readOnly = true)
    public Doctor getById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Médico no encontrado con id " + id));
    }

    /**
     * Lists all registered doctors.
     *
     * @return doctors currently persisted in the system
     */
    @Transactional(readOnly = true)
    public List<Doctor> findAll() {
        return doctorRepository.findAll();
    }
}
