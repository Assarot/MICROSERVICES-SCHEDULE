# ⚠️ Notas Importantes

## Archivo de Configuración

**NOTA**: El proyecto ahora usa `application.yml` en lugar de `application.properties`.

Si existe un archivo `application.properties` en tu proyecto, puedes:

1. **Eliminarlo manualmente** (recomendado):
   - Navega a `src/main/resources/`
   - Elimina el archivo `application.properties`

2. **Dejarlo** (no recomendado):
   - Spring Boot dará prioridad a `application.yml`
   - Sin embargo, puede causar confusión

### Para eliminar application.properties:

**Windows (PowerShell):**
```powershell
Remove-Item "src/main/resources/application.properties"
```

**Linux/Mac:**
```bash
rm src/main/resources/application.properties
```

## Configuración de Cloudinary

### ⚠️ IMPORTANTE: Debes configurar Cloudinary antes de usar la función de upload

1. **Regístrate en Cloudinary** (gratis): https://cloudinary.com/
2. **Obtén tus credenciales** del Dashboard
3. **Configura las variables de entorno**:

#### Opción 1: Archivo .env (recomendado para desarrollo)
```env
CLOUDINARY_CLOUD_NAME=tu_cloud_name
CLOUDINARY_API_KEY=tu_api_key
CLOUDINARY_API_SECRET=tu_api_secret
```

#### Opción 2: Variables de entorno del sistema

**Windows:**
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

#### Opción 3: Directamente en application.yml (NO recomendado para producción)
```yaml
cloudinary:
  cloud-name: tu_cloud_name
  api-key: tu_api_key
  api-secret: tu_api_secret
```

## Orden de Creación de Datos

Para evitar errores de FK, crea los datos en este orden:

1. ✅ **Primero**: Severity y Status (no tienen dependencias)
2. ✅ **Segundo**: Incident (depende de Severity y Status)
3. ✅ **Tercero**: IncidentAttachment (depende de Incident)

## Ejemplo de Flujo Completo

```bash
# 1. Crear Severity
POST /api/v1/severities
{
  "name": "High",
  "isActive": true
}
# Respuesta: { "idSeverity": 1, "name": "High", "isActive": true }

# 2. Crear Status
POST /api/v1/statuses
{
  "name": "Open",
  "isActive": true
}
# Respuesta: { "idStatus": 1, "name": "Open", "isActive": true }

# 3. Crear Incident
POST /api/v1/incidents
{
  "title": "Computer not working",
  "description": "The computer in lab 301 is not turning on",
  "reportDate": "2024-10-15T10:30:00",
  "idReportedBy": 1,
  "idSeverity": 1,  # <-- USA EL ID OBTENIDO EN PASO 1
  "idStatus": 1     # <-- USA EL ID OBTENIDO EN PASO 2
}
# Respuesta: { "idIncident": 1, ... }

# 4. Subir imagen
POST /api/v1/attachments
Form-data:
  - incidentId: 1    # <-- USA EL ID OBTENIDO EN PASO 3
  - file: [imagen]
# Respuesta: { "idIncidentAttachment": 1, "photoUrl": "https://...", ... }
```

## Solución de Problemas Comunes

### Error: "Severity not found with id: X"
**Causa**: Intentas crear un Incident con un idSeverity que no existe.
**Solución**: Crea primero la Severity y usa su ID.

### Error: "Status not found with id: X"
**Causa**: Intentas crear un Incident con un idStatus que no existe.
**Solución**: Crea primero el Status y usa su ID.

### Error: "Incident not found with id: X"
**Causa**: Intentas subir un attachment a un incidente que no existe.
**Solución**: Crea primero el Incident y usa su ID.

### Error: "Could not upload image to Cloudinary"
**Causa**: Credenciales de Cloudinary no configuradas o incorrectas.
**Solución**: Verifica tus credenciales en `.env` o variables de entorno.

### Error: "Port 8080 already in use"
**Causa**: Otro servicio está usando el puerto 8080.
**Solución**: 
- Detén el otro servicio, o
- Cambia el puerto en `application.yml`: `server.port: 8081`

## Datos Iniciales Opcionales

Si deseas cargar datos iniciales automáticamente al iniciar la aplicación:

1. El archivo `data.sql` ya tiene datos de ejemplo
2. Habilítalo en `application.yml`:
   ```yaml
   spring:
     sql:
       init:
         mode: always
     jpa:
       defer-datasource-initialization: true
   ```

**⚠️ ADVERTENCIA**: Esto puede causar errores si los datos ya existen. Úsalo solo en desarrollo.

## Seguridad

**NUNCA** subas a Git:
- ❌ Archivo `.env` con credenciales reales
- ❌ `application.yml` con credenciales hardcodeadas
- ❌ Credenciales de Cloudinary

**SIEMPRE** usa:
- ✅ Variables de entorno
- ✅ Archivo `.env` local (en .gitignore)
- ✅ `.env.example` para documentar las variables necesarias

---

**¿Necesitas ayuda?** Consulta:
- `README.md` - Documentación completa
- `GETTING_STARTED.md` - Guía de inicio rápido
- `PROJECT_SUMMARY.md` - Resumen del proyecto
