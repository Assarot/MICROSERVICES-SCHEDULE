# Script para iniciar MS-SCHEDULE con Docker Compose
# Uso: .\start.ps1

Write-Host "==================================" -ForegroundColor Cyan
Write-Host "  MS-SCHEDULE Microservice" -ForegroundColor Cyan
Write-Host "==================================" -ForegroundColor Cyan
Write-Host ""

# Verificar si Docker está instalado
Write-Host "Verificando Docker..." -ForegroundColor Yellow
$dockerInstalled = Get-Command docker -ErrorAction SilentlyContinue
if (-not $dockerInstalled) {
    Write-Host "ERROR: Docker no está instalado o no está en el PATH" -ForegroundColor Red
    exit 1
}

# Verificar si Docker Compose está disponible
Write-Host "Verificando Docker Compose..." -ForegroundColor Yellow
$dockerComposeInstalled = Get-Command docker-compose -ErrorAction SilentlyContinue
if (-not $dockerComposeInstalled) {
    Write-Host "ERROR: Docker Compose no está instalado" -ForegroundColor Red
    exit 1
}

Write-Host "Docker y Docker Compose encontrados!" -ForegroundColor Green
Write-Host ""

# Detener contenedores existentes
Write-Host "Deteniendo contenedores existentes..." -ForegroundColor Yellow
docker-compose down

# Construir e iniciar los servicios
Write-Host ""
Write-Host "Iniciando servicios..." -ForegroundColor Yellow
docker-compose up -d --build

# Esperar a que los servicios estén listos
Write-Host ""
Write-Host "Esperando a que los servicios estén listos..." -ForegroundColor Yellow
Start-Sleep -Seconds 10

# Mostrar estado de los servicios
Write-Host ""
Write-Host "Estado de los servicios:" -ForegroundColor Cyan
docker-compose ps

Write-Host ""
Write-Host "==================================" -ForegroundColor Green
Write-Host "  Servicios iniciados!" -ForegroundColor Green
Write-Host "==================================" -ForegroundColor Green
Write-Host ""
Write-Host "PostgreSQL: http://localhost:5432" -ForegroundColor White
Write-Host "MS-SCHEDULE API: http://localhost:8081" -ForegroundColor White
Write-Host ""
Write-Host "Prueba la API:" -ForegroundColor Cyan
Write-Host "  curl http://localhost:8081/api/v1/type-hours" -ForegroundColor White
Write-Host ""
Write-Host "Ver logs:" -ForegroundColor Cyan
Write-Host "  docker-compose logs -f" -ForegroundColor White
Write-Host ""
Write-Host "Detener servicios:" -ForegroundColor Cyan
Write-Host "  docker-compose down" -ForegroundColor White
Write-Host ""
