# 🚀 Guía Rápida - Orquestador de Horarios

## ⚡ Inicio Rápido en 5 minutos

### Paso 1: Verificar Requisitos

```bash
# Eureka Server debe estar corriendo
curl http://localhost:8761

# No necesita base de datos local propia
# Este servicio es stateless y coordina otros microservicios
```

### Paso 2: Compilar el Microservicio

```bash
cd microservice-import
mvn clean install
```

### Paso 3: Iniciar el Servicio

```bash
mvn spring-boot:run
# O directamente la app desde IDE
```

### Paso 4: Verificar Salud

```bash
curl http://localhost:8008/api/v1/import/health

# Respuesta esperada:
{
  "status": "UP",
  "service": "microservice-import",
  "message": "Microservicio de importación y orquestación de horarios activo"
}
```

### Paso 5: Procesar Carga Psico

```bash
# Opción A: Usar archivo predefinido en recursos
curl -X POST http://localhost:8008/api/v1/import/process-default-excel

# Opción B: Subir archivo Excel
curl -X POST -F "file=@/ruta/a/carga psico.xlsx" \
  http://localhost:8008/api/v1/import/upload-carga-psico
```

### Paso 6: Importar Maestros

```bash
# Excel separado con: nombre, apellido, correo
curl -X POST -F "file=@/ruta/a/docentes.xlsx" \
  http://localhost:8008/api/v1/import/upload-teachers
```

## 📊 Respuesta Esperada

```json
{
  "status": "SUCCESS",
  "message": "Carga Psico procesada exitosamente",
  "filename": "carga psico.xlsx",
  "timestamp": 1719604800000
}
```

## 🔍 Monitorear Progreso

### Ver logs en tiempo real

```bash
tail -f logs/microservice-import.log
```

### Logs importantes a buscar

```
✓ X cursos leídos del Excel
✓ Y espacios académicos disponibles
✓ Z grupos diferentes identificados
✓ Cursos creados: W
✓ Horarios asignados: V
```

## ⚙️ Parámetros Clave

| Parámetro       | Valor      | Descripción                   |
| --------------- | ---------- | ----------------------------- |
| Puerto          | 8008       | Donde corre el servicio       |
| Slots de Tiempo | 8:00-17:00 | Rango de horarios disponibles |
| Duración Slot   | 90 minutos | Cada clase dura 1.5 horas     |
| Sesiones/Semana | 3 (L-M-V)  | Lunes, Miércoles, Viernes     |
| Max File Size   | 10MB       | Tamaño máximo de Excel        |
| Arquitectura    | Stateless  | Sin BD local propia           |

## 🐛 Diagnóstico Rápido

### ❌ "Feign: no such host"

```
→ Eureka Server no está corriendo
→ Solución: Inicia microservice-eureka-server primero
```

### ❌ "No hay espacios académicos"

```
→ MS-ENVIRONMENT sin datos
→ Solución: Precarga espacios académicos antes de cargar psico
```

### ❌ "Connection refused"

```
→ Algún microservicio destino no está corriendo
→ Solución: Inicia Eureka y verifica que MS-COURSE, MS-USER, MS-SCHEDULE y MS-ENVIRONMENT estén registrados
```

### ❌ "Invalid Excel"

```
→ Archivo no es .xlsx válido
→ Solución: Usa archivo Excel bien formado con headers en fila 1
```

## 📈 Estadísticas Típicas

Para archivo "carga psico.xlsx" estándar:

- **Cursos procesados**: ~224
- **Horarios asignados**: ~672 (3 por curso)
- **Tiempo procesamiento**: ~2-5 minutos
- **Salones utilizados**: 15-20

## 🎯 Casos de Uso

### Caso 1: Carga Inicial del Semestre

```bash
# Al inicio de semestre, procesa la carga psico completa
curl -X POST http://localhost:8008/api/v1/import/process-default-excel
```

### Caso 2: Carga de Ciclo Específico

```bash
# Prepara Excel solo con ciclo 1
# Luego sube
curl -X POST -F "file=@carga_ciclo1.xlsx" \
  http://localhost:8008/api/v1/import/upload-carga-psico
```

### Caso 3: Reporte de Conflictos

```bash
# Los logs mostrarán qué cursos no pudieron asignar horarios
# Busca líneas con "Error al procesar" o "Horario no asignado"
```

## 🔗 Integración con Otros Servicios

```
┌─────────────────────────────────────────────┐
│        MICROSERVICE-IMPORT (8008)           │
│         Orquestador de Horarios             │
└─────────────────────────────────────────────┘
    ↓              ↓              ↓              ↓
┌──────────┐ ┌──────────┐ ┌────────────┐ ┌──────────────┐
│ MS-COURSE│ │ MS-USER  │ │ MS-SCHEDULE│ │MS-ENVIRONMENT│
│(Cursos)  │ │(Docentes)│ │(Horarios)  │ │(Salones)     │
└──────────┘ └──────────┘ └────────────┘ └──────────────┘
```

## 📞 URLs Útiles

| Endpoint                               | Método | Descripción                  |
| -------------------------------------- | ------ | ---------------------------- |
| `/api/v1/import/upload-carga-psico`    | POST   | Subir Excel                  |
| `/api/v1/import/upload-teachers`       | POST   | Importar maestros            |
| `/api/v1/import/process-default-excel` | POST   | Procesar archivo predefinido |
| `/api/v1/import/health`                | GET    | Salud del servicio           |
| `http://localhost:8761`                | GET    | Dashboard Eureka             |

## 💡 Tips y Trucos

### Tip 1: Procesar en horario nocturno

```bash
# Programar en cron para ejecutar a las 2:00 AM
0 2 * * * curl -X POST http://localhost:8008/api/v1/import/process-default-excel
```

### Tip 2: Validar Excel antes de cargar

- Abre en Excel
- Verifica headers en fila 1
- Asegúrate que ciclo y grupo estén rellenos
- Confirma que no haya filas vacías en medio

### Tip 3: Limpiar horarios anteriores

```bash
# Antes de recargar, borra horarios viejos en MS-SCHEDULE
DELETE FROM schedule WHERE created_at < NOW() - INTERVAL '1 day'
```

## 🎓 Próximos Pasos

1. ✅ Servicio orquestador implementado
2. ⏳ Crear dashboard de administración
3. ⏳ Agregar validación de conflictos más robusta
4. ⏳ Implementar algoritmo genético para optimización

---

**Estado**: Listo para Producción ✅  
**Última actualización**: 2026-04-28
