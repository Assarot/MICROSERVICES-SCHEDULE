-- Database initialization script for MS-SCHEDULE

-- Create database (run this separately if needed)
-- CREATE DATABASE ms_schedule_db;

-- Connect to the database
-- \c ms_schedule_db;

-- Insert sample data for TYPE_HOUR
INSERT INTO type_hour (id_type_hour, type_hour) VALUES (1, 'HT') ON CONFLICT (id_type_hour) DO UPDATE SET type_hour = EXCLUDED.type_hour;
INSERT INTO type_hour (id_type_hour, type_hour) VALUES (2, 'HP') ON CONFLICT (id_type_hour) DO UPDATE SET type_hour = EXCLUDED.type_hour;

-- Insert sample data for TYPE_ASSIGNMENT
INSERT INTO type_assignment (name, is_active) VALUES ('Presencial', true) ON CONFLICT DO NOTHING;
INSERT INTO type_assignment (name, is_active) VALUES ('Virtual', true) ON CONFLICT DO NOTHING;
INSERT INTO type_assignment (name, is_active) VALUES ('Híbrida', true) ON CONFLICT DO NOTHING;

-- Insert sample data for WEEK_DAY
INSERT INTO week_day (id_schedule, name, is_active) VALUES (1, 'Domingo', true) ON CONFLICT (id_schedule) DO UPDATE SET name = EXCLUDED.name, is_active = EXCLUDED.is_active;
INSERT INTO week_day (id_schedule, name, is_active) VALUES (2, 'Lunes', true) ON CONFLICT (id_schedule) DO UPDATE SET name = EXCLUDED.name, is_active = EXCLUDED.is_active;
INSERT INTO week_day (id_schedule, name, is_active) VALUES (3, 'Martes', true) ON CONFLICT (id_schedule) DO UPDATE SET name = EXCLUDED.name, is_active = EXCLUDED.is_active;
INSERT INTO week_day (id_schedule, name, is_active) VALUES (4, 'Miércoles', true) ON CONFLICT (id_schedule) DO UPDATE SET name = EXCLUDED.name, is_active = EXCLUDED.is_active;
INSERT INTO week_day (id_schedule, name, is_active) VALUES (5, 'Jueves', true) ON CONFLICT (id_schedule) DO UPDATE SET name = EXCLUDED.name, is_active = EXCLUDED.is_active;
INSERT INTO week_day (id_schedule, name, is_active) VALUES (6, 'Viernes', true) ON CONFLICT (id_schedule) DO UPDATE SET name = EXCLUDED.name, is_active = EXCLUDED.is_active;
INSERT INTO week_day (id_schedule, name, is_active) VALUES (7, 'Sábado', true) ON CONFLICT (id_schedule) DO UPDATE SET name = EXCLUDED.name, is_active = EXCLUDED.is_active;

-- Note: SCHEDULE and SCHEDULE_ASSIGNMENT tables depend on foreign keys
-- from other microservices (id_academic_space, id_course_assignment, id_user_profile)
-- Insert sample data for these tables once the referenced data exists in other microservices

COMMIT;
