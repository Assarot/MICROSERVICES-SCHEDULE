# 🚀 Guía de Despliegue - MS-USER

Esta guía proporciona instrucciones detalladas para desplegar el microservicio MS-USER en diferentes entornos.

## 📋 Tabla de Contenidos

1. [Requisitos](#requisitos)
2. [Despliegue Local](#despliegue-local)
3. [Despliegue con Docker](#despliegue-con-docker)
4. [Configuración de Base de Datos](#configuración-de-base-de-datos)
5. [Verificación](#verificación)
6. [Solución de Problemas](#solución-de-problemas)

---

## 🛠️ Requisitos

### Despliegue Local
- Java 17 o superior
- Maven 3.8+
- PostgreSQL 12+
- Git

### Despliegue con Docker
- Docker 20.10+
- Docker Compose 2.0+

---

## 💻 Despliegue Local

### 1. Clonar el Repositorio

```bash
git clone <repository-url>
cd microservice_user
```

### 2. Configurar Base de Datos PostgreSQL

#### Crear la base de datos:

```sql
-- Conectarse a PostgreSQL
psql -U postgres

-- Crear la base de datos
CREATE DATABASE ms_user_db;

-- Conectarse a la base de datos
\c ms_user_db

-- Verificar que está vacía
\dt
```

#### Actualizar credenciales:

Editar `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/ms_user_db
spring.datasource.username=TU_USUARIO
spring.datasource.password=TU_CONTRASEÑA
```

### 3. Compilar el Proyecto

```bash
# Limpiar y compilar
mvn clean install

# O sin ejecutar tests
mvn clean install -DskipTests
```

### 4. Ejecutar la Aplicación

#### Opción A: Con Maven

```bash
mvn spring-boot:run
```

#### Opción B: Con Maven Wrapper (Windows)

```bash
.\mvnw.cmd spring-boot:run
```

#### Opción C: Con Maven Wrapper (Linux/Mac)

```bash
./mvnw spring-boot:run
```

#### Opción D: Con JAR

```bash
java -jar target/microservice_user-0.0.1-SNAPSHOT.jar
```

### 5. Verificar que está corriendo

Abrir navegador en: `http://localhost:8080/api/v1/user-profiles`

---

## 🐳 Despliegue con Docker

### Método 1: Docker Compose (Recomendado)

Este método levanta tanto la base de datos PostgreSQL como el microservicio automáticamente.

```bash
# Construir y levantar todos los servicios
docker-compose up -d

# Ver logs
docker-compose logs -f ms-user

# Detener servicios
docker-compose down

# Detener y eliminar volúmenes (elimina datos de BD)
docker-compose down -v
```

**Servicios disponibles:**
- MS-USER API: `http://localhost:8080`
- PostgreSQL: `localhost:5432`
- PgAdmin: `http://localhost:5050` (usuario: admin@msuser.com, password: admin)

### Método 2: Docker manual

#### 1. Crear red Docker

```bash
docker network create ms-user-network
```

#### 2. Levantar PostgreSQL

```bash
docker run -d \
  --name ms-user-postgres \
  --network ms-user-network \
  -e POSTGRES_DB=ms_user_db \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -p 5432:5432 \
  postgres:15-alpine
```

#### 3. Construir imagen del microservicio

```bash
docker build -t ms-user:latest .
```

#### 4. Ejecutar el microservicio

```bash
docker run -d \
  --name ms-user-app \
  --network ms-user-network \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://ms-user-postgres:5432/ms_user_db \
  -e SPRING_DATASOURCE_USERNAME=postgres \
  -e SPRING_DATASOURCE_PASSWORD=postgres \
  -p 8080:8080 \
  ms-user:latest
```

#### 5. Ver logs

```bash
docker logs -f ms-user-app
```

---

## 🗄️ Configuración de Base de Datos

### Inicialización Automática

El microservicio está configurado para ejecutar automáticamente:
- `schema.sql`: Crea la tabla user_profile
- `data.sql`: Inserta datos de ejemplo

Para desactivar la inicialización automática, comentar en `application.properties`:

```properties
# spring.sql.init.mode=always
# spring.sql.init.continue-on-error=false
```

### Inicialización Manual

Si prefieres ejecutar los scripts manualmente:

```bash
# Conectar a PostgreSQL
psql -U postgres -d ms_user_db

# Ejecutar schema
\i src/main/resources/schema.sql

# Ejecutar datos iniciales
\i src/main/resources/data.sql
```

### Verificar Datos

```sql
-- Ver todos los perfiles
SELECT * FROM user_profile;

-- Contar registros
SELECT COUNT(*) FROM user_profile;

-- Ver solo activos
SELECT * FROM user_profile WHERE is_active = true;
```

---

## ✅ Verificación

### 1. Health Check

```bash
# Verificar que el servicio responde
curl http://localhost:8080/api/v1/user-profiles
```

### 2. Pruebas con cURL

```bash
# Listar todos los perfiles
curl http://localhost:8080/api/v1/user-profiles

# Crear un nuevo perfil
curl -X POST http://localhost:8080/api/v1/user-profiles \
  -H "Content-Type: application/json" \
  -d '{
    "names": "Test User",
    "lastName": "Test LastName",
    "email": "test.user@email.com",
    "phoneNumber": "+51987654321",
    "address": "Test Address",
    "dob": "1990-01-01",
    "isActive": true
  }'

# Obtener por ID
curl http://localhost:8080/api/v1/user-profiles/1

# Obtener por email
curl http://localhost:8080/api/v1/user-profiles/email/juan.garcia@email.com
```

### 3. Importar Colección de Postman

1. Abrir Postman
2. Importar archivo `postman_collection.json`
3. Ejecutar los requests de la colección

---

## 🔧 Solución de Problemas

### Error: "No se puede conectar a la base de datos"

**Solución:**
```bash
# Verificar que PostgreSQL está corriendo
docker ps | grep postgres

# O en local
sudo systemctl status postgresql
```

### Error: "Puerto 8080 ya está en uso"

**Solución:**
```bash
# Encontrar proceso usando el puerto
netstat -ano | findstr :8080  # Windows
lsof -i :8080                  # Linux/Mac

# Cambiar puerto en application.properties
server.port=8081
```

### Error: "Email duplicado" al iniciar

**Solución:**
```bash
# Limpiar datos de la tabla
psql -U postgres -d ms_user_db -c "TRUNCATE TABLE user_profile RESTART IDENTITY CASCADE;"

# O desactivar data.sql temporalmente
# Comentar: spring.sql.init.mode=always
```

### Error: "Could not find or load main class"

**Solución:**
```bash
# Recompilar el proyecto
mvn clean install -DskipTests
```

### Docker: "No se puede construir la imagen"

**Solución:**
```bash
# Limpiar caché de Docker
docker system prune -a

# Reconstruir sin caché
docker-compose build --no-cache
```

### Logs para debugging

```bash
# Ver logs completos del microservicio
docker-compose logs ms-user

# Ver logs de PostgreSQL
docker-compose logs postgres

# Seguir logs en tiempo real
docker-compose logs -f
```

---

## 📊 Monitoreo

### Ver estado de contenedores

```bash
docker-compose ps
```

### Ver uso de recursos

```bash
docker stats
```

### Entrar al contenedor del microservicio

```bash
docker exec -it ms-user-app sh
```

### Entrar al contenedor de PostgreSQL

```bash
docker exec -it ms-user-postgres psql -U postgres -d ms_user_db
```

---

## 🔒 Seguridad para Producción

### Cambiar credenciales por defecto

1. Crear archivo `.env` (basado en `.env.example`)
2. Actualizar valores sensibles
3. **NO** subir `.env` a repositorio

### Usar variables de entorno

```bash
# Ejecutar con variables de entorno
export DB_PASSWORD=mi_password_seguro
java -jar target/microservice_user-0.0.1-SNAPSHOT.jar
```

### Configurar SSL/TLS

Agregar en `application.properties`:

```properties
server.ssl.enabled=true
server.ssl.key-store=classpath:keystore.p12
server.ssl.key-store-password=changeit
server.ssl.key-store-type=PKCS12
```

---

## 📞 Soporte

Para problemas o consultas:
- Revisar logs: `docker-compose logs`
- Verificar configuración en `application.properties`
- Consultar documentación en `README_MS_USER.md`

---

## 🎉 ¡Listo!

El microservicio MS-USER debería estar corriendo correctamente. Puedes empezar a consumir la API en:

**http://localhost:8080/api/v1/user-profiles**
