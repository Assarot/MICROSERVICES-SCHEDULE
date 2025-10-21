# MS-USER - Microservicio de Gestión de Perfiles de Usuario

Microservicio desarrollado con **arquitectura hexagonal** para la gestión de perfiles de usuario.

## 🏗️ Arquitectura

Este proyecto implementa **Arquitectura Hexagonal** (Ports & Adapters):

```
microservice_user/
├── domain/                          # Capa de Dominio (núcleo del negocio)
│   ├── model/                      # Entidades de dominio
│   │   └── UserProfile.java
│   ├── port/
│   │   ├── in/                     # Puertos de entrada (casos de uso)
│   │   │   └── UserProfileServicePort.java
│   │   └── out/                    # Puertos de salida (repositorios)
│   │       └── UserProfileRepositoryPort.java
│   └── exception/                  # Excepciones de dominio
│       ├── UserProfileNotFoundException.java
│       ├── EmailAlreadyExistsException.java
│       └── InvalidUserProfileException.java
│
├── application/                     # Capa de Aplicación
│   ├── service/                    # Servicios de aplicación (lógica de negocio)
│   │   └── UserProfileService.java
│   ├── dto/                        # Data Transfer Objects
│   │   ├── UserProfileRequestDto.java
│   │   └── UserProfileResponseDto.java
│   └── mapper/                     # Mappers entre capas
│       └── UserProfileMapper.java
│
└── infrastructure/                  # Capa de Infraestructura (adaptadores)
    ├── persistence/                # Adaptador de persistencia
    │   ├── entity/                 # Entidades JPA
    │   │   └── UserProfileEntity.java
    │   ├── repository/             # Repositorios JPA
    │   │   └── JpaUserProfileRepository.java
    │   ├── adapter/                # Implementación del puerto de salida
    │   │   └── UserProfileRepositoryAdapter.java
    │   └── mapper/                 # Mappers de persistencia
    │       └── UserProfileEntityMapper.java
    └── rest/                       # Adaptador REST
        ├── controller/             # Controladores REST
        │   └── UserProfileController.java
        ├── dto/                    # DTOs de respuesta
        │   └── ErrorResponseDto.java
        └── exception/              # Manejadores de excepciones
            └── GlobalExceptionHandler.java
```

## 🚀 Tecnologías

- **Java 17**
- **Spring Boot 3.5.6**
- **PostgreSQL**
- **Spring Data JPA**
- **Lombok**
- **Bean Validation**
- **Maven**

## 📋 Requisitos Previos

- Java 17+
- PostgreSQL 12+
- Maven 3.8+

## ⚙️ Configuración de Base de Datos

1. Crear la base de datos en PostgreSQL:

```sql
CREATE DATABASE ms_user_db;
```

2. Configurar credenciales en `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/ms_user_db
spring.datasource.username=postgres
spring.datasource.password=postgres
```

## 🔧 Instalación y Ejecución

### Opción 1: Maven
```bash
# Compilar el proyecto
mvn clean install

# Ejecutar la aplicación
mvn spring-boot:run
```

### Opción 2: Maven Wrapper
```bash
# En Windows
.\mvnw.cmd spring-boot:run

# En Linux/Mac
./mvnw spring-boot:run
```

La aplicación estará disponible en: `http://localhost:8080`

## 📡 Endpoints API

### Base URL: `/api/v1/user-profiles`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/v1/user-profiles` | Crear nuevo perfil |
| GET | `/api/v1/user-profiles` | Listar todos los perfiles |
| GET | `/api/v1/user-profiles/active` | Listar perfiles activos |
| GET | `/api/v1/user-profiles/{id}` | Obtener perfil por ID |
| GET | `/api/v1/user-profiles/email/{email}` | Obtener perfil por email |
| PUT | `/api/v1/user-profiles/{id}` | Actualizar perfil |
| DELETE | `/api/v1/user-profiles/{id}` | Eliminar perfil (físico) |
| PATCH | `/api/v1/user-profiles/{id}/activate` | Activar perfil |
| PATCH | `/api/v1/user-profiles/{id}/deactivate` | Desactivar perfil (lógico) |

## 📝 Ejemplos de Uso

### Crear un perfil de usuario

**Request:**
```http
POST /api/v1/user-profiles
Content-Type: application/json

{
  "names": "Juan Carlos",
  "lastName": "García López",
  "email": "juan.garcia@email.com",
  "phoneNumber": "+51987654321",
  "address": "Av. Principal 123, Lima",
  "dob": "1990-05-15",
  "profilePicture": "https://example.com/photo.jpg",
  "isActive": true
}
```

**Response:**
```json
{
  "idUserProfile": 1,
  "names": "Juan Carlos",
  "lastName": "García López",
  "email": "juan.garcia@email.com",
  "phoneNumber": "+51987654321",
  "address": "Av. Principal 123, Lima",
  "dob": "1990-05-15",
  "profilePicture": "https://example.com/photo.jpg",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00",
  "isActive": true
}
```

