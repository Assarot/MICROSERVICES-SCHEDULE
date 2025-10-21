-- Script SQL para poblar datos iniciales (opcional)
-- Este script se ejecutará automáticamente si configuras spring.jpa.defer-datasource-initialization=true

-- Insertar Severidades
INSERT INTO severity (name, is_active) VALUES ('Low', true) ON CONFLICT DO NOTHING;
INSERT INTO severity (name, is_active) VALUES ('Medium', true) ON CONFLICT DO NOTHING;
INSERT INTO severity (name, is_active) VALUES ('High', true) ON CONFLICT DO NOTHING;
INSERT INTO severity (name, is_active) VALUES ('Critical', true) ON CONFLICT DO NOTHING;

-- Insertar Estados
INSERT INTO status (name, is_active) VALUES ('Open', true) ON CONFLICT DO NOTHING;
INSERT INTO status (name, is_active) VALUES ('In Progress', true) ON CONFLICT DO NOTHING;
INSERT INTO status (name, is_active) VALUES ('Resolved', true) ON CONFLICT DO NOTHING;
INSERT INTO status (name, is_active) VALUES ('Closed', true) ON CONFLICT DO NOTHING;
