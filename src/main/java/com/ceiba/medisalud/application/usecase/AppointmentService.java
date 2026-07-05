package com.ceiba.medisalud.application.usecase;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ceiba.medisalud.application.command.RescheduleAppointmentCommand;
import com.ceiba.medisalud.application.command.ScheduleAppointmentCommand;
import com.ceiba.medisalud.domain.model.AppointmentSearchCriteria;
import com.ceiba.medisalud.application.query.AvailableSlotsQuery;
import com.ceiba.medisalud.domain.exception.BadRequestException;
import com.ceiba.medisalud.domain.exception.ConflictException;
import com.ceiba.medisalud.domain.exception.NotFoundException;
import com.ceiba.medisalud.domain.model.Appointment;
import com.ceiba.medisalud.domain.model.AvailableSlot;
import com.ceiba.medisalud.domain.model.Patient;
import com.ceiba.medisalud.domain.model.Penalty;
import com.ceiba.medisalud.domain.policy.WorkingHoursPolicy;
import com.ceiba.medisalud.domain.repository.AppointmentRepositoryPort;
import com.ceiba.medisalud.domain.repository.DoctorRepositoryPort;
import com.ceiba.medisalud.domain.repository.PatientRepositoryPort;
import com.ceiba.medisalud.domain.repository.PenaltyRepositoryPort;
import com.ceiba.medisalud.domain.repository.SlotReservationPort;
import com.ceiba.medisalud.domain.service.AppointmentFactory;
import com.ceiba.medisalud.domain.service.AppointmentRulesService;

/**
 * Coordinates appointment scheduling, cancellation, rescheduling, attendance, and availability use cases.
 */
@Service
@Transactional
public class AppointmentService {

    private static final int PENALTY_BLOCK_THRESHOLD = 3;
    private static final int PENALTY_WINDOW_DAYS = 30;

    private final DoctorRepositoryPort doctorRepository;
    private final PatientRepositoryPort patientRepository;
    private final AppointmentRepositoryPort appointmentRepository;
    private final PenaltyRepositoryPort penaltyRepository;
    private final SlotReservationPort slotReservationPort;
    private final AppointmentFactory appointmentFactory;
    private final AppointmentRulesService appointmentRulesService;
    private final WorkingHoursPolicy workingHoursPolicy;
    private final Clock clock;

    /**
     * Creates the appointment service with the required persistence ports, domain services and clock.
     *
     * @param doctorRepository port used to validate and query doctors
     * @param patientRepository port used to validate and query patients
     * @param appointmentRepository port used to persist and query appointments
     * @param penaltyRepository port used to persist and count penalties
     * @param slotReservationPort port used to reserve and release active slot locks
     * @param appointmentFactory factory used to create scheduled appointments
     * @param appointmentRulesService domain service with appointment validation rules
     * @param workingHoursPolicy policy used to calculate serviceable slots
     * @param clock application clock used to make time-dependent rules testable
     */
    public AppointmentService(
            DoctorRepositoryPort doctorRepository,
            PatientRepositoryPort patientRepository,
            AppointmentRepositoryPort appointmentRepository,
            PenaltyRepositoryPort penaltyRepository,
            SlotReservationPort slotReservationPort,
            AppointmentFactory appointmentFactory,
            AppointmentRulesService appointmentRulesService,
            WorkingHoursPolicy workingHoursPolicy,
            Clock clock
    ) {
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.penaltyRepository = penaltyRepository;
        this.slotReservationPort = slotReservationPort;
        this.appointmentFactory = appointmentFactory;
        this.appointmentRulesService = appointmentRulesService;
        this.workingHoursPolicy = workingHoursPolicy;
        this.clock = clock;
    }

    /**
     * Schedules a new appointment after applying business validations and slot reservations.
     *
     * @param command scheduling command with patient, doctor and requested date-time
     * @return persisted scheduled appointment
     */
    public Appointment schedule(ScheduleAppointmentCommand command) {
        Patient patient = getPatientOrFail(command.patientId());
        ensureDoctorExists(command.doctorId());
        LocalDateTime now = LocalDateTime.now(clock);

        appointmentRulesService.validatePatientAgeAtScheduling(patient, LocalDate.now(clock));
        ensurePatientCanSchedule(patient.id(), now);
        appointmentRulesService.validateSchedulableDateTime(command.appointmentDateTime(), now);
        validateNoConflicts(command.doctorId(), command.patientId(), command.appointmentDateTime(), null);
        slotReservationPort.reserve(command.doctorId(), command.patientId(), command.appointmentDateTime());

        Appointment appointment = appointmentFactory.createScheduled(
                command.patientId(),
                command.doctorId(),
                command.appointmentDateTime()
        );
        return appointmentRepository.save(appointment);
    }

