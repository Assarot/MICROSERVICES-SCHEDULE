# 📊 Importación de Datos Primarios Completos - Microservice Import

## 🎯 Propósito

El microservicio `microservice-import` actúa como **orquestador completo** y maneja la importación de todos los datos primarios necesarios para el funcionamiento del sistema académico, incluyendo los ambientes académicos físicos.

## 📁 Archivos Agregados/Modificados

### DTOs Nuevos/Actualizados

- `StateDTO.java` - Estados de ambientes
- `TypeAcademicSpaceDTO.java` - Tipos de espacios académicos
- `BuildingDTO.java` - Edificios
- `FloorDTO.java` - Pisos
- `PrimaryDataImportResult.java` - Resultado de importación (agregado `academicSpacesCreated`)

### Servicio Actualizado

- `PrimaryDataImportService.java` - Lógica completa de importación con múltiples hojas

### Cliente Feign Expandido

- `EnvironmentClient.java` - Agregados endpoints para crear ambientes académicos

### Controlador Actualizado

- `ExcelUpload.java` - Nuevo endpoint `/upload-primary-data`

## 🔗 Endpoint Nuevo

```
POST /api/v1/import/upload-primary-data
```

**Body**: `multipart/form-data` con campo `file`

## 📋 Formato Excel con Múltiples Hojas

### 📄 **Hoja 1: "Datos Básicos"** (o primera hoja si no tiene nombre específico)

Encabezados en fila 1 para datos maestros:

| Estado           | Estado Activo | Tipo Academico | Tipo Activo | Edificio   | Edificio Activo | Piso Numero | Piso Activo | Edificio Piso |
| ---------------- | ------------- | -------------- | ----------- | ---------- | --------------- | ----------- | ----------- | ------------- |
| Disponible       | A             | Aula           | A           | Pabellón A | A               | 1           | A           | Pabellón A    |
| Ocupado          | A             | Laboratorio    | A           | Pabellón A | A               | 2           | A           | Pabellón A    |
| En Mantenimiento | A             | Taller         | A           | Pabellón B | A               | 1           | A           | Pabellón B    |

### 📄 **Hoja 2: "Ambientes Académicos"** (o cualquier hoja con "ambiente", "espacio", "academic" en el nombre)

Encabezados en fila 1 para espacios físicos:

| Espacio    | Observacion            | Ubicacion    | Capacidad | Estado     | Tipo        | Edificio   | Piso |
| ---------- | ---------------------- | ------------ | --------- | ---------- | ----------- | ---------- | ---- |
| AULA 101   | Proyector disponible   | Planta baja  | 40        | Disponible | Aula        | Pabellón A | 1    |
| LAB 201    | Computadoras i7        | Segundo piso | 25        | Disponible | Laboratorio | Pabellón A | 2    |
| TALLER 301 | Herramientas completas | Tercer piso  | 20        | Disponible | Taller      | Pabellón B | 1    |

## 🔍 Identificación de Hojas

El sistema identifica automáticamente el tipo de hoja por su nombre:

- **Datos Básicos**: Contiene "básico", "basico", "datos" o es la primera hoja
- **Ambientes Académicos**: Contiene "ambiente", "espacio", "academic"

## 📊 Encabezados Alternativos Aceptados

### Para Datos Básicos:

- **Estado**: `state`, `statename`, `estado`
- **Estado Activo**: `stateactive`, `stateisactive`, `estadoactivo`
- **Tipo Academico**: `typeacademicspace`, `tipoacademico`, `typeacademicspacename`
- **Tipo Activo**: `typeactive`, `typeisactive`, `tipoactivo`
- **Edificio**: `building`, `buildingname`, `edificio`
- **Edificio Activo**: `buildingactive`, `buildingisactive`, `edificioactivo`
- **Piso Numero**: `floornumber`, `floor`, `piso`, `pisonumero`
- **Piso Activo**: `flooractive`, `floorisactive`, `pisoactivo`
- **Edificio Piso**: `floorbuilding`, `buildingforfloor`, `edificiopiso`

### Para Ambientes Académicos:

