# Postman

## Colección

El repositorio incluye una colección Postman:

```text
docs/postman/medical-schedule.postman_collection.json
```

## Importar

1. Abrir Postman.
2. Seleccionar **Import**.
3. Elegir **Files**.
4. Seleccionar `docs/postman/medical-schedule.postman_collection.json`.
5. Confirmar importación.

Variable principal:

```text
baseUrl = http://localhost:8080
```

## Levantar la aplicación

### Windows PowerShell

```powershell
.\gradlew.bat bootRun
```

### Linux / macOS

```bash
./gradlew bootRun
```

Validar:

```http
GET http://localhost:8080/actuator/health
```

## Carpetas principales

```text
Actuator / Observabilidad
Médicos
Pacientes
Citas
Franjas disponibles
Flujo sugerido de prueba
Flujo con penalización por cancelación tardía
Flujos por Reglas de Negocio (RN-01 a RN-06)
```

## Flujos por reglas de negocio

Ejecutar primero:

```text
Flujos por Reglas de Negocio (RN-01 a RN-06)
└── 00 - Configuración
    └── 00 - Configurar variables de fechas
```

Luego ejecutar cada carpeta:

```text
RN-01 - Franjas horarias de atención
RN-02 - No duplicidad de citas por médico
RN-03 - Fecha de nacimiento futura
RN-04 - Conflicto de paciente
RN-05 - Penalización por cancelación tardía
RN-06 - Reprogramación
```

## RN-05 con reloj fijo

RN-05 depende de la hora actual de la aplicación. Para probarla de forma controlada, iniciar la app con:

### Windows PowerShell

```powershell
.\gradlew.bat bootRun --args="--medisalud.clock.fixed-now=2026-07-06T07:30:00"
```

### Linux / macOS

```bash
./gradlew bootRun --args="--medisalud.clock.fixed-now=2026-07-06T07:30:00"
```

Configurar en Postman:

```text
rn05LateAppointmentDateTime = 2026-07-06T08:00:00
rn05NextAppointmentDateTime = 2026-07-06T08:30:00
```

Luego ejecutar:

```text
Flujos por Reglas de Negocio (RN-01 a RN-06)
└── RN-05 - Penalización por cancelación tardía
```

Con la hora simulada en `07:30`, una cita de `08:00` se cancela con menos de 2 horas de anticipación y debe generar penalización.

## Verificación en H2

```sql
SELECT * FROM penalties ORDER BY id DESC;
SELECT * FROM appointments ORDER BY id DESC;
```

Después de RN-05 deben existir penalizaciones registradas.
