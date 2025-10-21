-- Database initialization script for MS-SCHEDULE

-- Create database (run this separately if needed)
-- CREATE DATABASE ms_schedule_db;

-- Connect to the database
-- \c ms_schedule_db;

-- Insert sample data for TYPE_HOUR
INSERT INTO type_hour (type_hour) VALUES ('Teórica') ON CONFLICT DO NOTHING;
INSERT INTO type_hour (type_hour) VALUES ('Práctica') ON CONFLICT DO NOTHING;
INSERT INTO type_hour (type_hour) VALUES ('Laboratorio') ON CONFLICT DO NOTHING;

-- Insert sample data for TYPE_ASSIGNMENT
INSERT INTO type_assignment (name, is_active) VALUES ('Presencial', true) ON CONFLICT DO NOTHING;
INSERT INTO type_assignment (name, is_active) VALUES ('Virtual', true) ON CONFLICT DO NOTHING;
INSERT INTO type_assignment (name, is_active) VALUES ('Híbrida', true) ON CONFLICT DO NOTHING;

-- Insert sample data for WEEK_DAY
INSERT INTO week_day (name, is_active) VALUES ('Lunes', true) ON CONFLICT DO NOTHING;
INSERT INTO week_day (name, is_active) VALUES ('Martes', true) ON CONFLICT DO NOTHING;
INSERT INTO week_day (name, is_active) VALUES ('Miércoles', true) ON CONFLICT DO NOTHING;
INSERT INTO week_day (name, is_active) VALUES ('Jueves', true) ON CONFLICT DO NOTHING;
INSERT INTO week_day (name, is_active) VALUES ('Viernes', true) ON CONFLICT DO NOTHING;
INSERT INTO week_day (name, is_active) VALUES ('Sábado', true) ON CONFLICT DO NOTHING;
INSERT INTO week_day (name, is_active) VALUES ('Domingo', false) ON CONFLICT DO NOTHING;

-- Note: SCHEDULE and SCHEDULE_ASSIGNMENT tables depend on foreign keys
-- from other microservices (id_academic_space, id_course_assignment, id_user_profile)
-- Insert sample data for these tables once the referenced data exists in other microservices

COMMIT;
