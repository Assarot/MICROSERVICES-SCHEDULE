# 📚 Datos de Prueba para Carga Académica

## Archivo: `CARGA_ACADEMICA_PRUEBA.txt`

Este archivo contiene 10 cursos de prueba que usan los docentes del archivo `DOCENTES_PRUEBA.txt`.

**Formato:** 16 columnas separadas por tabulación (ver abajo)

---

## 📊 Cursos de Prueba Incluidos

| Ciclo | Grupo | Nombre Curso              | Docente                     | Escuela                   |
| ----- | ----- | ------------------------- | --------------------------- | ------------------------- |
| 1     | 1     | Teoría del Aprendizaje    | Juan Pérez                  | Psicología Clínica        |
| 1     | 1     | Psicología del Desarrollo | María García López          | Psicología Clínica        |
| 1     | 2     | Métodos de Investigación  | Juan Tomás Pérez Del Aguila | Psicología Clínica        |
| 1     | 2     | Estadística Básica        | Carlos Rodríguez            | Psicología Clínica        |
| 2     | 1     | Psicopedagogía            | Ana Martínez Gómez          | Psicología Educativa      |
| 2     | 2     | Didáctica General         | Roberto López Flores        | Psicología Educativa      |
| 3     | 1     | Gestión Laboral           | Sofia Díaz                  | Psicología Organizacional |
| 3     | 1     | Clima Organizacional      | Luis Fernández              | Psicología Organizacional |
| 4     | 1     | Psicología Comunitaria    | Patricia Sánchez            | Psicología Social         |
| 4     | 1     | Cambio Social             | Miguel Quispe Condori       | Psicología Social         |

---

## 🗂️ Estructura de Columnas (16 en Total)

| #   | Nombre Columna          | Descripción                 | Ejemplo                      |
| --- | ----------------------- | --------------------------- | ---------------------------- |
| 1   | FACULTAD                | Nombre de la facultad       | Psicología                   |
| 2   | ESCUELA                 | Nombre de la escuela        | Psicología Clínica           |
| 3   | NOMBRE CURSO            | Nombre del curso            | Teoría del Aprendizaje       |
| 4   | MODO                    | Modo de enseñanza           | Presencial, Híbrido, Virtual |
| 5   | CICLO                   | Ciclo académico (número)    | 1, 2, 3, 4                   |
| 6   | GRUPO                   | Número del grupo            | 1, 2, 3                      |
| 7   | PLAN                    | Plan académico (año-ciclo)  | 2025-1                       |
| 8   | CREDITO                 | Número de créditos          | 3, 4                         |
| 9   | HT                      | Horas teóricas por semana   | 2, 3                         |
| 10  | HP                      | Horas prácticas por semana  | 2, 1                         |
| 11  | TOTAL HORAS             | Total de horas              | 4, 5                         |
| 12  | HORAS LECTIVAS          | Horas lectivas totales      | 64, 96                       |
| 13  | MODALIDAD               | Modalidad de clase          | Presencial, Virtual, Híbrido |
| 14  | DOCENTE                 | Nombre completo del docente | Juan Pérez                   |
| 15  | AFORO POR CURSO Y GRUPO | Número de estudiantes       | 40, 35, 25                   |
| 16  | AMBIENTE ESPECIALIZADO  | Tipo de ambiente            | Aula, Laboratorio, Taller    |

---

## 🛠️ Cómo Crear el Excel desde Este Archivo

### **Opción 1: Copiar y pegar en Excel (2 minutos)** ⭐ RECOMENDADO

1. Abre [CARGA_ACADEMICA_PRUEBA.txt](CARGA_ACADEMICA_PRUEBA.txt)
2. Selecciona todo el contenido (Ctrl+A)
3. Cópialo (Ctrl+C)
4. Abre Microsoft Excel
5. En celda A1, pega (Ctrl+V)
6. Excel detectará automáticamente las 16 columnas
7. Guarda como **carga_academica_prueba.xlsx**

### **Opción 2: Importar como Texto Delimitado**

1. En Excel: Datos → De archivo de texto
2. Selecciona [CARGA_ACADEMICA_PRUEBA.txt](CARGA_ACADEMICA_PRUEBA.txt)
3. Marca: "Delimitados por → Tabulación"
4. Finaliza y guarda como .xlsx

---

## ✅ Validación Antes de Cargar

Antes de enviar el Excel al importador, verifica:

- ✓ **Exactamente 16 columnas**
- ✓ **Header en fila 1** con los nombres de las columnas
- ✓ **Datos empiezan en fila 2**
- ✓ **Ciclo y Grupo son números** (no texto)
- ✓ **DOCENTE coincide con un docente importado** (ej: "Juan Pérez")
- ✓ **AFORO es número mayor a 0**
- ✓ **Ninguna fila vacía en medio**
- ✓ **Extensión es .xlsx**

