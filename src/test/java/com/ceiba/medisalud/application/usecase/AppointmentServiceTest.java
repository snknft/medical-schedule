package com.ceiba.medisalud.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ceiba.medisalud.application.command.RescheduleAppointmentCommand;
import com.ceiba.medisalud.application.command.ScheduleAppointmentCommand;
import com.ceiba.medisalud.domain.model.AppointmentSearchCriteria;
import com.ceiba.medisalud.application.query.AvailableSlotsQuery;
import com.ceiba.medisalud.domain.exception.BadRequestException;
import com.ceiba.medisalud.domain.exception.ConflictException;
import com.ceiba.medisalud.domain.model.Appointment;
import com.ceiba.medisalud.domain.model.AppointmentStatus;
import com.ceiba.medisalud.domain.model.AvailableSlot;
import com.ceiba.medisalud.domain.model.Doctor;
import com.ceiba.medisalud.domain.model.Patient;
import com.ceiba.medisalud.domain.model.Penalty;
import com.ceiba.medisalud.domain.policy.DefaultMedicalWorkingHoursPolicy;
import com.ceiba.medisalud.domain.policy.HolidayProvider;
import com.ceiba.medisalud.domain.repository.AppointmentRepositoryPort;
import com.ceiba.medisalud.domain.repository.DoctorRepositoryPort;
import com.ceiba.medisalud.domain.repository.PatientRepositoryPort;
import com.ceiba.medisalud.domain.repository.PenaltyRepositoryPort;
import com.ceiba.medisalud.domain.repository.SlotReservationPort;
import com.ceiba.medisalud.domain.service.AppointmentFactory;
import com.ceiba.medisalud.domain.service.AppointmentRulesService;

class AppointmentServiceTest {

    private AppointmentService service;
    private InMemoryDoctorRepository doctors;
    private InMemoryPatientRepository patients;
    private InMemoryAppointmentRepository appointments;
    private InMemoryPenaltyRepository penalties;
    private Doctor doctor;
    private Doctor secondDoctor;
    private Patient patient;
    private Patient secondPatient;
    private Clock clock;

    @BeforeEach
    void setUp() {
        ZoneId zone = ZoneId.of("America/Bogota");
        clock = Clock.fixed(Instant.parse("2026-07-01T13:00:00Z"), zone);
        doctors = new InMemoryDoctorRepository();
        patients = new InMemoryPatientRepository();
        appointments = new InMemoryAppointmentRepository();
        penalties = new InMemoryPenaltyRepository();

        HolidayProvider holidayProvider = date -> Set.of(LocalDate.of(2026, 7, 20)).contains(date);
        DefaultMedicalWorkingHoursPolicy workingHoursPolicy = new DefaultMedicalWorkingHoursPolicy(holidayProvider);
        AppointmentRulesService rules = new AppointmentRulesService(workingHoursPolicy);
        service = new AppointmentService(
                doctors,
                patients,
                appointments,
                penalties,
                new InMemorySlotReservation(),
                new AppointmentFactory(),
                rules,
                workingHoursPolicy,
                clock
        );

        doctor = doctors.save(new Doctor(null, "Dra. María González", "Cardiología", "555-1001", "maria.gonzalez@medisalud.com"));
        secondDoctor = doctors.save(new Doctor(null, "Dr. Carlos Ruiz", "Pediatría", "555-1002", "carlos.ruiz@medisalud.com"));
        patient = patients.save(new Patient(null, "Omar López", "123456789", "3001234567", "omar@example.com", LocalDate.of(1997, 5, 10)));
        secondPatient = patients.save(new Patient(null, "Ana Torres", "987654321", "3007654321", "ana@example.com", null));
    }

    @Test
    void schedulesAppointmentAtValidSlot() {
        Appointment appointment = service.schedule(new ScheduleAppointmentCommand(
                patient.id(),
                doctor.id(),
                LocalDateTime.of(2026, 7, 6, 8, 0)
        ));

        assertThat(appointment.getId()).isNotNull();
        assertThat(appointment.getStatus()).isEqualTo(AppointmentStatus.PROGRAMADA);
    }

