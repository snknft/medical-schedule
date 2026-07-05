# MediSalud Appointment API

API REST backend para el sistema de agendamiento de citas médicas de MediSalud.

La solución está construida con **Java 21**, **Spring Boot 3.x**, **Gradle**, **Spring Data JPA**, **H2**, **Flyway**, **Bean Validation**, **JUnit 5**, **ArchUnit**, **JaCoCo** y **Spring Boot Actuator**.

> No se implementa frontend ni autenticación/autorización porque el enunciado solicita únicamente backend API REST y aclara que la autenticación no es requerida.

---

## Enfoque de la solución

Este proyecto no está planteado como un CRUD académico. Está organizado como un backend mantenible, testeable y preparado para evolucionar en ambiente enterprise:

- Arquitectura hexagonal / clean architecture.
- Dominio desacoplado de Spring, JPA y REST.
- Casos de uso transaccionales.
- Validaciones de negocio centralizadas.
- Migraciones versionadas con Flyway.
- Protección de concurrencia mediante locks lógicos con restricciones únicas en base de datos.
- Manejo centralizado de errores HTTP.
- Correlation ID para trazabilidad.
- Actuator para health, readiness, liveness y métricas.
- Tests unitarios de reglas críticas.
- Test de arquitectura con ArchUnit.
- Reporte de cobertura con JaCoCo.
- Dockerfile multi-stage y GitHub Actions.

---

## Funcionalidades implementadas

- Registro de médicos.
- Registro de pacientes.
- Reserva de citas.
- Consulta de franjas disponibles por médico y rango de fechas.
- Cancelación de citas.
- Penalización por cancelación tardía.
- Bloqueo de paciente con 3 o más penalizaciones en los últimos 30 días.
- Reprogramación de citas.
- Marcación de cita como atendida.
- Listado de citas con filtros opcionales.
- Carga inicial de médicos de referencia.

---

## Reglas de negocio implementadas

| Regla | Implementación |
| --- | --- |
| RN-01 | Solo se permiten citas en franjas exactas de 30 minutos. Lunes a viernes 08:00-18:00, sábados 08:00-13:00, domingos y festivos sin atención. |
| RN-02 | Un médico no puede tener dos citas `PROGRAMADA` en la misma franja. |
| RN-03 | Al agendar se valida que la fecha de nacimiento del paciente no sea futura. Si no existe, se asume edad 0. |
| RN-04 | Un paciente no puede tener otra cita `PROGRAMADA` en la misma franja horaria, incluso con otro médico. |
| RN-05 | Si una cita se cancela con menos de 2 horas de antelación, se registra penalización. Con 3 o más penalizaciones en los últimos 30 días, el paciente no puede agendar. |
| RN-06 | Reprogramar implica validar nuevo horario, reservar nueva franja, cancelar la cita original, aplicar penalización si corresponde y crear una nueva cita. |

---

## Arquitectura

```text
src/main/java/com/ceiba/medisalud
├── domain
│   ├── exception
│   ├── model
│   ├── policy
│   ├── repository
│   └── service
├── application
│   ├── command
│   ├── query
│   └── usecase
└── infrastructure
    ├── config
    ├── persistence
    ├── rest
    └── seed
```

### Capas

| Capa | Responsabilidad |
| --- | --- |
| `domain` | Modelo, reglas puras, políticas, excepciones y puertos. No depende de Spring, JPA ni REST. |
| `application` | Casos de uso transaccionales. Orquesta dominio, validaciones y puertos. |
| `infrastructure` | Adaptadores REST, persistencia JPA, migraciones, filtros, configuración y observabilidad. |

La independencia del dominio se valida con `ArchitectureTest` usando ArchUnit.

---

## Patrones de diseño aplicados y justificados

### 1. Hexagonal Architecture / Ports and Adapters

Los casos de uso dependen de puertos del dominio:

- `DoctorRepositoryPort`
- `PatientRepositoryPort`
- `AppointmentRepositoryPort`
- `PenaltyRepositoryPort`
- `SlotReservationPort`

