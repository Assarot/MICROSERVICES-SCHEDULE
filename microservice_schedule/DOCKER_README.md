# Docker Setup para MS-SCHEDULE

## Requisitos Previos

- Docker
- Docker Compose

## Ejecución con Docker Compose

### 1. Iniciar todos los servicios

```bash
docker-compose up -d
```

Esto iniciará:
- **PostgreSQL** en puerto `5432`
- **MS-SCHEDULE** en puerto `8081`

### 2. Ver logs

```bash
# Ver todos los logs
docker-compose logs -f

# Ver logs solo del microservicio
docker-compose logs -f ms-schedule

# Ver logs solo de PostgreSQL
docker-compose logs -f postgres
```

### 3. Verificar estado de los servicios

```bash
docker-compose ps
```

### 4. Detener servicios

```bash
docker-compose down
```

### 5. Detener y eliminar volúmenes (elimina datos de BD)

```bash
docker-compose down -v
```

## Construcción manual de la imagen

Si deseas construir solo la imagen del microservicio:

```bash
docker build -t ms-schedule:latest .
```

## Ejecución manual con Docker

### 1. Crear red

```bash
docker network create schedule-network
```

### 2. Ejecutar PostgreSQL

```bash
docker run -d \
  --name ms-schedule-postgres \
  --network schedule-network \
  -e POSTGRES_DB=ms_schedule_db \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -p 5432:5432 \
  postgres:15-alpine
```

### 3. Ejecutar MS-SCHEDULE

```bash
docker run -d \
  --name ms-schedule-app \
  --network schedule-network \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://ms-schedule-postgres:5432/ms_schedule_db \
  -e SPRING_DATASOURCE_USERNAME=postgres \
  -e SPRING_DATASOURCE_PASSWORD=postgres \
  -p 8081:8081 \
  ms-schedule:latest
```

## Variables de Entorno

| Variable | Descripción | Valor por Defecto |
|----------|-------------|-------------------|
| `SPRING_DATASOURCE_URL` | URL de conexión a PostgreSQL | `jdbc:postgresql://localhost:5432/ms_schedule_db` |
| `SPRING_DATASOURCE_USERNAME` | Usuario de PostgreSQL | `postgres` |
| `SPRING_DATASOURCE_PASSWORD` | Contraseña de PostgreSQL | `postgres` |
| `SERVER_PORT` | Puerto del microservicio | `8081` |
| `JAVA_OPTS` | Opciones de la JVM | `-Xms256m -Xmx512m` |

## Verificación

Una vez iniciados los servicios, verifica que todo funcione:

```bash
# Health check
curl http://localhost:8081/api/v1/type-hours

# Debería retornar una lista vacía: []
```

## Solución de Problemas

### El microservicio no puede conectarse a PostgreSQL

Verifica que PostgreSQL esté saludable:

```bash
docker-compose ps
```

Si PostgreSQL no está "healthy", revisa los logs:

```bash
docker-compose logs postgres
```

### Reiniciar un servicio específico

```bash
docker-compose restart ms-schedule
```

### Reconstruir imagen después de cambios en el código

```bash
docker-compose up -d --build
```

## Acceso a PostgreSQL

Para conectarte directamente a PostgreSQL:

```bash
docker exec -it ms-schedule-postgres psql -U postgres -d ms_schedule_db
```

## Volúmenes

Los datos de PostgreSQL se persisten en el volumen `postgres_data`. Para ver los volúmenes:

```bash
docker volume ls
```

Para eliminar el volumen (perderás todos los datos):

```bash
docker volume rm microservice_schedule_postgres_data
```
