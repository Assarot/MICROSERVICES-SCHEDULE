# 🚀 Guía de Pruebas - Paso a Paso

## 📌 Resumen Rápido

Este documento te guía a través de todas las pruebas necesarias para validar que el importador funciona correctamente con:

- ✅ Carga de docentes
- ✅ Carga académica con matching de nombres
- ✅ Idempotencia (sin duplicados al reimportar)

**Tiempo total esperado:** ~30 minutos (incluyendo creación de Excel)

---

## 🎯 Prerequisitos

Antes de empezar, verifica que tengas activos:

```bash
# 1. Eureka Server corriendo
curl http://localhost:8761

# 2. Microservicio de importación
curl http://localhost:8008/api/v1/import/health

# 3. Otros microservicios registrados (debería verlos en Eureka)
# - microservice-course-management (8085)
# - microservice-user (8086)
# - microservice-schedule (8087)
# - microservice-enviroment (8089)
```

Si alguno falla, inicia los servicios primero.

---

## 📝 PASO 1: Crear Archivo de Docentes

### Opción A: Copiar y Pegar (RECOMENDADO) - 2 minutos

1. **Abre:** [DOCENTES_PRUEBA.txt](DOCENTES_PRUEBA.txt)

   ```
   Nombre	Apellido	Email
   Juan	Pérez	juan.perez@upeu.edu.pe
   María	García López	maria.garcia@upeu.edu.pe
   ...
   ```

2. **Selecciona todo** (Ctrl+A) y **copia** (Ctrl+C)

3. **Abre Excel en blanco**

4. **Pega en A1** (Ctrl+V)
   - Excel autodetectará 3 columnas separadas

5. **Guarda como Excel**
   - Archivo → Guardar como
   - Formato: **Excel (.xlsx)**
   - Nombre: **docentes_prueba.xlsx**

### Opción B: Ver Instrucciones Completas

- Lee: [INSTRUCCIONES_DOCENTES_PRUEBA.md](INSTRUCCIONES_DOCENTES_PRUEBA.md)

---

## 📚 PASO 2: Crear Archivo de Carga Académica

### Sigue el mismo proceso:

1. **Abre:** [CARGA_ACADEMICA_PRUEBA.txt](CARGA_ACADEMICA_PRUEBA.txt)
2. **Ctrl+A → Ctrl+C**
3. **Abre Excel → Pega en A1**
4. **Guarda como:** **carga_academica_prueba.xlsx**

✅ Importante: Este archivo ya usa los nombres de los docentes que vas a importar.

---

## 🧪 PASO 3: Importar Docentes

### Ejecuta en terminal:

```bash
curl -X POST -F "file=@docentes_prueba.xlsx" \
  http://localhost:8008/api/v1/import/upload-teachers
```

### Respuesta esperada (SUCCESS):

```json
{
  "status": "SUCCESS",
  "message": "Docentes importados exitosamente",
  "filename": "docentes_prueba.xlsx"
}
```

### Verifica en logs:

```
✓ Docente cargado: Juan Pérez
✓ Docente cargado: María García López
✓ Docente cargado: Juan Tomás Pérez Del Aguila
...
Se cargaron 10 docentes
```

### ❌ Si algo falla:

```
"Docente ya existente, se omite"
→ Normal si los correos ya estaban en BD

"Connection refused"
→ Verifica que MS-USER esté corriendo

"Invalid Excel"
→ Verifica que sea archivo .xlsx válido
```

---

## 📊 PASO 4: Procesar Carga Académica

### Ejecuta en terminal:

```bash
curl -X POST -F "file=@carga_academica_prueba.xlsx" \
  http://localhost:8008/api/v1/import/upload-carga-psico
```

### Respuesta esperada:

```json
{
  "status": "SUCCESS",
  "message": "Carga Psico procesada exitosamente",
  "filename": "carga_academica_prueba.xlsx",
  "timestamp": 1719604800000
}
```

### Verifica en logs (muy importante):

```
========== INICIANDO PROCESO DE ORQUESTACIÓN ==========
Paso 1: Leyendo archivo Excel...
✓ 10 cursos leídos del Excel

Paso 2: Obteniendo espacios académicos...
✓ X espacios académicos disponibles

Paso 3: Agrupando cursos por ciclo y grupo...
✓ 4 grupos diferentes identificados

Paso 4: Creando cursos y asignando horarios...
✓ Curso creado: Teoría del Aprendizaje (ID: 125)
✓ Horario asignado: LUNES 08:00 - 09:30 en sala AULA 101
...

========== PROCESO COMPLETADO ==========
✓ Cursos creados: 10
✓ Horarios asignados: 30
========== FIN DEL PROCESO ==========
```

