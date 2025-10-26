package com.medicqr.service;

import com.medicqr.dao.*;
import com.medicqr.model.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MedicQRService {
    public UsuarioDAO usuarioDAO;
    public TutorDAO tutorDAO;
    public PerfilInformacionDAO perfilDAO;
    public PulseraQRDAO pulseraDAO;
    
    public MedicQRService() {
        this.usuarioDAO = new UsuarioDAO();
        this.tutorDAO = new TutorDAO();
        this.perfilDAO = new PerfilInformacionDAO();
        this.pulseraDAO = new PulseraQRDAO();
    }
    
    public boolean inicializar() {
        if (!DatabaseConnection.getInstance().testConnection()) {
            System.err.println("\nERROR CRÍTICO: No se pudo conectar a la base de datos MySQL");
            return false;
        }
        
        return true;
    }
    
    public Usuario registrarUsuario(String nombre, String apellido, String telefono, 
                                   String email, String fechaNacimiento, int tutorId,
                                   boolean tieneDiscapacidad, String tipoDiscapacidad) {
        try {
            Tutor tutor = buscarTutorPorId(tutorId);
            if (tutor == null) {
                System.err.println("[ERROR] Tutor no encontrado con ID: " + tutorId);
                return null;
            }
            
            Usuario usuario = new Usuario();
            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
            usuario.setTelefono(telefono);
            usuario.setEmail(email);
            usuario.setFechaNacimiento(fechaNacimiento);
            usuario.setTutor(tutor);
            usuario.setTieneDiscapacidad(tieneDiscapacidad);
            usuario.setTipoDiscapacidad(tipoDiscapacidad);
            
            if (usuarioDAO.insertarUsuario(usuario)) {
                PerfilInformacion perfil = new PerfilInformacion();
                perfil.setId(usuario.getId());
                usuario.setPerfilInformacion(perfil);
                
                return usuario;
            }
        } catch (Exception e) {
            System.err.println("[ERROR] Error al registrar usuario: " + e.getMessage());
        }
        
        return null;
    }
    
    public Tutor registrarTutor(String nombre, String apellido, String telefono, 
                               String email, String fechaNacimiento, String relacionConUsuario) {
        try {
            Tutor tutor = new Tutor();
            tutor.setNombre(nombre);
            tutor.setApellido(apellido);
            tutor.setTelefono(telefono);
            tutor.setEmail(email);
            tutor.setFechaNacimiento(fechaNacimiento);
            tutor.setRelacionConUsuario(relacionConUsuario);
            
            if (tutorDAO.insertarTutor(tutor)) {
                return tutor;
            }
        } catch (Exception e) {
            System.err.println("[ERROR] Error al registrar tutor: " + e.getMessage());
        }
        
        return null;
    }
    
    public PulseraQR crearPulseraQR(Usuario usuario) {
        try {
            PulseraQR pulsera = new PulseraQR();
            pulsera.setUsuario(usuario);
            
            if (pulseraDAO.insertarPulsera(pulsera)) {
                return pulsera;
            }
        } catch (Exception e) {
            System.err.println("[ERROR] Error al crear pulsera QR: " + e.getMessage());
        }
        
        return null;
    }
    
    public List<Usuario> buscarUsuarios(String nombre) {
        List<Usuario> resultados = new ArrayList<>();
        List<Usuario> todosLosUsuarios = usuarioDAO.obtenerTodos();
        
        for (Usuario usuario : todosLosUsuarios) {
            if (usuario.getNombre().toLowerCase().contains(nombre.toLowerCase()) ||
                usuario.getApellido().toLowerCase().contains(nombre.toLowerCase())) {
                resultados.add(usuario);
            }
        }
        
        return resultados;
    }
    
    public List<Usuario> obtenerUsuariosOrdenados() {
        List<Usuario> usuariosOrdenados = new ArrayList<>(usuarioDAO.obtenerTodos());
        
        for (int i = 0; i < usuariosOrdenados.size() - 1; i++) {
            for (int j = 0; j < usuariosOrdenados.size() - i - 1; j++) {
                if (usuariosOrdenados.get(j).getApellido().compareToIgnoreCase(usuariosOrdenados.get(j + 1).getApellido()) > 0) {
                    Usuario temp = usuariosOrdenados.get(j);
                    usuariosOrdenados.set(j, usuariosOrdenados.get(j + 1));
                    usuariosOrdenados.set(j + 1, temp);
                }
            }
        }
        
        return usuariosOrdenados;
    }
    
    public String escanearQR(String codigoQR) {
        try {
            PulseraQR pulsera = pulseraDAO.buscarPorCodigo(codigoQR);
            
            if (pulsera != null) {
                if (pulsera.puedeSerEscaneada()) {
                    pulsera.registrarAcceso();
                    pulseraDAO.actualizarPulsera(pulsera);
                    return pulsera.obtenerInformacionQR();
                } else {
                    return "[ERROR] Pulsera inactiva o sin usuario asignado";
                }
            }
        } catch (Exception e) {
            System.err.println("[ERROR] Error al escanear QR: " + e.getMessage());
        }
        
        return "[ERROR] Código QR no encontrado";
    }
    
    private Tutor buscarTutorPorId(int id) {
        return tutorDAO.buscarPorId(id);
    }
    
    public boolean crearPerfilInformacion(int usuarioId, String alergias, String medicamentos,
                                        String enfermedadesCronicas, String grupoSanguineo,
                                        String contactoEmergencia, String telefonoEmergencia,
                                        String observacionesMedicas, boolean requiereAtencionInmediata) {
        try {
            PerfilInformacion perfil = new PerfilInformacion();
            perfil.setId(usuarioId);
            perfil.setAlergias(alergias);
            perfil.setMedicamentos(medicamentos);
            perfil.setEnfermedadesCronicas(enfermedadesCronicas);
            perfil.setGrupoSanguineo(grupoSanguineo);
            perfil.setContactoEmergencia(contactoEmergencia);
            perfil.setTelefonoEmergencia(telefonoEmergencia);
            perfil.setObservacionesMedicas(observacionesMedicas);
            perfil.setRequiereAtencionInmediata(requiereAtencionInmediata);
            
            return perfilDAO.insertarPerfil(perfil, usuarioId);
            
        } catch (Exception e) {
            System.err.println("[ERROR] Error al crear perfil de información: " + e.getMessage());
        }
        
        return false;
    }
    
    public PulseraQR crearPulseraVacia() {
        try {
            PulseraQR pulsera = new PulseraQR();
            pulsera.setUsuario(null);
            
            if (pulseraDAO.insertarPulsera(pulsera)) {
                return pulsera;
            }
        } catch (Exception e) {
            System.err.println("[ERROR] Error al crear pulsera vacía: " + e.getMessage());
        }
        
        return null;
    }
    
    public PulseraQR registroCompleto(
        String tutorNombre, String tutorApellido, String tutorTelefono, String tutorEmail, String tutorFechaNacimiento, String relacionConUsuario,
        String usuarioNombre, String usuarioApellido, String usuarioTelefono, String usuarioEmail, String usuarioFechaNacimiento,
        boolean tieneDiscapacidad, String tipoDiscapacidad,
        String alergias, String medicamentos, String enfermedadesCronicas, String grupoSanguineo,
        String contactoEmergencia, String telefonoEmergencia, String observacionesMedicas, boolean requiereAtencionInmediata
    ) {
        try {
            Tutor tutor = registrarTutor(tutorNombre, tutorApellido, tutorTelefono, tutorEmail, tutorFechaNacimiento, relacionConUsuario);
            if (tutor == null) {
                throw new Exception("Error al crear el tutor");
            }
            
            Usuario usuario = registrarUsuario(usuarioNombre, usuarioApellido, usuarioTelefono, usuarioEmail, usuarioFechaNacimiento,
                                              tutor.getId(), tieneDiscapacidad, tipoDiscapacidad);
            if (usuario == null) {
                throw new Exception("Error al crear el usuario");
            }
            
            boolean perfilCreado = crearPerfilInformacion(usuario.getId(), alergias, medicamentos, enfermedadesCronicas,
                                                         grupoSanguineo, contactoEmergencia, telefonoEmergencia,
                                                         observacionesMedicas, requiereAtencionInmediata);
            if (!perfilCreado) {
                throw new Exception("Error al crear el perfil médico");
            }
            
            PerfilInformacion perfil = perfilDAO.buscarPorUsuarioId(usuario.getId());
            usuario.setPerfilInformacion(perfil);
            
            PulseraQR pulsera = crearPulseraQR(usuario);
            if (pulsera == null) {
                throw new Exception("Error al crear la pulsera QR");
            }
            
            System.out.println("[EXITO] Registro completo exitoso - Pulsera: " + pulsera.getCodigoUnico());
            return pulsera;
            
        } catch (Exception e) {
            System.err.println("[ERROR] Error en registro completo: " + e.getMessage());
        }
        
        return null;
    }
    
    public Map<String, Object> obtenerInformacionCompletaPulsera(int pulseraId) {
        try {
            PulseraQR pulsera = pulseraDAO.buscarPorId(pulseraId);
            
            if (pulsera == null) {
                return null;
            }
            
            Map<String, Object> resultado = new HashMap<>();
            resultado.put("pulsera", pulsera);
            
            if (pulsera.getUsuario() != null) {
                resultado.put("paciente", pulsera.getUsuario());
                
                if (pulsera.getUsuario().getTutor() != null) {
                    resultado.put("tutor", pulsera.getUsuario().getTutor());
                }
                
                if (pulsera.getUsuario().getPerfilInformacion() != null) {
                    resultado.put("perfilMedico", pulsera.getUsuario().getPerfilInformacion());
                }
            }
            
            return resultado;
            
        } catch (Exception e) {
            System.err.println("[ERROR] Error al obtener información completa de pulsera: " + e.getMessage());
        }
        
        return null;
    }
    
    public List<Map<String, Object>> buscarPulserasPorNombre(String busqueda) {
        List<Map<String, Object>> resultados = new ArrayList<>();
        
        try {
            List<PulseraQR> pulseras = pulseraDAO.obtenerTodas();
            
            for (PulseraQR pulsera : pulseras) {
                if (pulsera.getUsuario() != null) {
                    Usuario usuario = pulsera.getUsuario();
                    boolean coincide = false;
                    
                    if (usuario.getNombre().toLowerCase().contains(busqueda.toLowerCase()) ||
                        usuario.getApellido().toLowerCase().contains(busqueda.toLowerCase())) {
                        coincide = true;
                    }
                    
                    if (usuario.getTutor() != null) {
                        Tutor tutor = usuario.getTutor();
                        if (tutor.getNombre().toLowerCase().contains(busqueda.toLowerCase()) ||
                            tutor.getApellido().toLowerCase().contains(busqueda.toLowerCase())) {
                            coincide = true;
                        }
                    }
                    
                    if (coincide) {
                        Map<String, Object> info = new HashMap<>();
                        info.put("pulsera", pulsera);
                        info.put("paciente", usuario);
                        
                        if (usuario.getTutor() != null) {
                            info.put("tutor", usuario.getTutor());
                        }
                        
                        if (usuario.getPerfilInformacion() != null) {
                            info.put("perfilMedico", usuario.getPerfilInformacion());
                        }
                        
                        resultados.add(info);
                    }
                }
            }
            
        } catch (Exception e) {
            System.err.println("[ERROR] Error al buscar pulseras por nombre: " + e.getMessage());
        }
        
        return resultados;
    }
    
    public void cerrar() {
        DatabaseConnection.getInstance().closeConnection();
        System.out.println("[EXITO] Servicio MedicQR cerrado correctamente");
    }
}
