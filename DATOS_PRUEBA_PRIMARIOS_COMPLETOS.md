# 📋 Datos de Prueba - Importación Primaria Completa

## 📄 Archivo Excel de Ejemplo: `datos_primarios_completos.xlsx`

### Hoja 1: "Datos Básicos"

```
Estado,Estado Activo,Tipo Academico,Tipo Activo,Edificio,Edificio Activo,Piso Numero,Piso Activo,Edificio Piso
Disponible,A,Aula,A,Pabellón A,A,1,A,Pabellón A
Ocupado,A,Laboratorio,A,Pabellón A,A,2,A,Pabellón A
En Mantenimiento,A,Taller,A,Pabellón A,A,3,A,Pabellón A
,A,Laboratorio de Redes,A,Pabellón A,A,4,A,Pabellón A
,A,Laboratorio de Software,A,Pabellón B,A,1,A,Pabellón B
,A,Laboratorio de Cómputo,A,Pabellón B,A,2,A,Pabellón B
,A,Aula Magna,A,Pabellón B,A,3,A,Pabellón B
,A,Biblioteca,A,Pabellón C,A,1,A,Pabellón C
,A,Auditorio,A,Pabellón C,A,2,A,Pabellón C
,A,Gimnasio,A,Pabellón D,A,1,A,Pabellón D
```

### Hoja 2: "Ambientes Académicos"

```
Espacio,Observacion,Ubicacion,Capacidad,Estado,Tipo,Edificio,Piso
AULA 101,Proyector HD y pizarra digital,Planta baja - Ala Norte,45,Disponible,Aula,Pabellón A,1
AULA 102,Pizarra tradicional y ventiladores,Planta baja - Ala Sur,40,Disponible,Aula,Pabellón A,1
AULA 103,Aula pequeña para seminarios,Planta baja - Ala Este,25,Disponible,Aula,Pabellón A,1
AULA 104,Aula con aire acondicionado,Planta baja - Ala Oeste,35,Disponible,Aula,Pabellón A,1
LAB 201,30 computadoras i7 + proyector,Segundo piso - Laboratorio principal,30,Disponible,Laboratorio,Pabellón A,2
LAB 202,25 computadoras i5 + impresora,Segundo piso - Laboratorio secundario,25,Disponible,Laboratorio de Redes,Pabellón A,2
LAB 203,20 workstations para desarrollo,Segundo piso - Laboratorio de software,20,Disponible,Laboratorio de Software,Pabellón A,2
AULA 301,Seminarios y conferencias,Tercer piso - Sala de reuniones,50,Disponible,Aula Magna,Pabellón A,3
TALLER 302,Electrónica y soldadura,Tercer piso - Taller técnico,15,Ocupado,Taller,Pabellón A,3
LAB 101,Computadoras básicas,Primer piso - Sala de cómputo,30,Disponible,Laboratorio de Cómputo,Pabellón B,1
BIB 201,Biblioteca central con zona de estudio,Segundo piso - Biblioteca,100,Disponible,Biblioteca,Pabellón B,2
AUD 301,Auditorio principal con escenario,Tercer piso - Auditorio,200,Disponible,Auditorio,Pabellón B,3
GIM 101,Cancha polideportiva y vestuarios,Planta baja - Gimnasio,150,Disponible,Gimnasio,Pabellón C,1
AULA 501,Aula administrativa,Quinto piso - Administración,30,Disponible,Aula,Pabellón C,2
```

## 🧪 Resultado Esperado

Después de subir este archivo, deberías obtener:

```json
{
  "status": "SUCCESS",
  "message": "Importación completada correctamente",
  "statesCreated": 3,
  "typesCreated": 8,
  "buildingsCreated": 4,
  "floorsCreated": 10,
  "academicSpacesCreated": 14
}
```

## 📊 Verificación en Base de Datos

### Estados creados:

- Disponible
- Ocupado
- En Mantenimiento

### Tipos de espacios creados:

- Aula
- Laboratorio
- Taller
- Laboratorio de Redes
- Laboratorio de Software
- Laboratorio de Cómputo
- Aula Magna
- Biblioteca
- Auditorio
- Gimnasio

### Edificios creados:

- Pabellón A (4 pisos)
- Pabellón B (3 pisos)
- Pabellón C (2 pisos)
- Pabellón D (1 piso)

### Ambientes académicos creados:

- 14 espacios físicos con todas las relaciones correctas

## 🔍 Validaciones Realizadas

1. **Referencias correctas**: Cada ambiente apunta al estado, tipo, edificio y piso correctos
2. **Capacidades apropiadas**: Desde 15 hasta 200 personas según el tipo de espacio
3. **Ubicaciones descriptivas**: Información clara sobre la localización física
4. **Estados apropiados**: Mayoría disponible, algunos ocupados para testing
5. **Observaciones útiles**: Información adicional sobre equipamiento y características

## 📋 Cómo Crear el Archivo Excel

1. **Crear nuevo archivo Excel** llamado `datos_primarios_completos.xlsx`
2. **Hoja 1**: Nombrarla "Datos Básicos" y copiar el contenido CSV
3. **Hoja 2**: Nombrarla "Ambientes Académicos" y copiar el contenido CSV
4. **Guardar como**: Excel Workbook (.xlsx)

## 🚀 Comando de Prueba

```bash
curl -X POST http://localhost:8008/api/v1/import/upload-primary-data \
  -F "file=@datos_primarios_completos.xlsx"
```

Este archivo de prueba te permitirá verificar que:

- ✅ Se crean todos los datos maestros
- ✅ Se crean todos los ambientes académicos
- ✅ Las relaciones entre entidades funcionan correctamente
- ✅ El sistema maneja múltiples hojas correctamente
- ✅ La carga académica posterior podrá encontrar espacios disponibles
