# 📱 API Reference - Microservicio de Importación

## Base URL

```
http://localhost:8008/api/v1/import
```

---

## 📤 POST - Subir y Procesar Archivo Excel

### Endpoint

```
POST /upload-carga-psico
```

### Descripción

Sube un archivo Excel y procesa automáticamente la creación de cursos y horarios.

### Parámetros

| Parámetro | Tipo             | Requerido | Descripción                  |
| --------- | ---------------- | --------- | ---------------------------- |
| file      | File (multipart) | ✅        | Archivo Excel (.xlsx o .xls) |

### Content-Type

```
multipart/form-data
```

### Ejemplo con cURL

```bash
curl -X POST \
  -F "file=@/ruta/a/carga psico.xlsx" \
  http://localhost:8008/api/v1/import/upload-carga-psico
```

### Ejemplo con Python

```python
import requests

files = {'file': open('carga psico.xlsx', 'rb')}
response = requests.post(
    'http://localhost:8008/api/v1/import/upload-carga-psico',
    files=files
)
print(response.json())
```

### Ejemplo con JavaScript/Fetch

```javascript
const formData = new FormData();
formData.append("file", document.getElementById("fileInput").files[0]);

fetch("http://localhost:8008/api/v1/import/upload-carga-psico", {
  method: "POST",
  body: formData,
})
  .then((response) => response.json())
  .then((data) => console.log(data));
```

### Respuesta Exitosa (200 OK)

```json
{
  "status": "SUCCESS",
  "message": "Carga Psico procesada exitosamente",
  "filename": "carga psico.xlsx",
  "timestamp": 1719604800000
}
```

### Respuesta de Error (400 Bad Request)

```json
{
  "status": "ERROR",
  "message": "El archivo debe ser Excel (.xlsx o .xls)",
  "filename": null,
  "timestamp": null
}
```

### Respuesta de Error (500 Internal Server Error)

```json
{
  "status": "ERROR",
  "message": "Error al procesar carga: [descripción del error]",
  "filename": "carga psico.xlsx",
  "timestamp": 1719604800000
}
```

### Códigos de Estado

| Código | Significado                                                   |
| ------ | ------------------------------------------------------------- |
| 200    | Carga procesada exitosamente                                  |
| 400    | Archivo inválido o formato incorrecto                         |
| 500    | Error en procesamiento o integración con otros microservicios |

### Validaciones

- ✅ Solo acepta .xlsx o .xls
- ✅ Archivo no debe estar vacío
- ✅ Máximo tamaño: 10MB
- ✅ Debe tener headers en fila 1

---

## 👥 POST - Importar Maestros

### Endpoint

```
POST /upload-teachers
```

### Descripción

Importa docentes desde un Excel separado con columnas: nombre, apellido y correo.

### Parámetros

| Parámetro | Tipo             | Requerido | Descripción                               |
| --------- | ---------------- | --------- | ----------------------------------------- |
| file      | File (multipart) | ✅        | Archivo Excel con docentes (.xlsx o .xls) |

### Respuesta Exitosa (200 OK)

```json
{
  "status": "SUCCESS",
  "message": "Docentes importados exitosamente",
  "filename": "docentes.xlsx"
}
```

### Validaciones

- ✅ Solo acepta .xlsx o .xls
- ✅ Debe tener nombre, apellido y correo
- ✅ Evita duplicados por correo

---

## 🔄 POST - Procesar Excel Predefinido

### Endpoint

```
POST /process-default-excel
```

### Descripción

Procesa el archivo `carga psico.xlsx` que está en recursos del microservicio. Útil para tests y pruebas.

### Parámetros

Ninguno requerido.

### Ejemplo con cURL

```bash
curl -X POST http://localhost:8008/api/v1/import/process-default-excel
```

### Ejemplo con Python

```python
import requests

response = requests.post('http://localhost:8008/api/v1/import/process-default-excel')
print(response.json())
```

### Ejemplo con JavaScript

```javascript
fetch("http://localhost:8008/api/v1/import/process-default-excel", {
  method: "POST",
})
  .then((response) => response.json())
  .then((data) => console.log(data));
```

### Respuesta Exitosa (200 OK)

```json
{
  "status": "SUCCESS",
  "message": "Excel predefinido procesado exitosamente",
  "timestamp": 1719604800000
}
```

### Respuesta de Error (400 Bad Request)

```json
{
  "status": "ERROR",
  "message": "Archivo 'carga psico.xlsx' no encontrado en recursos",
  "timestamp": null
}
```

### Códigos de Estado

| Código | Significado                       |
| ------ | --------------------------------- |
| 200    | Procesamiento exitoso             |
| 400    | Archivo no encontrado en recursos |
| 500    | Error en procesamiento            |

---

## 🏥 GET - Health Check

### Endpoint

```
GET /health
```

### Descripción

Verifica el estado del microservicio.

### Parámetros

Ninguno.

### Ejemplo con cURL

```bash
curl http://localhost:8008/api/v1/import/health
```

### Respuesta (200 OK)

```json
{
  "status": "UP",
  "service": "microservice-import",
  "message": "Microservicio de importación y orquestación de horarios activo"
}
```

