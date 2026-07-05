# MediSalud Appointment API

[![CI](https://github.com/snknft/medical-schedule/actions/workflows/ci.yml/badge.svg?branch=main)](https://github.com/snknft/medical-schedule/actions/workflows/ci.yml)
![Java](https://img.shields.io/badge/Java-21-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.x-brightgreen)
![Gradle](https://img.shields.io/badge/Build-Gradle-blue)
![JaCoCo](https://img.shields.io/badge/JaCoCo-70%25%2B-success)
![Architecture](https://img.shields.io/badge/Architecture-Hexagonal-informational)

API REST backend para el sistema de agendamiento de citas médicas de **MediSalud**.

El proyecto permite registrar médicos y pacientes, agendar citas, consultar disponibilidad, cancelar citas, aplicar penalizaciones por cancelación tardía, listar citas con filtros y reprogramar citas.

---

## Tecnologías principales

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
- Docker
- GitHub Actions

---

## Requisitos locales

- Java 21
- Git
- Docker Desktop opcional, solo si se desea probar con contenedores

No es necesario instalar Gradle si se usa el Gradle Wrapper incluido en el repositorio.

Verificar Java:

```bash
java -version
```

---

## Ejecución local con Gradle

### Windows PowerShell

```powershell
.\gradlew.bat bootRun
```

### Linux / macOS

```bash
./gradlew bootRun
```

La API queda disponible en:

```text
http://localhost:8080
```

Validar estado de la aplicación:

```http
GET http://localhost:8080/actuator/health
```

Respuesta esperada:

```json
{
  "status": "UP"
}
```

---

## Ejecutar pruebas

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

El comando `clean check` ejecuta pruebas automatizadas, validación de arquitectura y verificación de cobertura.

Reportes locales:

```text
build/reports/tests/test/index.html
build/reports/jacoco/test/html/index.html
```

---

## Construir y ejecutar JAR

### Windows PowerShell

```powershell
.\gradlew.bat clean bootJar
java -jar build/libs/medisalud-appointments-1.0.0.jar
```

### Linux / macOS

```bash
./gradlew clean bootJar
java -jar build/libs/medisalud-appointments-1.0.0.jar
```

---

## Consola H2

Con la aplicación levantada:

```text
http://localhost:8080/h2-console
```

Datos de conexión:

```text
JDBC URL: jdbc:h2:mem:medisalud
User: sa
Password: password
```

---

## Funcionalidades implementadas

| Código | Funcionalidad |
| --- | --- |
| RF-01 | Registro de médicos |
| RF-02 | Registro de pacientes |
| RF-03 | Reserva de citas |
| RF-04 | Consulta de franjas disponibles |
| RF-05 | Cancelación de citas con penalización cuando aplica |
| RF-06 | Listado de citas con filtros opcionales |
| RN-06 | Reprogramación de citas |

---

## Arquitectura

La solución utiliza **arquitectura hexagonal**, también conocida como **Ports and Adapters**.

La decisión se tomó para mantener el núcleo de negocio aislado de detalles técnicos como REST, JPA, H2 o Spring Data. Los casos de uso trabajan contra puertos definidos en el dominio y la infraestructura implementa esos puertos mediante adaptadores.

Estructura principal:

```text
src/main/java/com/ceiba/medisalud
├── domain
├── application
└── infrastructure
```

Resumen por capa:

| Capa | Responsabilidad |
| --- | --- |
| `domain` | Modelos, reglas de negocio, políticas, excepciones y puertos. |
| `application` | Casos de uso transaccionales y orquestación de reglas. |
| `infrastructure` | Controladores REST, persistencia JPA, configuración, migraciones, filtros y adaptadores. |

Más detalle en:

```text
docs/ARCHITECTURE.md
```

---

## Patrones y decisiones de diseño

| Patrón / decisión | Uso en el proyecto |
| --- | --- |
| Hexagonal Architecture | Separación entre negocio, aplicación e infraestructura. |
| Repository Pattern | Puertos de repositorio para desacoplar persistencia. |
| Strategy Pattern | Política de horarios laborales mediante `WorkingHoursPolicy`. |
| Factory Pattern | Creación consistente de citas programadas. |
| Specification Pattern | Filtros dinámicos para búsqueda de citas. |
| Mapper Pattern | Separación entre DTOs REST, dominio y entidades JPA. |
| Guarded Slot Reservation | Locks lógicos y restricciones únicas para evitar doble reserva concurrente. |

---

## Manejo de errores

Los errores se centralizan en `GlobalExceptionHandler` y se devuelven con una estructura uniforme.

Códigos manejados:

| Código | Uso |
| --- | --- |
| `200 OK` | Consultas y operaciones exitosas. |
| `201 Created` | Creación de recursos. |
| `400 Bad Request` | Entradas inválidas o reglas de formato incumplidas. |
| `404 Not Found` | Recursos inexistentes. |
| `409 Conflict` | Conflictos de negocio. |
| `500 Internal Server Error` | Errores no controlados. |

---

## Seguridad y validación de entradas

La API valida entradas con Bean Validation en los DTOs REST:

- Campos obligatorios.
- Longitudes mínimas y máximas.
- Formato de email.
- Teléfono con mínimo 7 dígitos reales.
- Conversión tipada de parámetros como `Long`, `LocalDateTime` y `AppointmentStatus`.

El acceso a datos se realiza con Spring Data JPA y Specifications, evitando construir SQL mediante concatenación de strings recibidos desde el usuario.

La aplicación no implementa autenticación/autorización porque no hace parte del alcance solicitado. Tampoco maneja contraseñas, tokens, tarjetas ni historias clínicas.

---

## Endpoints principales

### Médicos

```http
POST /api/doctors
GET  /api/doctors
GET  /api/doctors/{id}
```

### Pacientes

```http
POST /api/patients
GET  /api/patients
GET  /api/patients/{id}
```

### Citas

```http
POST  /api/appointments
GET   /api/appointments
GET   /api/appointments/{id}
PATCH /api/appointments/{id}/cancel
PATCH /api/appointments/{id}/reschedule
PATCH /api/appointments/{id}/attend
```

### Franjas disponibles

```http
GET /api/doctors/{doctorId}/available-slots?fechaInicio=2026-07-06&fechaFin=2026-07-10
```

La API también acepta alias en español para compatibilidad con el enunciado:

```text
doctorId  / medicoId
patientId / pacienteId
```

---

## Documentación adicional

| Documento | Contenido |
| --- | --- |
| `docs/ARCHITECTURE.md` | Arquitectura, capas, patrones, decisiones técnicas y separación de responsabilidades. |
| `docs/TESTING.md` | Matriz de pruebas, reglas de negocio cubiertas, flujos críticos y casos borde. |
| `docs/POSTMAN.md` | Importación de colección, flujos por regla de negocio y prueba de penalización con reloj fijo. |
| `docs/DOCKER.md` | Ejecución con Docker en Windows y Linux. |
| `docs/PRODUCTION_READINESS.md` | Consideraciones de operación, observabilidad, configuración y preparación productiva. |
| `docs/API.http` | Ejemplos ejecutables de requests HTTP. |
| `docs/postman/medical-schedule.postman_collection.json` | Colección Postman importable. |

---

## Datos iniciales

Se cargan médicos de referencia mediante migración Flyway:

| ID | Nombre | Especialidad |
| --- | --- | --- |
| 1 | Dra. María González | Cardiología |
| 2 | Dr. Carlos Ruiz | Pediatría |
| 3 | Dra. Ana López | Dermatología |

---

## Comandos útiles

```bash
# Ejecutar aplicación
./gradlew bootRun

# Ejecutar pruebas
./gradlew clean test

# Verificación completa
./gradlew clean check

# Construir JAR
./gradlew clean bootJar

# Ejecutar JAR
java -jar build/libs/medisalud-appointments-1.0.0.jar
```

En Windows reemplazar `./gradlew` por `.\gradlew.bat`.