---

## 🧪 Flujo de Prueba Completo

### **Paso 1: Importar Docentes**

```bash
curl -X POST -F "file=@docentes_prueba.xlsx" \
  http://localhost:8008/api/v1/import/upload-teachers
```

**Respuesta esperada:**

```json
{
  "status": "SUCCESS",
  "message": "Docentes importados exitosamente",
  "filename": "docentes_prueba.xlsx"
}
```

### **Paso 2: Procesar Carga Académica**

```bash
curl -X POST -F "file=@carga_academica_prueba.xlsx" \
  http://localhost:8008/api/v1/import/upload-carga-psico
```

**Respuesta esperada:**

```json
{
  "status": "SUCCESS",
  "message": "Carga Psico procesada exitosamente",
  "filename": "carga_academica_prueba.xlsx",
  "timestamp": 1719604800000
}
```

### **Paso 3: Verificar en Logs**

Busca líneas como:

```
✓ 10 cursos leídos del Excel
✓ 15 espacios académicos disponibles
✓ 4 grupos diferentes identificados
✓ Curso creado: Teoría del Aprendizaje (ID: 125)
✓ Horario asignado: LUNES 08:00 - 09:30 en sala AULA 101
✓ Cursos creados: 10
✓ Horarios asignados: 30
```

### **Paso 4: Reimportar Idempotencia**

Carga **el mismo archivo nuevamente**:

```bash
curl -X POST -F "file=@carga_academica_prueba.xlsx" \
  http://localhost:8008/api/v1/import/upload-carga-psico
```

**Respuesta esperada:**

```
✓ Curso duplicado omitido: Teoría del Aprendizaje
✓ Curso duplicado omitido: Psicología del Desarrollo
...
✓ Cursos creados: 0
✓ Horarios asignados: 0
```

---

## 🎯 Casos de Prueba Avanzados

### **Caso 1: Docente no encontrado**

Si cambias el nombre del docente a uno que NO existe en el catálogo:

- Esperado: Se omite el curso, ves en logs "Docente no encontrado en catálogo maestro"

### **Caso 2: Nombre compuesto**

Los datos ya incluyen:

- "Juan Tomás Pérez Del Aguila" (4 palabras)
- "García López", "Quispe Condori" (nombres/apellidos compuestos)
- Esperado: Se encuentran correctamente

### **Caso 3: Ciclos diferentes**

Los datos incluyen ciclos 1, 2, 3 y 4. Cada ciclo tiene grupos diferentes:

- Ciclo 1: Grupos 1, 2
- Ciclo 2: Grupos 1, 2
- Ciclo 3: Grupos 1
- Ciclo 4: Grupos 1
- Esperado: Se agrupan correctamente y se asignan horarios

### **Caso 4: Validar Aforos**

Los datos tienen aforos diferentes (40, 35, 30, 25):

- Esperado: El sistema verifica que el ambiente tenga suficiente capacidad

---

## 📋 Comparación: Este Archivo vs. Carga Real

| Aspecto                 | Prueba       | Producción   |
| ----------------------- | ------------ | ------------ |
| Número de cursos        | 10           | 200+         |
| Número de docentes      | 10           | 50+          |
| Tiempo de procesamiento | ~10 segundos | ~2-5 minutos |
| Salones necesarios      | 5-10         | 20-30        |
| Ciclos cubiertos        | 1-4          | 1-6          |

---

## 💡 Tips

1. **Antes de cargar los docentes:** Asegúrate que los ambientes académicos ya existan en MS-ENVIRONMENT
2. **Verifica los nombres:** Los docentes del Excel deben coincidir EXACTAMENTE con los importados
3. **Normalización:** El sistema normaliza tildes y espacios, pero es mejor ser consistente
4. **Horarios:** Se asignan automáticamente lunes-miércoles-viernes, 8:00 AM a 5:00 PM en slots de 90 minutos

---

## 🔍 Troubleshooting

| Problema                     | Causa                                    | Solución                            |
| ---------------------------- | ---------------------------------------- | ----------------------------------- |
| "Docente no encontrado"      | El nombre no coincide con la importación | Verifica ortografía exacta          |
| "Curso duplicado omitido"    | Se cargó el mismo archivo dos veces      | Normal en idempotencia              |
| "No hay espacios académicos" | MS-ENVIRONMENT sin datos                 | Crea ambientes antes                |
| "Connection refused"         | Microservicio no está corriendo          | Inicia MS-COURSE, MS-SCHEDULE, etc. |

---

**Estado:** Listo para pruebas ✅
