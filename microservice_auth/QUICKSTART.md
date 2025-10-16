# Quick Start Guide

## Prerequisitos

- Java 17 o superior
- Maven 3.6+
- Docker y Docker Compose (opcional, para base de datos)
- PostgreSQL 12+ (si no usas Docker)

## Pasos para Ejecutar

### 1. Iniciar Base de Datos PostgreSQL

#### Opción A: Con Docker (Recomendado)
```bash
docker-compose up -d
```

#### Opción B: PostgreSQL Local
Crear base de datos manualmente:
```sql
CREATE DATABASE ms_auth_db;
```

### 2. Configurar Variables de Entorno (Opcional)

Si tu PostgreSQL tiene diferentes credenciales, configura las variables:

**Windows (PowerShell):**
```powershell
$env:DB_URL="jdbc:postgresql://localhost:5432/ms_auth_db"
$env:DB_USERNAME="postgres"
$env:DB_PASSWORD="tu_password"
$env:JWT_SECRET="tu_secret_key_super_seguro"
```

**Linux/Mac:**
```bash
export DB_URL="jdbc:postgresql://localhost:5432/ms_auth_db"
export DB_USERNAME="postgres"
export DB_PASSWORD="tu_password"
export JWT_SECRET="tu_secret_key_super_seguro"
```

### 3. Compilar y Ejecutar

```bash
# Compilar el proyecto
mvn clean install

# Ejecutar la aplicación
mvn spring-boot:run
```

La aplicación iniciará en: `http://localhost:8080`

### 4. Verificar que Funciona

#### Prueba Login con usuario por defecto:

**cURL:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d "{\"username\":\"admin\",\"password\":\"admin123\"}"
```

**PowerShell:**
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/auth/login" `
  -Method Post `
  -ContentType "application/json" `
  -Body '{"username":"admin","password":"admin123"}'
```

**Respuesta esperada:**
```json
{
  "accessToken": "eyJhbGc...",
  "refreshToken": "eyJhbGc...",
  "expiresIn": 900,
  "user": {
    "id": 1,
    "username": "admin",
    "isActive": true,
    "roles": [{"id": 1, "name": "ADMIN"}]
  }
}
```

### 5. Probar Endpoints Protegidos

Guarda el `accessToken` de la respuesta anterior y úsalo:

**cURL:**
```bash
curl -X GET http://localhost:8080/api/users \
  -H "Authorization: Bearer TU_ACCESS_TOKEN_AQUI"
```

**PowerShell:**
```powershell
$token = "TU_ACCESS_TOKEN_AQUI"
Invoke-RestMethod -Uri "http://localhost:8080/api/users" `
  -Method Get `
  -Headers @{"Authorization"="Bearer $token"}
```

## Credenciales por Defecto

- **Usuario:** `admin`
- **Contraseña:** `admin123`
- **Rol:** `ADMIN`

## Detener Servicios

### Detener aplicación Spring Boot
Presiona `Ctrl+C` en la terminal

### Detener base de datos Docker
```bash
docker-compose down
```

## Estructura de Base de Datos

Las tablas se crean automáticamente con Flyway al iniciar:
- `role` - Roles del sistema
- `auth_user` - Usuarios
- `auth_user_role` - Relación usuarios-roles
- `refresh_token` - Tokens de refresco
- `auth_session` - Sesiones activas

Roles creados por defecto:
- ADMIN
- USER
- MODERATOR

## Documentación Adicional

- Ver `README.md` para documentación completa
- Ver `ENDPOINTS.md` para ejemplos de todos los endpoints
- Ver `application.yml` para configuración detallada

## Troubleshooting

### Error: "Connection refused" a PostgreSQL
- Verifica que PostgreSQL esté corriendo: `docker ps`
- Verifica las credenciales en `application.yml`

### Error: "Port 8080 already in use"
- Cambia el puerto en `application.yml`: `server.port: 8081`
- O cierra la aplicación que usa el puerto 8080

### Error: "JWT secret too short"
- Asegúrate de que `JWT_SECRET` tenga al menos 256 bits (32 caracteres)

### Error: Flyway migration failed
- Elimina el volumen de Docker: `docker-compose down -v`
- Vuelve a crear: `docker-compose up -d`
