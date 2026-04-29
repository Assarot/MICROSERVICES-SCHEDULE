# ⚡ QUICK REFERENCE - Comandos Útiles

## 🚀 Inicio Rápido

### 1. Compilar

```bash
cd d:\Proyectos\MICROSERVICES-SCHEDULE\microservice-import
./mvnw clean install
# o
./mvnw.cmd clean install
```

### 2. Ejecutar

```bash
./mvnw spring-boot:run
# o desde IDE: Click derecho en MicroserviceImportApplication.java → Run
```

### 3. Verificar Salud

```bash
curl http://localhost:8008/api/v1/import/health
```

### 4. Procesar Carga

```bash
# Archivo predefinido
curl -X POST http://localhost:8008/api/v1/import/process-default-excel

# O subir archivo
curl -X POST -F "file=@carga psico.xlsx" \
  http://localhost:8008/api/v1/import/upload-carga-psico
```

---

## 📚 Documentos de Referencia

| Documento                                              | Propósito               |
| ------------------------------------------------------ | ----------------------- |
| [ORCHESTRATOR_README.md](ORCHESTRATOR_README.md)       | Documentación principal |
| [QUICKSTART.md](QUICKSTART.md)                         | Guía de 5 minutos       |
| [API_REFERENCE.md](API_REFERENCE.md)                   | Referencia de endpoints |
| [ARCHITECTURE.md](ARCHITECTURE.md)                     | Diagramas y flujos      |
| [CHANGELOG.md](CHANGELOG.md)                           | Historial de cambios    |
| [IMPLEMENTATION_SUMMARY.md](IMPLEMENTATION_SUMMARY.md) | Resumen ejecutivo       |

---

## 🔍 Archivos Importantes

### Servicios

```
src/main/java/.../service/
├── ExcelReaderService.java           ← Lee Excel
├── ScheduleOrchestratorService.java  ← Orquestador (⭐ PRINCIPAL)
└── TeacherLoadService.java           ← Carga docentes
```

### Clientes

```
src/main/java/.../client/
├── CourseManagementClient.java       ← MS-COURSE
├── ScheduleClient.java               ← MS-SCHEDULE
├── EnvironmentClient.java            ← MS-ENVIRONMENT
├── CourseAssignmentClient.java       ← Asignaciones
└── TeacherClient.java                ← MS-USER
```

### DTOs

```
src/main/java/.../dto/
├── CargaPsicoExcelDTO.java
├── CreateCourseDTO.java
├── CreateScheduleDTO.java
├── AcademicSpaceDTO.java
├── TeacherDTO.java
└── CourseAssignmentDTO.java
```

### Controlador

```
src/main/java/.../controller/
└── ExcelUpload.java                  ← REST Endpoints
```

### Configuración

```
src/main/resources/
├── application.yml                   ← Config principal
└── carga psico.xlsx                  ← Archivo de ejemplo
```

---

## 🧪 Testing

### Ejecutar Tests

```bash
./mvnw test

# O solo tests de un archivo
./mvnw test -Dtest=MicroserviceImportApplicationTests
```

### Tests Incluidos

- ✅ Excel Reader test
- ✅ Context loads test
- ✅ Mock Academic Spaces test
- ✅ Schedule Tracking test
- ✅ Application Properties test

---

## 🐛 Troubleshooting Rápido

### Problema: "Feign: no such host"

**Causa**: Eureka Server no está corriendo  
**Solución**:

```bash
# Inicia Eureka Server primero
cd ../microservice-eureka-server
./mvnw spring-boot:run
```

### Problema: "No hay espacios académicos"

**Causa**: MS-ENVIRONMENT sin datos  
**Solución**: Precarga espacios antes via API de MS-ENVIRONMENT

### Problema: "Connection refused" (DB)

**Causa**: PostgreSQL no disponible  
**Solución**:

```bash
# Verifica PostgreSQL en puerto 5432
netstat -an | grep 5432  # Linux/Mac
netstat -an | findstr 5432  # Windows
```

### Problema: Compilación falla

**Causa**: Maven cache corrupta  
**Solución**:

```bash
./mvnw clean
del ~/.m2/repository  # Borra cache Maven
./mvnw compile
```

### Problema: Archivo Excel no encontrado

**Causa**: Archivo en ruta incorrecta  
**Solución**: Coloca `carga psico.xlsx` en `src/main/resources/`

---

## 📊 URLs Importantes

| Servicio          | URL                     |
| ----------------- | ----------------------- |
| **Import (Este)** | `http://localhost:8008` |
| Eureka Server     | `http://localhost:8761` |
| MS-COURSE         | `http://localhost:8085` |
| MS-USER           | `http://localhost:8086` |
| MS-SCHEDULE       | `http://localhost:8087` |
| MS-ENVIRONMENT    | `http://localhost:8089` |
| Gateway           | `http://localhost:8080` |

---

## 💾 Base de Datos

### Conexión PostgreSQL

```
Host: localhost
Port: 5432
Database: microservices_import
Username: postgres
Password: 123456
```

