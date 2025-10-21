-- Script de datos iniciales para la tabla USER_PROFILE
-- Este archivo se ejecuta automáticamente al iniciar la aplicación

-- Insertar perfiles de usuario de ejemplo
INSERT INTO user_profile (names, last_name, email, phone_number, address, dob, profile_picture, is_active, created_at, updated_at)
VALUES 
    ('Juan Carlos', 'García López', 'juan.garcia@email.com', '+51987654321', 'Av. Principal 123, Lima', '1990-05-15', 'https://via.placeholder.com/150', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('María Elena', 'Rodríguez Pérez', 'maria.rodriguez@email.com', '+51976543210', 'Jr. Los Olivos 456, Arequipa', '1985-08-22', 'https://via.placeholder.com/150', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Pedro Antonio', 'Martínez Silva', 'pedro.martinez@email.com', '+51965432109', 'Calle Las Flores 789, Cusco', '1992-11-30', 'https://via.placeholder.com/150', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Ana Sofía', 'López Torres', 'ana.lopez@email.com', '+51954321098', 'Av. Los Incas 321, Trujillo', '1988-03-10', 'https://via.placeholder.com/150', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Carlos Eduardo', 'Fernández Ruiz', 'carlos.fernandez@email.com', '+51943210987', 'Jr. San Martín 654, Piura', '1995-07-18', 'https://via.placeholder.com/150', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (email) DO NOTHING;
