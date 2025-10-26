package com.medicqr.model;

import java.util.Objects;

public abstract class Persona {
    private int id;
    private String nombre;
    private String apellido;
    private String telefono;
    private String email;
    private String fechaNacimiento;
    
    public Persona() {
        this.id = 0;
        this.nombre = "";
        this.apellido = "";
        this.telefono = "";
        this.email = "";
        this.fechaNacimiento = "";
    }
    
    public Persona(int id, String nombre, String apellido, String telefono, 
                   String email, String fechaNacimiento) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.email = email;
        this.fechaNacimiento = fechaNacimiento;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        if (id > 0) {
            this.id = id;
        }
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        if (nombre != null && !nombre.trim().isEmpty()) {
            this.nombre = nombre.trim();
        }
    }
    
    public String getApellido() {
        return apellido;
    }
    
    public void setApellido(String apellido) {
        if (apellido != null && !apellido.trim().isEmpty()) {
            this.apellido = apellido.trim();
        }
    }
    
    public String getTelefono() {
        return telefono;
    }
    
    public void setTelefono(String telefono) {
        if (telefono != null && !telefono.trim().isEmpty()) {
            this.telefono = telefono.trim();
        }
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        if (email != null && email.contains("@")) {
            this.email = email.trim().toLowerCase();
        }
    }
    
    public String getFechaNacimiento() {
        return fechaNacimiento;
    }
    
    public void setFechaNacimiento(String fechaNacimiento) {
        if (fechaNacimiento != null && !fechaNacimiento.trim().isEmpty()) {
            this.fechaNacimiento = fechaNacimiento.trim();
        }
    }
    
    public abstract String obtenerTipoPersona();
    
    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }
    
    @Override
    public String toString() {
        return String.format("ID: %d | %s: %s %s | Tel: %s | Email: %s", 
                           id, obtenerTipoPersona(), nombre, apellido, telefono, email);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Persona persona = (Persona) obj;
        return id == persona.id;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
