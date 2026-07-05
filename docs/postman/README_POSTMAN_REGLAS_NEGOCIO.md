# Flujos Postman por reglas de negocio

Esta colección incluye una carpeta específica para validar las reglas de negocio del enunciado:

```text
Flujos por Reglas de Negocio (RN-01 a RN-06)
```

## Orden recomendado

Ejecuta primero:

```text
Flujos por Reglas de Negocio (RN-01 a RN-06)
└── 00 - Configuración
    └── 00 - Configurar variables de fechas
```

Ese request configura variables dinámicas para usar el próximo día laboral, sábado y domingo. Después puedes ejecutar cada carpeta:

```text
RN-01 - Franjas horarias de atención
RN-02 - No duplicidad de citas por médico
RN-03 - Fecha de nacimiento futura
RN-04 - Conflicto de paciente
RN-05 - Penalización por cancelación tardía
RN-06 - Reprogramación
```

## RN-05: condición especial

La regla RN-05 depende de la hora real del sistema. Para que se genere penalización, la cita debe cancelarse con menos de 2 horas de anticipación y debe estar dentro de una franja laboral válida.

Ejemplo:

```text
Si hoy es lunes 07:10 a. m., usa una cita para las 08:00 a. m.
```

Si estás en domingo o fuera de una ventana laboral cercana, Postman no podrá generar una penalización real de forma natural. En ese caso:

1. Ejecuta los tests automatizados de `AppointmentServiceTest`.
2. O espera una ventana laboral válida.
3. O ajusta manualmente la variable `rn05LateAppointmentDateTime` con una franja válida menor a 2 horas desde el momento actual.

Variables relevantes:

```text
rn05LateAppointmentDateTime
rn05NextAppointmentDateTime
```

## Verificación en H2 para RN-05

Con la aplicación levantada:

```text
http://localhost:8080/h2-console
```

Datos:

```text
JDBC URL: jdbc:h2:mem:medisalud
User: sa
Password: password
```

Consulta:

```sql
SELECT * FROM penalties ORDER BY id DESC;
```

Después de ejecutar correctamente RN-05, deben existir tres penalizaciones para el paciente usado en el flujo.
