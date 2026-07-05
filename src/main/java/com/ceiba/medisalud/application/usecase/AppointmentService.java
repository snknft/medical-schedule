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

    public Appointment attend(Long appointmentId) {
        Appointment appointment = getAppointmentOrFail(appointmentId);
        appointment.attend();
        Appointment saved = appointmentRepository.save(appointment);
        slotReservationPort.release(appointment.getDoctorId(), appointment.getPatientId(), appointment.getAppointmentDateTime());
        return saved;
    }

    @Transactional(readOnly = true)
    public Appointment getById(Long appointmentId) {
        return getAppointmentOrFail(appointmentId);
    }

    @Transactional(readOnly = true)
    public List<Appointment> search(AppointmentSearchCriteria criteria) {
        return appointmentRepository.search(criteria);
    }

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

    private void validateNoConflicts(Long doctorId, Long patientId, LocalDateTime dateTime, Long excludedAppointmentId) {
        if (appointmentRepository.existsScheduledForDoctorAt(doctorId, dateTime, excludedAppointmentId)) {
            throw new ConflictException("El médico ya tiene una cita programada en esa franja horaria");
        }
        if (appointmentRepository.existsScheduledForPatientAt(patientId, dateTime, excludedAppointmentId)) {
            throw new ConflictException("El paciente ya tiene una cita programada en esa franja horaria");
        }
    }

    private void ensurePatientCanSchedule(Long patientId, LocalDateTime now) {
        LocalDateTime since = now.minusDays(PENALTY_WINDOW_DAYS);
        long penalties = penaltyRepository.countByPatientIdSince(patientId, since);
        if (penalties >= PENALTY_BLOCK_THRESHOLD) {
            throw new ConflictException("El paciente tiene 3 o más penalizaciones en los últimos 30 días y no puede agendar nuevas citas");
        }
    }

    private void ensureDoctorExists(Long doctorId) {
        if (!doctorRepository.existsById(doctorId)) {
            throw new NotFoundException("Médico no encontrado con id " + doctorId);
        }
    }

    private Patient getPatientOrFail(Long patientId) {
        return patientRepository.findById(patientId)
                .orElseThrow(() -> new NotFoundException("Paciente no encontrado con id " + patientId));
    }

    private Appointment getAppointmentOrFail(Long appointmentId) {
        return appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new NotFoundException("Cita no encontrada con id " + appointmentId));
    }
}
