# Guía de Uso de Postman para MS-SCHEDULE

## Importar la Colección

### Opción 1: Importar desde archivo

1. Abre Postman
2. Click en **Import** (esquina superior izquierda)
3. Selecciona **File**
4. Navega a la carpeta `postman/` del proyecto
5. Selecciona `MS-SCHEDULE.postman_collection.json`
6. Click en **Import**

### Opción 2: Importar el entorno

1. Click en **Import**
2. Selecciona `MS-SCHEDULE.postman_environment.json`
3. Click en **Import**
4. En la esquina superior derecha, selecciona el entorno **MS-SCHEDULE Local**

## Estructura de la Colección

La colección está organizada en 5 carpetas principales:

### 1. **Type Hour** (Tipo de Hora)
- ✅ Create Type Hour
- ✅ Get All Type Hours
- ✅ Get Type Hour by ID
- ✅ Update Type Hour
- ✅ Delete Type Hour

### 2. **Type Assignment** (Tipo de Asignación)
- ✅ Create Type Assignment
- ✅ Get All Type Assignments
- ✅ Get Type Assignment by ID
- ✅ Update Type Assignment
- ✅ Delete Type Assignment

### 3. **Week Day** (Día de la Semana)
- ✅ Create Week Day
- ✅ Get All Week Days
- ✅ Get Week Day by ID
- ✅ Update Week Day
- ✅ Delete Week Day

### 4. **Schedule** (Horario)
- ✅ Create Schedule
- ✅ Get All Schedules
- ✅ Get Schedule by ID
- ✅ Update Schedule
- ✅ Delete Schedule

### 5. **Schedule Assignment** (Asignación de Horario)
- ✅ Create Schedule Assignment
- ✅ Get All Schedule Assignments
- ✅ Get Schedule Assignment by ID
- ✅ Update Schedule Assignment
- ✅ Delete Schedule Assignment

## Flujo de Prueba Recomendado

### Paso 1: Crear datos básicos

#### 1.1. Crear Type Hour
```json
POST /api/v1/type-hours
{
  "typeHour": "Teórica"
}
```

#### 1.2. Crear Type Assignment
```json
POST /api/v1/type-assignments
{
  "name": "Presencial",
  "isActive": true
}
```

#### 1.3. Crear Week Day
```json
POST /api/v1/week-days
{
  "name": "Lunes",
  "isActive": true
}
```

### Paso 2: Crear Schedule

```json
POST /api/v1/schedules
{
  "startTime": "08:00:00",
  "endTime": "10:00:00",
  "duration": 120,
  "idTypeSchedule": 1,
  "idAcademicSpace": 1,
  "idCourseAssignment": 1,
  "idWeekName": 1
}
```

### Paso 3: Crear Schedule Assignment

```json
POST /api/v1/schedule-assignments
{
  "assignmentDate": "2024-10-15",
  "idSchedule": 1,
  "idTypeAssignment": 1,
  "idUserProfile": 1,
  "isActive": true
}
```

## Variables de Entorno

La colección utiliza la variable `{{base_url}}` que está configurada en:

- **Desarrollo Local**: `http://localhost:8081`
- **Docker**: `http://localhost:8081`

Puedes crear entornos adicionales para diferentes ambientes:

- **Desarrollo**: `http://localhost:8081`
- **Testing**: `http://test-server:8081`
- **Producción**: `https://api.production.com`

## Ejemplos de Respuestas

### Respuesta Exitosa (201 Created)

```json
{
  "idTypeHour": 1,
  "typeHour": "Teórica"
}
```

### Respuesta de Lista (200 OK)

```json
[
  {
    "idTypeHour": 1,
    "typeHour": "Teórica"
  },
  {
    "idTypeHour": 2,
    "typeHour": "Práctica"
  }
]
```

### Error de Validación (400 Bad Request)

```json
{
  "timestamp": "2024-10-15T16:30:00",
  "status": 400,
  "error": "Validation Error",
  "message": "Invalid input parameters",
  "validationErrors": {
    "name": "Name is required",
    "isActive": "isActive is required"
  }
}
```

### Recurso No Encontrado (404 Not Found)

```json
{
  "timestamp": "2024-10-15T16:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Resource not found"
}
```

## Tips de Uso

### 1. Usar Tests en Postman

Puedes agregar tests para validar respuestas automáticamente:

```javascript
// Test que el status sea 201
pm.test("Status code is 201", function () {
    pm.response.to.have.status(201);
});

// Guardar el ID en variable de entorno
var jsonData = pm.response.json();
pm.environment.set("type_hour_id", jsonData.idTypeHour);
```

### 2. Ejecutar toda la colección

1. Click derecho en la colección **MS-SCHEDULE API**
2. Click en **Run collection**
3. Selecciona las carpetas que deseas ejecutar
4. Click en **Run MS-SCHEDULE API**

### 3. Exportar resultados

Después de ejecutar la colección:
1. Click en **Export Results**
2. Selecciona el formato (JSON, PDF, etc.)
3. Guarda el archivo

## Troubleshooting

### Error: "Could not get any response"

- ✅ Verifica que el microservicio esté corriendo: `curl http://localhost:8081/api/v1/type-hours`
- ✅ Revisa que el puerto 8081 esté disponible
- ✅ Verifica el firewall o antivirus

### Error: "Connection refused"

- ✅ El servicio no está iniciado: `mvn spring-boot:run` o `docker-compose up`

### Error 500: Internal Server Error

- ✅ Revisa los logs del microservicio
- ✅ Verifica que PostgreSQL esté corriendo
- ✅ Confirma las credenciales de base de datos

## Scripts de Postman Útiles

### Pre-request Script (generar timestamp)

```javascript
pm.environment.set("timestamp", new Date().toISOString());
```

### Test Script (validar estructura)

```javascript
pm.test("Response has correct structure", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData).to.have.property('idTypeHour');
    pm.expect(jsonData).to.have.property('typeHour');
});
```

## Contacto y Soporte

Para reportar problemas o sugerencias, contacta al equipo de desarrollo.
