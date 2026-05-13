# 📮 Guía Postman - Importar Docentes y Carga Académica

## 🎯 URLs de los Endpoints

```
📌 Importar Docentes:
POST http://localhost:8008/api/v1/import/upload-teachers

📌 Procesar Carga Académica:
POST http://localhost:8008/api/v1/import/upload-carga-psico

📌 Health Check (verificar si está activo):
GET http://localhost:8008/api/v1/import/health
```

---

## ✅ PASO 1: Verificar Salud del Servicio (Opcional)

Para confirmar que el importador está corriendo:

1. **Abre Postman**
2. **Click en "New" → "Request"**
3. **Configuración:**
   - Método: **GET**
   - URL: `http://localhost:8008/api/v1/import/health`
   - Headers: (ninguno necesario)
4. **Click "Send"**

**Respuesta esperada:**

```json
{
  "status": "UP",
  "service": "microservice-import",
  "message": "Microservicio de importación y orquestación de horarios activo"
}
```

Si ves error "Could not get any response", el importador NO está corriendo → inicia primero.

---

## 📤 PASO 2: Importar Docentes en Postman

### 2.1 Crear la Solicitud

1. **Click en "New" → "Request"**
2. **Dale un nombre:** "Importar Docentes Prueba"
3. **Selecciona método:** `POST`
4. **URL:** `http://localhost:8008/api/v1/import/upload-teachers`

### 2.2 Agregar el Archivo Excel

1. **Click en la pestaña "Body"**
2. **Selecciona opción "form-data"** (no JSON)
3. **Haz clic en el campo "Key"**
   - Escribe: `file`
4. **En el campo "Value":**
   - Click en el dropdown que dice "Text" (lado derecho)
   - Cambia a **"File"**
5. **Click en "Select Files"**
   - Busca y selecciona tu archivo: `docentes_prueba.xlsx`

### 2.3 Configuración Completa

Tu solicitud debería verse así en Postman:

```
POST http://localhost:8008/api/v1/import/upload-teachers

Body → form-data
┌─────────────┬──────────────────────────────┐
│ Key         │ Value                        │
├─────────────┼──────────────────────────────┤
│ file        │ docentes_prueba.xlsx ✓       │
│ (TYPE: File)│                              │
└─────────────┴──────────────────────────────┘
```

### 2.4 Enviar la Solicitud

1. **Click en "Send"** (botón azul)
2. **Espera la respuesta** (5-10 segundos)

### 2.5 Respuesta Esperada

Si todo funciona:

```json
{
  "status": "SUCCESS",
  "message": "Docentes importados exitosamente",
  "filename": "docentes_prueba.xlsx"
}
```

**✅ Código HTTP:** 200 OK

### 2.6 Verificar en Logs

Abre la terminal/consola donde corre el importador. Deberías ver:

```
✓ Docente cargado: Juan Pérez
✓ Docente cargado: María García López
✓ Docente cargado: Juan Tomás Pérez Del Aguila
...
Se cargaron 10 docentes
```

---

## 📚 PASO 3: Procesar Carga Académica en Postman

**Repite exactamente el mismo proceso, pero:**

1. **Crea una nueva solicitud:** "Procesar Carga Académica"
2. **Método:** `POST`
3. **URL:** `http://localhost:8008/api/v1/import/upload-carga-psico`
4. **Body → form-data**
5. **Key:** `file`
6. **Value:** Selecciona `carga_academica_prueba.xlsx` (con File)
7. **Click "Send"**

### Respuesta Esperada:

```json
{
  "status": "SUCCESS",
  "message": "Carga Psico procesada exitosamente",
  "filename": "carga_academica_prueba.xlsx",
  "timestamp": 1719604800000
}
```

**✅ Código HTTP:** 200 OK

---

## 🔄 PASO 4: Probar Idempotencia (Reimportar)

Usa **la misma solicitud de Postman** del PASO 3 (sin cambiar el archivo):

1. **Click "Send"** nuevamente
2. **Respuesta:** `status: SUCCESS` (igual que antes)
3. **En los logs:**
   - Verás: `✓ Curso duplicado omitido: [NOMBRE]`
   - Verás: `✓ Cursos creados: 0`
   - Verás: `✓ Horarios asignados: 0`

✅ **Esto confirma que NO se duplicó nada.**

---

## 💾 Guardar las Solicitudes en Postman

Para no tener que crear todo de nuevo:

1. **Click en "Save"** después de crear cada solicitud
2. **Crea una "Collection":** "Pruebas Importador"
3. Agrega ambas solicitudes a la collection
4. **Próxima vez:** Click en tu collection y están listas

---

## ❌ Troubleshooting en Postman

| Problema                        | Solución                                                   |
| ------------------------------- | ---------------------------------------------------------- |
| "Could not get any response"    | El servicio no está corriendo → inicia microservice-import |
| "404 Not Found"                 | URL incorrecta → verifica puerto 8008                      |
| "400 Bad Request"               | El file no está configurado como "File" en form-data       |
| "No file part"                  | Falta configurar el "Key" como `file`                      |
| "Connection refused"            | MS-IMPORT no está corriendo o Eureka está apagado          |
| Error en Body → "Invalid Excel" | Verifica que sea archivo .xlsx válido                      |

---

## 🎯 Vista Rápida de las 3 Solicitudes

### Solicitud 1: Health Check

```
GET http://localhost:8008/api/v1/import/health
```

(sin body)

### Solicitud 2: Importar Docentes

```
POST http://localhost:8008/api/v1/import/upload-teachers
Body: form-data
  Key: file
  Value: docentes_prueba.xlsx (File)
```

### Solicitud 3: Carga Académica

```
POST http://localhost:8008/api/v1/import/upload-carga-psico
Body: form-data
  Key: file
  Value: carga_academica_prueba.xlsx (File)
```

---

## 📋 Checklist de Postman

- [ ] Health check responde OK
- [ ] Docentes importados sin errores
- [ ] Carga académica procesada sin errores
- [ ] Reimportación de carga académica sin duplicados
- [ ] Logs muestran todos los ✓
- [ ] Colección guardada en Postman

---

## 💡 Tips Postman

1. **Pre-request Script:** Si necesitas pasar token o variables dinámicas
   - Click en la solicitud → "Pre-request Script"

2. **Tests:** Para validar respuestas automáticamente
   - Click en la solicitud → "Tests"
   - Ejemplo:
     ```javascript
     pm.test("Status is success", function () {
       pm.expect(pm.response.json().status).to.eql("SUCCESS");
     });
     ```

3. **Variables de Environment:** Para reutilizar URLs
   - Crea variable: `base_url = http://localhost:8008`
   - Úsala: `{{base_url}}/api/v1/import/upload-teachers`

---

**Versión:** 1.0  
**Última actualización:** 2026-04-29
