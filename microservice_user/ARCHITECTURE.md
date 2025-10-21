# 🏗️ Arquitectura del Microservicio MS-USER

## Visión General

MS-USER es un microservicio diseñado siguiendo los principios de **Arquitectura Hexagonal** (también conocida como Ports & Adapters), que garantiza:

- ✅ **Independencia de frameworks**: El dominio no depende de Spring
- ✅ **Testabilidad**: Cada capa puede probarse independientemente
- ✅ **Flexibilidad**: Fácil cambio de adaptadores (BD, API, etc.)
- ✅ **Mantenibilidad**: Separación clara de responsabilidades

---

## 📐 Diagrama de Arquitectura

```
┌─────────────────────────────────────────────────────────────────┐
│                        CAPA DE PRESENTACIÓN                      │
│                     (Infrastructure - REST)                      │
│                                                                  │
│  ┌──────────────────────────────────────────────────────────┐   │
│  │  UserProfileController                                    │   │
│  │  - POST /api/v1/user-profiles                            │   │
│  │  - GET /api/v1/user-profiles                             │   │
│  │  - GET /api/v1/user-profiles/{id}                        │   │
│  │  - PUT /api/v1/user-profiles/{id}                        │   │
│  │  - DELETE /api/v1/user-profiles/{id}                     │   │
│  │  - PATCH /api/v1/user-profiles/{id}/activate            │   │
│  │  - PATCH /api/v1/user-profiles/{id}/deactivate          │   │
│  └──────────────────────────────────────────────────────────┘   │
│                             ▼                                    │
│  ┌──────────────────────────────────────────────────────────┐   │
│  │  GlobalExceptionHandler                                   │   │
│  │  - UserProfileNotFoundException                          │   │
│  │  - EmailAlreadyExistsException                           │   │
│  │  - InvalidUserProfileException                           │   │
│  │  - MethodArgumentNotValidException                       │   │
│  └──────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────┘
                             ▼
┌─────────────────────────────────────────────────────────────────┐
│                      CAPA DE APLICACIÓN                          │
│                 (Application - Use Cases)                        │
│                                                                  │
│  ┌──────────────────────────────────────────────────────────┐   │
│  │  UserProfileMapper                                        │   │
│  │  - toDomain(RequestDto)                                  │   │
│  │  - toResponseDto(Domain)                                 │   │
│  │  - toResponseDtoList(List<Domain>)                       │   │
│  └──────────────────────────────────────────────────────────┘   │
│                             ▼                                    │
│  ┌──────────────────────────────────────────────────────────┐   │
│  │  UserProfileService (implements ServicePort)             │   │
│  │  - createUserProfile()                                   │   │
│  │  - updateUserProfile()                                   │   │
│  │  - getUserProfileById()                                  │   │
│  │  - getAllUserProfiles()                                  │   │
│  │  - deleteUserProfile()                                   │   │
│  │  - activateUserProfile()                                 │   │
│  │  - deactivateUserProfile()                               │   │
│  └──────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────┘
                             ▼
┌─────────────────────────────────────────────────────────────────┐
│                        CAPA DE DOMINIO                           │
│                    (Domain - Business Logic)                     │
│                                                                  │
│  ┌──────────────────────────────────────────────────────────┐   │
│  │  UserProfile (Domain Entity)                             │   │
│  │  - idUserProfile: Long                                   │   │
│  │  - names: String                                         │   │
│  │  - lastName: String                                      │   │
│  │  - email: String                                         │   │
│  │  - phoneNumber: String                                   │   │
│  │  - address: String                                       │   │
│  │  - dob: LocalDate                                        │   │
│  │  - profilePicture: String                                │   │
│  │  - createdAt: LocalDateTime                              │   │
│  │  - updatedAt: LocalDateTime                              │   │
│  │  - isActive: Boolean                                     │   │
│  │  + activate()                                            │   │
│  │  + deactivate()                                          │   │
│  │  + isEmailValid()                                        │   │
│  └──────────────────────────────────────────────────────────┘   │
│                             ▲                                    │
│  ┌─────────────────────┐   │   ┌────────────────────────────┐   │
│  │  ServicePort        │───┘   │  RepositoryPort            │   │
│  │  (Input Port)       │       │  (Output Port)             │   │
│  └─────────────────────┘       └────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────┘
                             ▼
┌─────────────────────────────────────────────────────────────────┐
│                   CAPA DE INFRAESTRUCTURA                        │
│                 (Infrastructure - Persistence)                   │
│                                                                  │
│  ┌──────────────────────────────────────────────────────────┐   │
│  │  UserProfileRepositoryAdapter                            │   │
│  │  (implements RepositoryPort)                             │   │
│  └──────────────────────────────────────────────────────────┘   │
│                             ▼                                    │
│  ┌──────────────────────────────────────────────────────────┐   │
│  │  UserProfileEntityMapper                                 │   │
│  │  - toEntity(Domain)                                      │   │
│  │  - toDomain(Entity)                                      │   │
│  └──────────────────────────────────────────────────────────┘   │
│                             ▼                                    │
│  ┌──────────────────────────────────────────────────────────┐   │
│  │  JpaUserProfileRepository                                │   │
│  │  extends JpaRepository<UserProfileEntity, Long>          │   │
│  └──────────────────────────────────────────────────────────┘   │
│                             ▼                                    │
│  ┌──────────────────────────────────────────────────────────┐   │
│  │  UserProfileEntity (JPA Entity)                          │   │
│  │  @Table(name = "user_profile")                           │   │
│  └──────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────┘
                             ▼
                    ┌─────────────────┐
                    │   PostgreSQL    │
                    │   ms_user_db    │
                    └─────────────────┘
```

