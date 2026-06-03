-- =====================================================
-- Datos iniciales: reservation_status
-- Se ejecuta automáticamente con Spring Boot sql.init.mode=always
-- =====================================================
INSERT INTO reservation_status (id_status, name, is_active) VALUES
(1, 'Pendiente',  true),
(2, 'Aprobada',   true),
(3, 'Rechazada',  true),
(4, 'Anulada',    true),
(5, 'Finalizada', true),
(6, 'Revocada',   true)
ON CONFLICT (id_status) DO NOTHING;
