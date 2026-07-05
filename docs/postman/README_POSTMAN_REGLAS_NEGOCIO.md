# Flujos Postman por reglas de negocio

La colección Postman incluye una carpeta específica para validar las reglas de negocio del enunciado:

```text
Flujos por Reglas de Negocio (RN-01 a RN-06)
```

## Orden recomendado

Ejecutar primero:

```text
Flujos por Reglas de Negocio (RN-01 a RN-06)
└── 00 - Configuración
    └── 00 - Configurar variables de fechas
```

Ese request configura variables dinámicas para usar el próximo día laboral, sábado y domingo. Después se pueden ejecutar las carpetas:

```text
RN-01 - Franjas horarias de atención
RN-02 - No duplicidad de citas por médico
RN-03 - Fecha de nacimiento futura
RN-04 - Conflicto de paciente
RN-05 - Penalización por cancelación tardía
RN-06 - Reprogramación
```

## RN-05: penalización por cancelación tardía

La regla RN-05 depende de la hora actual. Una cancelación genera penalización solo cuando la cita se cancela con menos de 2 horas de anticipación.

Ejemplo que no genera penalización:

```text
Hora actual de la aplicación: 2026-07-05T03:00:00
Hora de la cita:              2026-07-06T08:00:00
Resultado: no hay penalización, porque faltan aproximadamente 29 horas.
```

Ejemplo que sí genera penalización:

```text
Hora actual de la aplicación: 2026-07-06T07:30:00
Hora de la cita:              2026-07-06T08:00:00
Resultado: hay penalización, porque faltan 30 minutos.
```

## Probar RN-05 con reloj fijo

Para no depender de la hora real del equipo, levantar la aplicación con la propiedad `medisalud.clock.fixed-now`.

### Windows PowerShell

```powershell
.\gradlew.bat bootRun --args="--medisalud.clock.fixed-now=2026-07-06T07:30:00"
```

### Linux / macOS

```bash
./gradlew bootRun --args='--medisalud.clock.fixed-now=2026-07-06T07:30:00'
```

Luego, en Postman, configurar estas variables de colección:

```text
rn05LateAppointmentDateTime = 2026-07-06T08:00:00
rn05NextAppointmentDateTime = 2026-07-06T08:30:00
```

Ejecutar la carpeta:

```text
RN-05 - Penalización por cancelación tardía
```

Después de tres cancelaciones tardías, el último request del flujo debe responder `409 Conflict`, porque el paciente acumula tres penalizaciones en los últimos 30 días.

## Verificación en H2

Con la aplicación levantada:

```text
http://localhost:8080/h2-console
```

Datos de conexión:

```text
JDBC URL: jdbc:h2:mem:medisalud
User: sa
Password:
```

Consulta:

```sql
SELECT * FROM penalties ORDER BY id DESC;
```

Después de ejecutar correctamente RN-05 deben existir tres registros de penalización para el paciente usado en el flujo.