---

## 🔄 Flujo de Datos

### Ejemplo: Crear un UserProfile

```
1. Cliente HTTP
   └─> POST /api/v1/user-profiles
       Body: UserProfileRequestDto

2. UserProfileController
   └─> Valida RequestDto (@Valid)
   └─> Llama a UserProfileMapper.toDomain()

3. UserProfileMapper
   └─> Convierte RequestDto → Domain Entity

4. UserProfileService
   └─> Valida email único (lógica de negocio)
   └─> Valida formato email
   └─> Establece timestamps
   └─> Llama a RepositoryPort.save()

5. UserProfileRepositoryAdapter
   └─> Llama a UserProfileEntityMapper.toEntity()
   └─> Convierte Domain → JPA Entity

6. JpaUserProfileRepository
   └─> Ejecuta INSERT en PostgreSQL

7. PostgreSQL
   └─> Guarda en tabla user_profile
   └─> Retorna entity con ID generado

8. Respuesta al cliente
   └─> Entity → Domain → ResponseDto
   └─> HTTP 201 Created + UserProfileResponseDto
```

---

## 🎯 Principios SOLID Aplicados

### 1. Single Responsibility Principle (SRP)
- **UserProfile**: Solo contiene lógica del dominio
- **UserProfileService**: Solo lógica de negocio
- **UserProfileController**: Solo manejo de HTTP
- **UserProfileRepositoryAdapter**: Solo persistencia

### 2. Open/Closed Principle (OCP)
- Puertos (interfaces) abiertos para extensión
- Implementaciones cerradas para modificación
- Fácil agregar nuevos adaptadores sin modificar el dominio

### 3. Liskov Substitution Principle (LSP)
- Cualquier implementación de `RepositoryPort` es intercambiable
- Se puede cambiar de PostgreSQL a MongoDB sin afectar el dominio

### 4. Interface Segregation Principle (ISP)
- `ServicePort`: Define solo operaciones de negocio
- `RepositoryPort`: Define solo operaciones de persistencia
- No hay interfaces gordas con métodos innecesarios

### 5. Dependency Inversion Principle (DIP)
- El dominio NO depende de infraestructura
- Infraestructura depende del dominio (via puertos)
- Spring inyecta dependencias en tiempo de ejecución

---

## 📦 Estructura de Paquetes

```
pe.edu.upeu.microservice_user/
│
├── domain/                              # CAPA DE DOMINIO
│   ├── model/                          # Entidades de negocio
│   │   └── UserProfile.java           # Entidad principal
│   ├── port/
│   │   ├── in/                         # Puertos de entrada (casos de uso)
│   │   │   └── UserProfileServicePort.java
│   │   └── out/                        # Puertos de salida (repositorios)
│   │       └── UserProfileRepositoryPort.java
│   └── exception/                      # Excepciones de negocio
│       ├── UserProfileNotFoundException.java
│       ├── EmailAlreadyExistsException.java
│       └── InvalidUserProfileException.java
│
├── application/                         # CAPA DE APLICACIÓN
│   ├── service/                        # Servicios de aplicación
│   │   └── UserProfileService.java    # Implementa ServicePort
│   ├── dto/                            # Data Transfer Objects
│   │   ├── UserProfileRequestDto.java
│   │   └── UserProfileResponseDto.java
│   └── mapper/                         # Mappers
│       └── UserProfileMapper.java
│
└── infrastructure/                      # CAPA DE INFRAESTRUCTURA
    ├── persistence/                    # Adaptador de persistencia
    │   ├── entity/                     # Entidades JPA
    │   │   └── UserProfileEntity.java
    │   ├── repository/                 # Repositorios JPA
    │   │   └── JpaUserProfileRepository.java
    │   ├── adapter/                    # Implementación de puertos
    │   │   └── UserProfileRepositoryAdapter.java
    │   └── mapper/                     # Mappers de persistencia
    │       └── UserProfileEntityMapper.java
    └── rest/                           # Adaptador REST
        ├── controller/                 # Controladores
        │   └── UserProfileController.java
        ├── dto/                        # DTOs de infraestructura
        │   └── ErrorResponseDto.java
        └── exception/                  # Manejadores de excepciones
            └── GlobalExceptionHandler.java
```

---

## 🔐 Validaciones Implementadas

### Validaciones de Bean Validation (Jakarta)

