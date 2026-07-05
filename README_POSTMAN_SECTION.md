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

## Pruebas automatizadas de API

Además de las pruebas unitarias y de arquitectura, el proyecto incluye pruebas de integración para los endpoints principales:

```text
src/test/java/com/ceiba/medisalud/infrastructure/rest/ActuatorApiIntegrationTest.java
src/test/java/com/ceiba/medisalud/infrastructure/rest/DoctorApiIntegrationTest.java
src/test/java/com/ceiba/medisalud/infrastructure/rest/PatientApiIntegrationTest.java
src/test/java/com/ceiba/medisalud/infrastructure/rest/AppointmentApiIntegrationTest.java
src/test/java/com/ceiba/medisalud/infrastructure/rest/AvailableSlotsApiIntegrationTest.java
src/test/java/com/ceiba/medisalud/infrastructure/rest/FullEndToEndApiIntegrationTest.java
```

Estas pruebas cubren salud de la aplicación, médicos, pacientes, citas, franjas disponibles y un flujo completo end-to-end de agendamiento, consulta, reprogramación y atención.