    @Test
    void rejectsAppointmentOutsideWorkingHours() {
        assertThatThrownBy(() -> service.schedule(new ScheduleAppointmentCommand(
                patient.id(),
                doctor.id(),
                LocalDateTime.of(2026, 7, 6, 18, 0)
        ))).isInstanceOf(BadRequestException.class)
                .hasMessageContaining("horario laboral");
    }

    @Test
    void rejectsSundayAppointment() {
        assertThatThrownBy(() -> service.schedule(new ScheduleAppointmentCommand(
                patient.id(),
                doctor.id(),
                LocalDateTime.of(2026, 7, 5, 8, 0)
        ))).isInstanceOf(BadRequestException.class);
    }

    @Test
    void rejectsHolidayAppointment() {
        assertThatThrownBy(() -> service.schedule(new ScheduleAppointmentCommand(
                patient.id(),
                doctor.id(),
                LocalDateTime.of(2026, 7, 20, 8, 0)
        ))).isInstanceOf(BadRequestException.class);
    }

    @Test
    void rejectsNonThirtyMinuteSlot() {
        assertThatThrownBy(() -> service.schedule(new ScheduleAppointmentCommand(
                patient.id(),
                doctor.id(),
                LocalDateTime.of(2026, 7, 6, 8, 15)
        ))).isInstanceOf(BadRequestException.class);
    }

    @Test
    void rejectsDuplicatedDoctorSlot() {
        LocalDateTime slot = LocalDateTime.of(2026, 7, 6, 8, 0);
        service.schedule(new ScheduleAppointmentCommand(patient.id(), doctor.id(), slot));

        assertThatThrownBy(() -> service.schedule(new ScheduleAppointmentCommand(secondPatient.id(), doctor.id(), slot)))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("médico ya tiene");
    }

    @Test
    void rejectsPatientConflictEvenWithAnotherDoctor() {
        LocalDateTime slot = LocalDateTime.of(2026, 7, 6, 8, 0);
        service.schedule(new ScheduleAppointmentCommand(patient.id(), doctor.id(), slot));

        assertThatThrownBy(() -> service.schedule(new ScheduleAppointmentCommand(patient.id(), secondDoctor.id(), slot)))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("paciente ya tiene");
    }

    @Test
    void registersPenaltyWhenCancellationIsLate() {
        LocalDateTime nearSlot = LocalDateTime.now(clock).plusHours(1).withMinute(0).withSecond(0).withNano(0);
        Appointment appointment = service.schedule(new ScheduleAppointmentCommand(patient.id(), doctor.id(), nearSlot));

        Appointment canceled = service.cancel(appointment.getId());

        assertThat(canceled.getStatus()).isEqualTo(AppointmentStatus.CANCELADA);
        assertThat(penalties.countByPatientIdSince(patient.id(), LocalDateTime.now(clock).minusDays(30))).isEqualTo(1);
    }

    @Test
    void blocksPatientWithThreePenaltiesInLastThirtyDays() {
        LocalDateTime now = LocalDateTime.now(clock);
        penalties.save(new Penalty(null, patient.id(), 1L, now.minusDays(1), "late"));
        penalties.save(new Penalty(null, patient.id(), 2L, now.minusDays(2), "late"));
        penalties.save(new Penalty(null, patient.id(), 3L, now.minusDays(3), "late"));

        assertThatThrownBy(() -> service.schedule(new ScheduleAppointmentCommand(
                patient.id(),
                doctor.id(),
                LocalDateTime.of(2026, 7, 6, 8, 0)
        ))).isInstanceOf(ConflictException.class)
                .hasMessageContaining("penalizaciones");
    }

    @Test
    void ignoresPenaltiesOlderThanThirtyDays() {
        LocalDateTime now = LocalDateTime.now(clock);
        penalties.save(new Penalty(null, patient.id(), 1L, now.minusDays(31), "old"));
        penalties.save(new Penalty(null, patient.id(), 2L, now.minusDays(32), "old"));
        penalties.save(new Penalty(null, patient.id(), 3L, now.minusDays(33), "old"));

        Appointment appointment = service.schedule(new ScheduleAppointmentCommand(
                patient.id(),
                doctor.id(),
                LocalDateTime.of(2026, 7, 6, 8, 0)
        ));

        assertThat(appointment.getStatus()).isEqualTo(AppointmentStatus.PROGRAMADA);
    }

