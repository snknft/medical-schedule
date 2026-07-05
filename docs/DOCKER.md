# Ejecución con Docker

Esta guía explica cómo construir, ejecutar y probar la API usando Docker en Windows y Linux.

El proyecto incluye:

```text
Dockerfile
docker-compose.yml
```

La aplicación expone el puerto `8080` y usa H2 en memoria. Cada vez que el contenedor se reinicia, los datos vuelven al estado inicial definido por las migraciones de Flyway.

---

## Requisitos

- Docker instalado.
- Docker Compose disponible como plugin del comando `docker compose`.
- Puerto `8080` libre en el equipo.

Validar instalación:

```bash
docker version
docker compose version
docker ps
```

`docker ps` puede mostrar una lista vacía, pero no debe devolver error de conexión.

---

## Windows

### 1. Iniciar Docker Desktop

Abrir **Docker Desktop** desde el menú de Windows y esperar a que finalice el arranque.

Debe estar usando contenedores Linux. Si aparece la opción **Switch to Linux containers**, seleccionarla.

Validar desde PowerShell:

```powershell
docker version
docker info
docker ps
```

### 2. Construir y levantar la API

Desde la raíz del proyecto:

```powershell
docker compose up --build
```

La API queda disponible en:

```text
http://localhost:8080
```

### 3. Probar el estado de la aplicación

En otra terminal PowerShell:

```powershell
Invoke-RestMethod http://localhost:8080/actuator/health
Invoke-RestMethod http://localhost:8080/actuator/health/readiness
```

También puede abrirse en navegador:

```text
http://localhost:8080/actuator/health
```

### 4. Detener la aplicación

Presionar `Ctrl + C` en la terminal donde está corriendo Docker Compose.

Para limpiar contenedores creados por Compose:

```powershell
docker compose down
```

Si se quiere limpiar también volúmenes y recursos huérfanos:

```powershell
docker compose down --volumes --remove-orphans
```

---

## Linux

### 1. Validar Docker Engine

```bash
docker version
docker compose version
docker ps
```

Si Docker no está activo:

```bash
sudo systemctl start docker
```

Para habilitarlo al iniciar el sistema:

```bash
sudo systemctl enable docker
```

Si el usuario no tiene permisos para ejecutar Docker sin `sudo`:

```bash
sudo usermod -aG docker $USER
newgrp docker
```

### 2. Construir y levantar la API

Desde la raíz del proyecto:

```bash
docker compose up --build
```

La API queda disponible en:

```text
http://localhost:8080
```

### 3. Probar el estado de la aplicación

```bash
curl http://localhost:8080/actuator/health
curl http://localhost:8080/actuator/health/readiness
```

### 4. Detener la aplicación

```bash
docker compose down
```

---

## Prueba rápida de endpoints

Con la API arriba, crear un paciente:

```bash
curl -X POST http://localhost:8080/api/patients \
  -H "Content-Type: application/json" \
  -d '{
    "fullName": "Juan Pérez",
    "documentNumber": "123456789",
    "phone": "5553001",
    "email": "juan.perez@email.com",
    "birthDate": "1995-05-20"
  }'
```

Crear una cita usando el médico semilla `1`:

```bash
curl -X POST http://localhost:8080/api/appointments \
  -H "Content-Type: application/json" \
  -d '{
    "patientId": 1,
    "doctorId": 1,
    "appointmentDateTime": "2026-07-06T08:00:00"
  }'
```

Consultar disponibilidad:

```bash
curl "http://localhost:8080/api/doctors/1/available-slots?fechaInicio=2026-07-06&fechaFin=2026-07-10"
```

---

## Probar con Postman

También se puede importar la colección ubicada en:

```text
docs/postman/medical-schedule.postman_collection.json
```

La variable principal de la colección es:

```text
baseUrl = http://localhost:8080
```

Con Docker Compose levantado, las solicitudes de Postman se ejecutan contra el mismo puerto local `8080`.

---

## Errores frecuentes

### `open //./pipe/dockerDesktopLinuxEngine: El sistema no puede encontrar el archivo especificado`

En Windows, este error indica que Docker Desktop no está iniciado o que el motor Linux no está disponible.

Solución:

```powershell
wsl --shutdown
```

Luego abrir Docker Desktop, esperar a que termine de iniciar y ejecutar:

```powershell
docker ps
docker compose up --build
```

### `port is already allocated`

El puerto `8080` está ocupado por otro proceso.

Opciones:

1. Detener la aplicación que usa el puerto.
2. Cambiar el mapeo en `docker-compose.yml`:

```yaml
ports:
  - "8081:8080"
```

En ese caso, la API quedaría disponible en:

```text
http://localhost:8081
```

### `Docker daemon is not running`

Docker no está activo.

En Windows, iniciar Docker Desktop.

En Linux:

```bash
sudo systemctl start docker
```

### Build lento en la primera ejecución

Es normal. La primera vez Docker descarga imágenes base y dependencias de Gradle. Las siguientes ejecuciones usan caché.

---

## Comandos útiles

```bash
# Construir y levantar
 docker compose up --build

# Levantar sin reconstruir
 docker compose up

# Ejecutar en segundo plano
 docker compose up -d --build

# Ver logs
 docker compose logs -f

# Detener
 docker compose down

# Detener y limpiar recursos asociados
 docker compose down --volumes --remove-orphans
```
