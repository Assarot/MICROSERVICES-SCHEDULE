# 🏗️ Arquitectura Visual del Orquestador

## Flujo Completo del Sistema

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                         USUARIO/ADMIN                                        │
│                    (Sube archivo Excel o API Call)                           │
└──────────────────────────┬──────────────────────────────────────────────────┘
                           │
                           ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│               MICROSERVICE-IMPORT (ORQUESTADOR)                               │
│                     Port: 8008                                                │
│                                                                               │
│  ┌────────────────────────────────────────────────────────────────────────┐ │
│  │  ExcelReaderService                                                    │ │
│  │  ├─ Lee archivo Excel                                                 │ │
│  │  ├─ Mapea a CargaPsicoExcelDTO                                       │ │
│  │  ├─ Valida estructura                                                │ │
│  │  └─ Retorna lista de ~224 cursos                                     │ │
│  └────────────────────────────────────────────────────────────────────────┘ │
│                           │                                                   │
│                           ▼                                                   │
│  ┌────────────────────────────────────────────────────────────────────────┐ │
│  │  ScheduleOrchestratorService (NÚCLEO)                                  │ │
│  │                                                                        │ │
│  │  1. Agrupa cursos por (Ciclo, Grupo)                                 │ │
│  │     └─ Ejemplo: CICLO_1_GRUPO_1                                       │ │
│  │                                                                        │ │
│  │  2. Para cada curso:                                                 │ │
│  │     ├─ Crea CourseDTO                                                │ │
│  │     ├─ Obtiene Teacher (o crea uno)                                  │ │
│  │     ├─ Crea CourseAssignment                                         │ │
│  │     └─ Asigna 3 horarios (L-M-V)                                     │ │
│  │                                                                        │ │
│  │  3. Algoritmo ANTI-CONFLICTOS:                                       │ │
│  │     ├─ ScheduleTracker registra slots ocupados                       │ │
│  │     ├─ Verifica: ¿Aula disponible en horario?                        │ │
│  │     ├─ Clave: "DÍA_HORA_INICIO_HORA_FIN_ID_AULA"                     │ │
│  │     └─ Evita solapamientos                                           │ │
│  └────────────────────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────────────────┘
      │                 │                    │                    │
      │                 │                    │                    │
      ▼                 ▼                    ▼                    ▼
┌──────────────┐ ┌──────────────┐ ┌──────────────┐ ┌──────────────┐
│ MS-COURSE-  │ │   MS-USER    │ │ MS-SCHEDULE  │ │MS-ENVIRONMENT│
│ MANAGEMENT  │ │  (Docentes)  │ │  (Horarios)  │ │  (Aulas)     │
│             │ │              │ │              │ │              │
│ POST Courses│ │ POST Teachers│ │ POST Schedule│ │ GET Spaces   │
│ Assignments │ │              │ │              │ │              │
└──────────────┘ └──────────────┘ └──────────────┘ └──────────────┘
      │                 │                    │                    │
      └─────────────────┴────────────────────┴────────────────────┘
                        │
                        ▼
            ┌────────────────────────┐
            │  EUREKA SERVICE        │
            │  REGISTRY              │
            │ (Descubrimiento)       │
            │                        │
            │ http://8761/eureka/    │
            └────────────────────────┘
```

## Ciclo de Vida de un Curso

```
Excel Input (1 fila)
        │
        ▼
CargaPsicoExcelDTO
├─ nombre: "Comunicación Oral"
├─ ciclo: 1
├─ grupo: 1
├─ docente: "Apellido Nombre"
└─ aforo: 30
        │
        ▼
CreateCourseDTO
├─ name: "Comunicación Oral"
├─ code: "PSI-1-1-COM" (generado)
├─ totalHours: 5
├─ modalidad: "PRESENCIAL"
└─ idGroup: 1
        │
        ▼
[MS-COURSE-MANAGEMENT]
Crea: CourseEntity (idCourse = 101)
        │
        ▼
TeacherDTO
├─ name: "Nombre"
└─ email: "nombre.apellido@upeu.edu.pe"
        │
        ▼
[MS-USER]
Crea: TeacherEntity (idTeacher = 42)
        │
        ▼
CourseAssignmentDTO
├─ idTeacher: 42
└─ idCourse: 101
        │
        ▼
[MS-COURSE-MANAGEMENT]
Crea: CourseAssignmentEntity (idCourseAssignment = 5)
        │
        ▼
[x3] CreateScheduleDTO
├─ dayName: "LUNES"
├─ startTime: 08:00
├─ endTime: 09:30
├─ idAcademicSpace: 7
└─ idCourseAssignment: 5
        │
        ▼
[MS-SCHEDULE] [MS-ENVIRONMENT]
Crea: ScheduleEntity + Verifica Aula
        │
        ▼
✅ CURSO COMPLETAMENTE CONFIGURADO
```

## Algoritmo de Distribución - Diagrama de Flujo

```
INICIO: Lista de 224 cursos del Excel
        │
        ▼
