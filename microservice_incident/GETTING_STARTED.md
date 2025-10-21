# 🚀 Guía de Inicio Rápido - MS-INCIDENT

## Paso 1: Configurar Cloudinary

1. Ve a [Cloudinary](https://cloudinary.com/) y crea una cuenta gratuita
2. Copia tus credenciales del Dashboard
3. Crea un archivo `.env` en la raíz del proyecto:

```env
CLOUDINARY_CLOUD_NAME=tu_cloud_name
CLOUDINARY_API_KEY=tu_api_key
CLOUDINARY_API_SECRET=tu_api_secret
```

## Paso 2: Iniciar PostgreSQL

```bash
docker-compose up -d postgres
```

## Paso 3: Ejecutar la aplicación

```bash
./mvnw spring-boot:run
```

O si estás en Windows:
```bash
mvnw.cmd spring-boot:run
```

## Paso 4: Probar los endpoints

### 4.1 Crear una Severidad
```bash
curl -X POST http://localhost:8080/api/v1/severities \
  -H "Content-Type: application/json" \
  -d '{"name": "High", "isActive": true}'
```

### 4.2 Crear un Estado
```bash
curl -X POST http://localhost:8080/api/v1/statuses \
  -H "Content-Type: application/json" \
  -d '{"name": "Open", "isActive": true}'
```

### 4.3 Crear un Incidente
```bash
curl -X POST http://localhost:8080/api/v1/incidents \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Computer not working",
    "description": "The computer in lab 301 is not turning on",
    "reportDate": "2024-10-15T10:30:00",
    "idReportedBy": 1,
    "idAcademicSpace": 1,
    "idResource": 1,
    "idSeverity": 1,
    "idStatus": 1
  }'
```

### 4.4 Subir una imagen (usando Postman)
1. Abre Postman
2. Importa `MS-INCIDENT.postman_collection.json`
3. Ejecuta "Upload Attachment"
4. En el body, selecciona form-data:
   - `incidentId`: 1
   - `file`: Selecciona una imagen de tu computadora

## Paso 5: Verificar la aplicación

Accede a: `http://localhost:8080/api/v1/severities`

Deberías ver la lista de severidades creadas.

## 🐳 Ejecutar con Docker (Opcional)

Para ejecutar toda la aplicación con Docker:

```bash
docker-compose up -d
```

Esto iniciará tanto PostgreSQL como la aplicación.

## 📌 Endpoints principales

- **Severities**: `http://localhost:8080/api/v1/severities`
- **Statuses**: `http://localhost:8080/api/v1/statuses`
- **Incidents**: `http://localhost:8080/api/v1/incidents`
- **Attachments**: `http://localhost:8080/api/v1/attachments`

## ❓ Solución de problemas

### Error: "Could not connect to database"
- Verifica que PostgreSQL esté corriendo: `docker ps`
- Revisa las credenciales en `application.yml`

### Error: "Cloudinary configuration error"
- Verifica que las variables de entorno estén configuradas
- En Windows, configura las variables en el sistema o en `application.yml`

### Error: "Port 8080 already in use"
- Cambia el puerto en `application.yml`: `server.port: 8081`
