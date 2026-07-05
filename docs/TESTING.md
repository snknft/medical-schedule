# Pruebas automatizadas

## Objetivo

Las pruebas automatizadas validan:

- Requerimientos funcionales.
- Reglas de negocio.
- Manejo de errores.
- Casos borde.
- Arquitectura.
- Flujo REST end-to-end.
- Cobertura mínima con JaCoCo.

## Ejecutar pruebas

### Windows PowerShell

```powershell
.\gradlew.bat clean check
```

### Linux / macOS

```bash
./gradlew clean check
```

## Reportes

```text
build/reports/tests/test/index.html
build/reports/jacoco/test/html/index.html
```

## Matriz de reglas de negocio

| Regla | Qué se valida | Tests relacionados |
| --- | --- | --- |
| RN-01 | Horarios laborales, sábados, domingos, festivos y franjas de 30 minutos. | `AppointmentServiceTest`, `DefaultMedicalWorkingHoursPolicyTest`, `AppointmentRulesServiceTest`, `AppointmentApiIntegrationTest` |
| RN-02 | Un médico no puede tener dos citas programadas en la misma franja. | `AppointmentServiceTest`, `AppointmentApiIntegrationTest` |
| RN-03 | No se aceptan fechas de nacimiento futuras al agendar; si no hay fecha, se asume edad 0. | `AppointmentRulesServiceTest`, `AppointmentServiceTest` |
| RN-04 | Un paciente no puede tener dos citas en la misma franja. | `AppointmentServiceTest` |
| RN-05 | Cancelación tardía, registro de penalizaciones y bloqueo con 3 penalizaciones en 30 días. | `AppointmentServiceTest`, `LateCancellationApiIntegrationTest` |
| RN-06 | Reprogramación cancela la cita anterior y crea una nueva en una franja válida. | `AppointmentServiceTest`, `AppointmentApiIntegrationTest`, `FullEndToEndApiIntegrationTest` |

## Clases de prueba

### `AppointmentServiceTest`

Valida reglas de negocio desde el caso de uso principal:

- Agendamiento exitoso.
- Rechazo por horario inválido.
- Rechazo por domingo o festivo.
- Rechazo por franja no exacta de 30 minutos.
- Duplicidad de médico.
- Conflicto de paciente.
- Fecha de nacimiento futura.
- Cancelación tardía.
- Bloqueo por penalizaciones.
- Penalizaciones antiguas fuera de ventana.
- Reprogramación.

### `LateCancellationApiIntegrationTest`

Valida RN-05 desde la API REST.

Usa reloj fijo:

```text
medisalud.clock.fixed-now=2026-07-06T07:30:00
```

Flujo cubierto:

1. Crea médico y paciente.
2. Agenda cita para `2026-07-06T08:00:00`.
3. Cancela la cita.
4. Verifica penalización.
5. Repite hasta acumular 3 penalizaciones.
6. Intenta agendar una nueva cita.
7. Espera `409 Conflict`.

### `DoctorApiIntegrationTest`

Valida endpoints de médicos:

- Crear médico.
- Consultar médico.
- Listar médicos.
- Validar datos inválidos.
- Recurso inexistente.

### `PatientApiIntegrationTest`

Valida endpoints de pacientes:

- Crear paciente.
- Consultar paciente.
- Listar pacientes.
- Documento único.
- Validaciones de nombre, documento, teléfono y email.

### `AppointmentApiIntegrationTest`

Valida endpoints de citas:

- Crear cita.
- Consultar cita.
- Listar citas.
- Cancelar cita.
- Reprogramar cita.
- Marcar como atendida.
- Conflictos de negocio.
- Alias `medicoId` / `pacienteId`.

### `AvailableSlotsApiIntegrationTest`

Valida consulta de franjas disponibles:

- Generación de franjas.
- Exclusión de franjas ocupadas.
- Médico inexistente.
- Rango de fechas.

### `FullEndToEndApiIntegrationTest`

Valida un flujo completo desde REST:

1. Crear médico.
2. Crear paciente.
3. Agendar cita.
4. Consultar disponibilidad.
5. Reprogramar.
6. Consultar cita original.
7. Consultar cita nueva.
8. Marcar cita como atendida.

### `ArchitectureTest`

Valida separación arquitectónica con ArchUnit:

- El dominio no depende de infraestructura.
- El dominio no depende de controladores REST.
- Las dependencias apuntan hacia el núcleo.

## Casos borde cubiertos

- Cita fuera de horario.
- Cita en domingo.
- Cita en festivo.
- Franja no exacta de 30 minutos.
- Médico duplicado en la misma franja.
- Paciente duplicado en la misma franja.
- Fecha de nacimiento futura.
- Cancelación tardía.
- 3 penalizaciones en 30 días.
- Penalizaciones fuera de ventana de 30 días.
- Reprogramación a horario ocupado.
- Recurso inexistente.
- Validaciones de entrada.
- Conflictos de negocio.
