# 🚀 Guía de Inicio Rápido - MS-INVENTORY

## Pasos para ejecutar el proyecto

### 1. Configurar Cloudinary

1. Regístrate en [Cloudinary](https://cloudinary.com/)
2. Obtén tus credenciales del Dashboard:
   - Cloud Name
   - API Key
   - API Secret

### 2. Configurar Variables de Entorno

Edita el archivo `src/main/resources/application.yml` y actualiza:

```yaml
cloudinary:
  cloud-name: TU_CLOUD_NAME
  api-key: TU_API_KEY
  api-secret: TU_API_SECRET
```

**O** configura como variables de entorno:

```bash
# Windows (PowerShell)
$env:CLOUDINARY_CLOUD_NAME="tu_cloud_name"
$env:CLOUDINARY_API_KEY="tu_api_key"
$env:CLOUDINARY_API_SECRET="tu_api_secret"

# Linux/Mac
export CLOUDINARY_CLOUD_NAME="tu_cloud_name"
export CLOUDINARY_API_KEY="tu_api_key"
export CLOUDINARY_API_SECRET="tu_api_secret"
```

### 3. Iniciar la Base de Datos

```bash
docker-compose up -d
```

Esto inicia:
- **PostgreSQL**: `localhost:5432`
- **pgAdmin**: `http://localhost:5050`

### 4. Compilar y Ejecutar la Aplicación

```bash
# Compilar
mvn clean install

# Ejecutar
mvn spring-boot:run
```

**La aplicación estará disponible en:** `http://localhost:8080/api/v1`

### 5. Probar los Endpoints

#### Opción A: Importar Postman Collection

1. Abre Postman
2. Importa el archivo: `MS-INVENTORY.postman_collection.json`
3. Ejecuta las peticiones

#### Opción B: Usar cURL

**Crear un Estado:**
```bash
curl -X POST http://localhost:8080/api/v1/states \
  -H "Content-Type: application/json" \
  -d "{\"name\":\"Available\",\"isActive\":true}"
```

**Crear una Categoría:**
```bash
curl -X POST http://localhost:8080/api/v1/category-resources \
  -H "Content-Type: application/json" \
  -d "{\"name\":\"Electronics\",\"isActive\":true}"
```

**Crear un Tipo de Recurso:**
```bash
curl -X POST http://localhost:8080/api/v1/resource-types \
  -H "Content-Type: application/json" \
  -d "{\"name\":\"Laptop\",\"isActive\":true,\"idCategoryResource\":1}"
```

**Crear un Recurso con Imagen:**
```bash
curl -X POST http://localhost:8080/api/v1/resources \
  -F 'resource={"code":"LAP001","stock":10,"observation":"Laptop Dell XPS","idResourceType":1,"idState":1}' \
  -F 'photo=@C:/ruta/a/tu/imagen.jpg'
```

**Obtener todos los Recursos:**
```bash
curl -X GET http://localhost:8080/api/v1/resources
```

### 6. Acceder a pgAdmin (Opcional)

1. Abre: `http://localhost:5050`
2. Login:
   - Email: `admin@inventory.com`
   - Password: `admin`
3. Agregar servidor PostgreSQL:
   - Host: `postgres`
   - Port: `5432`
   - Database: `inventory_db`
   - Username: `postgres`
   - Password: `postgres`

## 📝 Orden Recomendado para Crear Datos

1. **States** (Estados)
2. **Category Resources** (Categorías)
3. **Resource Types** (Tipos de Recursos)
4. **Resources** (Recursos) - Puede incluir imágenes
5. **Resource Assignments** (Asignaciones)

## 🛑 Solución de Problemas Comunes

### Error: "Failed to upload image to Cloudinary"
- Verifica que las credenciales de Cloudinary sean correctas
- Asegúrate de que el archivo de imagen sea válido

### Error: "Port 5432 is already in use"
- PostgreSQL ya está corriendo en tu sistema
- Detén el servicio o cambia el puerto en `docker-compose.yml`

### Error: "CategoryResource not found"
- Asegúrate de crear las categorías antes de crear tipos de recursos

### Error: "ResourceType not found"
- Asegúrate de crear los tipos de recursos antes de crear recursos

## 📊 Datos de Ejemplo

### Estados
```json
[
  {"name": "Available", "isActive": true},
  {"name": "In Maintenance", "isActive": true},
  {"name": "Out of Service", "isActive": true}
]
```

### Categorías
```json
[
  {"name": "Electronics", "isActive": true},
  {"name": "Furniture", "isActive": true},
  {"name": "Equipment", "isActive": true}
]
```

### Tipos de Recursos
```json
[
  {"name": "Laptop", "isActive": true, "idCategoryResource": 1},
  {"name": "Projector", "isActive": true, "idCategoryResource": 1},
  {"name": "Desk", "isActive": true, "idCategoryResource": 2}
]
```

## 📖 Más Información

Para más detalles, consulta el archivo [README.md](README.md)

---

**¡Listo para usar! 🎉**
