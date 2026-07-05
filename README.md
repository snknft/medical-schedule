# MediSalud Appointment API

# MediSalud Appointment API

[![CI](https://github.com/snknft/medical-schedule/actions/workflows/ci.yml/badge.svg?branch=main)](https://github.com/snknft/medical-schedule/actions/workflows/ci.yml)
![Java](https://img.shields.io/badge/Java-21-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.x-brightgreen)
![Gradle](https://img.shields.io/badge/Build-Gradle-blue)
![Coverage](https://img.shields.io/badge/JaCoCo-70%25%2B-success)
![Architecture](https://img.shields.io/badge/Architecture-Hexagonal-informational)
![Database](https://img.shields.io/badge/Database-H2-orange)

API REST para el agendamiento de citas médicas de **MediSalud**.

El proyecto permite registrar médicos y pacientes, reservar citas, consultar disponibilidad por médico, cancelar citas, reprogramarlas y listar citas usando filtros. También incluye validaciones de negocio, persistencia con H2, migraciones con Flyway, pruebas automatizadas y configuración básica para ejecución local.

## Tecnologías utilizadas

- Java 21
- Spring Boot 3.3.x
- Gradle
- Spring Web
- Spring Data JPA
- Bean Validation
- H2 Database
- Flyway
- JUnit 5
- ArchUnit
- JaCoCo
- Spring Boot Actuator
- Docker
- GitHub Actions

## Funcionalidades implementadas

| Código | Funcionalidad | Estado |
| --- | --- | --- |
| RF-01 | Registro de médicos | Implementado |
| RF-02 | Registro de pacientes | Implementado |
| RF-03 | Reserva de citas | Implementado |
| RF-04 | Consulta de franjas disponibles por médico y rango de fechas | Implementado |
| RF-05 | Cancelación de citas con penalización cuando aplica | Implementado |
| RF-06 | Listado de citas con filtros opcionales | Implementado |
| RN-06 | Reprogramación de citas | Implementado |

Adicionalmente, se incluye la opción de marcar una cita como `ATENDIDA`.

## Reglas de negocio

| Regla | Descripción |
| --- | --- |
| RN-01 | Las citas solo pueden programarse en franjas exactas de 30 minutos. Lunes a viernes de 08:00 a 18:00, sábados de 08:00 a 13:00, domingos y festivos sin atención. |
| RN-02 | Un médico no puede tener dos citas `PROGRAMADA` en la misma franja horaria. |
| RN-03 | Al agendar se valida que la fecha de nacimiento del paciente no sea futura. Si no existe, se asume edad 0. |
| RN-04 | Un paciente no puede tener otra cita `PROGRAMADA` en la misma franja horaria. |
| RN-05 | Si una cita se cancela con menos de 2 horas de antelación, se registra una penalización. Con 3 o más penalizaciones en los últimos 30 días, el paciente no puede agendar nuevas citas. |
| RN-06 | Reprogramar una cita implica cancelar la cita anterior, aplicar penalización si corresponde y crear una nueva cita validando disponibilidad. |

## Arquitectura

El proyecto está organizado siguiendo una arquitectura por capas con separación entre dominio, aplicación e infraestructura.

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

| Capa | Responsabilidad |
| --- | --- |
| `domain` | Modelos de dominio, servicios de dominio, políticas, excepciones y puertos. |
| `application` | Casos de uso transaccionales y orquestación de reglas de negocio. |
| `infrastructure` | Adaptadores REST, persistencia JPA, configuración, filtros, migraciones y datos iniciales. |

La separación entre dominio e infraestructura se valida mediante pruebas de arquitectura con ArchUnit.

## Patrones y decisiones de diseño

### Arquitectura hexagonal / Ports and Adapters

Los casos de uso dependen de puertos definidos en el dominio:

- `DoctorRepositoryPort`
- `PatientRepositoryPort`
- `AppointmentRepositoryPort`
- `PenaltyRepositoryPort`
- `SlotReservationPort`

La infraestructura implementa estos puertos mediante adaptadores JPA.

### Repository Pattern

El acceso a datos se realiza a través de puertos de repositorio. Esto permite mantener los casos de uso independientes de Spring Data JPA.

### Strategy Pattern

`WorkingHoursPolicy` define la estrategia para validar horarios laborales y generar franjas disponibles.

Implementación actual:

- `DefaultMedicalWorkingHoursPolicy`

### Factory Pattern

`AppointmentFactory` centraliza la creación de citas y garantiza que una nueva cita inicie en estado `PROGRAMADA`.

### Specification Pattern

`AppointmentJpaSpecifications` permite construir consultas dinámicas para el listado de citas con filtros opcionales.

### Mapper Pattern

Se separan los DTOs REST, los modelos de dominio y las entidades JPA para evitar acoplamiento entre API, negocio y persistencia.

### Reserva protegida de franjas

La reserva de franjas activas se apoya en tablas de bloqueo lógico:

- `doctor_slot_locks`
- `patient_slot_locks`

Estas tablas tienen restricciones únicas para evitar duplicidad de reservas ante solicitudes concurrentes.

## Persistencia y migraciones

La base de datos configurada para ejecución local es **H2 en memoria**.

El esquema se administra con Flyway:

```text
src/main/resources/db/migration/V1__create_schema.sql
src/main/resources/db/migration/V2__seed_reference_data.sql
```

Hibernate no crea ni actualiza el esquema automáticamente:

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: none
```

Con esto, la estructura de base de datos queda controlada por migraciones versionadas.

## Manejo de errores

Los errores se centralizan en `GlobalExceptionHandler` y se devuelven con una estructura uniforme:

```json
{
  "timestamp": "2026-07-05T00:00:00",
  "status": 409,
  "error": "Conflict",
  "message": "El médico ya tiene una cita programada en esa franja horaria",
  "path": "/api/appointments",
  "correlationId": "...",
  "details": []
}
```

Códigos HTTP utilizados:

| Código | Uso |
| --- | --- |
| `201 Created` | Creación exitosa de recursos. |
| `200 OK` | Consultas, cancelaciones, reprogramaciones y marcación como atendida. |
| `400 Bad Request` | Datos inválidos o incumplimiento de reglas de formato. |
| `404 Not Found` | Médico, paciente o cita inexistente. |
| `409 Conflict` | Conflictos de negocio, duplicidad de franja o penalizaciones. |
| `500 Internal Server Error` | Error inesperado. |

## Observabilidad

Se incluye Spring Boot Actuator para consultar el estado de la aplicación:

```http
GET /actuator/health
GET /actuator/health/readiness
GET /actuator/health/liveness
GET /actuator/metrics
GET /actuator/info
```

También se maneja el header `X-Correlation-Id` para trazabilidad de requests y errores.

## Requisitos locales

- Java 21
- Git
- No es necesario instalar Gradle si se usa el wrapper incluido

Verificar versión de Java:

```bash
java -version
```

## Ejecución local

### Windows PowerShell

```powershell
.\gradlew.bat bootRun
```

### Linux / macOS

```bash
./gradlew bootRun
```

La aplicación queda disponible en:

```text
http://localhost:8080
```

Consola H2:

```text
http://localhost:8080/h2-console
```

Datos de conexión H2:

```text
JDBC URL: jdbc:h2:mem:medisalud
User: sa
Password:
```

## Ejecución de pruebas

### Windows PowerShell

```powershell
.\gradlew.bat clean test
.\gradlew.bat clean check
```

### Linux / macOS

```bash
./gradlew clean test
./gradlew clean check
```

El comando `check` ejecuta:

- Tests unitarios
- Tests de integración REST para médicos, pacientes, citas, franjas disponibles y Actuator
- Prueba end-to-end del flujo de agendamiento
- Pruebas de arquitectura con ArchUnit
- Generación de reporte JaCoCo

Reportes generados:

```text
build/reports/tests/test/index.html
build/reports/jacoco/test/html/index.html
```

## Ejecución con Docker

El proyecto incluye `Dockerfile` y `docker-compose.yml` para construir y ejecutar la API en contenedor.

```bash
docker compose up --build
```

La API queda disponible en:

```text
http://localhost:8080
```

Guía completa para Windows, Linux, validaciones y errores frecuentes:

```text
docs/DOCKER.md
```


## Colección Postman

El repositorio incluye una colección para probar la API desde Postman:

```text
docs/postman/medical-schedule.postman_collection.json
```

### Importar la colección

1. Abrir Postman.
2. Seleccionar **Import**.
3. Elegir **Files**.
4. Seleccionar el archivo `docs/postman/medical-schedule.postman_collection.json`.
5. Confirmar la importación.

La colección usa la variable:

```text
baseUrl = http://localhost:8080
```

Si la aplicación se ejecuta en otro puerto, actualizar la variable `baseUrl` en Postman.

### Probar la API con Postman

Antes de ejecutar las solicitudes, levantar la aplicación:

```bash
./gradlew bootRun
```

En Windows:

```powershell
.\gradlew.bat bootRun
```

Orden sugerido de prueba:

1. `Actuator / Health`
2. `Pacientes / Crear paciente`
3. `Citas / Crear cita`
4. `Citas / Consultar cita por ID`
5. `Franjas disponibles / Consultar franjas disponibles`
6. `Citas / Crear cita duplicada - conflicto`
7. `Citas / Crear cita fuera de horario - bad request`

La colección también incluye una carpeta llamada **Flujo sugerido de prueba**, que permite ejecutar un recorrido básico de la API usando variables de colección para reutilizar IDs creados durante la prueba.

## Endpoints principales

### Médicos

```http
POST /api/doctors
GET  /api/doctors
GET  /api/doctors/{id}
```

Crear médico:

```json
{
  "fullName": "Dra. Laura Pérez",
  "specialty": "Cardiología",
  "phone": "555-2001",
  "email": "laura.perez@medisalud.com"
}
```

### Pacientes

```http
POST /api/patients
GET  /api/patients
GET  /api/patients/{id}
```

Crear paciente:

```json
{
  "fullName": "Juan Pérez",
  "documentNumber": "123456789",
  "phone": "555-3001",
  "email": "juan.perez@email.com",
  "birthDate": "1995-05-20"
}
```

Los teléfonos aceptan dígitos y separadores comunes (`+`, `-`, espacios y paréntesis), pero deben contener al menos 7 dígitos reales.

### Citas

```http
POST  /api/appointments
GET   /api/appointments
GET   /api/appointments/{id}
PATCH /api/appointments/{id}/cancel
PATCH /api/appointments/{id}/reschedule
PATCH /api/appointments/{id}/attend
```

Crear cita:

```json
{
  "patientId": 1,
  "doctorId": 1,
  "appointmentDateTime": "2026-07-06T08:00:00"
}
```

También se aceptan los alias `pacienteId` y `medicoId` en el cuerpo de la solicitud:

```json
{
  "pacienteId": 1,
  "medicoId": 1,
  "appointmentDateTime": "2026-07-06T08:00:00"
}
```

Reprogramar cita:

```json
{
  "newDateTime": "2026-07-06T08:30:00"
}
```

Filtros de listado:

```http
GET /api/appointments?doctorId=1&patientId=1&status=PROGRAMADA&fechaInicio=2026-07-06T00:00:00&fechaFin=2026-07-10T23:59:59
```

Para facilitar la validación contra el enunciado, también se aceptan `medicoId` y `pacienteId` como alias de consulta:

```http
GET /api/appointments?medicoId=1&pacienteId=1&status=PROGRAMADA&fechaInicio=2026-07-06T00:00:00&fechaFin=2026-07-10T23:59:59
```

### Franjas disponibles

```http
GET /api/doctors/{doctorId}/available-slots?fechaInicio=2026-07-06&fechaFin=2026-07-10
```

## Datos iniciales

Se cargan médicos de referencia mediante migración Flyway:

| ID | Nombre | Especialidad | Teléfono | Email |
| --- | --- | --- | --- | --- |
| 1 | Dra. María González | Cardiología | 555-1001 | maria.gonzalez@medisalud.com |
| 2 | Dr. Carlos Ruiz | Pediatría | 555-1002 | carlos.ruiz@medisalud.com |
| 3 | Dra. Ana López | Dermatología | 555-1003 | ana.lopez@medisalud.com |

## Documentación adicional

| Documento | Descripción |
| --- | --- |
| `docs/API.http` | Ejemplos de requests ejecutables desde IntelliJ IDEA Ultimate, VS Code REST Client o herramientas compatibles. |
| `README_POSTMAN_SECTION.md` | Guía breve para importar la colección en Postman y ejecutar un flujo básico de prueba. |
| `docs/postman/medical-schedule.postman_collection.json` | Colección Postman importable con solicitudes para médicos, pacientes, citas, franjas disponibles, Actuator y flujo end-to-end. |
| `docs/DOCKER.md` | Guía de ejecución con Docker en Windows y Linux, incluyendo validaciones y solución de errores frecuentes. |
| `docs/ARCHITECTURE.md` | Descripción de arquitectura, capas, responsabilidades y decisiones de diseño. |
| `docs/PRODUCTION_READINESS.md` | Consideraciones de preparación productiva, observabilidad, resiliencia y operación. |

## Comandos útiles

```bash
# Ejecutar tests
./gradlew clean test

# Ejecutar verificación completa
./gradlew clean check

# Levantar aplicación
./gradlew bootRun

# Construir jar
./gradlew clean bootJar

# Ejecutar jar
java -jar build/libs/medisalud-appointments-1.0.0.jar
```

En Windows reemplazar `./gradlew` por `.\gradlew.bat`.