### Obtener perfil por ID

**Request:**
```http
GET /api/v1/user-profiles/1
```

**Response:**
```json
{
  "idUserProfile": 1,
  "names": "Juan Carlos",
  "lastName": "García López",
  "email": "juan.garcia@email.com",
  "phoneNumber": "+51987654321",
  "address": "Av. Principal 123, Lima",
  "dob": "1990-05-15",
  "profilePicture": "https://example.com/photo.jpg",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00",
  "isActive": true
}
```

### Actualizar perfil

**Request:**
```http
PUT /api/v1/user-profiles/1
Content-Type: application/json

{
  "names": "Juan Carlos",
  "lastName": "García López",
  "email": "juan.garcia.updated@email.com",
  "phoneNumber": "+51987654321",
  "address": "Av. Principal 456, Lima",
  "dob": "1990-05-15",
  "profilePicture": "https://example.com/new-photo.jpg",
  "isActive": true
}
```

### Desactivar perfil

**Request:**
```http
PATCH /api/v1/user-profiles/1/deactivate
```

## 🛡️ Validaciones

El microservicio implementa las siguientes validaciones:

### Campos Obligatorios
- `names`: 2-100 caracteres
- `lastName`: 2-100 caracteres
- `email`: formato válido, máximo 150 caracteres
- `isActive`: booleano requerido

### Validaciones Personalizadas
- **Email único**: No se permiten emails duplicados
- **Formato de email**: Validación con expresión regular
- **Teléfono**: Patrón numérico de 9-15 dígitos
- **Fecha de nacimiento**: Debe ser una fecha pasada
- **Direcciones y URLs**: Límites de longitud

## 📊 Modelo de Datos

### Tabla: `user_profile`

| Campo | Tipo | Descripción |
|-------|------|-------------|
| id_user_profile | BIGSERIAL | Primary Key |
| names | VARCHAR(100) | Nombres del usuario |
| last_name | VARCHAR(100) | Apellidos del usuario |
| email | VARCHAR(150) | Email único |
| phone_number | VARCHAR(15) | Número telefónico |
| address | VARCHAR(255) | Dirección |
| dob | DATE | Fecha de nacimiento |
| profile_picture | VARCHAR(500) | URL de foto de perfil |
| created_at | TIMESTAMP | Fecha de creación |
| updated_at | TIMESTAMP | Fecha de actualización |
| is_active | BOOLEAN | Estado activo/inactivo |

## 🔍 Manejo de Errores

El microservicio devuelve respuestas de error estandarizadas:

### Ejemplo de error de validación:
```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 400,
  "error": "Validation Error",
  "message": "Error de validación en los campos del formulario",
  "path": "/api/v1/user-profiles",
  "validationErrors": {
    "email": "El email debe ser válido",
    "names": "El nombre es obligatorio"
  }
}
```

### Códigos de Estado HTTP

- `200 OK`: Operación exitosa
- `201 Created`: Recurso creado exitosamente
- `204 No Content`: Eliminación exitosa
- `400 Bad Request`: Error de validación
- `404 Not Found`: Recurso no encontrado
- `409 Conflict`: Email duplicado
- `500 Internal Server Error`: Error del servidor

## 🧪 Testing

### Probar con cURL

```bash
# Crear perfil
curl -X POST http://localhost:8080/api/v1/user-profiles \
  -H "Content-Type: application/json" \
  -d '{
    "names": "Test User",
    "lastName": "Test LastName",
    "email": "test@email.com",
    "phoneNumber": "+51987654321",
    "address": "Test Address",
    "dob": "1990-01-01",
    "isActive": true
  }'

# Listar todos
curl http://localhost:8080/api/v1/user-profiles

# Obtener por ID
curl http://localhost:8080/api/v1/user-profiles/1
```

## 📦 Compilación

```bash
# Crear JAR ejecutable
mvn clean package

# El JAR se generará en: target/microservice_user-0.0.1-SNAPSHOT.jar

# Ejecutar JAR
java -jar target/microservice_user-0.0.1-SNAPSHOT.jar
```

## 🐳 Docker (Opcional)

```dockerfile
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/microservice_user-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

## 📚 Principios Aplicados

- **Arquitectura Hexagonal**: Separación clara de capas
- **SOLID**: Principios de diseño orientado a objetos
- **Clean Code**: Código limpio y mantenible
- **RESTful API**: Diseño de API siguiendo estándares REST
- **Exception Handling**: Manejo centralizado de excepciones
- **DTO Pattern**: Separación entre entidades de dominio y DTOs

## 👨‍💻 Autor

Proyecto desarrollado como parte del curso de Microservicios - Universidad Peruana Unión

## 📄 Licencia

Este proyecto es de uso académico.