    /**
     * Cancels an appointment and applies a late cancellation penalty when required.
     *
     * @param appointmentId appointment identifier
     * @return cancelled appointment
     */
    public Appointment cancel(Long appointmentId) {
        Appointment appointment = getAppointmentOrFail(appointmentId);
        LocalDateTime now = LocalDateTime.now(clock);
        appointmentRulesService.validateCancelable(appointment.getAppointmentDateTime(), now);

        appointment.cancel(now);
        Appointment saved = appointmentRepository.save(appointment);
        slotReservationPort.release(appointment.getDoctorId(), appointment.getPatientId(), appointment.getAppointmentDateTime());

        if (appointmentRulesService.isLateCancellation(appointment.getAppointmentDateTime(), now)) {
            penaltyRepository.save(new Penalty(
                    null,
                    appointment.getPatientId(),
                    appointment.getId(),
                    now,
                    "Cancelación con menos de 2 horas de antelación"
            ));
        }
        return saved;
    }

    /**
     * Reprograms an existing appointment by cancelling it and creating a new scheduled appointment.
     *
     * @param command rescheduling command with the original appointment and new date-time
     * @return new appointment created for the requested date-time
     */
    public Appointment reschedule(RescheduleAppointmentCommand command) {
        Appointment original = getAppointmentOrFail(command.appointmentId());
        Patient patient = getPatientOrFail(original.getPatientId());
        LocalDateTime now = LocalDateTime.now(clock);

        if (original.getAppointmentDateTime().equals(command.newDateTime())) {
            throw new ConflictException("El nuevo horario debe ser diferente al horario actual");
        }

        appointmentRulesService.validateCancelable(original.getAppointmentDateTime(), now);
        appointmentRulesService.validatePatientAgeAtScheduling(patient, LocalDate.now(clock));
        appointmentRulesService.validateSchedulableDateTime(command.newDateTime(), now);
        validateNoConflicts(original.getDoctorId(), original.getPatientId(), command.newDateTime(), original.getId());
        ensureReprogrammingDoesNotCreatePenaltyBlock(original, now);
        slotReservationPort.reserve(original.getDoctorId(), original.getPatientId(), command.newDateTime());

        original.cancel(now);
        appointmentRepository.save(original);
        slotReservationPort.release(original.getDoctorId(), original.getPatientId(), original.getAppointmentDateTime());

        if (appointmentRulesService.isLateCancellation(original.getAppointmentDateTime(), now)) {
            penaltyRepository.save(new Penalty(
                    null,
                    original.getPatientId(),
                    original.getId(),
                    now,
                    "Penalización por reprogramación tardía"
            ));
        }

        Appointment newAppointment = appointmentFactory.createScheduled(
                original.getPatientId(),
                original.getDoctorId(),
                command.newDateTime()
        );
        return appointmentRepository.save(newAppointment);
    }

    /**
     * Marks an appointment as attended and releases its active slot reservation.
     *
     * @param appointmentId appointment identifier
     * @return attended appointment
     */
    public Appointment attend(Long appointmentId) {
        Appointment appointment = getAppointmentOrFail(appointmentId);
        appointment.attend();
        Appointment saved = appointmentRepository.save(appointment);
        slotReservationPort.release(appointment.getDoctorId(), appointment.getPatientId(), appointment.getAppointmentDateTime());
        return saved;
    }

    /**
     * Retrieves an appointment by identifier.
     *
     * @param appointmentId appointment identifier
     * @return appointment when found
     */
    @Transactional(readOnly = true)
    public Appointment getById(Long appointmentId) {
        return getAppointmentOrFail(appointmentId);
    }

    /**
     * Searches appointments using the provided optional filters.
     *
     * @param criteria optional search filters
     * @return appointments matching the criteria
     */
    @Transactional(readOnly = true)
    public List<Appointment> search(AppointmentSearchCriteria criteria) {
        return appointmentRepository.search(criteria);
    }