    @Test
    void returnsAvailableSlotsExcludingOccupiedOnes() {
        LocalDateTime occupiedSlot = LocalDateTime.of(2026, 7, 6, 8, 0);
        service.schedule(new ScheduleAppointmentCommand(patient.id(), doctor.id(), occupiedSlot));

        List<AvailableSlot> slots = service.findAvailableSlots(new AvailableSlotsQuery(
                doctor.id(),
                LocalDate.of(2026, 7, 6),
                LocalDate.of(2026, 7, 6)
        ));

        assertThat(slots).extracting(AvailableSlot::start).doesNotContain(occupiedSlot);
        assertThat(slots).hasSize(19);
    }

    @Test
    void reschedulesByCancelingOriginalAndCreatingANewAppointment() {
        Appointment original = service.schedule(new ScheduleAppointmentCommand(
                patient.id(),
                doctor.id(),
                LocalDateTime.of(2026, 7, 6, 8, 0)
        ));

        Appointment newAppointment = service.reschedule(new RescheduleAppointmentCommand(
                original.getId(),
                LocalDateTime.of(2026, 7, 6, 9, 0)
        ));

        assertThat(appointments.findById(original.getId()).orElseThrow().getStatus()).isEqualTo(AppointmentStatus.CANCELADA);
        assertThat(newAppointment.getId()).isNotEqualTo(original.getId());
        assertThat(newAppointment.getStatus()).isEqualTo(AppointmentStatus.PROGRAMADA);
        assertThat(newAppointment.getAppointmentDateTime()).isEqualTo(LocalDateTime.of(2026, 7, 6, 9, 0));
    }

    private static final class InMemoryDoctorRepository implements DoctorRepositoryPort {
        private final AtomicLong sequence = new AtomicLong(1);
        private final Map<Long, Doctor> data = new HashMap<>();

        @Override
        public Doctor save(Doctor doctor) {
            Long id = doctor.id() == null ? sequence.getAndIncrement() : doctor.id();
            Doctor saved = new Doctor(id, doctor.fullName(), doctor.specialty(), doctor.phone(), doctor.email());
            data.put(id, saved);
            return saved;
        }

        @Override
        public Optional<Doctor> findById(Long id) {
            return Optional.ofNullable(data.get(id));
        }

        @Override
        public List<Doctor> findAll() {
            return new ArrayList<>(data.values());
        }

        @Override
        public boolean existsById(Long id) {
            return data.containsKey(id);
        }

        @Override
        public long count() {
            return data.size();
        }
    }

    private static final class InMemoryPatientRepository implements PatientRepositoryPort {
        private final AtomicLong sequence = new AtomicLong(1);
        private final Map<Long, Patient> data = new HashMap<>();

        @Override
        public Patient save(Patient patient) {
            Long id = patient.id() == null ? sequence.getAndIncrement() : patient.id();
            Patient saved = new Patient(id, patient.fullName(), patient.documentNumber(), patient.phone(), patient.email(), patient.birthDate());
            data.put(id, saved);
            return saved;
        }

        @Override
        public Optional<Patient> findById(Long id) {
            return Optional.ofNullable(data.get(id));
        }

        @Override
        public List<Patient> findAll() {
            return new ArrayList<>(data.values());
        }

        @Override
        public boolean existsByDocumentNumber(String documentNumber) {
            return data.values().stream().anyMatch(patient -> patient.documentNumber().equals(documentNumber));
        }
    }

    private static final class InMemoryAppointmentRepository implements AppointmentRepositoryPort {
        private final AtomicLong sequence = new AtomicLong(1);
        private final Map<Long, Appointment> data = new HashMap<>();

        @Override
        public Appointment save(Appointment appointment) {
            Long id = appointment.getId() == null ? sequence.getAndIncrement() : appointment.getId();
            Appointment saved = new Appointment(
                    id,
                    appointment.getPatientId(),
                    appointment.getDoctorId(),
                    appointment.getAppointmentDateTime(),
                    appointment.getStatus(),
                    appointment.getCancellationDateTime()
            );
            data.put(id, saved);
            return saved;
        }

        @Override
        public Optional<Appointment> findById(Long id) {
            return Optional.ofNullable(data.get(id));
        }

