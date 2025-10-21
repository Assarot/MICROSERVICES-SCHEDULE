# MS-INCIDENT - Microservicio de Gestión de Incidentes

Microservicio desarrollado con **Arquitectura Hexagonal** para la gestión de incidentes, utilizando **Spring Boot 3**, **Java 17**, **PostgreSQL** y **Cloudinary** para almacenamiento de imágenes.

## 🏗️ Arquitectura

El proyecto sigue el patrón de **Arquitectura Hexagonal** (Ports & Adapters):

```
src/main/java/pe/edu/upeu/microservice_incident/
├── domain/                     # Capa de Dominio
│   ├── model/                 # Entidades del dominio
│   │   ├── Incident.java
│   │   ├── IncidentAttachment.java
│   │   ├── Severity.java
│   │   └── Status.java
│   └── port/                  # Interfaces de repositorio (Ports)
│       ├── IncidentRepository.java
│       ├── IncidentAttachmentRepository.java
│       ├── SeverityRepository.java
│       └── StatusRepository.java
│
├── application/               # Capa de Aplicación
│   ├── dto/                  # Data Transfer Objects
│   │   ├── IncidentDTO.java
│   │   ├── IncidentAttachmentDTO.java
│   │   ├── SeverityDTO.java
│   │   └── StatusDTO.java
│   └── service/              # Casos de uso
│       ├── IncidentService.java
│       ├── IncidentAttachmentService.java
│       ├── SeverityService.java
│       └── StatusService.java
│
└── infrastructure/            # Capa de Infraestructura
    ├── adapter/              # Adaptadores de repositorio
    │   ├── IncidentRepositoryAdapter.java
    │   ├── IncidentAttachmentRepositoryAdapter.java
    │   ├── SeverityRepositoryAdapter.java
    │   └── StatusRepositoryAdapter.java
    ├── controller/           # Controladores REST
    │   ├── IncidentController.java
    │   ├── IncidentAttachmentController.java
    │   ├── SeverityController.java
    │   └── StatusController.java
    ├── repository/jpa/       # Repositorios JPA
    │   ├── IncidentJpaRepository.java
    │   ├── IncidentAttachmentJpaRepository.java
    │   ├── SeverityJpaRepository.java
    │   └── StatusJpaRepository.java
    ├── config/               # Configuraciones
    │   ├── CloudinaryConfig.java
    │   └── ModelMapperConfig.java
    └── service/              # Servicios de infraestructura
        └── CloudinaryService.java
```

## 📋 Entidades

### Severity
- `id_severity` (PK)
- `name` (unique)
- `is_active`

### Status
- `id_status` (PK)
- `name` (unique)
- `is_active`

### Incident
- `id_incident` (PK)
- `title`
- `description`
- `report_date`
- `resolution_date`
- `id_reported_by`
- `id_resolved_by`
- `id_academic_space`
- `id_resource`
- `id_severity` (FK)
- `id_status` (FK)

### IncidentAttachment
- `id_incident_attachment` (PK)
- `photo_url`
- `uploaded_at`
- `id_incident` (FK)

## 🚀 Configuración

### 1. Variables de entorno de Cloudinary

Crea un archivo `.env` en la raíz del proyecto basado en `.env.example`:

```env
CLOUDINARY_CLOUD_NAME=tu_cloud_name
CLOUDINARY_API_KEY=tu_api_key
CLOUDINARY_API_SECRET=tu_api_secret
```

### 2. Iniciar la base de datos con Docker

```bash
docker-compose up -d postgres
```

### 3. Ejecutar la aplicación

#### Opción 1: Con Maven
```bash
./mvnw spring-boot:run
```

#### Opción 2: Con Docker Compose (aplicación completa)
```bash
docker-compose up -d
```

## 📡 Endpoints API

### Severity
- `POST /api/v1/severities` - Crear severidad
- `GET /api/v1/severities` - Listar todas las severidades
- `GET /api/v1/severities/{id}` - Obtener severidad por ID
- `PUT /api/v1/severities/{id}` - Actualizar severidad
- `DELETE /api/v1/severities/{id}` - Eliminar severidad

### Status
- `POST /api/v1/statuses` - Crear estado
- `GET /api/v1/statuses` - Listar todos los estados
- `GET /api/v1/statuses/{id}` - Obtener estado por ID
- `PUT /api/v1/statuses/{id}` - Actualizar estado
- `DELETE /api/v1/statuses/{id}` - Eliminar estado

### Incident
- `POST /api/v1/incidents` - Crear incidente
- `GET /api/v1/incidents` - Listar todos los incidentes
- `GET /api/v1/incidents/{id}` - Obtener incidente por ID
- `PUT /api/v1/incidents/{id}` - Actualizar incidente
- `DELETE /api/v1/incidents/{id}` - Eliminar incidente

### Incident Attachment
- `POST /api/v1/attachments` - Subir archivo (form-data: incidentId, file)
- `GET /api/v1/attachments` - Listar todos los adjuntos
- `GET /api/v1/attachments/{id}` - Obtener adjunto por ID
- `GET /api/v1/attachments/incident/{incidentId}` - Obtener adjuntos por ID de incidente
- `DELETE /api/v1/attachments/{id}` - Eliminar adjunto

## 📮 Postman Collection

Importa el archivo `MS-INCIDENT.postman_collection.json` en Postman para probar todos los endpoints.

## 🛠️ Tecnologías

- **Java 17**
- **Spring Boot 3.5.6**
- **Spring Data JPA**
- **PostgreSQL 15**
- **Cloudinary** (almacenamiento de imágenes)
- **Lombok**
- **ModelMapper**
- **Maven**
- **Docker & Docker Compose**

## ✅ Validaciones

- Unicidad en nombres de `Severity` y `Status`
- Validación de campos obligatorios con Bean Validation
- Validación de existencia de relaciones FK antes de crear/actualizar

## 📝 Notas

- Las imágenes se suben automáticamente a Cloudinary y retornan la URL
- La aplicación usa `ddl-auto: update` para crear/actualizar las tablas automáticamente
- El tamaño máximo de archivo es de 10MB
- Los endpoints retornan códigos HTTP apropiados (201, 200, 204, 404, etc.)

## 📄 Licencia

Este proyecto es de uso académico para la Universidad.
