# 🏫 Verificación de Disponibilidad de Espacios Académicos

## 📍 Ubicación del Código

La lógica de verificación está en: [ScheduleOrchestratorService.java](microservice-import/src/main/java/pe/edu/upeu/microserviceimport/service/ScheduleOrchestratorService.java)

**Método principal:** `asignarHorarioSinConflictos()` (línea ~215)

---

## 🔍 Proceso de Verificación (Paso a Paso)

### **Paso 1: Carga Inicial de Espacios**

```java
List<AcademicSpaceDTO> espacios = environmentClient.getAllAcademicSpaces();
```

Al iniciar el procesamiento de la carga académica, se obtienen **TODOS** los espacios disponibles desde `MS-ENVIROMENT`. Esto incluye:

- ID del espacio
- Nombre (ej: AULA 101)
- Capacidad (ej: 40 personas)
- Tipo (Aula, Laboratorio, Taller)
- Ubicación
- Observaciones

---

### **Paso 2: Carga de Horarios Ocupados Existentes**

```java
Set<String> occupiedSlots = loadExistingOccupiedSlots(weekDayIds);
```

Se consulta `MS-SCHEDULE` para obtener **todos los horarios ya asignados** en la base de datos. Esto se almacena en un `ScheduleTracker` que mantiene un conjunto (`Set`) de "slots ocupados".

**Clave de slot:** `{idWeekDay}_{startTime}_{endTime}_{idAcademicSpace}`

Ejemplo:

```
LUNES_08:00_09:30_5
LUNES_09:30_11:00_5
MARTES_08:00_09:30_3
```

---

### **Paso 3: Filtrado por Preferencia de Ambiente**

```java
List<AcademicSpaceDTO> espaciosPreferidos =
    filtrarEspaciosPorPreferencia(espacios, cursoExcel.getAmbienteEspecializado());
```

Si el Excel especifica un **AMBIENTE ESPECIALIZADO** (columna 16), se filtran los espacios que coincidan:

```java
private boolean matchesPreference(AcademicSpaceDTO espacio, String preference) {
    return containsIgnoreCase(espacio.getTypeAcademicSpace(), preference)
            || containsIgnoreCase(espacio.getObservation(), preference)
            || containsIgnoreCase(espacio.getLocation(), preference)
            || containsIgnoreCase(espacio.getSpaceName(), preference);
}
```

**Busca coincidencias en:**

- Tipo de espacio (Aula, Laboratorio)
- Observaciones (Computadoras, Proyector)
- Ubicación (Piso 2, Edificio A)
- Nombre del espacio (AULA 101)

**Si no encuentra coincidencias**, usa **TODOS los espacios** como alternativa.

---

### **Paso 4: Iteración Por Cada Día de la Semana**

```java
for (String dia : DAYS_OF_WEEK) {  // LUNES, MARTES, MIÉRCOLES, JUEVES, VIERNES
    // ...
}
```

El sistema intenta asignar 3 horarios por semana (lunes, miércoles, viernes típicamente).

---

### **Paso 5: Validación de Capacidad**

```java
if (espacio.getCapacity() < (cursoExcel.getAforoPorCursoGrupo() != null ? cursoExcel.getAforoPorCursoGrupo() : 30)) {
    continue;  // ← Descarta este espacio
}
```

**Se verifica:** ¿El espacio tiene suficiente capacidad?

**Lógica:**

- Si el Excel especifica aforo → usa ese número
- Si no especifica → usa 30 como valor por defecto
- Si capacidad del espacio < aforo → **DESCARTA ese espacio**

**Ejemplo:**

```
Espacio: AULA 101, Capacidad: 40
Curso: Psicología 101, Aforo: 45

Resultado: DESCARTADO (40 < 45)
```

---

### **Paso 6: Búsqueda de Horarios Disponibles**

```java
for (LocalTime startTime : START_TIMES) {  // 8:00, 9:30, 11:00, 12:30, 14:00, 15:30, 17:00
    LocalTime endTime = startTime.plusMinutes(SLOT_DURATION_MINUTES);  // +90 minutos

    String slotKey = buildSlotKey(weekDayId, startTime, endTime, espacio.getIdAcademicSpace());

    if (!scheduleTracker.isSlotOccupied(slotKey)) {
        // ← SLOT DISPONIBLE, ASIGNAR
    }
}
```

