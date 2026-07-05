# Docker

## Requisitos

- Docker Desktop en Windows, o Docker Engine en Linux.
- Contenedores Linux habilitados.
- Puerto 8080 disponible.

## Windows

1. Abrir Docker Desktop.
2. Esperar a que el motor esté iniciado.
3. Validar:

```powershell
docker ps
```

4. Ejecutar:

```powershell
docker compose up --build
```

Si aparece un error como `dockerDesktopLinuxEngine`, Docker Desktop no está iniciado o no está disponible el motor Linux.

## Linux

Validar Docker:

```bash
docker ps
```

Ejecutar:

```bash
docker compose up --build
```

## Probar API

```http
GET http://localhost:8080/actuator/health
```

## RN-05 con Docker

Para simular hora fija:

```yaml
environment:
  MEDISALUD_CLOCK_FIXED_NOW: "2026-07-06T07:30:00"
```

Luego:

```bash
docker compose up --build
```

Quitar la variable para volver a usar la hora real del sistema.
