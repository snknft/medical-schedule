# Arquitectura y patrones aplicados

## Arquitectura

La solución usa arquitectura hexagonal / clean architecture.

```text
com.ceiba.medisalud
├── domain          # Modelo, reglas, políticas y puertos
├── application     # Casos de uso y comandos/queries
└── infrastructure  # REST, JPA, Flyway, configuración, filtros y observabilidad
```

El dominio no depende de Spring ni de JPA. Esa regla se valida con ArchUnit.

## Patrones aplicados

### Ports and Adapters

Los casos de uso hablan con interfaces del dominio, no con implementaciones técnicas.

Ejemplos:

- `AppointmentRepositoryPort`
- `DoctorRepositoryPort`
- `PatientRepositoryPort`
- `PenaltyRepositoryPort`
- `SlotReservationPort`

Adaptadores:

- `AppointmentJpaRepositoryAdapter`
- `DoctorJpaRepositoryAdapter`
- `PatientJpaRepositoryAdapter`
- `PenaltyJpaRepositoryAdapter`
- `JpaSlotReservationAdapter`

### Repository Pattern

Centraliza persistencia por agregado y evita que los casos de uso conozcan Spring Data JPA.

### Strategy Pattern

`WorkingHoursPolicy` permite reemplazar la política de horarios sin modificar los casos de uso.

Implementación actual:

- `DefaultMedicalWorkingHoursPolicy`

### Factory Pattern

`AppointmentFactory` centraliza la creación de una cita programada y evita estados iniciales inconsistentes.

### Specification Pattern

`AppointmentJpaSpecifications` compone filtros opcionales para la búsqueda de citas.

### Mapper Pattern

Se separan modelos REST, entidades JPA y modelos de dominio.

Ejemplos:

- `AppointmentJpaMapper`
- `PatientJpaMapper`
- `DoctorJpaMapper`
- `ApiResponseMapper`

### Unit of Work / Transaction Script controlado

Cada caso de uso crítico está delimitado por `@Transactional`, especialmente agendamiento, cancelación y reprogramación.

### Guarded Slot Reservation

`SlotReservationPort` representa una reserva transaccional de franja. En infraestructura se implementa con tablas de lock y restricciones únicas, protegiendo el sistema de condiciones de carrera.
