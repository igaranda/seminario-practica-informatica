package com.medicqr.dao;

import com.medicqr.model.Usuario;
import com.medicqr.model.Tutor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    private DatabaseConnection dbConnection;
    
    public UsuarioDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }
    
    public boolean insertarUsuario(Usuario usuario) {
        String sql = "INSERT INTO usuarios (nombre, apellido, telefono, email, fecha_nacimiento, tutor_id, tiene_discapacidad, tipo_discapacidad) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getApellido());
            
            // Handle optional telefono
            if (usuario.getTelefono() == null || usuario.getTelefono().trim().isEmpty()) {
                stmt.setString(3, "N/A");
            } else {
                stmt.setString(3, usuario.getTelefono());
            }
            
            // Handle optional email
            if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
                stmt.setString(4, "N/A");
            } else {
                stmt.setString(4, usuario.getEmail());
            }
            
            stmt.setString(5, usuario.getFechaNacimiento());
            stmt.setInt(6, usuario.getTutor() != null ? usuario.getTutor().getId() : 0);
            stmt.setBoolean(7, usuario.isTieneDiscapacidad());
            
            // Handle optional tipo_discapacidad
            if (usuario.getTipoDiscapacidad() == null || usuario.getTipoDiscapacidad().trim().isEmpty()) {
                stmt.setNull(8, java.sql.Types.VARCHAR);
            } else {
                stmt.setString(8, usuario.getTipoDiscapacidad());
            }
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    usuario.setId(generatedKeys.getInt(1));
                }
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("[ERROR] Error al insertar usuario: " + e.getMessage());
        }
        
        return false;
    }
    
    public Usuario buscarPorId(int id) {
        String sql = "SELECT u.*, t.nombre as tutor_nombre, t.apellido as tutor_apellido, t.telefono as tutor_telefono " +
                    "FROM usuarios u LEFT JOIN tutores t ON u.tutor_id = t.id WHERE u.id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapearUsuario(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("[ERROR] Error al buscar usuario por ID: " + e.getMessage());
        }
        
        return null;
    }
    
    public List<Usuario> buscarPorNombre(String nombre) {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT u.*, t.nombre as tutor_nombre, t.apellido as tutor_apellido, t.telefono as tutor_telefono " +
                    "FROM usuarios u LEFT JOIN tutores t ON u.tutor_id = t.id WHERE u.nombre LIKE ? OR u.apellido LIKE ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String busqueda = "%" + nombre + "%";
            stmt.setString(1, busqueda);
            stmt.setString(2, busqueda);
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                usuarios.add(mapearUsuario(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("[ERROR] Error al buscar usuarios por nombre: " + e.getMessage());
        }
        
        return usuarios;
    }
    
    public List<Usuario> obtenerTodos() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT u.*, t.nombre as tutor_nombre, t.apellido as tutor_apellido, t.telefono as tutor_telefono " +
                    "FROM usuarios u LEFT JOIN tutores t ON u.tutor_id = t.id ORDER BY u.apellido, u.nombre";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                usuarios.add(mapearUsuario(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("[ERROR] Error al obtener todos los usuarios: " + e.getMessage());
        }
        
        return usuarios;
    }
    
    public boolean actualizarUsuario(Usuario usuario) {
        String sql = "UPDATE usuarios SET nombre = ?, apellido = ?, telefono = ?, email = ?, " +
                    "fecha_nacimiento = ?, tutor_id = ?, tiene_discapacidad = ?, tipo_discapacidad = ? WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getApellido());
            stmt.setString(3, usuario.getTelefono());
            stmt.setString(4, usuario.getEmail());
            stmt.setString(5, usuario.getFechaNacimiento());
            stmt.setInt(6, usuario.getTutor() != null ? usuario.getTutor().getId() : 0);
            stmt.setBoolean(7, usuario.isTieneDiscapacidad());
            stmt.setString(8, usuario.getTipoDiscapacidad());
            stmt.setInt(9, usuario.getId());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("[ERROR] Error al actualizar usuario: " + e.getMessage());
        }
        
        return false;
    }
    
    public boolean eliminarUsuario(int id) {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("[ERROR] Error al eliminar usuario: " + e.getMessage());
        }
        
        return false;
    }
    
    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(rs.getInt("id"));
        usuario.setNombre(rs.getString("nombre"));
        usuario.setApellido(rs.getString("apellido"));
        usuario.setTelefono(rs.getString("telefono"));
        usuario.setEmail(rs.getString("email"));
        usuario.setFechaNacimiento(rs.getString("fecha_nacimiento"));
        usuario.setTieneDiscapacidad(rs.getBoolean("tiene_discapacidad"));
        usuario.setTipoDiscapacidad(rs.getString("tipo_discapacidad"));
        
        if (rs.getString("tutor_nombre") != null) {
            Tutor tutor = new Tutor();
            tutor.setId(rs.getInt("tutor_id"));
            tutor.setNombre(rs.getString("tutor_nombre"));
            tutor.setApellido(rs.getString("tutor_apellido"));
            tutor.setTelefono(rs.getString("tutor_telefono"));
            usuario.setTutor(tutor);
        }
        
        return usuario;
    }
}