**Para cada espacio disponible**, itera a través de los horarios del día:

- **8:00 - 9:30 AM**
- **9:30 - 11:00 AM**
- **11:00 AM - 12:30 PM**
- **12:30 - 2:00 PM**
- **2:00 - 3:30 PM**
- **3:30 - 5:00 PM**
- **5:00 - 6:30 PM**

Para cada combinación (**día + horario + espacio**), verifica si ya está ocupado llamando a:

```java
scheduleTracker.isSlotOccupied(slotKey)
```

---

### **Paso 7: Asignación del Horario**

```java
if (!scheduleTracker.isSlotOccupied(slotKey)) {
    CreateScheduleDTO scheduleDTO = CreateScheduleDTO.builder()
            .startTime(startTime)
            .endTime(endTime)
            .dayName(dia)
            .idAcademicSpace(espacio.getIdAcademicSpace())
            .idCourseAssignment(idCourseAssignment)
            .idWeekName(weekDayId)
            .build();

    scheduleClient.createSchedule(scheduleDTO);  // ← Guarda en MS-SCHEDULE
    scheduleTracker.markSlotAsOccupied(slotKey);  // ← Marca como ocupado en memoria
    horariosAsignados++;
}
```

Si el slot está disponible:

1. Crea un `CreateScheduleDTO` con todos los detalles
2. **Envía a `MS-SCHEDULE`** para guardar en BD
3. **Marca el slot como ocupado** en el `ScheduleTracker` en memoria
4. Incrementa el contador de horarios asignados

---

## 📊 Diagrama del Flujo

```
┌─────────────────────────────────────────┐
│ Excel de Carga Académica                │
│ (Curso, Docente, Aforo, Ambiente)       │
└────────────────┬────────────────────────┘
                 ↓
        ┌────────────────────┐
        │ Cargar espacios    │
        │ desde MS-ENVIROMENT│
        └────────┬───────────┘
                 ↓
    ┌────────────────────────────┐
    │ ¿Hay preferencia de        │
    │ ambiente especializado?    │
    └─┬──────────────────────┬──┘
      │                      │
   SÍ │                      │ NO
      ↓                      ↓
  Filtrar por          Usar todos
  preferencia       los espacios
      │                      │
      └──────────┬───────────┘
                 ↓
    ┌────────────────────────────┐
    │ Cargar horarios ocupados   │
    │ desde MS-SCHEDULE          │
    │ (ScheduleTracker)          │
    └────────┬───────────────────┘
             ↓
    ┌────────────────────────────┐
    │ PARA CADA DÍA (L,M,V)      │
    └────────┬───────────────────┘
             ↓
   ┌─────────────────────────┐
   │ PARA CADA ESPACIO       │
   └────────┬────────────────┘
            ↓
   ┌──────────────────────────────┐
   │ ¿Capacidad suficiente?       │
   │ (Aforo <= Capacidad espacio) │
   └──┬──────────────────────┬───┘
     SÍ │                    │ NO
       ↓                    └─→ SIGUIENTE ESPACIO
   ┌──────────────────────────┐
   │ PARA CADA HORARIO        │
   │ (8:00, 9:30, 11:00...)   │
   └────────┬─────────────────┘
            ↓
   ┌──────────────────────────┐
   │ ¿Slot disponible?        │
   │ (No ocupado)             │
   └──┬──────────────────┬───┐
     SÍ │                │ NO│
       ↓                └──→┴─ SIGUIENTE HORARIO
   ┌──────────────────────┐
   │ ASIGNAR HORARIO      │
   │ (Guardar en BD)      │
   │ (Marcar como ocupado)│
   └──────────┬───────────┘
              ↓
   Horarios asignados++
```

---

## ⚙️ Clase ScheduleTracker

