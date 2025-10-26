package com.medicqr;

import com.medicqr.service.MedicQRService;
import com.medicqr.model.*;
import java.util.*;

public class MedicQRApplication {
    private MedicQRService service;
    private Scanner scanner;
    private boolean ejecutando;
    
    public MedicQRApplication() {
        this.service = new MedicQRService();
        this.scanner = new Scanner(System.in);
        this.ejecutando = true;
    }
    
    public static void main(String[] args) {
        MedicQRApplication app = new MedicQRApplication();
        
        try {
            app.iniciar();
        } catch (Exception e) {
            System.err.println("[ERROR] Error crítico en la aplicación: " + e.getMessage());
            e.printStackTrace();
        } finally {
            app.cerrar();
        }
    }
    
    private void iniciar() {
        mostrarBanner();
        
        if (!service.inicializar()) {
            System.err.println("\n⛔ La aplicación no puede continuar sin base de datos.");
            System.err.println("   Por favor, configure MySQL e intente nuevamente.\n");
            return;
        }
        
        System.out.println("✓ Conexión a MySQL establecida correctamente\n");
        
        crearDatosDePrueba();
        
        System.out.println("✓ Sistema inicializado correctamente");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
        
        while (ejecutando) {
            try {
                mostrarMenuPrincipal();
                int opcion = leerOpcion();
                procesarOpcion(opcion);
            } catch (InputMismatchException e) {
                System.err.println("[ERROR] Error: Debe ingresar un número válido.");
                scanner.nextLine();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            } catch (Exception e) {
                System.err.println("[ERROR] Error inesperado: " + e.getMessage());
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
    
    private void mostrarBanner() {
        System.out.println("\n" +
            "╔══════════════════════════════════════════════════════════════╗\n" +
            "║                    SISTEMA MEDICQR v1.0                      ║\n" +
            "║        Sistema de Identificación Médica y Contacto           ║\n" +
            "║                   mediante Pulsera QR                        ║\n" +
            "║                                                              ║\n" +
            "║  Desarrollado por: Ignacio Aranda Galván                     ║\n" +
            "║  Universidad Empresarial Siglo XXI                          ║\n" +
            "╚══════════════════════════════════════════════════════════════╝\n");
    }
    
    private void mostrarMenuPrincipal() {
        System.out.println("\n" +
            "┌─────────────────────────────────────────────────────────────┐\n" +
            "│                    MENÚ PRINCIPAL                           │\n" +
            "├─────────────────────────────────────────────────────────────┤\n" +
            "│  1. Crear Nueva Pulsera (Registro Completo)               │\n" +
            "│  2. Gestionar Pulseras Existentes                         │\n" +
            "│  3. Buscar Pulseras                                       │\n" +
            "│  4. Simular Escaneo QR                                    │\n" +
            "│  5. Listar Todas las Pulseras                             │\n" +
            "│  6. Ordenar Pulseras (Algoritmo de Ordenación)             │\n" +
            "│  0. Salir del Sistema                                     │\n" +
            "└─────────────────────────────────────────────────────────────┘\n");
        System.out.print("Seleccione una opción (0-6): ");
    }
    
    private int leerOpcion() {
        try {
            if (!scanner.hasNextLine()) {
                System.out.println("[INFO] No hay más entrada disponible. Cerrando aplicación...");
                return 0;
            }
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                return -1;
            }
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.err.println("[ERROR] Por favor, ingrese un número válido.");
            return -1;
        } catch (NoSuchElementException e) {
            System.out.println("[INFO] No hay más entrada disponible. Cerrando aplicación...");
            return 0;
        }
    }
    
    private void procesarOpcion(int opcion) {
        if (opcion == -1) {
            return;
        }
        
        switch (opcion) {
            case 1:
                crearNuevaPulsera();
                break;
            case 2:
                gestionarPulserasExistentes();
                break;
            case 3:
                buscarPulseras();
                break;
            case 4:
                simularEscaneoQR();
                break;
            case 5:
                listarTodasLasPulseras();
                break;
            case 6:
                ordenarPulseras();
                break;
            case 0:
                salir();
                break;
            default:
                System.err.println("[ERROR] Opción no válida. Por favor, seleccione una opción del 0 al 6.");
        }
        
        if (opcion != 0) {
            pausar();
        }
    }
    
    private void crearNuevaPulsera() {
        System.out.println("\n┌─────────────────────────────────────────────────────────────┐");
        System.out.println("│              CREAR NUEVA PULSERA                            │");
        System.out.println("│        (Tutor + Paciente + Perfil Médico + Pulsera)        │");
        System.out.println("└─────────────────────────────────────────────────────────────┘");
        
        try {
            System.out.println("\n=== DATOS DEL TUTOR ===");
            System.out.print("Nombre del tutor: ");
            String tutorNombre = scanner.nextLine();
            
            System.out.print("Apellido del tutor: ");
            String tutorApellido = scanner.nextLine();
            
            System.out.print("Teléfono del tutor: ");
            String tutorTelefono = scanner.nextLine();
            
            System.out.print("Email del tutor: ");
            String tutorEmail = scanner.nextLine();
            
            System.out.print("Fecha de nacimiento del tutor (YYYY-MM-DD): ");
            String tutorFechaNacimiento = scanner.nextLine();
            
            System.out.print("Relación con el usuario (ej: Padre, Madre, Hijo): ");
            String relacionConUsuario = scanner.nextLine();
            
            System.out.println("\n=== DATOS DEL PACIENTE/USUARIO ===");
            System.out.print("Nombre del paciente: ");
            String usuarioNombre = scanner.nextLine();
            
            System.out.print("Apellido del paciente: ");
            String usuarioApellido = scanner.nextLine();
            
            System.out.print("Teléfono del paciente: ");
            String usuarioTelefono = scanner.nextLine();
            
            System.out.print("Email del paciente: ");
            String usuarioEmail = scanner.nextLine();
            
            System.out.print("Fecha de nacimiento del paciente (YYYY-MM-DD): ");
            String usuarioFechaNacimiento = scanner.nextLine();
            
            System.out.print("¿Tiene discapacidad? (true/false): ");
            boolean tieneDiscapacidad = scanner.nextBoolean();
            scanner.nextLine();
            
            String tipoDiscapacidad = "";
            if (tieneDiscapacidad) {
                System.out.print("Tipo de discapacidad: ");
                tipoDiscapacidad = scanner.nextLine();
            }
            
            System.out.println("\n=== DATOS MÉDICOS ===");
            System.out.print("Alergias: ");
            String alergias = scanner.nextLine();
            
            System.out.print("Medicamentos: ");
            String medicamentos = scanner.nextLine();
            
            System.out.print("Enfermedades crónicas: ");
            String enfermedadesCronicas = scanner.nextLine();
            
            System.out.print("Grupo sanguíneo (A+, A-, B+, B-, AB+, AB-, O+, O-): ");
            String grupoSanguineo = scanner.nextLine();
            
            System.out.print("Contacto de emergencia: ");
            String contactoEmergencia = scanner.nextLine();
            
            System.out.print("Teléfono de emergencia: ");
            String telefonoEmergencia = scanner.nextLine();
            
            System.out.print("Observaciones médicas: ");
            String observacionesMedicas = scanner.nextLine();
            
            System.out.print("¿Requiere atención inmediata? (true/false): ");
            boolean requiereAtencionInmediata = scanner.nextBoolean();
            scanner.nextLine();
            
            System.out.println("\n=== DATOS DE LA PULSERA ===");
            
            PulseraQR pulsera = service.registroCompleto(
                tutorNombre, tutorApellido, tutorTelefono, tutorEmail, tutorFechaNacimiento, relacionConUsuario,
                usuarioNombre, usuarioApellido, usuarioTelefono, usuarioEmail, usuarioFechaNacimiento,
                tieneDiscapacidad, tipoDiscapacidad,
                alergias, medicamentos, enfermedadesCronicas, grupoSanguineo,
                contactoEmergencia, telefonoEmergencia, observacionesMedicas, requiereAtencionInmediata
            );
            
            if (pulsera != null) {
                System.out.println("\n[EXITO] ¡Pulsera creada exitosamente!");
                System.out.println(" Código QR: " + pulsera.getCodigoUnico());
                System.out.println(" Paciente: " + usuarioNombre + " " + usuarioApellido);
                System.out.println(" Tutor: " + tutorNombre + " " + tutorApellido);
            } else {
                System.out.println("[ERROR] Error al crear la pulsera.");
            }
            
        } catch (Exception e) {
            System.err.println("[ERROR] Error al crear la pulsera: " + e.getMessage());
        }
    }
    
    private void gestionarPulserasExistentes() {
        System.out.println("\n┌─────────────────────────────────────────────────────────────┐");
        System.out.println("│              GESTIONAR PULSERAS EXISTENTES                    │");
        System.out.println("└─────────────────────────────────────────────────────────────┘");
        
        System.out.println("\nOpciones disponibles:");
        System.out.println("1. Ver información completa de una pulsera");
        System.out.println("3. Modificar información de pulsera");
        System.out.print("Seleccione una opción: ");
        
        try {
            String input = scanner.nextLine().trim();
            int subOpcion = Integer.parseInt(input);
            
            switch (subOpcion) {
                case 1:
                    verInformacionCompletaPulsera();
                    break;
                case 3:
                    System.out.println("⚠️  Funcionalidad de modificación en desarrollo.");
                    break;
                default:
                    System.err.println("[ERROR] Opción no válida. Seleccione 1, 2 o 3.");
            }
        } catch (NumberFormatException e) {
            System.err.println("[ERROR] Error: Debe ingresar un número válido.");
        }
    }
    
    private void buscarPulseras() {
        System.out.println("\n┌─────────────────────────────────────────────────────────────┐");
        System.out.println("│              BÚSQUEDA DE PULSERAS                             │");
        System.out.println("└─────────────────────────────────────────────────────────────┘");
        
        System.out.println("\nTipos de búsqueda disponibles:");
        System.out.println("1. Buscar por nombre del paciente");
        System.out.println("2. Buscar por nombre del tutor");
        System.out.println("3. Buscar por código QR");
        System.out.print("Seleccione una opción: ");
        
        try {
            String input = scanner.nextLine().trim();
            int opcion = Integer.parseInt(input);
            
            switch (opcion) {
                case 1:
                case 2:
                    System.out.print("Ingrese el nombre a buscar: ");
                    String busqueda = scanner.nextLine();
                    buscarPulserasPorNombre(busqueda);
                    break;
                case 3:
                    System.out.print("Ingrese el código QR: ");
                    String codigoQR = scanner.nextLine();
                    String resultado = service.escanearQR(codigoQR);
                    System.out.println("\n" + resultado);
                    break;
                default:
                    System.err.println("[ERROR] Opción no válida. Seleccione 1, 2 o 3.");
            }
        } catch (NumberFormatException e) {
            System.err.println("[ERROR] Error: Debe ingresar un número válido.");
        }
    }
    
    private void listarTodasLasPulseras() {
        System.out.println("\n┌─────────────────────────────────────────────────────────────┐");
        System.out.println("│              TODAS LAS PULSERAS                              │");
        System.out.println("└─────────────────────────────────────────────────────────────┘");
        
        List<PulseraQR> pulseras = service.pulseraDAO.obtenerTodas();
        
        if (pulseras.isEmpty()) {
            System.out.println("[ERROR] No hay pulseras registradas.");
        } else {
            System.out.println("[EXITO] Total de pulseras: " + pulseras.size());
            for (PulseraQR pulsera : pulseras) {
                System.out.println("\n Pulsera ID: " + pulsera.getId());
                System.out.println("🔢 Código QR: " + pulsera.getCodigoUnico());
                System.out.println(" Estado: " + (pulsera.isEstaActiva() ? "Activa" : "Inactiva"));
                if (pulsera.getUsuario() != null) {
                    System.out.println(" Paciente: " + pulsera.getUsuario().getNombreCompleto());
                } else {
                    System.out.println(" Paciente: Sin asignar");
                }
            }
        }
    }
    
    private void ordenarPulseras() {
        System.out.println("\n┌─────────────────────────────────────────────────────────────┐");
        System.out.println("│         PULSERAS ORDENADAS POR NOMBRE DE PACIENTE             │");
        System.out.println("│         (Algoritmo de Ordenación por Burbuja)               │");
        System.out.println("└─────────────────────────────────────────────────────────────┘");
        
        List<PulseraQR> pulseras = service.pulseraDAO.obtenerTodas();
        
        if (pulseras.isEmpty()) {
            System.out.println("[ERROR] No hay pulseras para ordenar.");
        } else {
            List<PulseraQR> pulserasConUsuario = new ArrayList<>();
            for (PulseraQR pulsera : pulseras) {
                if (pulsera.getUsuario() != null) {
                    pulserasConUsuario.add(pulsera);
                }
            }
            
            if (pulserasConUsuario.isEmpty()) {
                System.out.println("[ERROR] No hay pulseras con usuarios asignados para ordenar.");
            } else {
                for (int i = 0; i < pulserasConUsuario.size() - 1; i++) {
                    for (int j = 0; j < pulserasConUsuario.size() - i - 1; j++) {
                        String nombre1 = pulserasConUsuario.get(j).getUsuario().getApellido();
                        String nombre2 = pulserasConUsuario.get(j + 1).getUsuario().getApellido();
                        if (nombre1.compareToIgnoreCase(nombre2) > 0) {
                            PulseraQR temp = pulserasConUsuario.get(j);
                            pulserasConUsuario.set(j, pulserasConUsuario.get(j + 1));
                            pulserasConUsuario.set(j + 1, temp);
                        }
                    }
                }
                
                System.out.println("[EXITO] Pulseras ordenadas alfabéticamente por apellido del paciente:");
                for (PulseraQR pulsera : pulserasConUsuario) {
                    System.out.println("- " + pulsera.getUsuario().getNombreCompleto() + 
                                     " (Pulsera: " + pulsera.getCodigoUnico() + ")");
                }
            }
        }
    }
    
    private void simularEscaneoQR() {
        System.out.println("\n┌─────────────────────────────────────────────────────────────┐");
        System.out.println("│              SIMULACIÓN DE ESCANEO QR                       │");
        System.out.println("└─────────────────────────────────────────────────────────────┘");
        
        System.out.print("Ingrese el código QR a escanear: ");
        String codigoQR = scanner.nextLine();
        
        String resultado = service.escanearQR(codigoQR);
        System.out.println("\n" + resultado);
    }
    
    private void crearDatosDePrueba() {
        List<PulseraQR> pulserasExistentes = service.pulseraDAO.obtenerTodas();
        
        if (!pulserasExistentes.isEmpty()) {
            System.out.println("✓ Base de datos ya contiene " + pulserasExistentes.size() + " pulsera(s)\n");
            return;
        }
        
        System.out.println("📦 Creando datos de demostración...");
        
        Tutor tutor1 = service.registrarTutor("Maria", "Gonzalez", "123456789", "maria@email.com", "1970-05-15", "Madre");
        Tutor tutor2 = service.registrarTutor("Carlos", "Lopez", "987654321", "carlos@email.com", "1965-03-20", "Padre");
        Tutor tutor3 = service.registrarTutor("Ana", "Martinez", "555555555", "ana.martinez@email.com", "1980-08-12", "Hija");
        
        if (tutor1 != null) {
            Usuario usuario1 = service.registrarUsuario("Ana", "Gonzalez", "111111111", "ana.gonzalez@email.com", "1995-08-10", tutor1.getId(), false, "");
            if (usuario1 != null) {
                service.crearPerfilInformacion(usuario1.getId(), "Penicilina", "Ninguno", "Asma", "O+", 
                    "Maria Gonzalez", "123456789", "Paciente con asma leve", false);
                PulseraQR pulsera1 = service.crearPulseraQR(usuario1);
                if (pulsera1 != null) {
                    System.out.println("  ✓ Pulsera #1: " + usuario1.getNombreCompleto() + " → " + pulsera1.getCodigoUnico());
                }
            }
        }
        
        if (tutor2 != null) {
            Usuario usuario2 = service.registrarUsuario("Pedro", "Lopez", "222222222", "pedro.lopez@email.com", "2000-12-05", tutor2.getId(), true, "Autismo");
            if (usuario2 != null) {
                service.crearPerfilInformacion(usuario2.getId(), "Ninguna", "Risperidona", "Autismo", "A+", 
                    "Carlos Lopez", "987654321", "Requiere comunicación clara", false);
                PulseraQR pulsera2 = service.crearPulseraQR(usuario2);
                if (pulsera2 != null) {
                    System.out.println("  ✓ Pulsera #2: " + usuario2.getNombreCompleto() + " → " + pulsera2.getCodigoUnico());
                }
            }
        }
        
        if (tutor3 != null) {
            Usuario usuario3 = service.registrarUsuario("Jose", "Fernandez", "666666666", "jose.fernandez@email.com", "1945-09-15", tutor3.getId(), false, "");
            if (usuario3 != null) {
                service.crearPerfilInformacion(usuario3.getId(), "Ninguna", "Donepezilo", "Alzheimer", "AB+", 
                    "Ana Martinez", "555555555", "⚠️ PUEDE DESORIENTARSE - REQUIERE SUPERVISIÓN", true);
                PulseraQR pulsera3 = service.crearPulseraQR(usuario3);
                if (pulsera3 != null) {
                    System.out.println("  ✓ Pulsera #3: " + usuario3.getNombreCompleto() + " → " + pulsera3.getCodigoUnico() + " [CRÍTICO]");
                }
            }
        }
        
        System.out.println("✓ Datos de demostración cargados correctamente\n");
    }
    
    private void pausar() {
        System.out.print("\nPresione Enter para continuar...");
        scanner.nextLine();
    }
    
    private void salir() {
        System.out.println("\n┌─────────────────────────────────────────────────────────────┐");
        System.out.println("│                    SALIENDO DEL SISTEMA                     │");
        System.out.println("└─────────────────────────────────────────────────────────────┘");
        
        try {
            if (scanner.hasNextLine()) {
                System.out.println("¿Está seguro de que desea salir? (s/n): ");
                String respuesta = scanner.nextLine().toLowerCase();
                
                if (respuesta.equals("s") || respuesta.equals("si") || respuesta.equals("sí")) {
                    ejecutando = false;
                    System.out.println("👋 ¡Gracias por usar el Sistema MedicQR!");
                } else {
                    System.out.println("[EXITO] Continuando con el sistema...");
                }
            } else {
                ejecutando = false;
                System.out.println("👋 ¡Gracias por usar el Sistema MedicQR!");
            }
        } catch (NoSuchElementException e) {
            ejecutando = false;
            System.out.println("👋 ¡Gracias por usar el Sistema MedicQR!");
        }
    }
    
    private void buscarPulserasPorNombre(String busqueda) {
        System.out.println("\n┌─────────────────────────────────────────────────────────────┐");
        System.out.println("│              BÚSQUEDA DE PULSERAS POR NOMBRE                 │");
        System.out.println("└─────────────────────────────────────────────────────────────┘");
        
        List<Map<String, Object>> resultados = service.buscarPulserasPorNombre(busqueda);
        
        if (resultados.isEmpty()) {
            System.out.println("[ERROR] No se encontraron pulseras con ese criterio de búsqueda.");
        } else {
            System.out.println("[EXITO] Pulseras encontradas:");
            for (Map<String, Object> resultado : resultados) {
                PulseraQR pulsera = (PulseraQR) resultado.get("pulsera");
                Usuario paciente = (Usuario) resultado.get("paciente");
                Tutor tutor = (Tutor) resultado.get("tutor");
                PerfilInformacion perfil = (PerfilInformacion) resultado.get("perfilMedico");
                
                System.out.println("\n Pulsera ID: " + pulsera.getId());
                System.out.println("🔢 Código QR: " + pulsera.getCodigoUnico());
                System.out.println(" Paciente: " + paciente.getNombreCompleto());
                if (tutor != null) {
                    System.out.println(" Tutor: " + tutor.getNombreCompleto());
                }
                if (perfil != null && perfil.isRequiereAtencionInmediata()) {
                    System.out.println("[CRITICO] ¡REQUIERE ATENCIÓN INMEDIATA!");
                }
            }
        }
    }
    
    
    private void verInformacionCompletaPulsera() {
        System.out.println("\n┌─────────────────────────────────────────────────────────────┐");
        System.out.println("│              INFORMACIÓN COMPLETA DE PULSERA                 │");
        System.out.println("└─────────────────────────────────────────────────────────────┘");
        
        System.out.print("Ingrese el ID de la pulsera: ");
        
        try {
            int pulseraId = scanner.nextInt();
            scanner.nextLine();
            
            Map<String, Object> info = service.obtenerInformacionCompletaPulsera(pulseraId);
            
            if (info != null) {
                PulseraQR pulsera = (PulseraQR) info.get("pulsera");
                Usuario paciente = (Usuario) info.get("paciente");
                Tutor tutor = (Tutor) info.get("tutor");
                PerfilInformacion perfil = (PerfilInformacion) info.get("perfilMedico");
                
                System.out.println("\n === INFORMACIÓN DE LA PULSERA ===");
                System.out.println("ID: " + pulsera.getId());
                System.out.println("Código QR: " + pulsera.getCodigoUnico());
                System.out.println("Estado: " + (pulsera.isEstaActiva() ? "Activa" : "Inactiva"));
                System.out.println("Accesos: " + pulsera.getContadorAccesos());
                
                if (paciente != null) {
                    System.out.println("\n === INFORMACIÓN DEL PACIENTE ===");
                    System.out.println("Nombre: " + paciente.getNombreCompleto());
                    System.out.println("Teléfono: " + paciente.getTelefono());
                    System.out.println("Email: " + paciente.getEmail());
                    System.out.println("Fecha de nacimiento: " + paciente.getFechaNacimiento());
                    System.out.println("Edad: " + paciente.calcularEdad() + " años");
                    if (paciente.isTieneDiscapacidad()) {
                        System.out.println("Discapacidad: " + paciente.getTipoDiscapacidad());
                    }
                } else {
                    System.out.println("\n[ERROR] Esta pulsera no tiene usuario asignado.");
                }
                
                if (tutor != null) {
                    System.out.println("\n === INFORMACIÓN DEL TUTOR ===");
                    System.out.println("Nombre: " + tutor.getNombreCompleto());
                    System.out.println("Teléfono: " + tutor.getTelefono());
                    System.out.println("Email: " + tutor.getEmail());
                    System.out.println("Relación: " + tutor.getRelacionConUsuario());
                }
                
                if (perfil != null) {
                    System.out.println("\n🏥 === INFORMACIÓN MÉDICA ===");
                    System.out.println(perfil.obtenerResumenMedico());
                }
                
            } else {
                System.out.println("[ERROR] Pulsera no encontrada.");
            }
            
        } catch (NumberFormatException e) {
            System.err.println("[ERROR] Error: Debe ingresar un número válido.");
        }
    }
    
    private void cerrar() {
        if (service != null) {
            service.cerrar();
        }
        if (scanner != null) {
            scanner.close();
        }
    }
}
