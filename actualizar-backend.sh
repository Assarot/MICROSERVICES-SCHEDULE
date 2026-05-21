#!/bin/bash
echo "🚀 Iniciando despliegue de Backend..."

# 1. Limpiar y descargar
git clean -fd
git pull origin import

# 2. Compilar
mvn clean package -DskipTests

# 3. Apagar todo lo que esté corriendo
echo "🛑 Deteniendo servicios..."
pkill -f java

# 4. Encender los 11 servicios (Eureka y Gateway primero)
echo "⚡ Levantando microservicios..."

# Infraestructura
nohup java -jar microservice-eureka-server/target/microservice-eureka-server-0.0.1-SNAPSHOT.jar > ms-eureka.log 2>&1 &
sleep 15 # Espera a que Eureka levante
nohup java -jar microservice-gateway/target/microservice-gateway-0.0.1-SNAPSHOT.jar > ms-gateway.log 2>&1 &
sleep 10

# Backend Services
nohup java -jar microservice_auth/target/microservice_auth-0.0.1-SNAPSHOT.jar > ms-auth.log 2>&1 &
nohup java -jar microservice_user/target/microservice_user-0.0.1-SNAPSHOT.jar > ms-user.log 2>&1 &
nohup java -jar microservice-enviroment/target/microservice-enviroment-0.0.1-SNAPSHOT.jar > ms-enviroment.log 2>&1 &
nohup java -jar microservice-course-management/target/microservice-course-management-0.0.1-SNAPSHOT.jar > ms-course.log 2>&1 &
nohup java -jar microservice_schedule/target/microservice_schedule-0.0.1-SNAPSHOT.jar > ms-schedule.log 2>&1 &
nohup java -jar microservice_inventory/target/microservice_inventory-0.0.1-SNAPSHOT.jar > ms-inventory.log 2>&1 &
nohup java -jar microservice_incident/target/microservice_incident-0.0.1-SNAPSHOT.jar > ms-incident.log 2>&1 &
nohup java -jar microservice-import/target/microservice-import-1.0.0.jar > ms-import.log 2>&1 &

echo "✅ ¡Backend desplegado! (Eureka y Gateway activos)"