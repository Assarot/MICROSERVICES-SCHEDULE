-- Script de creación de la base de datos y tabla USER_PROFILE
-- Base de datos: ms_user_db

-- Crear la tabla USER_PROFILE
CREATE TABLE IF NOT EXISTS user_profile (
    id_user_profile BIGSERIAL PRIMARY KEY,
    names VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    phone_number VARCHAR(15),
    address VARCHAR(255),
    dob DATE,
    profile_picture VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN NOT NULL DEFAULT true
);

-- Crear índice en el campo email para optimizar búsquedas
CREATE INDEX IF NOT EXISTS idx_user_profile_email ON user_profile(email);

-- Crear índice en el campo is_active para optimizar búsquedas de usuarios activos
CREATE INDEX IF NOT EXISTS idx_user_profile_is_active ON user_profile(is_active);

-- Comentarios en la tabla y columnas
COMMENT ON TABLE user_profile IS 'Tabla que almacena los perfiles de usuario del sistema';
COMMENT ON COLUMN user_profile.id_user_profile IS 'Identificador único del perfil';
COMMENT ON COLUMN user_profile.names IS 'Nombres del usuario';
COMMENT ON COLUMN user_profile.last_name IS 'Apellidos del usuario';
COMMENT ON COLUMN user_profile.email IS 'Correo electrónico único del usuario';
COMMENT ON COLUMN user_profile.phone_number IS 'Número telefónico del usuario';
COMMENT ON COLUMN user_profile.address IS 'Dirección física del usuario';
COMMENT ON COLUMN user_profile.dob IS 'Fecha de nacimiento del usuario';
COMMENT ON COLUMN user_profile.profile_picture IS 'URL de la foto de perfil';
COMMENT ON COLUMN user_profile.created_at IS 'Fecha y hora de creación del registro';
COMMENT ON COLUMN user_profile.updated_at IS 'Fecha y hora de última actualización';
COMMENT ON COLUMN user_profile.is_active IS 'Estado activo/inactivo del perfil';
