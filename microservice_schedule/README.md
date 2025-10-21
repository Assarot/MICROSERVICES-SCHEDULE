# MS-SCHEDULE Microservice

Microservicio para gestión de horarios académicos desarrollado con arquitectura hexagonal.

## Tecnologías

- **Java 17**
- **Spring Boot 3.5.6**
- **PostgreSQL**
- **Maven**
- **Lombok**

## Arquitectura Hexagonal

El proyecto está organizado en las siguientes capas:

```
src/main/java/pe/edu/upeu/microservice_schedule/
├── domain/                          # Capa de dominio (núcleo)
│   ├── model/                       # Entidades de dominio
│   └── port/                        # Interfaces (puertos)
│       ├── in/                      # Casos de uso (input)
│       └── out/                     # Repositorios (output)
├── application/                     # Capa de aplicación
│   └── service/                     # Implementación de casos de uso
└── infrastructure/                  # Capa de infraestructura
    └── adapter/
        ├── in/                      # Adaptadores de entrada
        │   └── rest/                # Controladores REST
        │       ├── dto/             # DTOs de entrada/salida
        │       ├── mapper/          # Mapeadores DTO-Domain
        │       └── exception/       # Manejo de excepciones
        └── out/                     # Adaptadores de salida
            └── persistence/         # Persistencia JPA
                ├── entity/          # Entidades JPA
                ├── repository/      # Repositorios JPA
                └── mapper/          # Mapeadores Entity-Domain
```

## Entidades

### 1. TYPE_ASSIGNMENT (Tipo de Asignación)
- `id_schedule` (PK)
- `name` (UNIQUE)
- `is_active`

### 2. TYPE_HOUR (Tipo de Hora)
- `id_type_hour` (PK)
- `type_hour` (UNIQUE)

### 3. WEEK_DAY (Día de la Semana)
- `id_schedule` (PK)
- `name` (UNIQUE)
- `is_active`

### 4. SCHEDULE (Horario)
- `id_schedule` (PK)
- `start_time`
- `end_time`
- `duration`
- `id_type_schedule` (FK)
- `id_academic_space` (FK)
- `id_course_assignment` (FK)
- `id_week_name` (FK)

### 5. SCHEDULE_ASSIGNMENT (Asignación de Horario)
- `id_schedule_assignment` (PK)
- `assignment_date`
- `id_schedule` (FK)
- `id_type_assignment` (FK)
- `id_user_profile` (FK)
- `is_active`

## Configuración

### Base de Datos

1. Crear la base de datos en PostgreSQL:
```sql
CREATE DATABASE ms_schedule_db;
```

2. Configurar las credenciales en `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/ms_schedule_db
spring.datasource.username=postgres
spring.datasource.password=postgres
```

## Endpoints API

### Type Assignment
- `POST /api/v1/type-assignments` - Crear tipo de asignación
- `GET /api/v1/type-assignments` - Listar todos
- `GET /api/v1/type-assignments/{id}` - Obtener por ID
- `PUT /api/v1/type-assignments/{id}` - Actualizar
- `DELETE /api/v1/type-assignments/{id}` - Eliminar

### Type Hour
- `POST /api/v1/type-hours` - Crear tipo de hora
- `GET /api/v1/type-hours` - Listar todos
- `GET /api/v1/type-hours/{id}` - Obtener por ID
- `PUT /api/v1/type-hours/{id}` - Actualizar
- `DELETE /api/v1/type-hours/{id}` - Eliminar

### Week Day
- `POST /api/v1/week-days` - Crear día de la semana
- `GET /api/v1/week-days` - Listar todos
- `GET /api/v1/week-days/{id}` - Obtener por ID
- `PUT /api/v1/week-days/{id}` - Actualizar
- `DELETE /api/v1/week-days/{id}` - Eliminar

### Schedule
- `POST /api/v1/schedules` - Crear horario
- `GET /api/v1/schedules` - Listar todos
- `GET /api/v1/schedules/{id}` - Obtener por ID
- `PUT /api/v1/schedules/{id}` - Actualizar
- `DELETE /api/v1/schedules/{id}` - Eliminar

### Schedule Assignment
- `POST /api/v1/schedule-assignments` - Crear asignación de horario
- `GET /api/v1/schedule-assignments` - Listar todos
- `GET /api/v1/schedule-assignments/{id}` - Obtener por ID
- `PUT /api/v1/schedule-assignments/{id}` - Actualizar
- `DELETE /api/v1/schedule-assignments/{id}` - Eliminar

## Ejemplos de Uso

### Crear Type Hour
```bash
curl -X POST http://localhost:8081/api/v1/type-hours \
  -H "Content-Type: application/json" \
  -d '{"typeHour": "Teórica"}'
```

### Crear Schedule
```bash
curl -X POST http://localhost:8081/api/v1/schedules \
  -H "Content-Type: application/json" \
  -d '{
    "startTime": "08:00:00",
    "endTime": "10:00:00",
    "duration": 120,
    "idTypeSchedule": 1,
    "idAcademicSpace": 1,
    "idCourseAssignment": 1,
    "idWeekName": 1
  }'
```

### Crear Schedule Assignment
```bash
curl -X POST http://localhost:8081/api/v1/schedule-assignments \
  -H "Content-Type: application/json" \
  -d '{
    "assignmentDate": "2024-10-15",
    "idSchedule": 1,
    "idTypeAssignment": 1,
    "idUserProfile": 1,
    "isActive": true
  }'
```

## Ejecución

### Con Maven
```bash
./mvnw spring-boot:run
```

### Con Java
```bash
./mvnw clean package
java -jar target/microservice_schedule-0.0.1-SNAPSHOT.jar
```

El servidor se iniciará en el puerto **8081**.

## Validaciones Implementadas

- Campos obligatorios con `@NotNull` y `@NotBlank`
- Unicidad en campos `name` de TypeAssignment, TypeHour y WeekDay
- Validación de formato de fechas y horas
- Manejo global de excepciones con respuestas estructuradas

## Características

✅ Arquitectura hexagonal (puertos y adaptadores)  
✅ Separación de responsabilidades (Domain, Application, Infrastructure)  
✅ Operaciones CRUD completas para todas las entidades  
✅ Validaciones de entrada con Jakarta Validation  
✅ Manejo centralizado de excepciones  
✅ DTOs para transferencia de datos  
✅ Mappers para conversión entre capas  
✅ Transaccionalidad con Spring  
✅ Logs configurados  
✅ PostgreSQL con JPA/Hibernate  

## Autor

Desarrollado para la Universidad Peruana Unión (UPeU)
