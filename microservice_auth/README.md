# 🔐 Microservicio de Autenticación y Gestión de Usuarios

Microservicio desarrollado con **Spring Boot 3.5+**, **Java 17+** y **PostgreSQL**, implementando **Arquitectura Hexagonal** (Ports & Adapters) para autenticación JWT y gestión de usuarios.

[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.6-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue.svg)](https://www.postgresql.org/)
[![JWT](https://img.shields.io/badge/JWT-0.12.3-000000.svg)](https://jwt.io/)

## 🏗️ Arquitectura

### Arquitectura Hexagonal

```
├── domain/                          # Capa de Dominio (núcleo de negocio)
│   ├── model/                       # Entidades JPA
│   ├── exception/                   # Excepciones de dominio
│   └── port/
│       ├── input/                   # Casos de uso (interfaces)
│       └── output/                  # Puertos de salida (repositorios, servicios)
│
├── application/                     # Capa de Aplicación
│   └── service/                     # Implementación de casos de uso
│
└── infrastructure/                  # Capa de Infraestructura
    ├── adapter/
    │   ├── input/                   # Adaptadores de entrada
    │   │   ├── rest/                # Controllers REST
    │   │   ├── dto/                 # Data Transfer Objects
    │   │   └── mapper/              # Mappers Entity <-> DTO
    │   └── output/                  # Adaptadores de salida
    │       ├── persistence/         # Repositorios JPA
    │       └── security/            # JWT Service, Password Encoder
    └── config/                      # Configuración
        └── security/                # Spring Security, JWT Filter
```

## 📊 Modelo de Base de Datos

### Tablas

- **role**: Roles del sistema (ADMIN, USER, MODERATOR)
- **auth_user**: Usuarios con credenciales
- **auth_user_role**: Relación muchos-a-muchos entre usuarios y roles
- **refresh_token**: Tokens de refresco almacenados
- **auth_session**: Sesiones activas

## 🚀 Tecnologías

- **Java 17+**
- **Spring Boot 3.5.6**
- **Spring Security** (BCrypt, JWT)
- **Spring Data JPA** / Hibernate
- **PostgreSQL**
- **JJWT 0.12.3** (JSON Web Token)
- **Flyway** (Migraciones de BD)
- **Lombok**
- **Jakarta Validation**

## ⚙️ Configuración

### Variables de Entorno

Configura las siguientes variables de entorno o modifica `application.yml`:

```yaml
# Base de datos
DB_URL=jdbc:postgresql://localhost:5432/ms_auth_db
DB_USERNAME=postgres
DB_PASSWORD=postgres

# JWT
JWT_SECRET=tu_secret_key_aqui_cambiar_en_produccion
JWT_ACCESS_EXPIRATION=900000        # 15 minutos
JWT_REFRESH_EXPIRATION=604800000    # 7 días

# Server
SERVER_PORT=8080
```

### Base de Datos PostgreSQL

1. Crear base de datos:
```sql
CREATE DATABASE ms_auth_db;
```

2. Las tablas se crean automáticamente con Flyway al iniciar la aplicación.

## 📡 Endpoints API

### Autenticación (`/api/auth`)

#### POST `/api/auth/register`
Registrar nuevo usuario.

**Request Body:**
```json
{
  "username": "usuario123",
  "password": "password123",
  "userProfileId": 1
}
```

**Response (201 Created):**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "expiresIn": 900,
  "user": {
    "id": 1,
    "username": "usuario123",
    "isActive": true,
    "userProfileId": 1,
    "roles": [
      {"id": 2, "name": "USER"}
    ]
  }
}
```

#### POST `/api/auth/login`
Iniciar sesión.

**Request Body:**
```json
{
  "username": "admin",
  "password": "admin123"
}
```

**Response (200 OK):**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "expiresIn": 900,
  "user": {
    "id": 1,
    "username": "admin",
    "isActive": true,
    "userProfileId": null,
    "roles": [
      {"id": 1, "name": "ADMIN"}
    ]
  }
}
```

#### POST `/api/auth/refresh-token`
Renovar access token usando refresh token.

**Request Body:**
```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Response (200 OK):**
```json
{
  "accessToken": "nuevo_access_token...",
  "refreshToken": "nuevo_refresh_token...",
  "expiresIn": 900
}
```

#### POST `/api/auth/logout`
Cerrar sesión (revoca refresh token).

**Request Body:**
```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Response (200 OK):**
```json
{
  "message": "Logged out successfully"
}
```

### Gestión de Usuarios (`/api/users`)

**Nota:** Todos los endpoints de usuarios requieren autenticación con JWT en el header:
```
Authorization: Bearer {accessToken}
```

#### GET `/api/users`
Obtener todos los usuarios (requiere rol ADMIN o USER).

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "username": "admin",
    "isActive": true,
    "userProfileId": null,
    "roles": [{"id": 1, "name": "ADMIN"}]
  }
]
```

#### GET `/api/users/{id}`
Obtener usuario por ID (requiere rol ADMIN o USER).

**Response (200 OK):**
```json
{
  "id": 1,
  "username": "admin",
  "isActive": true,
  "userProfileId": null,
  "roles": [{"id": 1, "name": "ADMIN"}]
}
```

#### PUT `/api/users/{id}`
Actualizar usuario (requiere rol ADMIN).

**Request Body:**
```json
{
  "username": "nuevo_nombre",
  "isActive": false,
  "userProfileId": 5
}
```

#### DELETE `/api/users/{id}`
Eliminar usuario (requiere rol ADMIN).

**Response (200 OK):**
```json
{
  "message": "User deleted successfully"
}
```

#### POST `/api/users/{id}/roles/{roleName}`
Asignar rol a usuario (requiere rol ADMIN).

**Example:** `POST /api/users/2/roles/MODERATOR`

**Response (200 OK):**
```json
{
  "message": "Role assigned successfully"
}
```

#### DELETE `/api/users/{id}/roles/{roleName}`
Quitar rol de usuario (requiere rol ADMIN).

**Example:** `DELETE /api/users/2/roles/MODERATOR`

**Response (200 OK):**
```json
{
  "message": "Role removed successfully"
}
```

## 🔐 Seguridad

### JWT (JSON Web Token)

- **Access Token**: Expira en 15 minutos, contiene información del usuario y roles
- **Refresh Token**: Expira en 7 días, permite renovar el access token
- Los refresh tokens se almacenan en la base de datos y se implementa **token rotation**

### Hashing de Passwords

- BCrypt con Spring Security's `BCryptPasswordEncoder`
- Salt automático generado por BCrypt

### Protección de Endpoints

- Endpoints `/api/auth/**` son públicos
- Endpoints `/api/users/**` requieren autenticación JWT
- Operaciones de modificación requieren rol `ADMIN`

## 🏃 Ejecutar la Aplicación

### Con Maven

```bash
# Compilar
mvn clean install

# Ejecutar
mvn spring-boot:run
```

### Con Java

```bash
# Compilar
mvn clean package

# Ejecutar JAR
java -jar target/microservice_auth-0.0.1-SNAPSHOT.jar
```

## 🧪 Usuario por Defecto

Al iniciar la aplicación, se crea automáticamente:

- **Username:** `admin`
- **Password:** `admin123`
- **Role:** `ADMIN`

## 📝 Ejemplo de Uso con cURL

### Registrar usuario
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "test123",
    "userProfileId": 1
  }'
```

### Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }'
```

### Obtener usuarios (con JWT)
```bash
curl -X GET http://localhost:8080/api/users \
  -H "Authorization: Bearer {tu_access_token_aqui}"
```

## 🎯 Manejo de Errores

La API devuelve errores en formato JSON:

```json
{
  "timestamp": "2025-10-15T14:30:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Invalid username or password"
}
```

### Códigos de Estado HTTP

- **200 OK**: Operación exitosa
- **201 Created**: Recurso creado exitosamente
- **400 Bad Request**: Datos de entrada inválidos
- **401 Unauthorized**: Credenciales inválidas o token expirado
- **403 Forbidden**: Sin permisos suficientes
- **404 Not Found**: Recurso no encontrado
- **409 Conflict**: Usuario ya existe
- **500 Internal Server Error**: Error interno del servidor

## 🔧 Desarrollo

### Estructura del Proyecto

El proyecto sigue los principios de:
- **Arquitectura Hexagonal**: Separación clara entre dominio, aplicación e infraestructura
- **SOLID**: Principios de diseño orientado a objetos
- **Clean Code**: Código limpio y mantenible
- **Dependency Injection**: Inyección de dependencias con Spring

### Agregar Nuevas Funcionalidades

1. Crear caso de uso en `domain/port/input/`
2. Implementar servicio en `application/service/`
3. Crear controller en `infrastructure/adapter/input/rest/`
4. Agregar DTOs necesarios en `infrastructure/adapter/input/dto/`

## 📄 Licencia

Este proyecto es de código abierto para propósitos educativos.