La infraestructura implementa esos puertos con JPA.

**Justificación:** evita acoplar reglas de negocio a Spring Data, H2 o JPA. Permite cambiar el mecanismo de persistencia sin tocar la lógica central.

### 2. Repository Pattern

Cada agregado tiene un repositorio de dominio.

**Justificación:** centraliza el acceso a datos y evita consultas técnicas en los casos de uso.

### 3. Strategy Pattern

`WorkingHoursPolicy` define la estrategia de horarios disponibles.

Implementación actual:

- `DefaultMedicalWorkingHoursPolicy`

**Justificación:** permite cambiar horarios por sede, especialidad o agenda médica sin modificar el caso de uso de agendamiento.

### 4. Factory Pattern

`AppointmentFactory` centraliza la creación de citas programadas.

**Justificación:** garantiza que toda cita nueva nazca en estado `PROGRAMADA` y con construcción consistente.

### 5. Specification Pattern

`AppointmentJpaSpecifications` compone filtros dinámicos de búsqueda.

**Justificación:** evita crear múltiples métodos rígidos por combinación de filtros.

### 6. Mapper Pattern

Se separan DTOs REST, entidades JPA y modelos de dominio.

**Justificación:** evita filtrar detalles de infraestructura hacia el dominio y permite evolucionar la API sin romper la persistencia.

### 7. Guarded Slot Reservation Pattern

`SlotReservationPort` reserva franjas activas usando tablas de lock:

- `doctor_slot_locks`
- `patient_slot_locks`

Cada tabla tiene restricciones únicas.

**Justificación:** en producción, una validación previa no basta ante requests concurrentes. Las restricciones únicas protegen el sistema frente a condiciones de carrera.

---

## Persistencia y migraciones

Se usa H2 porque el enunciado permite base en memoria y para facilitar la revisión local. Sin embargo, el esquema no depende de `ddl-auto=update`; se maneja con Flyway:

```text
db/migration/V1__create_schema.sql
db/migration/V2__seed_reference_data.sql
```

La aplicación usa:

```yaml
spring.jpa.hibernate.ddl-auto: validate
```

Esto es más cercano a un flujo productivo: la base se versiona explícitamente y Hibernate solo valida que el modelo coincida con el esquema.

---

## Concurrencia

Para evitar doble reserva de citas bajo concurrencia:

1. El caso de uso valida reglas de negocio y disponibilidad.
2. Luego intenta reservar la franja en tablas de lock.
3. Las restricciones únicas de base de datos actúan como última línea de defensa.
4. Si hay colisión, se devuelve `409 Conflict`.

Esto cubre el escenario donde dos usuarios intentan reservar la misma franja al mismo tiempo.

---

## Ejecución local

Requisitos:

- Java 21.
- Gradle 8.x.

Ejecutar aplicación:

```bash
gradle bootRun
```

Construir JAR:

```bash
gradle clean build
```

Ejecutar JAR:

```bash
java -jar build/libs/medisalud-appointments-1.0.0.jar
```

Ejecutar tests:

```bash
gradle test
```

Ejecutar verificación completa:

```bash
gradle clean check
```

La API queda disponible en:

```text
http://localhost:8080
```

---

## H2 Console

```text
URL: http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:medisalud;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=false
User: sa
Password:
```

---

## Docker

Construir y ejecutar:

```bash
docker compose up --build
```

Validar health/readiness:

```bash
curl http://localhost:8080/actuator/health/readiness
```

---

## Observabilidad

Endpoints Actuator:

```http
GET /actuator/health
GET /actuator/health/readiness
GET /actuator/health/liveness
GET /actuator/metrics
GET /actuator/info
```

Cada request propaga o genera un header:

```http
X-Correlation-Id: valor-opcional
```

El correlation ID se retorna en la respuesta y aparece en logs y errores.

---

## Códigos HTTP

