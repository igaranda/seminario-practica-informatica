package com.medicqr.dao;

import com.medicqr.model.Tutor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TutorDAO {
    private DatabaseConnection dbConnection;
    
    public TutorDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }
    
    public boolean insertarTutor(Tutor tutor) {
        String sql = "INSERT INTO tutores (nombre, apellido, telefono, email, fecha_nacimiento, relacion_con_usuario) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, tutor.getNombre());
            stmt.setString(2, tutor.getApellido());
            stmt.setString(3, tutor.getTelefono());
            
            // Handle empty email
            if (tutor.getEmail() == null || tutor.getEmail().trim().isEmpty()) {
                stmt.setNull(4, java.sql.Types.VARCHAR);
            } else {
                stmt.setString(4, tutor.getEmail());
            }
            
            // Handle empty fecha_nacimiento
            if (tutor.getFechaNacimiento() == null || tutor.getFechaNacimiento().trim().isEmpty()) {
                stmt.setNull(5, java.sql.Types.DATE);
            } else {
                stmt.setString(5, tutor.getFechaNacimiento());
            }
            
            stmt.setString(6, tutor.getRelacionConUsuario());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    tutor.setId(generatedKeys.getInt(1));
                }
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("[ERROR] Error al insertar tutor: " + e.getMessage());
        }
        
        return false;
    }
    
    public Tutor buscarPorId(int id) {
        String sql = "SELECT * FROM tutores WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapearTutor(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("[ERROR] Error al buscar tutor por ID: " + e.getMessage());
        }
        
        return null;
    }
    
    public List<Tutor> buscarPorNombre(String nombre) {
        List<Tutor> tutores = new ArrayList<>();
        String sql = "SELECT * FROM tutores WHERE nombre LIKE ? OR apellido LIKE ? ORDER BY apellido, nombre";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String busqueda = "%" + nombre + "%";
            stmt.setString(1, busqueda);
            stmt.setString(2, busqueda);
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                tutores.add(mapearTutor(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("[ERROR] Error al buscar tutores por nombre: " + e.getMessage());
        }
        
        return tutores;
    }
    
    public List<Tutor> obtenerTodos() {
        List<Tutor> tutores = new ArrayList<>();
        String sql = "SELECT * FROM tutores ORDER BY apellido, nombre";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                tutores.add(mapearTutor(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("[ERROR] Error al obtener todos los tutores: " + e.getMessage());
        }
        
        return tutores;
    }
    
    public boolean actualizarTutor(Tutor tutor) {
        String sql = "UPDATE tutores SET nombre = ?, apellido = ?, telefono = ?, email = ?, " +
                    "fecha_nacimiento = ?, relacion_con_usuario = ?, es_activo = ? WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, tutor.getNombre());
            stmt.setString(2, tutor.getApellido());
            stmt.setString(3, tutor.getTelefono());
            
            // Handle empty email
            if (tutor.getEmail() == null || tutor.getEmail().trim().isEmpty()) {
                stmt.setNull(4, java.sql.Types.VARCHAR);
            } else {
                stmt.setString(4, tutor.getEmail());
            }
            
            // Handle empty fecha_nacimiento
            if (tutor.getFechaNacimiento() == null || tutor.getFechaNacimiento().trim().isEmpty()) {
                stmt.setNull(5, java.sql.Types.DATE);
            } else {
                stmt.setString(5, tutor.getFechaNacimiento());
            }
            
            stmt.setString(6, tutor.getRelacionConUsuario());
            stmt.setBoolean(7, tutor.isEsActivo());
            stmt.setInt(8, tutor.getId());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("[ERROR] Error al actualizar tutor: " + e.getMessage());
        }
        
        return false;
    }
    
    public boolean eliminarTutor(int id) {
        String sql = "UPDATE tutores SET es_activo = FALSE WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("[ERROR] Error al eliminar tutor: " + e.getMessage());
        }
        
        return false;
    }
    
    private Tutor mapearTutor(ResultSet rs) throws SQLException {
        Tutor tutor = new Tutor();
        tutor.setId(rs.getInt("id"));
        tutor.setNombre(rs.getString("nombre"));
        tutor.setApellido(rs.getString("apellido"));
        tutor.setTelefono(rs.getString("telefono"));
        tutor.setEmail(rs.getString("email"));
        tutor.setFechaNacimiento(rs.getString("fecha_nacimiento"));
        tutor.setRelacionConUsuario(rs.getString("relacion_con_usuario"));
        tutor.setEsActivo(true);
        
        return tutor;
    }
}
