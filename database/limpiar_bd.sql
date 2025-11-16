-- Script para limpiar la base de datos MedicQR
-- Elimina todos los datos existentes pero mantiene las tablas

USE medicqr_db;

SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE historial_accesos;
TRUNCATE TABLE pulseras_qr;
TRUNCATE TABLE perfiles_informacion;
TRUNCATE TABLE usuarios;
TRUNCATE TABLE tutores;

SET FOREIGN_KEY_CHECKS = 1;

SELECT 'Base de datos limpiada exitosamente. Ejecute la aplicación para crear los 3 datos de demostración.' AS mensaje;