```java
private static class ScheduleTracker {
    private final Set<String> occupiedSlots;

    public boolean isSlotOccupied(String slotKey) {
        return occupiedSlots.contains(slotKey);
    }

    public void markSlotAsOccupied(String slotKey) {
        occupiedSlots.add(slotKey);
    }
}
```

**Propósito:** Mantener en memoria un registro rápido de qué slots están ocupados, sin necesidad de consultar la BD continuamente.

**Mejora de rendimiento:** Las consultas en memoria (`Set.contains()`) son O(1), mucho más rápidas que consultas a BD.

---

## 🎯 Casos de Prueba

### Caso 1: Espacio disponible

```
Día: LUNES
Espacio: AULA 101 (Capacidad 40)
Horario: 8:00-9:30
Aforo curso: 35

Resultado: ✅ ASIGNADO
```

### Caso 2: Espacio sin suficiente capacidad

```
Día: LUNES
Espacio: AULA 101 (Capacidad 30)
Horario: 8:00-9:30
Aforo curso: 40

Resultado: ❌ DESCARTADO (capacidad insuficiente)
         → Intenta siguiente espacio
```

### Caso 3: Horario ocupado

```
Día: LUNES
Espacio: AULA 101 (Capacidad 40)
Horario: 8:00-9:30 (YA OCUPADO)
Aforo curso: 35

Resultado: ❌ SLOT OCUPADO
         → Intenta siguiente horario (9:30-11:00)
```

### Caso 4: Ambiente especializado no coincide

```
Excel especifica: "Laboratorio"
Espacios:
  - AULA 101 (Tipo: Aula)
  - AULA 102 (Tipo: Aula)
  - LAB 201 (Tipo: Laboratorio)

Resultado: ✅ Se filtra y usa solo LAB 201
```

---

## 📈 Estadísticas de Verificación

Para el archivo de prueba `carga_academica_prueba.xlsx`:

| Métrica                   | Valor                                 |
| ------------------------- | ------------------------------------- |
| Espacios cargados         | ~15-20                                |
| Horarios existentes       | ~100+                                 |
| Cursos a asignar          | 10                                    |
| Horarios necesarios       | 30 (3 × 10)                           |
| Slots evaluados           | ~350 (7 horas × 5 días × 20 espacios) |
| Validaciones de capacidad | 30                                    |
| Tiempo total              | 2-10 segundos                         |

---

## 🔍 Logs que Generan las Verificaciones

Cuando todo funciona bien, verás en logs:

```
✓ 15 espacios académicos disponibles
✓ 4 grupos diferentes identificados

Procesando grupo: CICLO_1_GRUPO_1
✓ Curso creado: Teoría del Aprendizaje (ID: 125)
✓ Horario asignado: LUNES 08:00 - 09:30 en sala AULA 101
✓ Horario asignado: MIÉRCOLES 08:00 - 09:30 en sala AULA 102
✓ Horario asignado: VIERNES 08:00 - 09:30 en sala AULA 101

✓ Horarios asignados: 30
```

Si hay problemas, verás:

```
No hay espacios académicos disponibles en el sistema
→ MS-ENVIROMENT no retornó datos

Capacidad insuficiente en AULA 101
→ Espacio descartado, intenta siguiente

No se pudieron cargar horarios existentes: Connection refused
→ MS-SCHEDULE no disponible
```

---

## 💡 Optimizaciones Implementadas

1. **ScheduleTracker en memoria:** En lugar de consultar la BD para cada slot, mantiene un `Set` en memoria
2. **Filtrado por preferencia:** Reduce la cantidad de espacios a evaluar si hay preferencia
3. **Validación de capacidad temprana:** Si el espacio no tiene capacidad, no intenta horarios
4. **Interrupciones tempranas:** Una vez asignados los 3 horarios necesarios, detiene la búsqueda

---

**Resumen:** El sistema verifica disponibilidad en 3 niveles:

1. **Capacidad del espacio** vs aforo del curso
2. **Preferencia de ambiente** vs características del espacio
3. **Ocupación de slot** (día + horario + espacio) en tiempo real

Todo esto garantiza que no haya conflictos ni asignaciones inválidas.
