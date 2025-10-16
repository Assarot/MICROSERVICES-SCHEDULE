-- Insert default admin user
-- Password: admin123 (BCrypt encoded)
INSERT INTO auth_user (username, password, failed_logins_attempts, is_active, id_user_profile) 
VALUES ('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 0, true, NULL);

-- Assign ADMIN role to admin user
INSERT INTO auth_user_role (id_auth_user, id_role) 
SELECT u.id_auth_user, r.id_role 
FROM auth_user u, role r 
WHERE u.username = 'admin' AND r.name = 'ADMIN';
