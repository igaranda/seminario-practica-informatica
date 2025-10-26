package com.medicqr.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class PulseraQR {
    private int id;
    private String codigoUnico;
    private Usuario usuario;
    private boolean estaActiva;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaUltimoUso;
    private int contadorAccesos;
    
    public PulseraQR() {
        this.id = 0;
        this.codigoUnico = generarCodigoUnico();
        this.usuario = null;
        this.estaActiva = true;
        this.fechaCreacion = LocalDateTime.now();
        this.fechaUltimoUso = null;
        this.contadorAccesos = 0;
    }
    
    public PulseraQR(int id, Usuario usuario) {
        this.id = id;
        this.codigoUnico = generarCodigoUnico();
        this.usuario = usuario;
        this.estaActiva = true;
        this.fechaCreacion = LocalDateTime.now();
        this.fechaUltimoUso = null;
        this.contadorAccesos = 0;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        if (id > 0) {
            this.id = id;
        }
    }
    
    public String getCodigoUnico() {
        return codigoUnico;
    }
    
    public void setCodigoUnico(String codigoUnico) {
        if (codigoUnico != null && !codigoUnico.trim().isEmpty()) {
            this.codigoUnico = codigoUnico.trim();
        }
    }
    
    public Usuario getUsuario() {
        return usuario;
    }
    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    public boolean isEstaActiva() {
        return estaActiva;
    }
    
    public void setEstaActiva(boolean estaActiva) {
        this.estaActiva = estaActiva;
    }
    
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    public LocalDateTime getFechaUltimoUso() {
        return fechaUltimoUso;
    }
    
    public void setFechaUltimoUso(LocalDateTime fechaUltimoUso) {
        this.fechaUltimoUso = fechaUltimoUso;
    }
    
    public int getContadorAccesos() {
        return contadorAccesos;
    }
    
    public void setContadorAccesos(int contadorAccesos) {
        if (contadorAccesos >= 0) {
            this.contadorAccesos = contadorAccesos;
        }
    }
    
    private String generarCodigoUnico() {
        return "MEDICQR_" + UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
    }
    
    public void registrarAcceso() {
        this.fechaUltimoUso = LocalDateTime.now();
        this.contadorAccesos++;
    }
    
    public boolean puedeSerEscaneada() {
        return estaActiva && usuario != null;
    }
    
    public String obtenerInformacionQR() {
        if (!puedeSerEscaneada()) {
            return "PULSERA INACTIVA O SIN USUARIO ASIGNADO";
        }
        
        StringBuilder info = new StringBuilder();
        info.append("=== MEDICQR - INFORMACIÓN DE EMERGENCIA ===\n\n");
        info.append("Código: ").append(codigoUnico).append("\n");
        info.append("Fecha de Creación: ").append(fechaCreacion.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))).append("\n\n");
        
        if (usuario != null) {
            info.append(usuario.obtenerInformacionEmergencia());
        }
        
        return info.toString();
    }
    
    public String obtenerEstadisticas() {
        StringBuilder stats = new StringBuilder();
        stats.append("ESTADÍSTICAS DE USO\n");
        stats.append("===================\n");
        stats.append("Código: ").append(codigoUnico).append("\n");
        stats.append("Estado: ").append(estaActiva ? "Activa" : "Inactiva").append("\n");
        stats.append("Total de Accesos: ").append(contadorAccesos).append("\n");
        stats.append("Fecha de Creación: ").append(fechaCreacion.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))).append("\n");
        
        if (fechaUltimoUso != null) {
            stats.append("Último Uso: ").append(fechaUltimoUso.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))).append("\n");
        } else {
            stats.append("Último Uso: Nunca\n");
        }
        
        return stats.toString();
    }
    
    @Override
    public String toString() {
        return String.format("ID: %d | Código: %s | Usuario: %s | Activa: %s | Accesos: %d",
                           id, codigoUnico,
                           usuario != null ? usuario.getNombreCompleto() : "Sin asignar",
                           estaActiva ? "Sí" : "No", contadorAccesos);
    }
}