### Códigos de Estado

| Código | Significado            |
| ------ | ---------------------- |
| 200    | Servicio está activo   |
| 503    | Servicio no disponible |

---

## 📊 Estructura de Datos

### Excel Input Format

Debe contener las siguientes columnas en orden:

| #   | Nombre                  | Tipo            | Ejemplo                          |
| --- | ----------------------- | --------------- | -------------------------------- |
| 1   | FACULTAD                | String          | Facultad de Ciencias de la Salud |
| 2   | ESCUELA                 | String          | EP Psicología                    |
| 3   | NOMBRE CURSO            | String          | Comunicación Oral y Escrita      |
| 4   | MODO                    | String          | Regular                          |
| 5   | CICLO                   | Integer         | 1                                |
| 6   | GRUPO                   | Integer         | 1                                |
| 7   | PLAN                    | String (YYYY-M) | 2025-1                           |
| 8   | CREDITO                 | Integer         | 4                                |
| 9   | HT                      | Integer         | 3                                |
| 10  | HP                      | Integer         | 2                                |
| 11  | TOTAL HORAS             | Integer         | 5                                |
| 12  | HORAS LECTIVAS          | Integer         | 4                                |
| 13  | MODALIDAD               | String          | PRESENCIAL                       |
| 14  | DOCENTE                 | String          | Apellido Nombre                  |
| 15  | AFORO POR CURSO Y GRUPO | Integer         | 30                               |
| 16  | AMBIENTE ESPECIALIZADO  | String          | AULA                             |

### JSON Response Format

```json
{
  "status": "SUCCESS|ERROR",
  "message": "Descripción del resultado",
  "filename": "nombre del archivo (opcional)",
  "timestamp": 1719604800000
}
```

---

## 🔗 Integración y Dependencias

El microservicio se comunica con los siguientes servicios:

### MS-COURSE-MANAGEMENT

```
POST /api/v1/courses
POST /api/v1/course-assignments
```

### MS-USER

```
POST /api/v1/teachers
```

### MS-SCHEDULE

```
POST /api/v1/schedules
GET /api/v1/schedules/by-space-and-time
```

### MS-ENVIRONMENT

```
GET /v1/api/academic-space
```

### Nota de Arquitectura

Este microservicio es **stateless**: no tiene base de datos propia y solo coordina otros microservicios.

---

## ⚙️ Configuración y Propiedades

### Propiedades Principales

```yaml
spring.application.name: microservice-import
server.port: 8008
spring.servlet.multipart.max-file-size: 10MB
eureka.client.serviceUrl.defaultZone: http://localhost:8761/eureka/
```

### Timeouts Feign

```yaml
feign.client.config.default:
  connect-timeout: 5000
  read-timeout: 5000
```

---

## 🚨 Manejo de Errores

### Errores Comunes

#### 1. "El archivo debe ser Excel (.xlsx o .xls)"

**Causa**: Archivo con extensión no soportada  
**Solución**: Usa Excel con extensión .xlsx o .xls

#### 2. "Archivo 'carga psico.xlsx' no encontrado en recursos"

**Causa**: Archivo no existe en src/main/resources  
**Solución**: Coloca el archivo en la ruta correcta

#### 3. "No hay espacios académicos disponibles"

**Causa**: MS-ENVIRONMENT sin datos  
**Solución**: Precarga espacios antes de ejecutar

#### 4. "Connect refused" o "Feign: no such host"

**Causa**: Otros microservicios no accesibles  
**Solución**: Verifica que todos los servicios estén corriendo y registrados en Eureka

#### 5. "Connection refused"

**Causa**: Algún microservicio destino no disponible  
**Solución**: Verifica Eureka y que MS-COURSE, MS-USER, MS-SCHEDULE y MS-ENVIRONMENT estén arriba

---

## 📈 Monitoreo y Logs

### Importante: Buscar en logs

```
Líneas de éxito:
✓ X cursos leídos del Excel
✓ Y espacios académicos disponibles
✓ Curso creado: {nombre}
✓ Horario asignado: {día} {hora} en sala {nombre}

Líneas de error:
✗ Error al procesar fila X
✗ Error al procesar curso {nombre}
✗ Horario no asignado para {curso}
```

### Nivel de Log

```yaml
logging.level.pe.edu.upeu.microserviceimport: DEBUG
```

---

## 🧪 Testing

### Test de Conexión

```bash
# Verificar que el servicio está corriendo
curl -v http://localhost:8008/api/v1/import/health
```

### Test de Procesamiento

```bash
# Procesar Excel predefinido
curl -v -X POST http://localhost:8008/api/v1/import/process-default-excel
```

### Test de Upload

```bash
# Subir archivo
curl -v -X POST -F "file=@test.xlsx" \
  http://localhost:8008/api/v1/import/upload-carga-psico
```

---

## 📞 Soporte

Para problemas, revisa:

1. Logs de aplicación: `STDOUT` o archivos de log
2. Dashboard Eureka: `http://localhost:8761`
3. Base de datos: Verifica que los datos se hayan creado correctamente

---

**Última actualización**: 2026-04-28  
**Versión API**: v1  
**Estado**: Producción ✅
