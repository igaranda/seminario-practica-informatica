/*Crear base de datos*/

CREATE DATABASE med_qr;
USE med_qr;

/*Creacion de tablas*/

CREATE TABLE tutor (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    dni VARCHAR(20) NOT NULL UNIQUE,
    telefono VARCHAR(25),
    email VARCHAR(100) NOT NULL UNIQUE
);


CREATE TABLE usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    foto_url VARCHAR(255),
    dni VARCHAR(20) NOT NULL UNIQUE,
    tutor_id BIGINT NOT NULL,
    FOREIGN KEY (tutor_id) REFERENCES tutor(id)
);


CREATE TABLE perfil_informacion (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    alergias TEXT,
    medicacion TEXT,
    enfermedades_cronicas TEXT,
    grupo_sanguineo VARCHAR(5),
    contacto_emergencia VARCHAR(100) NOT NULL,
    usuario_id BIGINT NOT NULL UNIQUE,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE CASCADE
);


CREATE TABLE pulsera_qr (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    codigo_unico VARCHAR(50) NOT NULL UNIQUE,
    estado ENUM('disponible', 'asignada', 'inactiva') NOT NULL DEFAULT 'disponible',
    usuario_id BIGINT,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE SET NULL
);

CREATE TABLE historial_acceso (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    fecha_hora DATETIME NOT NULL,
    ip_origen VARCHAR(45),
    pulsera_id BIGINT NOT NULL,
    FOREIGN KEY (pulsera_id) REFERENCES pulsera_qr(id)
);

/*INSERTAR DATOS*/

INSERT INTO tutor (id, nombre, dni, telefono, email)
VALUES (1, 'Laura Gómez', '28.123.456', '3815551234', 'laura.gomez@email.com');

INSERT INTO usuario (id, nombre, fecha_nacimiento, dni, tutor_id)
VALUES (1, 'Carlos Pérez', '1950-07-22', '7.987.654', 1);

INSERT INTO perfil_informacion (id, alergias, medicacion, enfermedades_cronicas, grupo_sanguineo, contacto_emergencia, usuario_id)
VALUES (1, 'Penicilina', 'Losartán 50mg (diario)', 'Hipertensión', 'A+', 'Hijo: Juan Pérez - 3814445678', 1);

INSERT INTO pulsera_qr (id, codigo_unico, estado, usuario_id)
VALUES (1, 'QRXYZ789', 'asignada', 1);
INSERT INTO pulsera_qr (id, codigo_unico, estado)
VALUES (2, 'QRABC123', 'disponible');

INSERT INTO historial_acceso (id, fecha_hora, ip_origen, pulsera_id)
VALUES (1, '2025-10-05 15:30:00', '190.10.20.30', 1);

/*CONSULTA DATOS DE PRUEBA*/

SELECT
    u.nombre AS nombre_usuario,
    pi.alergias,
    pi.medicacion,
    pi.enfermedades_cronicas,
    pi.grupo_sanguineo,
    pi.contacto_emergencia,
    t.nombre AS nombre_tutor,
    t.telefono AS telefono_tutor
FROM pulsera_qr pqr
JOIN usuario u ON pqr.usuario_id = u.id
JOIN perfil_informacion pi ON u.id = pi.usuario_id
JOIN tutor t ON u.tutor_id = t.id
WHERE pqr.codigo_unico = 'QRXYZ789';