| Código | Uso |
| --- | --- |
| 200 | Consulta o actualización exitosa. |
| 201 | Recurso creado. |
| 400 | Request inválido, formato incorrecto o regla de calendario inválida. |
| 404 | Recurso no encontrado. |
| 409 | Conflicto de negocio, duplicidad, penalización o colisión concurrente. |
| 500 | Error inesperado. |

Formato de error:

```json
{
  "timestamp": "2026-07-04T10:00:00",
  "status": 409,
  "error": "Conflict",
  "message": "El médico ya tiene una cita programada en esa franja horaria",
  "path": "/api/appointments",
  "correlationId": "demo-create-appointment",
  "details": []
}
```

---

## Endpoints

### Crear médico

```http
POST /api/doctors
Content-Type: application/json
```

```json
{
  "fullName": "Dra. Laura Pérez",
  "specialty": "Neurología",
  "phone": "555-2001",
  "email": "laura.perez@medisalud.com"
}
```

### Listar médicos

```http
GET /api/doctors
```

### Consultar médico por ID

```http
GET /api/doctors/{id}
```

### Crear paciente

```http
POST /api/patients
Content-Type: application/json
```

```json
{
  "fullName": "Omar López",
  "documentNumber": "123456789",
  "phone": "3001234567",
  "email": "omar@example.com",
  "birthDate": "1997-05-10"
}
```

### Listar pacientes

```http
GET /api/patients
```

### Consultar paciente por ID

```http
GET /api/patients/{id}
```

### Crear cita

```http
POST /api/appointments
Content-Type: application/json
```

```json
{
  "patientId": 1,
  "doctorId": 1,
  "appointmentDateTime": "2026-08-03T08:00:00"
}
```

### Consultar cita por ID

```http
GET /api/appointments/{id}
```

### Listar citas con filtros

```http
GET /api/appointments?doctorId=1&patientId=1&status=PROGRAMADA&fechaInicio=2026-08-01T00:00:00&fechaFin=2026-08-31T23:59:59
```

Todos los filtros son opcionales.

### Consultar franjas disponibles

```http
GET /api/doctors/1/available-slots?fechaInicio=2026-08-03&fechaFin=2026-08-03
```

### Cancelar cita

```http
PATCH /api/appointments/{id}/cancel
```

### Reprogramar cita

```http
PATCH /api/appointments/{id}/reschedule
Content-Type: application/json
```

```json
{
  "newDateTime": "2026-08-03T09:00:00"
}
```

### Marcar como atendida

```http
PATCH /api/appointments/{id}/attend
```

---

## Datos iniciales

Al iniciar se cargan por migración los médicos del enunciado:

| ID | Nombre | Especialidad | Teléfono | Email |
| --- | --- | --- | --- | --- |
| 1 | Dra. María González | Cardiología | 555-1001 | maria.gonzalez@medisalud.com |
| 2 | Dr. Carlos Ruiz | Pediatría | 555-1002 | carlos.ruiz@medisalud.com |
| 3 | Dra. Ana López | Dermatología | 555-1003 | ana.lopez@medisalud.com |

---

## Tests incluidos

- Tests unitarios de reglas de agendamiento.
- Tests de duplicidad médico/franja.
- Tests de conflicto paciente/franja.
- Tests de cancelación tardía y penalizaciones.
- Tests de bloqueo por penalizaciones.
- Tests de disponibilidad de franjas.
- Tests de reprogramación.
- Tests de arquitectura con ArchUnit.
- Reporte JaCoCo.

Reporte de cobertura:

```text
build/reports/jacoco/test/html/index.html
```

---

## Documentación adicional

- `docs/ARCHITECTURE.md`
- `docs/PRODUCTION_READINESS.md`
- `docs/API.http`

---

## Entrega sugerida

1. Subir el proyecto a GitHub público.
2. Verificar localmente:

```bash
gradle clean check
```

3. Adjuntar el enlace del repositorio en el correo de respuesta.