---

## 🔄 PASO 5: Probar Idempotencia (SIN DUPLICADOS)

### Ejecuta el MISMO comando nuevamente:

```bash
curl -X POST -F "file=@carga_academica_prueba.xlsx" \
  http://localhost:8008/api/v1/import/upload-carga-psico
```

### Respuesta esperada:

```json
{
  "status": "SUCCESS",
  "message": "Carga Psico procesada exitosamente",
  "filename": "carga_academica_prueba.xlsx",
  "timestamp": 1719604800001
}
```

### Verifica en logs:

```
✓ Curso duplicado omitido: Teoría del Aprendizaje
✓ Curso duplicado omitido: Psicología del Desarrollo
✓ Curso duplicado omitido: Métodos de Investigación
...

========== PROCESO COMPLETADO ==========
✓ Cursos creados: 0          ← CERO, no se duplicaron
✓ Horarios asignados: 0      ← CERO, no se duplicaron
========== FIN DEL PROCESO ==========
```

✅ **Esto confirma que la idempotencia funciona correctamente.**

---

## 🔍 PASO 6: Validar Matching de Docentes

### Verifica que los docentes se encontraron:

En los logs deberías ver líneas como:

```
✓ Docente creado: Juan Pérez
✓ Docente creado: María García López
✓ Docente creado: Juan Tomás Pérez Del Aguila
✓ Docente creado: Carlos Rodríguez
✓ Docente creado: Ana Martínez Gómez
✓ Docente creado: Roberto López Flores
✓ Docente creado: Sofia Díaz
✓ Docente creado: Luis Fernández
✓ Docente creado: Patricia Sánchez
✓ Docente creado: Miguel Quispe Condori
```

❌ Si ves:

```
Docente no encontrado en catálogo maestro: [NOMBRE]
```

→ Significa que el nombre en la carga académica NO coincide con el importado.

---

## 📋 Checklist de Validación Final

- [ ] Docentes importados: 10 sin duplicados
- [ ] Carga académica procesada: 10 cursos creados
- [ ] Horarios asignados: 30 (3 por curso)
- [ ] Reimportación: 0 duplicados, 0 horarios nuevos
- [ ] Nombres compuestos: Encontrados correctamente
- [ ] Logs sin errores: Todos ✓

---

## 🎯 Casos de Prueba Adicionales

### Caso 1: Cambiar nombre de docente

1. Edita `carga_academica_prueba.xlsx`
2. Cambia "Juan Pérez" → "NOMBRE INEXISTENTE"
3. Carga el archivo
4. Debería omitir ese curso

### Caso 2: Agregar un docente nuevo

1. En la carga académica, agrega un nuevo curso con docente nuevo
2. Carga
3. Debería fallar porque el docente no está en el catálogo

### Caso 3: Nombres invertidos

1. Cambia "Juan Pérez" → "Pérez Juan"
2. Carga
3. El sistema debería encontrarlo (porque normaliza)

---

## 🚨 Troubleshooting

| Error                        | Solución                                 |
| ---------------------------- | ---------------------------------------- |
| "No hay espacios académicos" | Crea ambientes en MS-ENVIRONMENT primero |
| "Feign: no such host"        | Inicia Eureka Server                     |
| "Connection refused"         | Verifica puerto del servicio             |
| "Invalid Excel"              | Asegura que sea .xlsx, no .csv           |
| "Docente no encontrado"      | Verifica ortografía exacta del nombre    |

---

## 📞 Contacto con Endpoints

### Health Check

```bash
curl http://localhost:8008/api/v1/import/health
```

### Ver Procesar Excel Predefinido (alternativa rápida)

```bash
curl -X POST http://localhost:8008/api/v1/import/process-default-excel
```

### Dashboard Eureka (monitor servicios)

```
http://localhost:8761
```

---

## ✅ Resultado Esperado

Si todo funciona correctamente, deberías tener:

**Base de Datos:**

- 10 docentes importados
- 10 cursos creados (ciclos 1-4)
- 30 horarios asignados
- 4 grupos académicos

**Logs:**

- Sin errores críticos
- Todos los matching exitosos
- Idempotencia funcionando

**Endpoint:**

- `/health` devuelve UP
- Importador registrado en Eureka

---

**Documento versión:** 1.0  
**Última actualización:** 2026-04-29  
**Estado:** Listo para usar ✅
