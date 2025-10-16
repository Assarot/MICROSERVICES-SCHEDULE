# Script para detener MS-SCHEDULE
# Uso: .\stop.ps1

Write-Host "==================================" -ForegroundColor Cyan
Write-Host "  Deteniendo MS-SCHEDULE" -ForegroundColor Cyan
Write-Host "==================================" -ForegroundColor Cyan
Write-Host ""

Write-Host "Deteniendo servicios..." -ForegroundColor Yellow
docker-compose down

Write-Host ""
Write-Host "Servicios detenidos correctamente!" -ForegroundColor Green
Write-Host ""

# Preguntar si se desean eliminar los volúmenes
$response = Read-Host "¿Deseas eliminar los datos de la base de datos? (s/N)"
if ($response -eq "s" -or $response -eq "S") {
    Write-Host "Eliminando volúmenes..." -ForegroundColor Yellow
    docker-compose down -v
    Write-Host "Volúmenes eliminados!" -ForegroundColor Green
}

Write-Host ""
Write-Host "¡Listo!" -ForegroundColor Green
