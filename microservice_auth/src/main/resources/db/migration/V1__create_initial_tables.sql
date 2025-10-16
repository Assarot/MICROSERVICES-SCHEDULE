-- Create ROLE table
CREATE TABLE IF NOT EXISTS role (
    id_role BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    is_active BOOLEAN NOT NULL DEFAULT true
);

-- Create AUTH_USER table
CREATE TABLE IF NOT EXISTS auth_user (
    id_auth_user BIGSERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    failed_logins_attempts INTEGER NOT NULL DEFAULT 0,
    is_active BOOLEAN NOT NULL DEFAULT true,
    id_user_profile BIGINT
);

-- Create AUTH_USER_ROLE junction table
CREATE TABLE IF NOT EXISTS auth_user_role (
    id_auth_user_role BIGSERIAL PRIMARY KEY,
    id_auth_user BIGINT NOT NULL,
    id_role BIGINT NOT NULL,
    CONSTRAINT fk_auth_user FOREIGN KEY (id_auth_user) REFERENCES auth_user(id_auth_user) ON DELETE CASCADE,
    CONSTRAINT fk_role FOREIGN KEY (id_role) REFERENCES role(id_role) ON DELETE CASCADE,
    CONSTRAINT uk_auth_user_role UNIQUE (id_auth_user, id_role)
);

-- Create REFRESH_TOKEN table
CREATE TABLE IF NOT EXISTS refresh_token (
    id_refresh_token BIGSERIAL PRIMARY KEY,
    refresh_token VARCHAR(500) NOT NULL UNIQUE,
    expiry_date TIMESTAMP NOT NULL,
    id_auth_user BIGINT NOT NULL,
    CONSTRAINT fk_refresh_token_user FOREIGN KEY (id_auth_user) REFERENCES auth_user(id_auth_user) ON DELETE CASCADE
);

-- Create AUTH_SESSION table
CREATE TABLE IF NOT EXISTS auth_session (
    id_auth_session BIGSERIAL PRIMARY KEY,
    token VARCHAR(500) NOT NULL UNIQUE,
    expires_in TIMESTAMP NOT NULL,
    id_auth_user BIGINT NOT NULL,
    CONSTRAINT fk_auth_session_user FOREIGN KEY (id_auth_user) REFERENCES auth_user(id_auth_user) ON DELETE CASCADE
);

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_auth_user_username ON auth_user(username);
CREATE INDEX IF NOT EXISTS idx_auth_user_is_active ON auth_user(is_active);
CREATE INDEX IF NOT EXISTS idx_refresh_token_token ON refresh_token(refresh_token);
CREATE INDEX IF NOT EXISTS idx_refresh_token_user ON refresh_token(id_auth_user);
CREATE INDEX IF NOT EXISTS idx_refresh_token_expiry ON refresh_token(expiry_date);
CREATE INDEX IF NOT EXISTS idx_auth_session_token ON auth_session(token);
CREATE INDEX IF NOT EXISTS idx_auth_session_user ON auth_session(id_auth_user);
CREATE INDEX IF NOT EXISTS idx_auth_session_expires ON auth_session(expires_in);
CREATE INDEX IF NOT EXISTS idx_role_name ON role(name);
