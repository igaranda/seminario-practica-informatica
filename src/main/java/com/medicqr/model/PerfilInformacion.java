package com.medicqr.model;

public class PerfilInformacion {
    private int id;
    private String alergias;
    private String medicamentos;
    private String enfermedadesCronicas;
    private String grupoSanguineo;
    private String contactoEmergencia;
    private String telefonoEmergencia;
    private String observacionesMedicas;
    private boolean requiereAtencionInmediata;
    
    public PerfilInformacion() {
        this.id = 0;
        this.alergias = "";
        this.medicamentos = "";
        this.enfermedadesCronicas = "";
        this.grupoSanguineo = "";
        this.contactoEmergencia = "";
        this.telefonoEmergencia = "";
        this.observacionesMedicas = "";
        this.requiereAtencionInmediata = false;
    }
    
    public PerfilInformacion(int id, String alergias, String medicamentos, 
                            String enfermedadesCronicas, String grupoSanguineo,
                            String contactoEmergencia, String telefonoEmergencia) {
        this.id = id;
        this.alergias = alergias != null ? alergias : "";
        this.medicamentos = medicamentos != null ? medicamentos : "";
        this.enfermedadesCronicas = enfermedadesCronicas != null ? enfermedadesCronicas : "";
        this.grupoSanguineo = grupoSanguineo != null ? grupoSanguineo : "";
        this.contactoEmergencia = contactoEmergencia != null ? contactoEmergencia : "";
        this.telefonoEmergencia = telefonoEmergencia != null ? telefonoEmergencia : "";
        this.observacionesMedicas = "";
        this.requiereAtencionInmediata = false;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        if (id > 0) {
            this.id = id;
        }
    }
    
    public String getAlergias() {
        return alergias;
    }
    
    public void setAlergias(String alergias) {
        this.alergias = alergias != null ? alergias.trim() : "";
    }
    
    public String getMedicamentos() {
        return medicamentos;
    }
    
    public void setMedicamentos(String medicamentos) {
        this.medicamentos = medicamentos != null ? medicamentos.trim() : "";
    }
    
    public String getEnfermedadesCronicas() {
        return enfermedadesCronicas;
    }
    
    public void setEnfermedadesCronicas(String enfermedadesCronicas) {
        this.enfermedadesCronicas = enfermedadesCronicas != null ? enfermedadesCronicas.trim() : "";
    }
    
    public String getGrupoSanguineo() {
        return grupoSanguineo;
    }
    
    public void setGrupoSanguineo(String grupoSanguineo) {
        if (grupoSanguineo != null && esGrupoSanguineoValido(grupoSanguineo)) {
            this.grupoSanguineo = grupoSanguineo.trim().toUpperCase();
        }
    }
    
    public String getContactoEmergencia() {
        return contactoEmergencia;
    }
    
    public void setContactoEmergencia(String contactoEmergencia) {
        this.contactoEmergencia = contactoEmergencia != null ? contactoEmergencia.trim() : "";
    }
    
    public String getTelefonoEmergencia() {
        return telefonoEmergencia;
    }
    
    public void setTelefonoEmergencia(String telefonoEmergencia) {
        this.telefonoEmergencia = telefonoEmergencia != null ? telefonoEmergencia.trim() : "";
    }
    
    public String getObservacionesMedicas() {
        return observacionesMedicas;
    }
    
    public void setObservacionesMedicas(String observacionesMedicas) {
        this.observacionesMedicas = observacionesMedicas != null ? observacionesMedicas.trim() : "";
    }
    
    public boolean isRequiereAtencionInmediata() {
        return requiereAtencionInmediata;
    }
    
    public void setRequiereAtencionInmediata(boolean requiereAtencionInmediata) {
        this.requiereAtencionInmediata = requiereAtencionInmediata;
    }
    
    private boolean esGrupoSanguineoValido(String grupo) {
        String[] gruposValidos = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
        for (String grupoValido : gruposValidos) {
            if (grupoValido.equalsIgnoreCase(grupo.trim())) {
                return true;
            }
        }
        return false;
    }
    
    public boolean tieneInformacionCritica() {
        return !alergias.isEmpty() || 
               !medicamentos.isEmpty() || 
               !enfermedadesCronicas.isEmpty() || 
               requiereAtencionInmediata;
    }
    
    public String obtenerResumenMedico() {
        StringBuilder resumen = new StringBuilder();
        resumen.append("RESUMEN MÉDICO\n");
        resumen.append("==============\n");
        
        if (!alergias.isEmpty()) {
            resumen.append("Alergias: ").append(alergias).append("\n");
        }
        
        if (!medicamentos.isEmpty()) {
            resumen.append("Medicamentos: ").append(medicamentos).append("\n");
        }
        
        if (!enfermedadesCronicas.isEmpty()) {
            resumen.append("Enfermedades Crónicas: ").append(enfermedadesCronicas).append("\n");
        }
        
        if (!grupoSanguineo.isEmpty()) {
            resumen.append("Grupo Sanguíneo: ").append(grupoSanguineo).append("\n");
        }
        
        if (requiereAtencionInmediata) {
            resumen.append("⚠️ REQUIERE ATENCIÓN INMEDIATA ⚠️\n");
        }
        
        return resumen.toString();
    }
    
    @Override
    public String toString() {
        return String.format("ID: %d | Grupo Sanguíneo: %s | Crítico: %s | Contacto: %s (%s)",
                           id, grupoSanguineo, 
                           tieneInformacionCritica() ? "Sí" : "No",
                           contactoEmergencia, telefonoEmergencia);
    }
}
