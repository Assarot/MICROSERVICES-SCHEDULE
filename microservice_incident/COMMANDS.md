# 🛠️ Comandos Útiles

## 📦 Maven

### Compilar el proyecto
```bash
./mvnw clean install
```

### Ejecutar la aplicación
```bash
./mvnw spring-boot:run
```

### Compilar sin tests
```bash
./mvnw clean install -DskipTests
```

### Crear JAR ejecutable
```bash
./mvnw clean package
java -jar target/microservice_incident-0.0.1-SNAPSHOT.jar
```

## 🐳 Docker

### Iniciar solo PostgreSQL
```bash
docker-compose up -d postgres
```

### Iniciar toda la aplicación
```bash
docker-compose up -d
```

### Ver logs
```bash
# Ver logs de todos los servicios
docker-compose logs -f

# Ver logs solo de la app
docker-compose logs -f app

# Ver logs solo de PostgreSQL
docker-compose logs -f postgres
```

### Detener servicios
```bash
# Detener todo
docker-compose down

# Detener y eliminar volúmenes (⚠️ elimina datos)
docker-compose down -v
```

### Reconstruir y reiniciar
```bash
# Reconstruir imagen y reiniciar
docker-compose up -d --build

# Forzar recreación de contenedores
docker-compose up -d --force-recreate
```

### Ver estado de contenedores
```bash
docker-compose ps
```

### Acceder al contenedor de PostgreSQL
```bash
docker exec -it incident_postgres psql -U postgres -d incident_db
```

### Ejecutar comandos SQL directamente
```bash
# Listar tablas
docker exec -it incident_postgres psql -U postgres -d incident_db -c "\dt"

# Contar registros
docker exec -it incident_postgres psql -U postgres -d incident_db -c "SELECT COUNT(*) FROM severity;"
```

## 🗄️ PostgreSQL (dentro del contenedor)

Una vez dentro del contenedor con `docker exec -it incident_postgres psql -U postgres -d incident_db`:

```sql
-- Listar tablas
\dt

-- Describir tabla
\d incident

-- Ver datos
SELECT * FROM severity;
SELECT * FROM status;
SELECT * FROM incident;
SELECT * FROM incident_attachment;

-- Contar registros
SELECT COUNT(*) FROM incident;

-- Ver incidentes con sus severidades y estados
SELECT 
    i.id_incident,
    i.title,
    s.name AS severity,
    st.name AS status
FROM incident i
JOIN severity s ON i.id_severity = s.id_severity
JOIN status st ON i.id_status = st.id_status;

-- Limpiar todas las tablas (⚠️ cuidado)
TRUNCATE TABLE incident_attachment CASCADE;
TRUNCATE TABLE incident CASCADE;
TRUNCATE TABLE severity CASCADE;
TRUNCATE TABLE status CASCADE;

-- Salir
\q
```

## 🧪 Testing con cURL

### Severity

#### Crear Severity
```bash
curl -X POST http://localhost:8080/api/v1/severities \
  -H "Content-Type: application/json" \
  -d '{"name": "Critical", "isActive": true}'
```

#### Listar Severities
```bash
curl http://localhost:8080/api/v1/severities
```

#### Obtener Severity por ID
```bash
curl http://localhost:8080/api/v1/severities/1
```

#### Actualizar Severity
```bash
curl -X PUT http://localhost:8080/api/v1/severities/1 \
  -H "Content-Type: application/json" \
  -d '{"name": "Very Critical", "isActive": true}'
```

#### Eliminar Severity
```bash
curl -X DELETE http://localhost:8080/api/v1/severities/1
```

### Status

#### Crear Status
```bash
curl -X POST http://localhost:8080/api/v1/statuses \
  -H "Content-Type: application/json" \
  -d '{"name": "Open", "isActive": true}'
```

#### Listar Statuses
```bash
curl http://localhost:8080/api/v1/statuses
```

### Incident

#### Crear Incident
```bash
curl -X POST http://localhost:8080/api/v1/incidents \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Computer malfunction",
    "description": "Computer in lab 301 not working",
    "reportDate": "2024-10-15T10:30:00",
    "idReportedBy": 1,
    "idAcademicSpace": 1,
    "idResource": 1,
    "idSeverity": 1,
    "idStatus": 1
  }'
```

#### Listar Incidents
```bash
curl http://localhost:8080/api/v1/incidents
```

#### Actualizar Incident
```bash
curl -X PUT http://localhost:8080/api/v1/incidents/1 \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Computer fixed",
    "description": "Computer in lab 301 has been repaired",
    "reportDate": "2024-10-15T10:30:00",
    "resolutionDate": "2024-10-15T15:30:00",
    "idReportedBy": 1,
    "idResolvedBy": 2,
    "idAcademicSpace": 1,
    "idResource": 1,
    "idSeverity": 1,
    "idStatus": 2
  }'
```

### Attachment

#### Subir imagen
```bash
curl -X POST http://localhost:8080/api/v1/attachments \
  -F "incidentId=1" \
  -F "file=@/path/to/image.jpg"
```

#### Listar attachments de un incidente
```bash
curl http://localhost:8080/api/v1/attachments/incident/1
```

## 🔍 Debugging

### Ver información de la aplicación Spring Boot

#### Verificar que la app está corriendo
```bash
curl http://localhost:8080/actuator/health
```

### Ver logs en tiempo real

#### Con Maven
```bash
./mvnw spring-boot:run | tee application.log
```

#### Con Docker
```bash
docker-compose logs -f app
```

## 🧹 Limpieza

### Limpiar compilación Maven
```bash
./mvnw clean
```

### Eliminar target directory
```bash
rm -rf target/
```

### Limpiar Docker
```bash
# Detener y eliminar contenedores
docker-compose down

# Eliminar imágenes
docker rmi microservice_incident-app

# Eliminar volúmenes (⚠️ elimina datos de BD)
docker volume rm microservice_incident_postgres_data

# Limpieza completa de Docker
docker system prune -a --volumes
```

## 📊 Monitoreo

### Ver uso de recursos de Docker
```bash
docker stats
```

### Ver espacio usado por Docker
```bash
docker system df
```

## 🔄 Reinicio Completo

Para reiniciar todo desde cero:

```bash
# 1. Detener todo
docker-compose down -v

# 2. Limpiar compilación
./mvnw clean

# 3. Recompilar
./mvnw clean install

# 4. Reiniciar Docker
docker-compose up -d --build

# 5. Ver logs
docker-compose logs -f
```

## ⚙️ Variables de Entorno

### Setear variables para la sesión actual

**Windows (PowerShell):**
```powershell
$env:CLOUDINARY_CLOUD_NAME="tu_cloud_name"
$env:CLOUDINARY_API_KEY="tu_api_key"
$env:CLOUDINARY_API_SECRET="tu_api_secret"
```

**Linux/Mac:**
```bash
export CLOUDINARY_CLOUD_NAME="tu_cloud_name"
export CLOUDINARY_API_KEY="tu_api_key"
export CLOUDINARY_API_SECRET="tu_api_secret"
```

### Verificar variables
```bash
# Windows
echo $env:CLOUDINARY_CLOUD_NAME

# Linux/Mac
echo $CLOUDINARY_CLOUD_NAME
```

---

**💡 Tip**: Guarda estos comandos en un archivo `.sh` o `.ps1` para reutilizarlos fácilmente.