┌───────────────────────┐
│ Agrupar por           │
│ (Ciclo, Grupo)        │
│ Resultado: 8 grupos   │
└────────────┬──────────┘
             │
             ▼
    ┌────────────────────────────┐
    │ Para cada GRUPO             │
    │ (ej: CICLO_1_GRUPO_1)       │
    └────────────┬────────────────┘
                 │
                 ▼
        ┌────────────────────────────┐
        │ Para cada CURSO en grupo   │
        │ (ej: 28 cursos)            │
        └────────────┬────────────────┘
                     │
                     ▼
            ┌────────────────────────────┐
            │ Obtener espacios disponibles
            │ Filtrar por capacidad      │
            │ (aforo >= estudiantes)     │
            └────────────┬────────────────┘
                         │
                         ▼
                ┌────────────────────────────┐
                │ Necesitar 3 horarios/semana │
                │ (LUNES, MIÉRCOLES, VIERNES) │
                └────────────┬────────────────┘
                             │
                             ▼
                    ┌────────────────────────────┐
                    │ Para cada DÍA (L,M,V)      │
                    └────────────┬────────────────┘
                                 │
                                 ▼
                        ┌────────────────────────────┐
                        │ Para cada AULA disponible  │
                        └────────────┬────────────────┘
                                     │
                                     ▼
                            ┌────────────────────────────┐
                            │ Para cada SLOT de tiempo   │
                            │ (08:00, 09:30, 11:00, ...) │
                            └────────────┬────────────────┘
                                         │
                                         ▼
                                ┌─────────────────────┐
                                │ ¿Slot ocupado?      │
                                └──┬──────────────┬──┘
                                   │ SÍ          │ NO
                                   │             │
                                   ▼             ▼
                            SIGUIENTE SLOT  ASIGNAR
                                   │         │
                                   │         ▼
                                   │   ┌──────────────────────┐
                                   │   │ Crear ScheduleDTO    │
                                   │   │ Marcar slot ocupado  │
                                   │   │ idAcademicSpace = X  │
                                   │   │ dayName = LUNES      │
                                   │   │ startTime = 08:00    │
                                   │   │ endTime = 09:30      │
                                   │   └──────────┬───────────┘
                                   │             │
                                   └─────────┬───┘
                                             │
                                             ▼
                                   ¿3 horarios asignados?
                                   YES / NO
                                     │       │
                                  SÍ │       │ NO
                                     │   SIGUIENTE AULA
                                     │       │
                                     ▼       └─→ [loop]
                                   SIGUIENTE
                                   CURSO
                                     │
                                     └──→ [loop]
                                        (repetir para
                                         cada curso)
                                             │
                                             ▼
                                  Todos los cursos
                                  procesados?
                                     │
                                     ▼
                                    FIN
                        ✅ 224 cursos creados
                        ✅ 672 horarios asignados
                        ✅ Sin conflictos
```

## Prevención de Conflictos - Ejemplo Real

```
LUNES:
  08:00-09:30 AULA 5: [CURSO A - CICLO 1] ← Slot ocupado
  08:00-09:30 AULA 6: [CURSO B - CICLO 1] ← Slot ocupado
  08:00-09:30 AULA 7: [DISPONIBLE] ← Asignar CURSO C aquí
  09:30-11:00 AULA 5: [DISPONIBLE]
  09:30-11:00 AULA 6: [CURSO D - CICLO 2] ← Slot ocupado
  ...

ScheduleTracker (Set):
  "LUNES_08:00_09:30_5" ✓ (ocupado)
  "LUNES_08:00_09:30_6" ✓ (ocupado)
  "LUNES_08:00_09:30_7" ✗ (vacío - ASIGNAR aquí)
  "LUNES_09:30_11:00_5" ✗ (vacío)
  "LUNES_09:30_11:00_6" ✓ (ocupado)
```

## Integración con Eureka

```
┌─────────────────────────────────────┐
│    EUREKA SERVER (8761)             │
│    Service Registry                 │
│                                     │
│  Registros:                         │
│  ├─ microservice-import       (8008)
│  ├─ microservice-course-mgmt  (8085)
│  ├─ microservice-user         (8086)
│  ├─ microservice-schedule     (8087)
│  ├─ microservice-environment  (8089)
│  └─ microservice-gateway      (8080)
└─────────────────────────────────────┘
           ▲       ▲       ▲
           │       │       │
      Heartbeat  Discovery Heartbeat
           │       │       │
    ┌──────┴───────┴───────┴──────┐
    │                              │
    ▼                              ▼
MS-IMPORT                    (Otros Microservicios)
Registrado ✓                 Registrados ✓
Heartbeat c/30s              Descubiertos automáticamente
```

## Base de Datos - Esquema Resultante

```
Después de procesar CARGA PSICO:

COURSE MANAGEMENT DB:
├─ courses
│  ├─ idCourse: 101-324 (224 registros)
│  ├─ name: "Comunicación Oral", ...
│  ├─ code: "PSI-1-1-COM", ...
│  └─ idGroup: 1-8 (varios ciclos/grupos)
│
├─ course_assignment
│  ├─ idCourseAssignment: 1-224
│  ├─ idTeacher: FK
│  └─ course_assignment_course: (relación muchos a muchos)
│
└─ teachers (parte del MS-USER)
   ├─ idTeacher: 1-150 (aprox)
   ├─ name: "Nombre", ...
   └─ email: "nombre.apellido@upeu.edu.pe", ...

SCHEDULE DB:
├─ schedule (672 registros = 224 cursos × 3 horarios)
│  ├─ idSchedule: 1-672
│  ├─ startTime: 08:00, 09:30, 11:00, ...
│  ├─ endTime: 09:30, 11:00, 12:30, ...
│  ├─ idAcademicSpace: 1-15
│  ├─ idCourseAssignment: FK
│  └─ idWeekName: LUNES, MARTES, ...
│
└─ week_name (referencia)
   ├─ LUNES, MARTES, MIÉRCOLES, JUEVES, VIERNES

ENVIRONMENT DB:
├─ academic_space (15 espacios)
│  ├─ idAcademicSpace: 1-15
│  ├─ spaceName: "AULA 101", "AULA 102", ...
│  ├─ capacity: 30-50 estudiantes
│  └─ idTypeAcademicSpace: AULA, LABORATORIO, ...
│
└─ type_academic_space
   ├─ AULA
   ├─ LABORATORIO
   └─ SALA DE VIDEOCONFERENCIA
```

---

**Última actualización**: 2026-04-28
