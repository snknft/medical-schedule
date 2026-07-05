# Production readiness

## Propósito

Este documento resume decisiones que acercan el proyecto a un estilo productivo y mantenible.

## Configuración

La aplicación soporta configuración por propiedades y variables de entorno.

Ejemplos:

```text
SPRING_PROFILES_ACTIVE
SERVER_PORT
MEDISALUD_CLOCK_FIXED_NOW
```

## Persistencia

El esquema se gestiona mediante Flyway.

Hibernate DDL automático está deshabilitado para evitar cambios implícitos en base de datos.

## Observabilidad

Se incluye:

- Spring Boot Actuator.
- Health, readiness y liveness.
- Correlation ID.
- Logs estructurados.

## Manejo de errores

Los errores se centralizan en `GlobalExceptionHandler`.

Las respuestas incluyen:

- timestamp
- status
- error
- message
- path
- correlationId
- details

## Seguridad

- Validación de entradas con Bean Validation.
- Repositorios JPA y Specifications para evitar SQL concatenado.
- No se exponen stacktraces al cliente.
- No se registran cuerpos completos de request en logs.
- H2 Console debe usarse solo en ejecución local.

## Concurrencia

La reserva de franjas usa locks lógicos y restricciones únicas en base de datos para evitar doble reserva bajo concurrencia.
