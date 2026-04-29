# 📚 Microservicio de Importación y Orquestación de Horarios

## 🎯 Propósito

Este microservicio actúa como **orquestador central** para procesar archivos Excel de carga académica (CARGA PSICO) y crear automáticamente:

1. **Cursos** en MS-COURSE-MANAGEMENT
2. **Asignaciones de docentes**
3. **Horarios optimizados** en MS-SCHEDULE
4. **Asignación de aulas/ambientes** desde MS-ENVIRONMENT

## 📋 Flujo de Proceso

```
Excel (CARGA PSICO)
        ↓
    [Lectura Excel]
        ↓
  [Análisis de Datos]
        ↓
[Agrupación por Ciclo/Grupo]
        ↓
[Creación de Cursos] ← MS-COURSE-MANAGEMENT
        ↓
[Creación de Asignaciones] ← MS-USER (Teachers)
        ↓
[Distribución de Horarios] ← MS-SCHEDULE
        ↓
[Asignación de Aulas] ← MS-ENVIRONMENT
        ↓
    ✅ Proceso Completado
```

## 🏗️ Arquitectura del Componente

### Diseño Stateless (Sin Base de Datos)

Este microservicio es **completamente stateless** - coordina datos sin persistirlos localmente:

```
┌─────────────────────────────────────────────┐
│     MICROSERVICE-IMPORT (STATELESS)         │
│     Sin BD propia - Solo coordina           │
└─────────────────────────────────────────────┘
    │              │             │             │
    ▼              ▼             ▼             ▼
   DB1            DB2           DB3           (ref)
MS-COURSE     MS-SCHEDULE   MS-USER        MS-ENVIRONMENT
(persiste)    (persiste)    (persiste)     (consulta)
```

### Servicios Principales

#### 1. **ExcelReaderService**

- Lee archivos .xlsx y .xls
- Mapea datos a DTOs
- Valida estructura de datos
- Salta filas vacías o inválidas

#### 2. **ScheduleOrchestratorService** (🔑 Núcleo)

- Coordina todo el proceso
- Maneja comunicación con otros microservicios
- **Algoritmo de scheduling** con prevención de conflictos

#### 3. **Controllers**

- `POST /api/v1/import/upload-carga-psico` - Subir Excel
- `POST /api/v1/import/process-default-excel` - Procesar archivo predefinido
- `GET /api/v1/import/health` - Health check

## ⚙️ Algoritmo de Distribución de Horarios

### Estrategia de Asignación

```
Para cada grupo de cursos (ciclo + grupo):
  ├── Para cada curso en el grupo:
  │   ├── 1. Obtener espacios académicos disponibles
  │   ├── 2. Filtrar por capacidad (aforo >= estudiantes esperados)
  │   ├── 3. Buscar 3 slots disponibles (Lunes, Miércoles, Viernes)
  │   ├── 4. Para cada día:
  │   │   ├── Iterar sobre slots de tiempo disponibles
  │   │   ├── Verificar que salón NO tenga otro curso en ese horario
  │   │   ├── Si disponible: ASIGNAR HORARIO
  │   │   └── Marcar slot como ocupado (prevenir conflicto)
  │   └── 5. Guardar en MS-SCHEDULE
```

### Horarios Predeterminados

```
8:00 AM  - 9:30 AM    (90 min)
9:30 AM  - 11:00 AM   (90 min)
11:00 AM - 12:30 PM   (90 min)
12:30 PM - 2:00 PM    (90 min)
2:00 PM  - 3:30 PM    (90 min)
3:30 PM  - 5:00 PM    (90 min)
5:00 PM  - 6:30 PM    (90 min)
```

### Días de Clase

- Lunes
- Martes
- Miércoles
- Jueves
- Viernes

(Se asignan 3 horarios por curso: Lunes, Miércoles, Viernes)

### Prevención de Conflictos

**ScheduleTracker** mantiene registro de slots ocupados usando:

```
Clave: "{DÍA}_{HORA_INICIO}_{HORA_FIN}_{ID_AULA}"
```

Ejemplo:

