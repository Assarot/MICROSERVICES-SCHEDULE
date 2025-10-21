# MS-INVENTORY - Microservicio de Inventario

Microservicio de gestión de inventario desarrollado con arquitectura hexagonal, Spring Boot 3.5.6 y Java 17.

## 📋 Tabla de Contenidos

- [Características](#características)
- [Arquitectura](#arquitectura)
- [Tecnologías](#tecnologías)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Requisitos Previos](#requisitos-previos)
- [Configuración](#configuración)
- [Instalación y Ejecución](#instalación-y-ejecución)
- [API Endpoints](#api-endpoints)
- [Base de Datos](#base-de-datos)
- [Postman Collection](#postman-collection)

## 🚀 Características

- **Arquitectura Hexagonal (Ports & Adapters)**: Separación clara entre dominio, aplicación e infraestructura
- **CRUD completo** para todas las entidades:
  - State (Estados)
  - CategoryResource (Categorías de Recursos)
  - ResourceType (Tipos de Recursos)
  - Resource (Recursos con imágenes)
  - ResourceAssignment (Asignaciones de Recursos)
- **Integración con Cloudinary** para almacenamiento de imágenes
- **PostgreSQL** como base de datos
- **Spring Boot 3.5.6** con Java 17
- **Docker Compose** para facilitar el despliegue
- **Validación de datos** con Bean Validation
- **Manejo global de excepciones**

## 🏗️ Arquitectura

El proyecto sigue la arquitectura hexagonal (Clean Architecture) con las siguientes capas:

```
src/main/java/pe/edu/upeu/microservice_inventory/
├── domain/                          # Capa de Dominio
│   ├── model/                       # Modelos de dominio
│   └── port/                        # Puertos (interfaces)
│       ├── in/                      # Puertos de entrada (casos de uso)
│       └── out/                     # Puertos de salida (repositorios)
├── application/                     # Capa de Aplicación
│   └── service/                     # Servicios (implementación de casos de uso)
└── infrastructure/                  # Capa de Infraestructura
    ├── adapter/
    │   ├── in/
    │   │   └── web/                 # Adaptadores REST (Controllers)
    │   │       ├── controller/
    │   │       ├── dto/
    │   │       ├── mapper/
    │   │       └── exception/
    │   └── out/
    │       ├── persistence/         # Adaptadores de persistencia (JPA)
    │       │   ├── entity/
    │       │   ├── repository/
    │       │   └── mapper/
    │       └── cloudinary/          # Adaptador de Cloudinary
    └── config/                      # Configuraciones
```

## 🛠️ Tecnologías

- **Java 17**
- **Spring Boot 3.5.6**
- **Spring Data JPA**
- **PostgreSQL 15**
- **Cloudinary** (almacenamiento de imágenes)
- **Lombok**
- **ModelMapper**
- **Docker & Docker Compose**
- **Maven**

## 📁 Estructura del Proyecto

```
microservice_inventory/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── pe/edu/upeu/microservice_inventory/
│   │   └── resources/
│   │       ├── application.yml
│   │       └── application.properties
│   └── test/
├── docker-compose.yml
├── .env.example
├── MS-INVENTORY.postman_collection.json
├── pom.xml
└── README.md
```

## ✅ Requisitos Previos

- Java 17 o superior
- Maven 3.6+
- Docker y Docker Compose
- Cuenta de Cloudinary (para almacenamiento de imágenes)

## ⚙️ Configuración

### 1. Configurar Variables de Entorno

Copiar el archivo `.env.example` a `.env` y configurar las variables:

```bash
cp .env.example .env
```

### 2. Configurar Cloudinary

1. Crear una cuenta en [Cloudinary](https://cloudinary.com/)
2. Obtener las credenciales (Cloud Name, API Key, API Secret)
3. Actualizar en `application.yml` o como variables de entorno:

```yaml
cloudinary:
  cloud-name: ${CLOUDINARY_CLOUD_NAME:your_cloud_name}
  api-key: ${CLOUDINARY_API_KEY:your_api_key}
  api-secret: ${CLOUDINARY_API_SECRET:your_api_secret}
```

## 🚀 Instalación y Ejecución

### Opción 1: Con Docker Compose

1. **Iniciar la base de datos:**

```bash
docker-compose up -d
```

Esto iniciará:
- PostgreSQL en el puerto 5432
- pgAdmin en el puerto 5050

2. **Compilar el proyecto:**

```bash
mvn clean install
```

3. **Ejecutar la aplicación:**

```bash
mvn spring-boot:run
```

### Opción 2: Ejecución Manual

1. **Asegurarse de tener PostgreSQL corriendo en el puerto 5432**

2. **Crear la base de datos:**

```sql
CREATE DATABASE inventory_db;
```

3. **Configurar las credenciales en `application.yml`**

4. **Ejecutar la aplicación:**

```bash
mvn spring-boot:run
```

La aplicación estará disponible en: `http://localhost:8080/api/v1`

## 📡 API Endpoints

### Base URL
```
http://localhost:8080/api/v1
```

### States (Estados)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/states` | Crear estado |
| GET | `/states` | Obtener todos los estados |
| GET | `/states/{id}` | Obtener estado por ID |
| GET | `/states/active` | Obtener estados activos |
| PUT | `/states/{id}` | Actualizar estado |
| DELETE | `/states/{id}` | Eliminar estado |

### Category Resources (Categorías de Recursos)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/category-resources` | Crear categoría |
| GET | `/category-resources` | Obtener todas las categorías |
| GET | `/category-resources/{id}` | Obtener categoría por ID |
| GET | `/category-resources/active` | Obtener categorías activas |
| PUT | `/category-resources/{id}` | Actualizar categoría |
| DELETE | `/category-resources/{id}` | Eliminar categoría |

### Resource Types (Tipos de Recursos)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/resource-types` | Crear tipo de recurso |
| GET | `/resource-types` | Obtener todos los tipos |
| GET | `/resource-types/{id}` | Obtener tipo por ID |
| GET | `/resource-types/active` | Obtener tipos activos |
| GET | `/resource-types/category/{categoryId}` | Obtener tipos por categoría |
| PUT | `/resource-types/{id}` | Actualizar tipo |
| DELETE | `/resource-types/{id}` | Eliminar tipo |

### Resources (Recursos)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/resources` | Crear recurso (con foto opcional) |
| GET | `/resources` | Obtener todos los recursos |
| GET | `/resources/{id}` | Obtener recurso por ID |
| GET | `/resources/type/{typeId}` | Obtener recursos por tipo |
| GET | `/resources/state/{stateId}` | Obtener recursos por estado |
| PUT | `/resources/{id}` | Actualizar recurso (con foto opcional) |
| DELETE | `/resources/{id}` | Eliminar recurso |

**Nota:** Para crear/actualizar recursos con foto, usar `multipart/form-data`:
- `resource`: JSON con los datos del recurso
- `photo`: Archivo de imagen (opcional)

### Resource Assignments (Asignaciones)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/resource-assignments` | Crear asignación |
| GET | `/resource-assignments` | Obtener todas las asignaciones |
| GET | `/resource-assignments/{id}` | Obtener asignación por ID |
| GET | `/resource-assignments/resource/{resourceId}` | Obtener asignaciones por recurso |
| GET | `/resource-assignments/academic-space/{spaceId}` | Obtener asignaciones por espacio académico |
| PUT | `/resource-assignments/{id}` | Actualizar asignación |
| DELETE | `/resource-assignments/{id}` | Eliminar asignación |

## 🗄️ Base de Datos

### Diagrama de Entidades

```
┌──────────────────┐
│     STATE        │
├──────────────────┤
│ id_state (PK)    │
│ name             │
│ is_active        │
└──────────────────┘

┌────────────────────────┐
│   CATEGORY_RESOURCE    │
├────────────────────────┤
│ id_category_resource(PK)│
│ name                   │
│ is_active              │
└────────────────────────┘
         │
         │ 1:N
         ▼
┌────────────────────────┐
│    RESOURCE_TYPE       │
├────────────────────────┤
│ id_resource_type (PK)  │
│ name                   │
│ is_active              │
│ id_category_resource_FK│
└────────────────────────┘
         │
         │ 1:N
         ▼
┌────────────────────────┐         ┌──────────────────────────┐
│      RESOURCE          │◄────────│  RESOURCE_ASSIGNMENT     │
├────────────────────────┤   1:N   ├──────────────────────────┤
│ id_resource (PK)       │         │ id_resource_assignment(PK)│
│ code                   │         │ id_academic_space_FK     │
│ stock                  │         │ id_resource_FK           │
│ resource_photo_url     │         └──────────────────────────┘
│ observation            │
│ id_resource_type_FK    │
│ id_state_FK            │
└────────────────────────┘
         ▲
         │ N:1
         │
┌──────────────────┘
```

### Acceso a pgAdmin

URL: `http://localhost:5050`
- Email: `admin@inventory.com`
- Password: `admin`

**Configurar conexión a PostgreSQL en pgAdmin:**
- Host: `postgres`
- Port: `5432`
- Database: `inventory_db`
- Username: `postgres`
- Password: `postgres`

## 📮 Postman Collection

El proyecto incluye una colección completa de Postman con todos los endpoints.

**Archivo:** `MS-INVENTORY.postman_collection.json`

### Importar en Postman:

1. Abrir Postman
2. Click en "Import"
3. Seleccionar el archivo `MS-INVENTORY.postman_collection.json`
4. La colección incluye ejemplos para todos los endpoints

### Variables de Entorno en Postman:

- `baseUrl`: `http://localhost:8080/api/v1`

## 📝 Ejemplos de Uso

### Crear un Estado

```bash
curl -X POST http://localhost:8080/api/v1/states \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Available",
    "isActive": true
  }'
```

### Crear un Recurso con Imagen

```bash
curl -X POST http://localhost:8080/api/v1/resources \
  -F 'resource={"code":"LAP001","stock":10,"observation":"Laptop Dell","idResourceType":1,"idState":1}' \
  -F 'photo=@/path/to/image.jpg'
```

### Obtener todos los Recursos

```bash
curl -X GET http://localhost:8080/api/v1/resources
```

## 🧪 Testing

Para ejecutar las pruebas:

```bash
mvn test
```

## 📦 Build para Producción

```bash
mvn clean package -DskipTests
```

El archivo JAR se generará en `target/microservice_inventory-0.0.1-SNAPSHOT.jar`

## 🐳 Deploy con Docker

Para crear una imagen Docker de la aplicación:

```bash
# Construir la imagen
docker build -t ms-inventory:latest .

# Ejecutar el contenedor
docker run -p 8080:8080 \
  -e CLOUDINARY_CLOUD_NAME=your_cloud_name \
  -e CLOUDINARY_API_KEY=your_api_key \
  -e CLOUDINARY_API_SECRET=your_api_secret \
  ms-inventory:latest
```

## 🛡️ Seguridad

- Las credenciales sensibles deben configurarse como variables de entorno
- No commitear archivos `.env` con datos reales
- Usar HTTPS en producción
- Implementar autenticación y autorización según sea necesario

## 📄 Licencia

Este proyecto fue desarrollado con fines académicos.

## 👥 Autor

**Universidad Peruana Unión**
- Facultad de Ingeniería

## 📧 Soporte

Para preguntas o soporte, contactar al equipo de desarrollo.

---

**Última actualización:** Octubre 2024
