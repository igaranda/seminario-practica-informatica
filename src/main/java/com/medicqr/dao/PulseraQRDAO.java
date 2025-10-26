package com.medicqr.dao;

import com.medicqr.model.PulseraQR;
import com.medicqr.model.Usuario;
import com.medicqr.model.Tutor;
import com.medicqr.model.PerfilInformacion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PulseraQRDAO {
    private DatabaseConnection dbConnection;
    
    public PulseraQRDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }
    
    public boolean insertarPulsera(PulseraQR pulsera) {
        String sql = "INSERT INTO pulseras_qr (codigo_unico, usuario_id, esta_activa, contador_accesos, fecha_creacion) " +
                    "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, pulsera.getCodigoUnico());
            stmt.setInt(2, pulsera.getUsuario() != null ? pulsera.getUsuario().getId() : 0);
            stmt.setBoolean(3, pulsera.isEstaActiva());
            stmt.setInt(4, pulsera.getContadorAccesos());
            stmt.setTimestamp(5, Timestamp.valueOf(pulsera.getFechaCreacion()));
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    pulsera.setId(generatedKeys.getInt(1));
                }
                return true;
            }
            
        } catch (SQLException e) {
        }
        
        return false;
    }
    
    public PulseraQR buscarPorCodigo(String codigoUnico) {
        String sql = "SELECT p.*, u.id as usuario_id, u.nombre, u.apellido, u.telefono, u.email, u.fecha_nacimiento, " +
                    "u.tiene_discapacidad, u.tipo_discapacidad, u.tutor_id " +
                    "FROM pulseras_qr p LEFT JOIN usuarios u ON p.usuario_id = u.id WHERE p.codigo_unico = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, codigoUnico);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                PulseraQR pulsera = mapearPulsera(rs);
                cargarRelacionesCompletas(pulsera);
                return pulsera;
            }
            
        } catch (SQLException e) {
            System.err.println("[ERROR] Error al buscar pulsera por código: " + e.getMessage());
        }
        
        return null;
    }
    
    public PulseraQR buscarPorId(int id) {
        String sql = "SELECT p.*, u.id as usuario_id, u.nombre, u.apellido, u.telefono, u.email, u.fecha_nacimiento, " +
                    "u.tiene_discapacidad, u.tipo_discapacidad, u.tutor_id " +
                    "FROM pulseras_qr p LEFT JOIN usuarios u ON p.usuario_id = u.id WHERE p.id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                PulseraQR pulsera = mapearPulsera(rs);
                cargarRelacionesCompletas(pulsera);
                return pulsera;
            }
            
        } catch (SQLException e) {
            System.err.println("[ERROR] Error al buscar pulsera por ID: " + e.getMessage());
        }
        
        return null;
    }
    
    public List<PulseraQR> buscarPorUsuario(int usuarioId) {
        List<PulseraQR> pulseras = new ArrayList<>();
        String sql = "SELECT p.*, u.id as usuario_id, u.nombre, u.apellido, u.telefono, u.email, u.fecha_nacimiento, " +
                    "u.tiene_discapacidad, u.tipo_discapacidad " +
                    "FROM pulseras_qr p LEFT JOIN usuarios u ON p.usuario_id = u.id WHERE p.usuario_id = ? ORDER BY p.fecha_creacion";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, usuarioId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                pulseras.add(mapearPulsera(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("[ERROR] Error al buscar pulseras por usuario: " + e.getMessage());
        }
        
        return pulseras;
    }
    
    public List<PulseraQR> obtenerTodas() {
        List<PulseraQR> pulseras = new ArrayList<>();
        String sql = "SELECT p.*, u.id as usuario_id, u.nombre, u.apellido, u.telefono, u.email, u.fecha_nacimiento, " +
                    "u.tiene_discapacidad, u.tipo_discapacidad, u.tutor_id " +
                    "FROM pulseras_qr p LEFT JOIN usuarios u ON p.usuario_id = u.id ORDER BY p.fecha_creacion";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                pulseras.add(mapearPulsera(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("[ERROR] Error al obtener todas las pulseras: " + e.getMessage());
        }
        
        return pulseras;
    }
    
    public boolean actualizarPulsera(PulseraQR pulsera) {
        String sql = "UPDATE pulseras_qr SET codigo_unico = ?, usuario_id = ?, esta_activa = ?, " +
                    "contador_accesos = ?, fecha_ultimo_uso = ? WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, pulsera.getCodigoUnico());
            stmt.setInt(2, pulsera.getUsuario() != null ? pulsera.getUsuario().getId() : 0);
            stmt.setBoolean(3, pulsera.isEstaActiva());
            stmt.setInt(4, pulsera.getContadorAccesos());
            
            if (pulsera.getFechaUltimoUso() != null) {
                stmt.setTimestamp(5, Timestamp.valueOf(pulsera.getFechaUltimoUso()));
            } else {
                stmt.setNull(5, Types.TIMESTAMP);
            }
            
            stmt.setInt(6, pulsera.getId());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("[ERROR] Error al actualizar pulsera: " + e.getMessage());
        }
        
        return false;
    }
    
    public boolean registrarAcceso(int pulseraId, String ipAcceso, String userAgent, String ubicacion) {
        String sql = "INSERT INTO historial_acceso (pulsera_id, ip_acceso, user_agent, ubicacion_aproximada) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, pulseraId);
            stmt.setString(2, ipAcceso);
            stmt.setString(3, userAgent);
            stmt.setString(4, ubicacion);
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("[ERROR] Error al registrar acceso: " + e.getMessage());
        }
        
        return false;
    }
    
    public boolean eliminarPulsera(int id) {
        String sql = "UPDATE pulseras_qr SET esta_activa = FALSE WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("[ERROR] Error al eliminar pulsera: " + e.getMessage());
        }
        
        return false;
    }
    
    private PulseraQR mapearPulsera(ResultSet rs) throws SQLException {
        PulseraQR pulsera = new PulseraQR();
        pulsera.setId(rs.getInt("id"));
        pulsera.setCodigoUnico(rs.getString("codigo_unico"));
        pulsera.setEstaActiva(rs.getBoolean("esta_activa"));
        pulsera.setContadorAccesos(rs.getInt("contador_accesos"));
        
        Timestamp fechaCreacion = rs.getTimestamp("fecha_creacion");
        if (fechaCreacion != null) {
            pulsera.setFechaCreacion(fechaCreacion.toLocalDateTime());
        }
        
        Timestamp fechaUltimoUso = rs.getTimestamp("fecha_ultimo_uso");
        if (fechaUltimoUso != null) {
            pulsera.setFechaUltimoUso(fechaUltimoUso.toLocalDateTime());
        }
        
        int usuarioId = rs.getInt("usuario_id");
        if (usuarioId > 0) {
            Usuario usuario = new Usuario();
            usuario.setId(usuarioId);
            usuario.setNombre(rs.getString("nombre"));
            usuario.setApellido(rs.getString("apellido"));
            usuario.setTelefono(rs.getString("telefono"));
            usuario.setEmail(rs.getString("email"));
            usuario.setFechaNacimiento(rs.getString("fecha_nacimiento"));
            usuario.setTieneDiscapacidad(rs.getBoolean("tiene_discapacidad"));
            usuario.setTipoDiscapacidad(rs.getString("tipo_discapacidad"));
            
            pulsera.setUsuario(usuario);
        }
        
        return pulsera;
    }
    
    private void cargarRelacionesCompletas(PulseraQR pulsera) {
        if (pulsera.getUsuario() != null) {
            Usuario usuario = pulsera.getUsuario();
            
            try {
                UsuarioDAO usuarioDAO = new UsuarioDAO();
                Usuario usuarioCompleto = usuarioDAO.buscarPorId(usuario.getId());
                if (usuarioCompleto != null) {
                    pulsera.setUsuario(usuarioCompleto);
                }
            } catch (Exception e) {
                System.err.println("[ERROR] Error al cargar relaciones: " + e.getMessage());
            }
        }
    }
}