| Campo | Validación | Mensaje |
|-------|------------|---------|
| names | `@NotBlank`, `@Size(2-100)` | El nombre es obligatorio y debe tener entre 2 y 100 caracteres |
| lastName | `@NotBlank`, `@Size(2-100)` | El apellido es obligatorio y debe tener entre 2 y 100 caracteres |
| email | `@NotBlank`, `@Email`, `@Size(max=150)` | El email es obligatorio y debe ser válido |
| phoneNumber | `@Pattern(regexp="^[+]?[0-9]{9,15}$")` | El número debe tener entre 9 y 15 dígitos |
| address | `@Size(max=255)` | La dirección no puede exceder 255 caracteres |
| dob | `@Past` | La fecha de nacimiento debe ser pasada |
| profilePicture | `@Size(max=500)` | La URL no puede exceder 500 caracteres |
| isActive | `@NotNull` | El estado activo es obligatorio |

### Validaciones de Negocio (Servicio)

1. **Email único**: No se permiten emails duplicados
2. **Formato de email**: Validación con regex
3. **Timestamps automáticos**: Se establecen al crear/actualizar
4. **Estado por defecto**: `isActive = true` si no se especifica

---

## 🔄 Patrones de Diseño Utilizados

### 1. Hexagonal Architecture (Ports & Adapters)
- Separación clara entre dominio e infraestructura
- Puertos definen contratos
- Adaptadores implementan los contratos

### 2. Repository Pattern
- Abstracción de la capa de datos
- `RepositoryPort` define operaciones
- `RepositoryAdapter` implementa con JPA

### 3. DTO Pattern
- Separación entre objetos de transferencia y dominio
- `RequestDto` para entrada
- `ResponseDto` para salida
- `Entity` para persistencia

### 4. Mapper Pattern
- Conversión entre capas
- `UserProfileMapper`: Application ↔ Domain
- `UserProfileEntityMapper`: Domain ↔ JPA

### 5. Dependency Injection
- Spring inyecta dependencias vía constructor
- Uso de `@RequiredArgsConstructor` de Lombok
- Inversión de control (IoC)

### 6. Exception Handler Pattern
- Manejo centralizado de excepciones
- `@RestControllerAdvice`
- Respuestas de error estandarizadas

---

## 🚀 Ventajas de Esta Arquitectura

### ✅ Testabilidad
```java
// Se puede testear el dominio sin Spring
UserProfile profile = new UserProfile();
profile.setEmail("test@email.com");
assertTrue(profile.isEmailValid());
```

### ✅ Independencia de Base de Datos
```java
// Cambiar de PostgreSQL a MongoDB solo requiere
// crear un nuevo adapter que implemente RepositoryPort
public class MongoUserProfileAdapter implements UserProfileRepositoryPort {
    // implementación con MongoDB
}
```

### ✅ Independencia de Framework REST
```java
// Cambiar de Spring MVC a GraphQL solo requiere
// crear un nuevo controlador GraphQL
@GraphQLController
public class UserProfileGraphQLController {
    // nueva implementación
}
```

### ✅ Facilidad de Testing
- **Unit Tests**: Testear lógica de dominio aislada
- **Integration Tests**: Testear con base de datos real
- **E2E Tests**: Testear endpoints completos

---

## 📝 Ejemplo de Flujo Completo

### Request HTTP

```http
POST /api/v1/user-profiles
Content-Type: application/json

{
  "names": "Juan",
  "lastName": "García",
  "email": "juan@email.com",
  "phoneNumber": "+51987654321",
  "isActive": true
}
```

### Procesamiento

```
UserProfileController
  ↓ (valida @Valid)
UserProfileMapper
  ↓ (RequestDto → Domain)
UserProfileService
  ↓ (valida email único, formato, timestamps)
UserProfileRepositoryAdapter
  ↓ (Domain → Entity)
JpaUserProfileRepository
  ↓ (INSERT SQL)
PostgreSQL
  ↓ (retorna ID generado)
UserProfileService
  ↓ (Domain actualizado)
UserProfileMapper
  ↓ (Domain → ResponseDto)
UserProfileController
  ↓ (HTTP 201 Created)
```

### Response HTTP

```http
HTTP/1.1 201 Created
Content-Type: application/json

{
  "idUserProfile": 1,
  "names": "Juan",
  "lastName": "García",
  "email": "juan@email.com",
  "phoneNumber": "+51987654321",
  "address": null,
  "dob": null,
  "profilePicture": null,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00",
  "isActive": true
}
```

---

## 🎓 Conclusión

Esta arquitectura hexagonal proporciona:

- ✅ **Separación de responsabilidades**: Cada capa tiene un propósito claro
- ✅ **Bajo acoplamiento**: Las capas se comunican vía interfaces
- ✅ **Alta cohesión**: Cada componente tiene una única responsabilidad
- ✅ **Testabilidad**: Fácil escribir tests unitarios e integración
- ✅ **Mantenibilidad**: Fácil agregar nuevas funcionalidades
- ✅ **Escalabilidad**: Preparado para crecer sin reescribir

**MS-USER** está diseñado siguiendo las mejores prácticas de la industria y listo para entornos de producción.
