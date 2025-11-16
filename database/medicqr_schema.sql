-- Script de creación de base de datos para el sistema MedicQR
-- Desarrollado por: Ignacio Aranda Galván
-- Universidad Empresarial Siglo XXI

-- Crear la base de datos
CREATE DATABASE IF NOT EXISTS medicqr_db;
USE medicqr_db;

-- Crear usuario para la aplicación
CREATE USER IF NOT EXISTS 'medicqr_user'@'localhost' IDENTIFIED BY 'medicqr_pass';
GRANT ALL PRIVILEGES ON medicqr_db.* TO 'medicqr_user'@'localhost';
FLUSH PRIVILEGES;

-- Tabla para tutores
CREATE TABLE IF NOT EXISTS tutores (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    telefono VARCHAR(20) NOT NULL,
    email VARCHAR(150),
    fecha_nacimiento DATE,
    relacion_con_usuario VARCHAR(100),
    es_activo BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Tabla para usuarios (pacientes)
CREATE TABLE IF NOT EXISTS usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    telefono VARCHAR(20) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    tutor_id INT,
    tiene_discapacidad BOOLEAN DEFAULT FALSE,
    tipo_discapacidad VARCHAR(200),
    es_activo BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (tutor_id) REFERENCES tutores(id) ON DELETE SET NULL
);

-- Tabla para perfil de información médica
CREATE TABLE IF NOT EXISTS perfiles_informacion (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT UNIQUE NOT NULL,
    alergias TEXT,
    medicamentos TEXT,
    enfermedades_cronicas TEXT,
    grupo_sanguineo VARCHAR(5),
    contacto_emergencia VARCHAR(100),
    telefono_emergencia VARCHAR(20),
    observaciones_medicas TEXT,
    requiere_atencion_inmediata BOOLEAN DEFAULT FALSE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

-- Tabla para pulseras QR
CREATE TABLE IF NOT EXISTS pulseras_qr (
    id INT AUTO_INCREMENT PRIMARY KEY,
    codigo_unico VARCHAR(50) UNIQUE NOT NULL,
    usuario_id INT NOT NULL,
    esta_activa BOOLEAN DEFAULT TRUE,
    contador_accesos INT DEFAULT 0,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_ultimo_uso TIMESTAMP NULL,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

-- Tabla para historial de accesos
CREATE TABLE IF NOT EXISTS historial_accesos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    pulsera_id INT NOT NULL,
    fecha_acceso TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ip_acceso VARCHAR(45),
    user_agent TEXT,
    ubicacion_aproximada VARCHAR(200),
    FOREIGN KEY (pulsera_id) REFERENCES pulseras_qr(id) ON DELETE CASCADE
);

-- Insertar datos de prueba
INSERT INTO tutores (nombre, apellido, telefono, email, fecha_nacimiento, relacion_con_usuario) VALUES
('Maria', 'Gonzalez', '123456789', 'maria.gonzalez@email.com', '1970-05-15', 'Madre'),
('Carlos', 'Lopez', '987654321', 'carlos.lopez@email.com', '1965-03-20', 'Padre'),
('Ana', 'Martinez', '555555555', 'ana.martinez@email.com', '1980-08-12', 'Hija'),
('Roberto', 'Fernandez', '444444444', 'roberto.fernandez@email.com', '1955-11-30', 'Hermano');

INSERT INTO usuarios (nombre, apellido, telefono, email, fecha_nacimiento, tutor_id, tiene_discapacidad, tipo_discapacidad) VALUES
('Ana', 'Gonzalez', '111111111', 'ana.gonzalez@email.com', '1995-08-10', 1, FALSE, NULL),
('Pedro', 'Lopez', '222222222', 'pedro.lopez@email.com', '2000-12-05', 2, TRUE, 'Autismo'),
('Maria', 'Martinez', '333333333', 'maria.martinez@email.com', '1985-04-22', 3, FALSE, NULL),
('Jose', 'Fernandez', '666666666', 'jose.fernandez@email.com', '1978-09-15', 4, TRUE, 'Alzheimer');

INSERT INTO perfiles_informacion (usuario_id, alergias, medicamentos, enfermedades_cronicas, grupo_sanguineo, contacto_emergencia, telefono_emergencia, observaciones_medicas, requiere_atencion_inmediata) VALUES
(1, 'Penicilina', 'Ninguno', 'Asma', 'O+', 'Maria Gonzalez', '123456789', 'Paciente con asma leve, requiere inhalador en caso de crisis.', FALSE),
(2, 'Ninguna', 'Risperidona', 'Autismo', 'A+', 'Carlos Lopez', '987654321', 'Paciente con autismo, requiere comunicacion clara y paciencia.', FALSE),
(3, 'Mariscos', 'Metformina', 'Diabetes tipo 2', 'B-', 'Ana Martinez', '555555555', 'Paciente diabetica, monitorear niveles de glucosa.', FALSE),
(4, 'Ninguna', 'Donepezilo, Memantina', 'Alzheimer', 'AB+', 'Roberto Fernandez', '444444444', 'Paciente con Alzheimer, puede desorientarse facilmente. REQUIERE ATENCION INMEDIATA.', TRUE);

INSERT INTO pulseras_qr (codigo_unico, usuario_id, contador_accesos) VALUES
('MEDICQR_ABC123DEF456', 1, 0),
('MEDICQR_XYZ789GHI012', 2, 0),
('MEDICQR_MNO345PQR678', 3, 0),
('MEDICQR_STU901VWX234', 4, 0);

-- Crear índices para mejorar el rendimiento
CREATE INDEX idx_usuario_tutor ON usuarios(tutor_id);
CREATE INDEX idx_perfil_usuario ON perfiles_informacion(usuario_id);
CREATE INDEX idx_pulsera_usuario ON pulseras_qr(usuario_id);
CREATE INDEX idx_pulsera_codigo ON pulseras_qr(codigo_unico);
CREATE INDEX idx_historial_pulsera ON historial_accesos(pulsera_id);
CREATE INDEX idx_historial_fecha ON historial_accesos(fecha_acceso);

-- Mostrar información de las tablas creadas
SHOW TABLES;
SELECT 'Base de datos MedicQR creada exitosamente' AS mensaje;

