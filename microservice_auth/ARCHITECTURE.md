# Arquitectura del Microservicio

## Diagrama de Arquitectura Hexagonal

```
┌─────────────────────────────────────────────────────────────────┐
│                    CAPA DE INFRAESTRUCTURA                       │
│                    (Adaptadores de Entrada)                      │
├─────────────────────────────────────────────────────────────────┤
│                                                                   │
│  ┌──────────────────┐         ┌──────────────────┐             │
│  │  AuthController  │         │  UserController  │             │
│  │  /api/auth/**    │         │  /api/users/**   │             │
│  └────────┬─────────┘         └────────┬─────────┘             │
│           │                             │                        │
│           │  REST API (JSON)            │                        │
│           └─────────────┬───────────────┘                        │
│                         │                                        │
└─────────────────────────┼────────────────────────────────────────┘
                          │
┌─────────────────────────┼────────────────────────────────────────┐
│                    CAPA DE APLICACIÓN                            │
│                  (Implementación de Casos de Uso)                │
├─────────────────────────┼────────────────────────────────────────┤
│                         ↓                                        │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │  RegisterService   LoginService   RefreshTokenService    │  │
│  │  UserManagementService                                    │  │
│  └──────────────────────┬───────────────────────────────────┘  │
│                         │                                        │
│                         │  Implementa Casos de Uso               │
│                         │                                        │
└─────────────────────────┼────────────────────────────────────────┘
                          │
┌─────────────────────────┼────────────────────────────────────────┐
│                    CAPA DE DOMINIO                               │
│              (Núcleo de Negocio - Sin Dependencias)              │
├─────────────────────────┼────────────────────────────────────────┤
│                         ↓                                        │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │  PUERTOS DE ENTRADA (Input Ports)                       │   │
│  │  - RegisterUseCase                                       │   │
│  │  - LoginUseCase                                          │   │
│  │  - RefreshTokenUseCase                                   │   │
│  │  - UserManagementUseCase                                 │   │
│  └─────────────────────────────────────────────────────────┘   │
│                                                                   │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │  MODELO DE DOMINIO                                       │   │
│  │  - AuthUser (usuario con credenciales)                  │   │
│  │  - Role (roles del sistema)                             │   │
│  │  - RefreshToken (tokens de refresco)                    │   │
│  │  - AuthSession (sesiones activas)                       │   │
│  └─────────────────────────────────────────────────────────┘   │
│                                                                   │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │  EXCEPCIONES DE DOMINIO                                  │   │
│  │  - UserNotFoundException                                 │   │
│  │  - InvalidCredentialsException                           │   │
│  │  - InvalidTokenException                                 │   │
│  │  - RoleNotFoundException                                 │   │
│  └─────────────────────────────────────────────────────────┘   │
│                                                                   │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │  PUERTOS DE SALIDA (Output Ports)                       │   │
│  │  - UserRepositoryPort                                    │   │
│  │  - RoleRepositoryPort                                    │   │
│  │  - RefreshTokenRepositoryPort                            │   │
│  │  - AuthSessionRepositoryPort                             │   │
│  │  - JwtServicePort                                        │   │
│  │  - PasswordEncoderPort                                   │   │
│  └─────────────────────┬───────────────────────────────────┘   │
│                         │                                        │
└─────────────────────────┼────────────────────────────────────────┘
                          │
┌─────────────────────────┼────────────────────────────────────────┐
│                    CAPA DE INFRAESTRUCTURA                       │
│                    (Adaptadores de Salida)                       │
├─────────────────────────┼────────────────────────────────────────┤
│                         ↓                                        │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │  ADAPTADORES DE PERSISTENCIA (JPA)                      │   │
│  │  - UserRepositoryAdapter → UserJpaRepository            │   │
│  │  - RoleRepositoryAdapter → RoleJpaRepository            │   │
│  │  - RefreshTokenRepositoryAdapter                        │   │
│  │  - AuthSessionRepositoryAdapter                         │   │
│  └────────────────────┬────────────────────────────────────┘   │
│                       │                                          │
│                       ↓                                          │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │              PostgreSQL Database                         │   │
│  │  - role                                                  │   │
│  │  - auth_user                                             │   │
│  │  - auth_user_role                                        │   │
│  │  - refresh_token                                         │   │
│  │  - auth_session                                          │   │
│  └─────────────────────────────────────────────────────────┘   │
│                                                                   │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │  ADAPTADORES DE SEGURIDAD                                │   │
│  │  - JwtServiceAdapter (JJWT)                             │   │
│  │  - PasswordEncoderAdapter (BCrypt)                      │   │
│  └─────────────────────────────────────────────────────────┘   │
│                                                                   │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │  CONFIGURACIÓN DE SPRING SECURITY                        │   │
│  │  - JwtAuthenticationFilter                              │   │
│  │  - CustomUserDetailsService                             │   │
│  │  - SecurityConfig                                        │   │
│  └─────────────────────────────────────────────────────────┘   │
│                                                                   │
└───────────────────────────────────────────────────────────────────┘
```

