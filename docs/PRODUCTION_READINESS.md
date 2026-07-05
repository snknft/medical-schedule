# Production readiness

Esta prueba no implementa autenticación porque el enunciado no la solicita, pero la solución está preparada con decisiones propias de un backend productivo.

## Decisiones enterprise aplicadas

### 1. Integridad transaccional ante concurrencia

La validación en memoria de disponibilidad no es suficiente en producción: dos requests simultáneos podrían consultar disponibilidad al mismo tiempo y ambos intentar reservar la misma franja.

Para mitigar ese escenario se agregaron tablas de bloqueo lógico:

- `doctor_slot_locks`: impide dos citas activas para el mismo médico en la misma franja.
- `patient_slot_locks`: impide dos citas activas para el mismo paciente en la misma franja.

Ambas tablas tienen restricciones `UNIQUE` a nivel de base de datos. La aplicación valida primero para devolver mensajes claros y luego reserva el lock dentro de la transacción para protegerse de condiciones de carrera.

### 2. Migraciones versionadas

El esquema se crea con Flyway:

- `V1__create_schema.sql`
- `V2__seed_reference_data.sql`

Esto evita depender de `hibernate.ddl-auto=update`, que no es una práctica recomendada para ambientes productivos.

### 3. Separación de capas

El dominio no depende de Spring, JPA ni infraestructura. Las reglas de arquitectura se validan con ArchUnit.

### 4. Observabilidad básica

Se incluye Spring Boot Actuator con endpoints de salud, readiness, liveness, métricas e información de la aplicación.

Endpoints útiles:

```http
GET /actuator/health
GET /actuator/health/readiness
GET /actuator/health/liveness
GET /actuator/metrics
GET /actuator/info
```

### 5. Trazabilidad por request

Cada request recibe o propaga un `X-Correlation-Id`. El mismo identificador se incluye en logs y respuestas de error.

### 6. Manejo de errores consistente

Los errores de validación, conflictos de negocio, recursos no encontrados e integridad de datos se traducen a códigos HTTP consistentes.

### 7. Pipeline CI

Se incluye GitHub Actions para ejecutar build, tests, verificación de arquitectura y reporte JaCoCo.

### 8. Contenedorización

Se incluye Dockerfile multi-stage y docker-compose para ejecución reproducible.
