# Arquitectura

## Enfoque

La solución utiliza arquitectura hexagonal, también conocida como Ports and Adapters.

El objetivo es mantener el núcleo de negocio aislado de detalles técnicos como REST, JPA, H2, Flyway o Spring Data. Esto permite probar reglas de negocio sin depender de infraestructura y facilita reemplazar adaptadores sin modificar el dominio.

## Estructura

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

## Capas

### Domain

Contiene el núcleo de negocio:

- Modelos de dominio.
- Políticas de negocio.
- Servicios de dominio.
- Excepciones de dominio.
- Puertos de repositorio.

Esta capa no depende de Spring, JPA ni REST.

### Application

Contiene los casos de uso:

- Registro de médicos.
- Registro de pacientes.
- Agendamiento de citas.
- Cancelación de citas.
- Reprogramación.
- Consulta de disponibilidad.
- Listado con filtros.

La capa de aplicación coordina transacciones y usa puertos del dominio.

### Infrastructure

Contiene los adaptadores técnicos:

- Controladores REST.
- DTOs request/response.
- Entidades JPA.
- Repositorios Spring Data.
- Adaptadores de persistencia.
- Configuración de Spring.
- Migraciones Flyway.
- Carga inicial de datos.
- Filtros de correlación.

## Patrones aplicados

### Hexagonal Architecture / Ports and Adapters

Los casos de uso dependen de puertos del dominio, no de implementaciones técnicas.

Puertos principales:

```text
DoctorRepositoryPort
PatientRepositoryPort
AppointmentRepositoryPort
PenaltyRepositoryPort
SlotReservationPort
```

Adaptadores principales:

```text
DoctorJpaRepositoryAdapter
PatientJpaRepositoryAdapter
AppointmentJpaRepositoryAdapter
PenaltyJpaRepositoryAdapter
JpaSlotReservationAdapter
```

### Repository Pattern

El acceso a datos se realiza mediante puertos de repositorio. Esto evita que los casos de uso conozcan detalles de JPA o Spring Data.

### Strategy Pattern

`WorkingHoursPolicy` encapsula las reglas de horarios laborales y generación de franjas disponibles.

Implementación actual:

```text
DefaultMedicalWorkingHoursPolicy
```

### Factory Pattern

`AppointmentFactory` centraliza la creación de citas nuevas y garantiza que nazcan en estado `PROGRAMADA`.

### Specification Pattern

`AppointmentJpaSpecifications` compone filtros dinámicos para búsqueda de citas por médico, paciente, estado y rango de fechas.

### Mapper Pattern

Se separan explícitamente:

- DTOs REST.
- Modelos de dominio.
- Entidades JPA.

Esto permite evolucionar la API o la persistencia sin contaminar el dominio.

### Guarded Slot Reservation Pattern

La reserva activa se protege con locks lógicos:

```text
doctor_slot_locks
patient_slot_locks
```

Estas tablas tienen restricciones únicas para evitar doble reserva concurrente.

## Persistencia

La base de datos local es H2 en memoria.

El esquema se administra con Flyway:

```text
src/main/resources/db/migration/V1__create_schema.sql
src/main/resources/db/migration/V2__seed_reference_data.sql
```

Hibernate no crea ni actualiza tablas automáticamente:

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: none
```

Esto permite que la evolución del esquema sea explícita, versionada y reproducible.

## Concurrencia

La disponibilidad se valida en dos niveles:

1. Validación funcional en el caso de uso.
2. Restricciones únicas en tablas de locks.

Esto evita que dos solicitudes concurrentes reserven la misma franja para el mismo médico o para el mismo paciente.

## Observabilidad

La API incluye:

- Spring Boot Actuator.
- Health, readiness y liveness.
- `X-Correlation-Id`.
- Logs estructurados con correlation ID.

## Validación arquitectónica

`ArchitectureTest` usa ArchUnit para verificar que el dominio no dependa de infraestructura.