        @Override
        public boolean existsScheduledForDoctorAt(Long doctorId, LocalDateTime dateTime, Long excludedAppointmentId) {
            return data.values().stream().anyMatch(appointment ->
                    appointment.getStatus() == AppointmentStatus.PROGRAMADA
                            && appointment.getDoctorId().equals(doctorId)
                            && appointment.getAppointmentDateTime().equals(dateTime)
                            && !appointment.getId().equals(excludedAppointmentId)
            );
        }

        @Override
        public boolean existsScheduledForPatientAt(Long patientId, LocalDateTime dateTime, Long excludedAppointmentId) {
            return data.values().stream().anyMatch(appointment ->
                    appointment.getStatus() == AppointmentStatus.PROGRAMADA
                            && appointment.getPatientId().equals(patientId)
                            && appointment.getAppointmentDateTime().equals(dateTime)
                            && !appointment.getId().equals(excludedAppointmentId)
            );
        }

        @Override
        public List<Appointment> findScheduledByDoctorBetween(Long doctorId, LocalDateTime from, LocalDateTime to) {
            return data.values().stream()
                    .filter(appointment -> appointment.getStatus() == AppointmentStatus.PROGRAMADA)
                    .filter(appointment -> appointment.getDoctorId().equals(doctorId))
                    .filter(appointment -> !appointment.getAppointmentDateTime().isBefore(from))
                    .filter(appointment -> !appointment.getAppointmentDateTime().isAfter(to))
                    .sorted(Comparator.comparing(Appointment::getAppointmentDateTime))
                    .toList();
        }

        @Override
        public List<Appointment> search(AppointmentSearchCriteria criteria) {
            return data.values().stream()
                    .filter(appointment -> criteria.doctorId() == null || appointment.getDoctorId().equals(criteria.doctorId()))
                    .filter(appointment -> criteria.patientId() == null || appointment.getPatientId().equals(criteria.patientId()))
                    .filter(appointment -> criteria.status() == null || appointment.getStatus() == criteria.status())
                    .filter(appointment -> criteria.fechaInicio() == null || !appointment.getAppointmentDateTime().isBefore(criteria.fechaInicio()))
                    .filter(appointment -> criteria.fechaFin() == null || !appointment.getAppointmentDateTime().isAfter(criteria.fechaFin()))
                    .sorted(Comparator.comparing(Appointment::getAppointmentDateTime))
                    .toList();
        }
    }


    private static final class InMemorySlotReservation implements SlotReservationPort {
        private final Set<String> doctorLocks = new java.util.HashSet<>();
        private final Set<String> patientLocks = new java.util.HashSet<>();

        @Override
        public void reserve(Long doctorId, Long patientId, LocalDateTime appointmentDateTime) {
            String doctorKey = doctorId + "@" + appointmentDateTime;
            String patientKey = patientId + "@" + appointmentDateTime;
            if (!doctorLocks.add(doctorKey)) {
                throw new ConflictException("La franja ya fue tomada por otro proceso para el médico seleccionado");
            }
            if (!patientLocks.add(patientKey)) {
                doctorLocks.remove(doctorKey);
                throw new ConflictException("La franja ya fue tomada por otro proceso para el paciente seleccionado");
            }
        }

        @Override
        public void release(Long doctorId, Long patientId, LocalDateTime appointmentDateTime) {
            doctorLocks.remove(doctorId + "@" + appointmentDateTime);
            patientLocks.remove(patientId + "@" + appointmentDateTime);
        }
    }

    private static final class InMemoryPenaltyRepository implements PenaltyRepositoryPort {
        private final AtomicLong sequence = new AtomicLong(1);
        private final List<Penalty> data = new ArrayList<>();

        @Override
        public Penalty save(Penalty penalty) {
            Penalty saved = new Penalty(
                    penalty.id() == null ? sequence.getAndIncrement() : penalty.id(),
                    penalty.patientId(),
                    penalty.appointmentId(),
                    penalty.penaltyDateTime(),
                    penalty.reason()
            );
            data.add(saved);
            return saved;
        }

        @Override
        public long countByPatientIdSince(Long patientId, LocalDateTime since) {
            return data.stream()
                    .filter(penalty -> penalty.patientId().equals(patientId))
                    .filter(penalty -> !penalty.penaltyDateTime().isBefore(since))
                    .count();
        }
    }
}