    /**
     * Calculates available appointment slots for a doctor within a date range.
     *
     * @param query availability query with doctor identifier and date range
     * @return available slots not occupied by scheduled appointments
     */
    @Transactional(readOnly = true)
    public List<AvailableSlot> findAvailableSlots(AvailableSlotsQuery query) {
        ensureDoctorExists(query.doctorId());
        if (query.fechaInicio() == null || query.fechaFin() == null) {
            throw new BadRequestException("fechaInicio y fechaFin son obligatorias");
        }
        if (query.fechaFin().isBefore(query.fechaInicio())) {
            throw new BadRequestException("fechaFin no puede ser anterior a fechaInicio");
        }

        LocalDateTime from = query.fechaInicio().atStartOfDay();
        LocalDateTime to = query.fechaFin().plusDays(1).atStartOfDay().minusNanos(1);
        Set<LocalDateTime> occupied = new HashSet<>(appointmentRepository
                .findScheduledByDoctorBetween(query.doctorId(), from, to)
                .stream()
                .map(Appointment::getAppointmentDateTime)
                .toList());

        return query.fechaInicio()
                .datesUntil(query.fechaFin().plusDays(1))
                .flatMap(date -> workingHoursPolicy.generateSlots(date).stream())
                .filter(slot -> !occupied.contains(slot.start()))
                .toList();
    }


    /**
     * Ensures that a reprogramming operation does not immediately block the patient.
     *
     * @param appointment appointment being reprogrammed
     * @param now current application date-time
     */
    private void ensureReprogrammingDoesNotCreatePenaltyBlock(Appointment appointment, LocalDateTime now) {
        if (!appointmentRulesService.isLateCancellation(appointment.getAppointmentDateTime(), now)) {
            return;
        }
        LocalDateTime since = now.minusDays(PENALTY_WINDOW_DAYS);
        long currentPenalties = penaltyRepository.countByPatientIdSince(appointment.getPatientId(), since);
        if (currentPenalties + 1 >= PENALTY_BLOCK_THRESHOLD) {
            throw new ConflictException("La reprogramación generaría 3 o más penalizaciones en los últimos 30 días; no es posible crear una nueva cita");
        }
    }

    /**
     * Validates that neither the doctor nor the patient has an active appointment in the same slot.
     *
     * @param doctorId doctor identifier
     * @param patientId patient identifier
     * @param dateTime appointment slot to validate
     * @param excludedAppointmentId appointment identifier ignored during validation, typically the original appointment in rescheduling
     */
    private void validateNoConflicts(Long doctorId, Long patientId, LocalDateTime dateTime, Long excludedAppointmentId) {
        if (appointmentRepository.existsScheduledForDoctorAt(doctorId, dateTime, excludedAppointmentId)) {
            throw new ConflictException("El médico ya tiene una cita programada en esa franja horaria");
        }
        if (appointmentRepository.existsScheduledForPatientAt(patientId, dateTime, excludedAppointmentId)) {
            throw new ConflictException("El paciente ya tiene una cita programada en esa franja horaria");
        }
    }

    /**
     * Ensures that the patient is not blocked by recent penalties.
     *
     * @param patientId patient identifier
     * @param now current application date-time
     */
    private void ensurePatientCanSchedule(Long patientId, LocalDateTime now) {
        LocalDateTime since = now.minusDays(PENALTY_WINDOW_DAYS);
        long penalties = penaltyRepository.countByPatientIdSince(patientId, since);
        if (penalties >= PENALTY_BLOCK_THRESHOLD) {
            throw new ConflictException("El paciente tiene 3 o más penalizaciones en los últimos 30 días y no puede agendar nuevas citas");
        }
    }

    /**
     * Ensures that a doctor exists before continuing the use case.
     *
     * @param doctorId doctor identifier
     */
    private void ensureDoctorExists(Long doctorId) {
        if (!doctorRepository.existsById(doctorId)) {
            throw new NotFoundException("Médico no encontrado con id " + doctorId);
        }
    }

    /**
     * Retrieves a patient or fails with a not-found exception.
     *
     * @param patientId patient identifier
     * @return patient when found
     */
    private Patient getPatientOrFail(Long patientId) {
        return patientRepository.findById(patientId)
                .orElseThrow(() -> new NotFoundException("Paciente no encontrado con id " + patientId));
    }

    /**
     * Retrieves an appointment or fails with a not-found exception.
     *
     * @param appointmentId appointment identifier
     * @return appointment when found
     */
    private Appointment getAppointmentOrFail(Long appointmentId) {
        return appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new NotFoundException("Cita no encontrada con id " + appointmentId));
    }
}
