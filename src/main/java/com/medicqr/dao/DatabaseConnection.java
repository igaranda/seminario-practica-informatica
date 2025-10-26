package com.medicqr.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/medicqr_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    
    private static DatabaseConnection instance;
    private Connection connection;
    
    private DatabaseConnection() {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
        }
    }
    
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }
    
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            crearTablasSiNoExisten();
        }
        return connection;
    }
    
    private void crearTablasSiNoExisten() {
        try {
            Statement stmt = connection.createStatement();
            
            String crearTutores = """
                CREATE TABLE IF NOT EXISTS tutores (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    nombre VARCHAR(100) NOT NULL,
                    apellido VARCHAR(100) NOT NULL,
                    telefono VARCHAR(20),
                    email VARCHAR(100),
                    fecha_nacimiento DATE,
                    relacion_con_usuario VARCHAR(50)
                )
            """;
            stmt.execute(crearTutores);
            
            String crearUsuarios = """
                CREATE TABLE IF NOT EXISTS usuarios (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    nombre VARCHAR(100) NOT NULL,
                    apellido VARCHAR(100) NOT NULL,
                    telefono VARCHAR(20),
                    email VARCHAR(100),
                    fecha_nacimiento DATE,
                    tutor_id INT,
                    tiene_discapacidad BOOLEAN DEFAULT FALSE,
                    tipo_discapacidad VARCHAR(100),
                    FOREIGN KEY (tutor_id) REFERENCES tutores(id)
                )
            """;
            stmt.execute(crearUsuarios);
            
            String crearPerfiles = """
                CREATE TABLE IF NOT EXISTS perfiles_informacion (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    usuario_id INT,
                    alergias TEXT,
                    medicamentos TEXT,
                    enfermedades_cronicas TEXT,
                    grupo_sanguineo VARCHAR(10),
                    contacto_emergencia VARCHAR(100),
                    telefono_emergencia VARCHAR(20),
                    observaciones_medicas TEXT,
                    requiere_atencion_inmediata BOOLEAN DEFAULT FALSE,
                    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
                )
            """;
            stmt.execute(crearPerfiles);
            
            String crearPulseras = """
                CREATE TABLE IF NOT EXISTS pulseras_qr (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    codigo_unico VARCHAR(50) UNIQUE NOT NULL,
                    usuario_id INT,
                    esta_activa BOOLEAN DEFAULT TRUE,
                    contador_accesos INT DEFAULT 0,
                    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    fecha_ultimo_uso TIMESTAMP NULL,
                    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
                )
            """;
            stmt.execute(crearPulseras);
            
            String crearHistorial = """
                CREATE TABLE IF NOT EXISTS historial_accesos (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    pulsera_id INT,
                    ip_address VARCHAR(45),
                    user_agent VARCHAR(255),
                    ubicacion VARCHAR(100),
                    fecha_acceso TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (pulsera_id) REFERENCES pulseras_qr(id)
                )
            """;
            stmt.execute(crearHistorial);
            
        } catch (SQLException e) {
            System.err.println("[ERROR] Error al crear tablas: " + e.getMessage());
        }
    }
    
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Error al cerrar la conexión: " + e.getMessage());
        }
    }
    
    public boolean isConnectionActive() {
        try {
            return connection != null && !connection.isClosed() && connection.isValid(5);
        } catch (SQLException e) {
            return false;
        }
    }
    
    public boolean testConnection() {
        try {
            Connection testConn = getConnection();
            return testConn != null && !testConn.isClosed() && testConn.isValid(5);
        } catch (Exception e) {
            return false;
        }
    }
}
