-- Script para inicializar la base de datos PostgreSQL
-- Este script se puede ejecutar manualmente si es necesario

-- Crear base de datos (ejecutar esto como superusuario)
-- CREATE DATABASE incident_db;

-- Conectar a la base de datos
\c incident_db;

-- Las tablas se crean automáticamente con Hibernate (ddl-auto: update)
-- Sin embargo, aquí está la estructura manual por referencia:

-- Tabla: severity
CREATE TABLE IF NOT EXISTS severity (
    id_severity BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE
);

-- Tabla: status
CREATE TABLE IF NOT EXISTS status (
    id_status BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE
);

-- Tabla: incident
CREATE TABLE IF NOT EXISTS incident (
    id_incident BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    report_date TIMESTAMP NOT NULL,
    resolution_date TIMESTAMP,
    id_reported_by BIGINT,
    id_resolved_by BIGINT,
    id_academic_space BIGINT,
    id_resource BIGINT,
    id_severity BIGINT NOT NULL,
    id_status BIGINT NOT NULL,
    FOREIGN KEY (id_severity) REFERENCES severity(id_severity),
    FOREIGN KEY (id_status) REFERENCES status(id_status)
);

-- Tabla: incident_attachment
CREATE TABLE IF NOT EXISTS incident_attachment (
    id_incident_attachment BIGSERIAL PRIMARY KEY,
    photo_url VARCHAR(500) NOT NULL,
    uploaded_at TIMESTAMP NOT NULL,
    id_incident BIGINT NOT NULL,
    FOREIGN KEY (id_incident) REFERENCES incident(id_incident) ON DELETE CASCADE
);

-- Índices para mejorar el rendimiento
CREATE INDEX IF NOT EXISTS idx_incident_severity ON incident(id_severity);
CREATE INDEX IF NOT EXISTS idx_incident_status ON incident(id_status);
CREATE INDEX IF NOT EXISTS idx_incident_report_date ON incident(report_date);
CREATE INDEX IF NOT EXISTS idx_attachment_incident ON incident_attachment(id_incident);

-- Datos iniciales de ejemplo
INSERT INTO severity (name, is_active) VALUES 
    ('Low', true),
    ('Medium', true),
    ('High', true),
    ('Critical', true)
ON CONFLICT (name) DO NOTHING;

INSERT INTO status (name, is_active) VALUES 
    ('Open', true),
    ('In Progress', true),
    ('Resolved', true),
    ('Closed', true),
    ('Cancelled', true)
ON CONFLICT (name) DO NOTHING;

-- Verificar datos
SELECT 'Severities:' as table_name, COUNT(*) as count FROM severity
UNION ALL
SELECT 'Statuses:' as table_name, COUNT(*) as count FROM status;
