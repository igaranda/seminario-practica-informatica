package com.medicqr.dao;

import com.medicqr.model.PerfilInformacion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PerfilInformacionDAO {
    private DatabaseConnection dbConnection;
    
    public PerfilInformacionDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }
    
    public boolean insertarPerfil(PerfilInformacion perfil, int usuarioId) {
        String sql = "INSERT INTO perfiles_informacion (usuario_id, alergias, medicamentos, enfermedades_cronicas, " +
                    "grupo_sanguineo, contacto_emergencia, telefono_emergencia, observaciones_medicas, requiere_atencion_inmediata) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, usuarioId);
            stmt.setString(2, perfil.getAlergias());
            stmt.setString(3, perfil.getMedicamentos());
            stmt.setString(4, perfil.getEnfermedadesCronicas());
            stmt.setString(5, perfil.getGrupoSanguineo());
            stmt.setString(6, perfil.getContactoEmergencia());
            stmt.setString(7, perfil.getTelefonoEmergencia());
            stmt.setString(8, perfil.getObservacionesMedicas());
            stmt.setBoolean(9, perfil.isRequiereAtencionInmediata());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    perfil.setId(generatedKeys.getInt(1));
                }
                return true;
            }
            
        } catch (SQLException e) {
        }
        
        return false;
    }
    
    public PerfilInformacion buscarPorUsuarioId(int usuarioId) {
        String sql = "SELECT * FROM perfiles_informacion WHERE usuario_id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, usuarioId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapearPerfil(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("[ERROR] Error al buscar perfil por usuario ID: " + e.getMessage());
        }
        
        return null;
    }
    
    public List<PerfilInformacion> buscarPerfilesCriticos() {
        List<PerfilInformacion> perfiles = new ArrayList<>();
        String sql = "SELECT * FROM perfiles_informacion WHERE requiere_atencion_inmediata = TRUE ORDER BY fecha_creacion";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                perfiles.add(mapearPerfil(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("[ERROR] Error al buscar perfiles críticos: " + e.getMessage());
        }
        
        return perfiles;
    }
    
    public List<PerfilInformacion> obtenerTodos() {
        List<PerfilInformacion> perfiles = new ArrayList<>();
        String sql = "SELECT * FROM perfiles_informacion ORDER BY fecha_creacion";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                perfiles.add(mapearPerfil(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("[ERROR] Error al obtener todos los perfiles: " + e.getMessage());
        }
        
        return perfiles;
    }
    
    public boolean actualizarPerfil(PerfilInformacion perfil) {
        String sql = "UPDATE perfiles_informacion SET alergias = ?, medicamentos = ?, enfermedades_cronicas = ?, " +
                    "grupo_sanguineo = ?, contacto_emergencia = ?, telefono_emergencia = ?, " +
                    "observaciones_medicas = ?, requiere_atencion_inmediata = ? WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, perfil.getAlergias());
            stmt.setString(2, perfil.getMedicamentos());
            stmt.setString(3, perfil.getEnfermedadesCronicas());
            stmt.setString(4, perfil.getGrupoSanguineo());
            stmt.setString(5, perfil.getContactoEmergencia());
            stmt.setString(6, perfil.getTelefonoEmergencia());
            stmt.setString(7, perfil.getObservacionesMedicas());
            stmt.setBoolean(8, perfil.isRequiereAtencionInmediata());
            stmt.setInt(9, perfil.getId());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("[ERROR] Error al actualizar perfil: " + e.getMessage());
        }
        
        return false;
    }
    
    public boolean eliminarPerfil(int id) {
        String sql = "DELETE FROM perfiles_informacion WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("[ERROR] Error al eliminar perfil: " + e.getMessage());
        }
        
        return false;
    }
    
    private PerfilInformacion mapearPerfil(ResultSet rs) throws SQLException {
        PerfilInformacion perfil = new PerfilInformacion();
        perfil.setId(rs.getInt("id"));
        perfil.setAlergias(rs.getString("alergias"));
        perfil.setMedicamentos(rs.getString("medicamentos"));
        perfil.setEnfermedadesCronicas(rs.getString("enfermedades_cronicas"));
        perfil.setGrupoSanguineo(rs.getString("grupo_sanguineo"));
        perfil.setContactoEmergencia(rs.getString("contacto_emergencia"));
        perfil.setTelefonoEmergencia(rs.getString("telefono_emergencia"));
        perfil.setObservacionesMedicas(rs.getString("observaciones_medicas"));
        perfil.setRequiereAtencionInmediata(rs.getBoolean("requiere_atencion_inmediata"));
        
        return perfil;
    }
}