### Tabla Principal (creada automáticamente)

```sql
-- Ver cursos creados
SELECT * FROM courses LIMIT 10;

-- Ver horarios asignados
SELECT * FROM schedule LIMIT 10;

-- Ver docentes
SELECT * FROM teachers LIMIT 10;
```

---

## 📝 Logs Clave

### Buscar éxito

```
✓ X cursos leídos del Excel
✓ Y espacios académicos disponibles
✓ Z grupos diferentes identificados
✓ Curso creado: [nombre]
✓ Horario asignado: [día] [hora]
```

### Buscar errores

```
✗ Error al procesar fila X
✗ Error al procesar curso [nombre]
✗ Feign: Connection refused
```

### Cambiar nivel de log

En `application.yml`:

```yaml
logging:
  level:
    pe.edu.upeu.microserviceimport: DEBUG # DEBUG, INFO, WARN, ERROR
```

---

## 🎯 Flujo de Ejecución

```
1. Usuario sube Excel
           ↓
2. ExcelReaderService lee y mapea
           ↓
3. ScheduleOrchestratorService procesa
           ↓
4. Para cada curso:
   - Crea en MS-COURSE
   - Obtiene/crea Docente (MS-USER)
   - Asigna horarios en MS-SCHEDULE
   - Valida espacios en MS-ENVIRONMENT
           ↓
5. ScheduleTracker previene conflictos
           ↓
6. Retorna respuesta SUCCESS
```

---

## ✅ Checklist Pre-Ejecución

Antes de procesar CARGA PSICO, verifica:

- [ ] PostgreSQL está corriendo en puerto 5432
- [ ] Eureka Server está en http://localhost:8761
- [ ] MS-COURSE-MANAGEMENT está registrado en Eureka
- [ ] MS-USER está registrado en Eureka
- [ ] MS-SCHEDULE está registrado en Eureka
- [ ] MS-ENVIRONMENT está registrado en Eureka
- [ ] MS-ENVIRONMENT tiene espacios académicos precargados
- [ ] `carga psico.xlsx` está en `src/main/resources/`
- [ ] application.yml tiene credenciales correctas
- [ ] Puerto 8008 está disponible (no usado por otro servicio)

---

## 🚀 Métodos Especiales

### Procesar Excel desde código

```java
@Autowired
private ScheduleOrchestratorService orchestrator;

// En algún lado de tu código:
InputStream stream = new FileInputStream("carga psico.xlsx");
orchestrator.processAndCreateSchedules(stream);
```

### Cargar solo docentes

```java
@Autowired
private TeacherLoadService teacherService;

InputStream stream = new FileInputStream("teachers.xlsx");
teacherService.cargarMaestros(stream);
```

---

## 📞 Comandos Git Útiles

```bash
# Ver cambios
git status
git diff

# Commit
git add .
git commit -m "Implementación del orquestador de horarios"

# Push
git push origin feature/orchestrator
```

---

## 🎓 Estructura Típica de Carpetas

```
MICROSERVICES-SCHEDULE/
├── microservice-import/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/pe/edu/upeu/microserviceimport/
│   │   │   │   ├── service/      ← Lógica principal
│   │   │   │   ├── client/       ← Feign clients
│   │   │   │   ├── dto/          ← Data objects
│   │   │   │   ├── controller/   ← REST endpoints
│   │   │   │   └── config/       ← Configuration
│   │   │   └── resources/
│   │   │       ├── application.yml
│   │   │       └── carga psico.xlsx
│   │   └── test/                 ← Tests
│   ├── pom.xml                   ← Maven config
│   ├── ORCHESTRATOR_README.md    ← Documentación
│   ├── API_REFERENCE.md
│   ├── ARCHITECTURE.md
│   ├── QUICKSTART.md
│   └── CHANGELOG.md
```

---

## 🔄 Ciclo de Desarrollo

```
1. Modificar código
           ↓
2. ./mvnw clean compile
           ↓
3. ./mvnw test
           ↓
4. ./mvnw spring-boot:run
           ↓
5. Probar endpoints
           ↓
6. Revisar logs
           ↓
7. git commit
           ↓
8. Listo para producción
```

---

## 💡 Tips Pro

1. **Compilación Rápida**

   ```bash
   # Skip tests para compilar más rápido
   ./mvnw clean compile -DskipTests
   ```

2. **Reload en Caliente** (Development)
   - Usa `spring-boot-devtools` en pom.xml
   - Los cambios se recargan automáticamente

3. **Logs Coloridos**
   - Añade a `application.yml`:

   ```yaml
   spring:
     output:
       ansi:
         enabled: always
   ```

4. **Debug**
   - En IDE: Debug → Set Breakpoint → F5/F6
   - En terminal:
   ```bash
   ./mvnw spring-boot:run -Dspring-boot.run.arguments="--debug"
   ```

---

## 📞 Contactos

- **Equipo Dev**: [contacto]
- **Support**: [contacto]
- **Issues**: GitHub Issues

---

**Última Actualización**: 28 de abril de 2026  
**Versión**: 1.0.0
