package com.medicqr.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.Period;

public class Usuario extends Persona {
    private Tutor tutor;
    private PerfilInformacion perfilInformacion;
    private boolean tieneDiscapacidad;
    private String tipoDiscapacidad;
    
    public Usuario() {
        super();
        this.tutor = null;
        this.perfilInformacion = null;
        this.tieneDiscapacidad = false;
        this.tipoDiscapacidad = "";
    }
    
    public Usuario(int id, String nombre, String apellido, String telefono, 
                   String email, String fechaNacimiento, Tutor tutor, 
                   boolean tieneDiscapacidad, String tipoDiscapacidad) {
        super(id, nombre, apellido, telefono, email, fechaNacimiento);
        this.tutor = tutor;
        this.perfilInformacion = new PerfilInformacion();
        this.tieneDiscapacidad = tieneDiscapacidad;
        this.tipoDiscapacidad = tipoDiscapacidad;
    }
    
    public Tutor getTutor() {
        return tutor;
    }
    
    public void setTutor(Tutor tutor) {
        this.tutor = tutor;
        if (tutor != null) {
            tutor.agregarUsuario(this);
        }
    }
    
    public PerfilInformacion getPerfilInformacion() {
        return perfilInformacion;
    }
    
    public void setPerfilInformacion(PerfilInformacion perfilInformacion) {
        this.perfilInformacion = perfilInformacion;
    }
    
    public boolean isTieneDiscapacidad() {
        return tieneDiscapacidad;
    }
    
    public void setTieneDiscapacidad(boolean tieneDiscapacidad) {
        this.tieneDiscapacidad = tieneDiscapacidad;
    }
    
    public String getTipoDiscapacidad() {
        return tipoDiscapacidad;
    }
    
    public void setTipoDiscapacidad(String tipoDiscapacidad) {
        if (tipoDiscapacidad != null) {
            this.tipoDiscapacidad = tipoDiscapacidad.trim();
        }
    }
    
    @Override
    public String obtenerTipoPersona() {
        return "Usuario";
    }
    
    public int calcularEdad() {
        try {
            if (getFechaNacimiento() == null || getFechaNacimiento().isEmpty()) {
                return 0;
            }
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate fechaNac = LocalDate.parse(getFechaNacimiento(), formatter);
            LocalDate hoy = LocalDate.now();
            
            return Period.between(fechaNac, hoy).getYears();
        } catch (Exception e) {
            return 0;
        }
    }
    
    public boolean esMayorDeEdad() {
        return calcularEdad() >= 18;
    }
    
    public String obtenerInformacionEmergencia() {
        StringBuilder info = new StringBuilder();
        
        info.append("╔═══════════════════════════════════════════════════════════╗\n");
        info.append("║         INFORMACIÓN DEL PACIENTE                          ║\n");
        info.append("╚═══════════════════════════════════════════════════════════╝\n");
        info.append("Nombre: ").append(getNombreCompleto()).append("\n");
        info.append("Edad: ").append(calcularEdad()).append(" años\n");
        info.append("Teléfono: ").append(getTelefono() != null ? getTelefono() : "No disponible").append("\n");
        info.append("Email: ").append(getEmail() != null ? getEmail() : "No disponible").append("\n");
        
        if (tieneDiscapacidad && tipoDiscapacidad != null && !tipoDiscapacidad.isEmpty()) {
            info.append("⚠️  DISCAPACIDAD: ").append(tipoDiscapacidad).append("\n");
        }
        
        if (perfilInformacion != null) {
            info.append("\n╔═══════════════════════════════════════════════════════════╗\n");
            info.append("║         🏥 INFORMACIÓN MÉDICA CRÍTICA                     ║\n");
            info.append("╚═══════════════════════════════════════════════════════════╝\n");
            
            if (perfilInformacion.isRequiereAtencionInmediata()) {
                info.append("┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓\n");
                info.append("┃  🚨 ATENCIÓN: REQUIERE ATENCIÓN MÉDICA INMEDIATA  🚨   ┃\n");
                info.append("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛\n");
            }
            
            if (perfilInformacion.getGrupoSanguineo() != null && !perfilInformacion.getGrupoSanguineo().isEmpty()) {
                info.append("\n🩸 GRUPO SANGUÍNEO: ").append(perfilInformacion.getGrupoSanguineo()).append("\n");
            }
            
            if (perfilInformacion.getAlergias() != null && !perfilInformacion.getAlergias().isEmpty()) {
                info.append("\n⚠️  ALERGIAS:\n");
                info.append("   ").append(perfilInformacion.getAlergias()).append("\n");
            }
            
            if (perfilInformacion.getMedicamentos() != null && !perfilInformacion.getMedicamentos().isEmpty()) {
                info.append("\n💊 MEDICAMENTOS ACTUALES:\n");
                info.append("   ").append(perfilInformacion.getMedicamentos()).append("\n");
            }
            
            if (perfilInformacion.getEnfermedadesCronicas() != null && !perfilInformacion.getEnfermedadesCronicas().isEmpty()) {
                info.append("\n🏥 ENFERMEDADES CRÓNICAS:\n");
                info.append("   ").append(perfilInformacion.getEnfermedadesCronicas()).append("\n");
            }
            
            if (perfilInformacion.getObservacionesMedicas() != null && !perfilInformacion.getObservacionesMedicas().isEmpty()) {
                info.append("\n📋 OBSERVACIONES MÉDICAS:\n");
                info.append("   ").append(perfilInformacion.getObservacionesMedicas()).append("\n");
            }
        }
        
        info.append("\n╔═══════════════════════════════════════════════════════════╗\n");
        info.append("║         📞 CONTACTOS DE EMERGENCIA                        ║\n");
        info.append("╚═══════════════════════════════════════════════════════════╝\n");
        
        if (perfilInformacion != null && 
            perfilInformacion.getContactoEmergencia() != null && 
            !perfilInformacion.getContactoEmergencia().isEmpty()) {
            info.append("\n🚨 CONTACTO PRINCIPAL DE EMERGENCIA:\n");
            info.append("   Nombre: ").append(perfilInformacion.getContactoEmergencia()).append("\n");
            info.append("   Teléfono: ").append(perfilInformacion.getTelefonoEmergencia() != null ? 
                                               perfilInformacion.getTelefonoEmergencia() : "No disponible").append("\n");
        }
        
        if (tutor != null) {
            info.append("\n👤 TUTOR/RESPONSABLE:\n");
            info.append("   Nombre: ").append(tutor.getNombreCompleto()).append("\n");
            info.append("   Relación: ").append(tutor.getRelacionConUsuario() != null ? 
                                               tutor.getRelacionConUsuario() : "No especificada").append("\n");
            info.append("   Teléfono: ").append(tutor.getTelefono() != null ? tutor.getTelefono() : "No disponible").append("\n");
            info.append("   Email: ").append(tutor.getEmail() != null ? tutor.getEmail() : "No disponible").append("\n");
        }
        
        info.append("\n═══════════════════════════════════════════════════════════\n");
        info.append("    Información generada el: ").append(java.time.LocalDateTime.now()
            .format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))).append("\n");
        info.append("═══════════════════════════════════════════════════════════\n");
        
        return info.toString();
    }
    
    @Override
    public String toString() {
        return String.format("%s | Edad: %d años | Tutor: %s | Discapacidad: %s",
                           super.toString(), calcularEdad(),
                           tutor != null ? tutor.getNombreCompleto() : "Sin asignar",
                           tieneDiscapacidad ? tipoDiscapacidad : "Ninguna");
    }
}