## Principios de Arquitectura Hexagonal

### 1. **Independencia del Dominio**
- El dominio no depende de frameworks externos
- Reglas de negocio puras en el centro
- Sin referencias a Spring, JPA, etc. en el dominio

### 2. **Puertos (Interfaces)**
- **Puertos de Entrada**: Definen casos de uso (RegisterUseCase, LoginUseCase)
- **Puertos de Salida**: Definen contratos para servicios externos (repositorios, JWT, etc.)

### 3. **Adaptadores**
- **Adaptadores de Entrada**: Controllers REST que traducen HTTP a llamadas de dominio
- **Adaptadores de Salida**: Implementaciones JPA, JWT, BCrypt que implementan puertos

### 4. **Inversión de Dependencias**
- El dominio define interfaces (puertos)
- La infraestructura implementa esas interfaces (adaptadores)
- El dominio no conoce detalles de implementación

## Flujo de una Petición (Ejemplo: Login)

```
1. Cliente HTTP POST /api/auth/login
           ↓
2. AuthController (Adaptador de Entrada)
   - Valida DTO
   - Mapea a tipos de dominio
           ↓
3. LoginService (Caso de Uso)
   - Orquesta la lógica de negocio
   - Usa puertos de salida
           ↓
4. UserRepositoryAdapter (Adaptador de Salida)
   - Consulta base de datos
   - Devuelve entidad de dominio
           ↓
5. PasswordEncoderAdapter (Adaptador de Salida)
   - Valida password con BCrypt
           ↓
6. JwtServiceAdapter (Adaptador de Salida)
   - Genera access token y refresh token
           ↓
7. RefreshTokenRepositoryAdapter (Adaptador de Salida)
   - Guarda refresh token en DB
           ↓
8. LoginService devuelve resultado
           ↓
9. AuthController mapea a DTO de respuesta
           ↓
10. Cliente recibe JSON con tokens
```

## Capas y Responsabilidades

### **Dominio** (`domain/`)
- **Propósito**: Contiene la lógica de negocio pura
- **Regla**: No puede depender de capas externas
- **Contiene**:
  - Entidades (AuthUser, Role, etc.)
  - Excepciones de dominio
  - Puertos (interfaces)

### **Aplicación** (`application/`)
- **Propósito**: Implementa casos de uso
- **Responsabilidad**: Orquestar el flujo de datos
- **Contiene**:
  - Servicios que implementan casos de uso
  - Lógica de coordinación entre puertos

### **Infraestructura** (`infrastructure/`)
- **Propósito**: Implementaciones concretas
- **Responsabilidad**: Conectar con tecnologías externas
- **Contiene**:
  - Adaptadores de entrada (controllers)
  - Adaptadores de salida (repositorios JPA, servicios)
  - Configuración (Spring Security, etc.)
  - DTOs (objetos de transferencia)

## Ventajas de esta Arquitectura

✅ **Mantenibilidad**: Código organizado y fácil de entender

✅ **Testabilidad**: Puedes mockear puertos fácilmente

✅ **Escalabilidad**: Agregar nuevas funcionalidades sin romper lo existente

✅ **Independencia de Frameworks**: Cambiar Spring por otro framework sin afectar dominio

✅ **Separación de Responsabilidades**: Cada capa tiene un propósito claro

✅ **Inversión de Dependencias**: El dominio no depende de detalles técnicos

## Tecnologías por Capa

| Capa            | Tecnologías                          |
|-----------------|--------------------------------------|
| Dominio         | Java puro, Lombok, JPA annotations   |
| Aplicación      | Spring @Service                      |
| Infraestructura | Spring MVC, Spring Data JPA, JJWT    |
| Persistencia    | PostgreSQL, Hibernate                |
| Seguridad       | Spring Security, BCrypt, JWT         |

## Patrones Utilizados

- **Hexagonal Architecture** (Ports & Adapters)
- **Dependency Injection** (Spring IoC)
- **Repository Pattern** (Spring Data JPA)
- **DTO Pattern** (Separación de entidades y transferencia)
- **Service Layer Pattern** (Casos de uso)
- **Exception Handling** (Global con @ControllerAdvice)