```
"LUNES_08:00_09:30_5" = Aula 5, Lunes 8am-9:30am [OCUPADO]
"MARTES_14:00_15:30_3" = Aula 3, Martes 2pm-3:30pm [DISPONIBLE]
```

## 📊 DTOs y Modelos

### CargaPsicoExcelDTO

```java
- facultad (String)
- escuela (String)
- nombreCurso (String)
- modo (String)
- ciclo (Integer)
- grupo (Integer)
- plan (String)
- credito (Integer)
- ht (Integer) // Horas teóricas
- hp (Integer) // Horas prácticas
- totalHoras (Integer)
- horasLectivas (Integer)
- modalidad (String)
- docente (String)
- aforoPorCursoGrupo (Integer)
- ambienteEspecializado (String)
```

### CreateCourseDTO

```java
- name (String)
- code (String) // Generado automáticamente
- description (String)
- theoreticalHours (Duration)
- practicalHours (Duration)
- totalHours (Duration)
- idCourseType (Long)
- idPlan (Long)
- idGroup (Long)
- credits (Integer)
- modalidad (String)
```

### CreateScheduleDTO

```java
- startTime (LocalTime)
- endTime (LocalTime)
- duration (Integer)
- idTypeSchedule (Long)
- idAcademicSpace (Long)
- idCourseAssignment (Long)
- idWeekName (Long)
- dayName (String)
```

## 🔗 Integración con Microservicios

### 1. MS-COURSE-MANAGEMENT

```
POST /api/v1/courses - Crear curso
POST /api/v1/course-assignments - Crear asignación docente
```

### 2. MS-USER

```
POST /api/v1/teachers - Crear/obtener docente
```

### 3. MS-SCHEDULE

```
POST /api/v1/schedules - Crear horario
GET /api/v1/schedules/by-space-and-time - Verificar disponibilidad
```

### 4. MS-ENVIRONMENT

```
GET /api/v1/academic-spaces - Listar espacios disponibles
```

## 🚀 Cómo Usar

### ⚠️ IMPORTANTE: Arquitectura Stateless

Este microservicio **NO tiene base de datos propia**. Es un orquestador puro que:

- ✅ Coordina múltiples microservicios
- ✅ Persiste datos en BD de otros servicios (MS-COURSE, MS-SCHEDULE, MS-USER, MS-ENVIRONMENT)
- ✅ Mantiene información solo en memoria durante procesamiento
- ✅ Puede escalar horizontalmente sin estado local

### 1. Subir archivo Excel

```bash
curl -X POST -F "file=@carga psico.xlsx" \
  http://localhost:8008/api/v1/import/upload-carga-psico
```

### 2. Procesar archivo predefinido

```bash
curl -X POST http://localhost:8008/api/v1/import/process-default-excel
```

### 3. Verificar salud del servicio

```bash
curl http://localhost:8008/api/v1/import/health
```

## 📈 Logs y Monitoreo

El servicio genera logs detallados:

```
[INFO] ========== INICIANDO PROCESO DE ORQUESTACIÓN ==========
[INFO] Paso 1: Leyendo archivo Excel...
[INFO] ✓ 224 cursos leídos del Excel
[INFO] Paso 2: Obteniendo espacios académicos...
[INFO] ✓ 15 espacios académicos disponibles
[INFO] Paso 3: Agrupando cursos por ciclo y grupo...
[INFO] ✓ 8 grupos diferentes identificados
[INFO] Paso 4: Creando cursos y asignando horarios...
[INFO] Procesando grupo: CICLO_1_GRUPO_1
[INFO] ✓ Curso creado: Comunicación Oral y Escrita (ID: PSI-1-1-COM)
[INFO] ✓ Horario asignado: LUNES 08:00 - 09:30 en sala AULA 5
...
[INFO] ✓ Cursos creados: 224
[INFO] ✓ Horarios asignados: 672
[INFO] ========== PROCESO COMPLETADO ==========
```

## ⚡ Características

