# 📋 Datos de Prueba para Importar Maestros

## Archivo: `DOCENTES_PRUEBA.txt`

Este archivo contiene datos tabulados con los 10 docentes de ejemplo para hacer pruebas del importador.

**Formato:** Nombre | Apellido | Email (separados por tabulación)

---

## 📊 Docentes de Prueba Incluidos

| Nombre     | Apellido         | Email                        |
| ---------- | ---------------- | ---------------------------- |
| Juan       | Pérez            | juan.perez@upeu.edu.pe       |
| María      | García López     | maria.garcia@upeu.edu.pe     |
| Juan Tomás | Pérez Del Aguila | juan.tomas@upeu.edu.pe       |
| Carlos     | Rodríguez        | carlos.rodriguez@upeu.edu.pe |
| Ana        | Martínez Gómez   | ana.martinez@upeu.edu.pe     |
| Roberto    | López Flores     | roberto.lopez@upeu.edu.pe    |
| Sofia      | Díaz             | sofia.diaz@upeu.edu.pe       |
| Luis       | Fernández        | luis.fernandez@upeu.edu.pe   |
| Patricia   | Sánchez          | patricia.sanchez@upeu.edu.pe |
| Miguel     | Quispe Condori   | miguel.quispe@upeu.edu.pe    |

---

## 🛠️ Cómo Crear el Excel desde Este Archivo

### **Opción 1: Copiar y pegar en Excel (2 minutos)**

1. Abre [DOCENTES_PRUEBA.txt](DOCENTES_PRUEBA.txt)
2. Selecciona todo el contenido (Ctrl+A)
3. Cópialo (Ctrl+C)
4. Abre Microsoft Excel
5. Pega en la celda A1 (Ctrl+V)
6. Excel detectará automáticamente que son 3 columnas
7. Guarda el archivo como **docentes_prueba.xlsx** (Archivo → Guardar como → Formato Excel)

### **Opción 2: Usar Excel importar de texto (alternativa)**

1. En Excel: Datos → De Archivo de Texto
2. Selecciona [DOCENTES_PRUEBA.txt](DOCENTES_PRUEBA.txt)
3. En el asistente, marca: "Delimitados por → Tabulación"
4. Finaliza y guarda como .xlsx

### **Opción 3: Script PowerShell (automático)**

Si quieres un comando que lo haga directamente en PowerShell, ejecuta:

```powershell
$excel = New-Object -ComObject Excel.Application
$excel.Visible = $true
$workbook = $excel.Workbooks.Add()
$worksheet = $workbook.Sheets.Item(1)

# Docentes
$data = @(
    @("Juan", "Pérez", "juan.perez@upeu.edu.pe"),
    @("María", "García López", "maria.garcia@upeu.edu.pe"),
    @("Juan Tomás", "Pérez Del Aguila", "juan.tomas@upeu.edu.pe"),
    @("Carlos", "Rodríguez", "carlos.rodriguez@upeu.edu.pe"),
    @("Ana", "Martínez Gómez", "ana.martinez@upeu.edu.pe"),
    @("Roberto", "López Flores", "roberto.lopez@upeu.edu.pe"),
    @("Sofia", "Díaz", "sofia.diaz@upeu.edu.pe"),
    @("Luis", "Fernández", "luis.fernandez@upeu.edu.pe"),
    @("Patricia", "Sánchez", "patricia.sanchez@upeu.edu.pe"),
    @("Miguel", "Quispe Condori", "miguel.quispe@upeu.edu.pe")
)

# Headers
$worksheet.Cells.Item(1, 1) = "Nombre"
$worksheet.Cells.Item(1, 2) = "Apellido"
$worksheet.Cells.Item(1, 3) = "Email"

# Filas
$row = 2
foreach ($d in $data) {
    $worksheet.Cells.Item($row, 1) = $d[0]
    $worksheet.Cells.Item($row, 2) = $d[1]
    $worksheet.Cells.Item($row, 3) = $d[2]
    $row++
}

# Ancho de columnas
$worksheet.Columns(1).ColumnWidth = 15
$worksheet.Columns(2).ColumnWidth = 25
$worksheet.Columns(3).ColumnWidth = 35

# Guardar
$workbook.SaveAs("$env:USERPROFILE\Desktop\docentes_prueba.xlsx")
$excel.Quit()
Write-Host "✓ Archivo guardado en Desktop"
```

---

## ✅ Validación Antes de Cargar

Antes de enviar el Excel al importador, verifica:

- ✓ **Header en fila 1:** Nombre | Apellido | Email
- ✓ **Datos empiezan en fila 2**
- ✓ **Ninguna fila vacía en medio**
- ✓ **Todos los emails tienen @**
- ✓ **Extensión es .xlsx** (no .xls, no .csv)
- ✓ **Máximo 10 MB de tamaño**

---

## 🧪 Casos de Prueba

### **Caso 1: Importar inicial**

- Carga `docentes_prueba.xlsx`
- Esperado: Se importan 10 docentes sin errores

### **Caso 2: Reimportar el mismo archivo**

- Carga el mismo archivo de nuevo
- Esperado: No se duplican, dice "Docente ya existente"

### **Caso 3: Agregar más docentes**

- Crea un Excel con docentes nuevos (ej: Rosa, Carlos)
- Carga el archivo
- Esperado: Se agregan solo los nuevos, ignora duplicados

### **Caso 4: Nombres compuestos**

- Los datos ya incluyen ejemplos: "García López", "Pérez Del Aguila", "Quispe Condori"
- Esperado: Se importan correctamente sin problema

---

## 📞 Endpoint para Cargar

Una vez que tengas el `docentes_prueba.xlsx` creado:

```bash
curl -X POST -F "file=@/ruta/a/docentes_prueba.xlsx" \
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

---

## 🔍 Verificar Logs

Mientras se importa, busca en los logs:

```
✓ Docente cargado: Juan Pérez
✓ Docente cargado: María García López
...
Se cargaron 10 docentes
```

Si ves "Docente ya existente, se omite", es porque el correo ya está en la BD.

---

**Nota:** Este archivo está listo para usar. Solo necesitas convertirlo a Excel (.xlsx) usando cualquiera de las 3 opciones arriba.