- **Espacio**: `spacename`, `academicspace`, `ambiente`, `espacio`
- **Observacion**: `observation`, `observacion`, `observaciones`
- **Ubicacion**: `location`, `ubicacion`, `lugar`
- **Capacidad**: `capacity`, `capacidad`, `aforo`
- **Estado**: `state`, `statename`, `estado`
- **Tipo**: `type`, `typename`, `tipo`, `tipoacademico`
- **Edificio**: `building`, `buildingname`, `edificio`
- **Piso**: `floor`, `floornumber`, `piso`

## 🔄 Proceso de Importación

### **Fase 1: Procesamiento de Datos Básicos**

1. Lee hoja identificada como "Datos Básicos"
2. Carga datos existentes desde `MS-ENVIROMENT`
3. Procesa fila por fila creando/actualizando:
   - Estados
   - Tipos de espacios académicos
   - Edificios
   - Pisos

### **Fase 2: Procesamiento de Ambientes Académicos**

1. Lee hoja identificada como "Ambientes Académicos"
2. Carga datos existentes de ambientes
3. Para cada fila:
   - Busca referencias (Estado, Tipo, Edificio+Piso) por nombre
   - Crea ambiente académico con todas las relaciones
   - Valida capacidad (default 30 si no especificada)

## 📊 Respuesta Exitosa

```json
{
  "status": "SUCCESS",
  "message": "Importación completada correctamente",
  "statesCreated": 3,
  "typesCreated": 3,
  "buildingsCreated": 5,
  "floorsCreated": 20,
  "academicSpacesCreated": 50
}
```

## ⚙️ Características Avanzadas

- **Idempotente**: No crea duplicados, actualiza si existe
- **Flexible**: Acepta múltiples formatos de encabezado
- **Validación**: Verifica existencia antes de crear
- **Normalización**: Maneja mayúsculas/minúsculas y espacios
- **Referencias**: Resuelve relaciones por nombre automáticamente
- **Múltiples Hojas**: Procesa todas las hojas del Excel

## 🧪 Archivo de Prueba Recomendado

### Hoja 1: "Datos Básicos"

```csv
Estado,Estado Activo,Tipo Academico,Tipo Activo,Edificio,Edificio Activo,Piso Numero,Piso Activo,Edificio Piso
Disponible,A,Aula,A,Pabellón A,A,1,A,Pabellón A
Ocupado,A,Laboratorio,A,Pabellón A,A,2,A,Pabellón A
En Mantenimiento,A,Taller,A,Pabellón B,A,1,A,Pabellón B
,A,Laboratorio de Redes,A,Pabellón B,A,2,A,Pabellón B
,A,Laboratorio de Software,A,Pabellón C,A,1,A,Pabellón C
```

### Hoja 2: "Ambientes Académicos"

```csv
Espacio,Observacion,Ubicacion,Capacidad,Estado,Tipo,Edificio,Piso
AULA 101,Proyector y pizarra digital,Planta baja,45,Disponible,Aula,Pabellón A,1
AULA 102,Pizarra tradicional,Planta baja,40,Disponible,Aula,Pabellón A,1
LAB 201,30 computadoras i7,Segundo piso,30,Disponible,Laboratorio,Pabellón A,2
LAB 202,25 computadoras i5,Segundo piso,25,Disponible,Laboratorio de Redes,Pabellón A,2
TALLER 301,Herramientas completas,Tercer piso,20,Disponible,Taller,Pabellón B,1
```

## 📈 Secuencia de Importación Recomendada

1. **`/upload-primary-data`** → Datos maestros + ambientes
2. **`/upload-teachers`** → Docentes
3. **`/upload-carga-psico`** → Carga académica

## 🔧 Dependencias

- Apache POI 5.2.3 (ya incluido)
- Feign Client para `MS-ENVIROMENT`
- Spring Boot Web

## 💡 Ventajas del Enfoque Multi-Hoja

1. **Separación lógica**: Datos maestros vs datos físicos
2. **Flexibilidad**: Una hoja puede tener solo datos básicos, otra solo ambientes
3. **Mantenibilidad**: Fácil agregar nuevas entidades en futuras hojas
4. **Rendimiento**: Procesa todas las hojas en una sola operación
5. **Escalabilidad**: Fácil extender para más tipos de datos