✅ **Lectura robusta de Excel** - Maneja múltiples formatos y validaciones
✅ **Orquestación automática** - Coordina múltiples microservicios
✅ **Algoritmo anti-conflictos** - Garantiza sin solapes de horarios
✅ **Escalable** - Procesa 224+ cursos en minutos
✅ **Logs detallados** - Rastreabilidad completa del proceso
✅ **Manejo de errores** - Continúa incluso si un curso falla
✅ **Integración Eureka** - Descubrimiento automático de servicios
✅ **Feign Clients** - Comunicación simplificada entre microservicios

## 🔧 Configuración

### application.yml

```yaml
spring:
  application:
    name: microservice-import
  datasource:
    url: jdbc:postgresql://localhost:5432/microservices_import
    username: postgres
    password: 123456
  servlet:
    multipart:
      max-file-size: 10MB
      max-request-size: 10MB

server:
  port: 8008

eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/
```

## 📦 Dependencias Principales

```xml
<!-- Spring Boot - REST & Validation -->
<spring-boot-starter-web/>
<spring-boot-starter-validation/>

<!-- Cloud - Eureka & Feign -->
<spring-cloud-starter-netflix-eureka-client/>
<spring-cloud-starter-openfeign/>

<!-- Excel Processing -->
<apache-poi-5.2.3/>
<apache-poi-ooxml-5.2.3/>

<!-- Utilities -->
<lombok/>

<!-- ❌ SIN DEPENDENCIAS DE BD (Stateless) -->
<!-- NO tiene spring-boot-starter-data-jpa -->
<!-- NO tiene postgresql driver -->
```

**Arquitectura Stateless**:

- ✅ Solo HTTP/REST para comunicación
- ✅ Feign Clients para coordinar otros servicios
- ✅ Eureka para service discovery
- ✅ Apache POI para procesamiento de Excel
- ✅ NO persistencia local

## 🎓 Estructura de Archivos

```
microservice-import/
├── src/main/java/pe/edu/upeu/microserviceimport/
│   ├── client/                      # Feign clients
│   │   ├── CourseManagementClient
│   │   ├── ScheduleClient
│   │   ├── EnvironmentClient
│   │   ├── CourseAssignmentClient
│   │   └── TeacherClient
│   ├── controller/
│   │   └── ExcelUpload              # REST endpoints
│   ├── dto/                         # Data Transfer Objects
│   │   ├── CargaPsicoExcelDTO
│   │   ├── CreateCourseDTO
│   │   ├── CreateScheduleDTO
│   │   └── ...
│   ├── service/
│   │   ├── ExcelReaderService       # Lectura de Excel
│   │   └── ScheduleOrchestratorService  # Orquestador principal
│   └── MicroserviceImportApplication  # Main
├── src/main/resources/
│   ├── application.yml              # Configuración
│   └── carga psico.xlsx             # Archivo de ejemplo
└── pom.xml                          # Maven dependencies
```

## 🐛 Troubleshooting

### "No hay espacios académicos disponibles"

- Verifica que MS-ENVIRONMENT esté corriendo
- Asegúrate de haber creado espacios académicos previo a la carga

### "Feign Client no encuentra el servicio"

- Verifica que Eureka Server esté corriendo en `http://localhost:8761`
- Valida que los otros microservicios estén registrados en Eureka
- Revisa los logs de Feign en nivel DEBUG

### "Error al leer el archivo Excel"

- Verifica que el archivo sea .xlsx o .xls válido
- Asegúrate que no esté vacío o corrupto
- Confirma que tiene headers en la fila 1

## 📝 Notas Importantes

1. **Primer Inicio**: El servicio requiere que MS-ENVIRONMENT tenga espacios académicos precargados
2. **Generación de Códigos**: Se generan automáticamente basados en Ciclo-Grupo-Nombre
3. **Docentes**: Se crean automáticamente o se reutilizan si ya existen
4. **Horarios**: Siempre 3 sesiones por semana (L-M-V) de 90 minutos

## 📞 Soporte y Contacto

Para problemas, revisa los logs detallados en:

- Consola del servicio
- `spring.log` (si está configurado)
- Dashboard de Eureka: `http://localhost:8761`

---

**Última actualización**: 2026-04-28  
**Versión**: 1.0.0  
**Estado**: ✅ Producción
