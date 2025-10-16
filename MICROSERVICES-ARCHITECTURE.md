# Arquitectura de Microservicios - Schedule System

## 📋 Resumen

Se han añadido los módulos de **Eureka Server** y **API Gateway** a la arquitectura de microservicios, configurando todos los servicios para que se registren automáticamente en Eureka y sean accesibles a través del Gateway.

## 🏗️ Arquitectura Completa

```
┌─────────────────────────────────────────────────────────────┐
│                      API GATEWAY (8080)                      │
│           Punto de entrada único a todos los servicios      │
└────────────────────────┬────────────────────────────────────┘
                         │
                         ▼
          ┌──────────────────────────────┐
          │  EUREKA SERVER (8761)        │
          │  Service Discovery Registry   │
          └──────────────────────────────┘
                         ▲
         ┌───────────────┼───────────────┐
         │               │               │
    ┌────▼────┐    ┌────▼────┐    ┌────▼────┐
    │ Auth    │    │ User    │    │ Course  │
    │ (8081)  │    │ (8082)  │    │ (8083)  │
    └─────────┘    └─────────┘    └─────────┘
         │               │               │
    ┌────▼────┐    ┌────▼────┐    ┌────▼────┐
    │Schedule │    │Inventory│    │Incident │
    │ (8084)  │    │ (8085)  │    │ (8086)  │
    └─────────┘    └─────────┘    └─────────┘
         │
    ┌────▼────┐
    │ Environ │
    │ (8087)  │
    └─────────┘
```

## 🚀 Servicios y Puertos

| Servicio | Puerto | Descripción |
|----------|--------|-------------|
| **Eureka Server** | 8761 | Registro y descubrimiento de servicios |
| **API Gateway** | 8080 | Punto de entrada único y enrutamiento |
| **microservice-auth** | 8081 | Autenticación y autorización (JWT) |
| **microservice-user** | 8082 | Gestión de usuarios |
| **microservice-course-management** | 8083 | Gestión de cursos |
| **microservice-schedule** | 8084 | Gestión de horarios |
| **microservice-inventory** | 8085 | Gestión de inventario |
| **microservice-incident** | 8086 | Gestión de incidencias |
| **microservice-enviroment** | 8087 | Gestión de ambientes |

## 📡 API Gateway - Rutas Configuradas

El Gateway enruta las peticiones a los microservicios según los siguientes paths:

| Path | Servicio Destino | Ejemplo |
|------|-----------------|---------|
| `/api/auth/**` | microservice-auth | `http://localhost:8080/api/auth/login` |
| `/api/users/**` | microservice-user | `http://localhost:8080/api/users/1` |
| `/api/courses/**` | microservice-course-management | `http://localhost:8080/api/courses/` |
| `/api/schedules/**` | microservice-schedule | `http://localhost:8080/api/schedules/` |
| `/api/inventory/**` | microservice-inventory | `http://localhost:8080/api/inventory/` |
| `/api/incidents/**` | microservice-incident | `http://localhost:8080/api/incidents/` |
| `/api/environments/**` | microservice-enviroment | `http://localhost:8080/api/environments/` |

## 🔧 Configuración Implementada

### 1. POM Principal
- ✅ Añadido Spring Cloud BOM (versión 2023.0.3)
- ✅ Módulos de eureka-server y gateway registrados

### 2. Eureka Server
**Archivos creados:**
- `microservice-eureka-server/pom.xml`
- `microservice-eureka-server/src/main/java/.../MicroserviceEurekaServerApplication.java`
- `microservice-eureka-server/src/main/resources/application.yml`

**Características:**
- No se registra a sí mismo en el registro
- Puerto por defecto: 8761
- Dashboard disponible en: `http://localhost:8761`

### 3. API Gateway
**Archivos creados:**
- `microservice-gateway/pom.xml`
- `microservice-gateway/src/main/java/.../MicroserviceGatewayApplication.java`
- `microservice-gateway/src/main/java/.../config/CorsConfig.java`
- `microservice-gateway/src/main/resources/application.yml`

**Características:**
- Balanceo de carga automático con Ribbon
- Service Discovery habilitado
- Configuración CORS para permitir peticiones cross-origin
- Rutas configuradas para todos los microservicios

### 4. Configuración de Microservicios

Todos los microservicios han sido actualizados con:

#### Dependencia Maven añadida:
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

#### Configuración en application.yml:
```yaml
eureka:
  client:
    service-url:
      defaultZone: ${EUREKA_SERVER:http://localhost:8761/eureka/}
    fetch-registry: true
    register-with-eureka: true
  instance:
    prefer-ip-address: true
    instance-id: ${spring.application.name}:${random.value}
```

## 🎯 Orden de Inicio

Para iniciar correctamente la arquitectura de microservicios, seguir este orden:

1. **Eureka Server** (puerto 8761)
   ```bash
   cd microservice-eureka-server
   mvn spring-boot:run
   ```

2. **API Gateway** (puerto 8080)
   ```bash
   cd microservice-gateway
   mvn spring-boot:run
   ```

3. **Microservicios** (en cualquier orden):
   - microservice-auth (8081)
   - microservice-user (8082)
   - microservice-course-management (8083)
   - microservice-schedule (8084)
   - microservice-inventory (8085)
   - microservice-incident (8086)
   - microservice-enviroment (8087)

## 📊 Monitoreo

### Eureka Dashboard
Accede a `http://localhost:8761` para ver:
- Servicios registrados
- Estado de instancias
- Métricas de disponibilidad

### Gateway Actuator
Accede a `http://localhost:8080/actuator/gateway/routes` para ver:
- Rutas configuradas
- Predicados activos
- Filtros aplicados

## 🔐 Variables de Entorno

Puedes personalizar la configuración mediante variables de entorno:

| Variable | Descripción | Valor por defecto |
|----------|-------------|-------------------|
| `EUREKA_SERVER` | URL del servidor Eureka | `http://localhost:8761/eureka/` |
| `SERVER_PORT` | Puerto del servicio | Variable según servicio |

## 🧪 Pruebas

### Verificar que un servicio está registrado en Eureka:
```bash
curl http://localhost:8761/eureka/apps
```

### Probar el Gateway:
```bash
# A través del Gateway
curl http://localhost:8080/api/auth/health

# Directamente al servicio
curl http://localhost:8081/health
```

## 📝 Notas Importantes

1. **Eureka Server debe iniciarse primero** para que los servicios puedan registrarse.

2. **El Gateway actúa como proxy inverso**, todas las peticiones de clientes deben ir a través del puerto 8080.

3. **Balanceo de carga automático**: Si ejecutas múltiples instancias de un microservicio, el Gateway distribuirá la carga automáticamente.

4. **Health Checks**: Todos los servicios tienen endpoints de actuator para monitoreo de salud.

5. **Resiliencia**: Si un servicio cae, Eureka lo detecta automáticamente y deja de enrutar tráfico hacia él.

## 🔄 Próximos Pasos Sugeridos

- [ ] Implementar Circuit Breaker (Resilience4j)
- [ ] Añadir Distributed Tracing (Sleuth + Zipkin)
- [ ] Configurar Spring Cloud Config Server
- [ ] Implementar API Rate Limiting en el Gateway
- [ ] Añadir autenticación centralizada en el Gateway

---

**Última actualización:** 15 de Octubre, 2025
**Spring Boot:** 3.3.5
**Spring Cloud:** 2023.0.3
