# Script para crear Excel de docentes de prueba
$excel = New-Object -ComObject Excel.Application
$excel.Visible = $false

# Crear libro
$workbook = $excel.Workbooks.Add()
$worksheet = $workbook.Sheets.Item(1)

# Agregar headers
$worksheet.Cells.Item(1, 1) = "Nombre"
$worksheet.Cells.Item(1, 2) = "Apellido"
$worksheet.Cells.Item(1, 3) = "Email"

# Docentes de prueba
$docentes = @(
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

# Insertar datos
$row = 2
foreach ($docente in $docentes) {
    $worksheet.Cells.Item($row, 1) = $docente[0]
    $worksheet.Cells.Item($row, 2) = $docente[1]
    $worksheet.Cells.Item($row, 3) = $docente[2]
    $row++
}

# Ajustar ancho de columnas
$worksheet.Columns(1).ColumnWidth = 15
$worksheet.Columns(2).ColumnWidth = 25
$worksheet.Columns(3).ColumnWidth = 35

# Guardar archivo
$outputPath = Join-Path (Get-Location) "docentes_prueba.xlsx"
$workbook.SaveAs($outputPath)
$excel.Quit()

Write-Host "✓ Archivo creado: $outputPath"
Write-Host "✓ Total de docentes: $($docentes.Count)"
Write-Host ""
Write-Host "Docentes incluidos:"
foreach ($docente in $docentes) {
    Write-Host "  - $($docente[0]) $($docente[1]) ($($docente[2]))"
}
