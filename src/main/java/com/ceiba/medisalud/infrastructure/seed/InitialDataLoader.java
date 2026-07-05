package com.ceiba.medisalud.infrastructure.seed;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ceiba.medisalud.domain.model.Doctor;
import com.ceiba.medisalud.domain.repository.DoctorRepositoryPort;

/**
 * Loads reference data required by the application when no seed data exists.
 */
@Component
public class InitialDataLoader implements CommandLineRunner {

    private final DoctorRepositoryPort doctorRepository;

    /**
     * Creates a new InitialDataLoader instance.
     */
    public InitialDataLoader(DoctorRepositoryPort doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    /**
     * Runs startup initialization logic.
     */
    @Override
    @Transactional
    public void run(String... args) {
        if (doctorRepository.count() > 0) {
            return;
        }
        doctorRepository.save(new Doctor(null, "Dra. María González", "Cardiología", "555-1001", "maria.gonzalez@medisalud.com"));
        doctorRepository.save(new Doctor(null, "Dr. Carlos Ruiz", "Pediatría", "555-1002", "carlos.ruiz@medisalud.com"));
        doctorRepository.save(new Doctor(null, "Dra. Ana López", "Dermatología", "555-1003", "ana.lopez@medisalud.com"));
    }
}
