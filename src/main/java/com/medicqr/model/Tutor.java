package com.medicqr.model;

import java.util.ArrayList;
import java.util.List;

public class Tutor extends Persona {
    private String relacionConUsuario;
    private List<Usuario> usuariosACargo;
    private boolean esActivo;
    
    public Tutor() {
        super();
        this.relacionConUsuario = "";
        this.usuariosACargo = new ArrayList<>();
        this.esActivo = true;
    }
    
    public Tutor(int id, String nombre, String apellido, String telefono, 
                 String email, String fechaNacimiento, String relacionConUsuario) {
        super(id, nombre, apellido, telefono, email, fechaNacimiento);
        this.relacionConUsuario = relacionConUsuario;
        this.usuariosACargo = new ArrayList<>();
        this.esActivo = true;
    }
    
    public String getRelacionConUsuario() {
        return relacionConUsuario;
    }
    
    public void setRelacionConUsuario(String relacionConUsuario) {
        if (relacionConUsuario != null && !relacionConUsuario.trim().isEmpty()) {
            this.relacionConUsuario = relacionConUsuario.trim();
        }
    }
    
    public List<Usuario> getUsuariosACargo() {
        return new ArrayList<>(usuariosACargo);
    }
    
    public void setUsuariosACargo(List<Usuario> usuariosACargo) {
        if (usuariosACargo != null) {
            this.usuariosACargo = new ArrayList<>(usuariosACargo);
        }
    }
    
    public boolean isEsActivo() {
        return esActivo;
    }
    
    public void setEsActivo(boolean esActivo) {
        this.esActivo = esActivo;
    }
    
    @Override
    public String obtenerTipoPersona() {
        return "Tutor";
    }
    
    public void agregarUsuario(Usuario usuario) {
        if (usuario != null && !usuariosACargo.contains(usuario)) {
            usuariosACargo.add(usuario);
        }
    }
    
    public void removerUsuario(Usuario usuario) {
        if (usuario != null) {
            usuariosACargo.remove(usuario);
        }
    }
    
    public int getCantidadUsuariosACargo() {
        return usuariosACargo.size();
    }
    
    @Override
    public String toString() {
        return String.format("%s | Relación: %s | Usuarios a cargo: %d | Activo: %s",
                           super.toString(), relacionConUsuario, 
                           getCantidadUsuariosACargo(), esActivo ? "Sí" : "No");
    }
}
