# MedicQR - Sistema de Identificación Médica mediante Pulsera QR

<div align="center">

![Version](https://img.shields.io/badge/version-1.0.0-blue.svg)
![Java](https://img.shields.io/badge/Java-11-orange.svg)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)
![Maven](https://img.shields.io/badge/Maven-3.6+-green.svg)

**Sistema de Identificación Médica y de Contacto mediante Pulsera QR**

Desarrollado por: Ignacio Aranda Galván  
Universidad Empresarial Siglo XXI

</div>

---

## Índice

- [Descripción del Proyecto](#-descripción-del-proyecto)
- [Características Principales](#-características-principales)
- [Requisitos Previos](#-requisitos-previos)
- [Instalación y Configuración](#-instalación-y-configuración)
- [Funcionalidades del Menú](#-funcionalidades-del-menú)
- [Estructura de la Base de Datos](#-estructura-de-la-base-de-datos)
- [Ejecución del Proyecto](#-ejecución-del-proyecto)
- [Tecnologías Utilizadas](#-tecnologías-utilizadas)

---

## Descripción del Proyecto

**MedicQR** es un sistema de gestión de información médica que permite asociar datos críticos de salud a pulseras identificadas mediante códigos QR únicos. El sistema está diseñado para facilitar el acceso rápido a información médica vital en situaciones de emergencia, especialmente para personas con discapacidades, enfermedades crónicas o condiciones que requieren atención especial.

---

## Características Principales

- **Registro completo** de tutores, pacientes y perfiles médicos
- **Búsqueda inteligente** por nombre de paciente, tutor o código QR
- **Gestión de pulseras** con información detallada
- **Perfiles médicos completos** con alergias, medicamentos y condiciones
- **Alertas de atención inmediata** para casos críticos
- **Ordenación de registros** mediante algoritmos de ordenación
- **Base de datos MySQL** con persistencia de datos
- **Interfaz de consola** intuitiva y fácil de usar

---

## Requisitos Previos

Antes de comenzar, asegúrate de tener instalado:

| Herramienta | Versión Requerida | Verificación |
|-------------|-------------------|--------------|
| **Java JDK** | 11 o superior | `java -version` |
| **MySQL Server** | 5.7 o superior (recomendado 8.0) | `mysql --version` |
| **Maven** | 3.6 o superior | `mvn -version` |

---

### Menú Principal

```
┌─────────────────────────────────────────────────────────────┐
│                    MENÚ PRINCIPAL                           │
├─────────────────────────────────────────────────────────────┤
│  1. Crear Nueva Pulsera (Registro Completo)                 │
│  2. Gestionar Pulseras Existentes                           │
│  3. Buscar Pulseras                                         │
│  4. Simular Escaneo QR                                      │
│  5. Listar Todas las Pulseras                               │
│  6. Ordenar Pulseras (Algoritmo de Ordenación)              │
│  0. Salir del Sistema                                       │
└─────────────────────────────────────────────────────────────┘
```

---

### 1) Crear Nueva Pulsera (Registro Completo)

**Descripción:**  
Permite realizar un registro completo en el sistema, creando todos los elementos necesarios para una pulsera funcional.

**¿Qué hace?**
- Registra un **tutor** (responsable del paciente)
- Registra un **usuario/paciente** (portador de la pulsera)
- Crea un **perfil médico** con información detallada
- Genera una **pulsera QR** con código único

**Información solicitada:**

**Datos del Tutor:**
- Nombre y apellido
- Teléfono y email
- Fecha de nacimiento (formato: YYYY-MM-DD)
- Relación con el usuario (ej: Padre, Madre, Hijo)

**Datos del Paciente:**
- Nombre y apellido
- Teléfono y email
- Fecha de nacimiento (formato: YYYY-MM-DD)
- ¿Tiene discapacidad? (true/false)
- Tipo de discapacidad (si aplica)

**Datos Médicos:**
- Alergias
- Medicamentos
- Enfermedades crónicas
- Grupo sanguíneo (A+, A-, B+, B-, AB+, AB-, O+, O-)
- Contacto de emergencia
- Teléfono de emergencia
- Observaciones médicas
- ¿Requiere atención inmediata? (true/false)

**Resultado:**
Genera un código QR único para la pulsera (formato: `MEDICQR_XXXXXX`)

**Ejemplo de uso:**
```
Tutor: María González, madre, tel: 123456789
Paciente: Ana González, 28 años, con asma
Perfil médico: Alérgica a penicilina, medicamento: inhalador
→ Genera pulsera con código: MEDICQR_ABC123DEF456
```

---

### 2) Gestionar Pulseras Existentes

**Descripción:**  
Permite administrar y consultar la información completa de pulseras ya registradas en el sistema.

**Opciones disponibles:**
- **Ver información completa de una pulsera**: Muestra todos los datos asociados

**¿Qué información muestra?**
- **Datos de la pulsera**: ID, código QR, estado, número de accesos
- **Información del paciente**: Nombre completo, teléfono, email, edad, discapacidad
- **Información del tutor**: Datos de contacto y relación
- **Perfil médico completo**: Alergias, medicamentos, enfermedades, grupo sanguíneo

**Cómo usar:**
1. Selecciona la opción 2 del menú principal
2. Ingresa el ID de la pulsera que deseas consultar
3. El sistema mostrará toda la información detallada

**Funcionalidades en desarrollo:**
- Modificar información de pulsera (Opción 3)

---

### 3) Buscar Pulseras

**Descripción:**  
Sistema de búsqueda inteligente que permite localizar pulseras por diferentes criterios.

**Tipos de búsqueda disponibles:**

#### Opción 1: Buscar por nombre del paciente
- Busca coincidencias en nombres y apellidos de pacientes
- No requiere coincidencia exacta (búsqueda parcial)
- Ejemplo: Buscar "Ana" encuentra "Ana González", "Ana Martínez", etc.

#### Opción 2: Buscar por nombre del tutor
- Busca coincidencias en nombres y apellidos de tutores
- Útil cuando no recuerdas el nombre del paciente pero sí del tutor
- Ejemplo: Buscar "María" encuentra todas las pulseras donde el tutor se llama María

#### Opción 3: Buscar por código QR
- Búsqueda exacta por código único de pulsera
- Equivalente a simular un escaneo QR
- Ejemplo: Buscar "MEDICQR_ABC123DEF456"

**Resultados de búsqueda incluyen:**
- ID de la pulsera
- Código QR
- Nombre del paciente
- Nombre del tutor
- Alerta si requiere atención inmediata ⚠️

---

### 4) Simular Escaneo QR

**Descripción:**  
Simula el escaneo de una pulsera QR física, mostrando toda la información médica crítica que aparecería en un escenario real de emergencia.

**¿Qué hace?**
1. Solicita el código QR a escanear
2. Busca la pulsera en la base de datos
3. Registra el acceso (incrementa contador)
4. Actualiza la fecha de último uso
5. Muestra la información médica completa

**Información mostrada al escanear:**
```
═══════════════════════════════════════
       INFORMACIÓN DE EMERGENCIA
═══════════════════════════════════════

PACIENTE: Ana González (28 años)
GRUPO SANGUÍNEO: O+

ALERGIAS: Penicilina
MEDICAMENTOS: Inhalador para asma
ENFERMEDADES: Asma leve

CONTACTO DE EMERGENCIA:
   María González (Madre)
   Tel: 123456789

OBSERVACIONES:
   Paciente con asma leve, requiere
   inhalador en caso de crisis.
```
---

### 5) Listar Todas las Pulseras

**Descripción:**  
Muestra un listado completo de todas las pulseras registradas en el sistema.

**¿Qué información muestra?**
- ID de la pulsera
- Código QR único
- Estado (Activa/Inactiva)
- Nombre completo del paciente
- Total de pulseras registradas

**Formato de salida:**
```
┌─────────────────────────────────────────────────────┐
│              TODAS LAS PULSERAS                      │
└─────────────────────────────────────────────────────┘

Total de pulseras: 3

 Pulsera ID: 1
🔢 Código QR: MEDICQR_ABC123DEF456
 Estado: Activa
 Paciente: Ana González

 Pulsera ID: 2
🔢 Código QR: MEDICQR_XYZ789GHI012
 Estado: Activa
 Paciente: Pedro López
...
```

**Utilidad:**
- Vista general del sistema
- Verificación rápida de pulseras activas
- Obtener IDs para otras operaciones

---

### 6) Ordenar Pulseras (Algoritmo de Ordenación)

**Descripción:**  
Ordena alfabéticamente todas las pulseras por el apellido del paciente utilizando el algoritmo de ordenación (Burbuja).

**¿Qué hace?**
1. Obtiene todas las pulseras con usuarios asignados
2. Aplica el algoritmo de ordenación por burbuja
3. Ordena por apellido del paciente (orden alfabético)
4. Muestra el listado ordenado

**Ejemplo de salida:**
```
┌──────────────────────────────────────────────────┐
│   PULSERAS ORDENADAS POR NOMBRE DE PACIENTE      │
│   (Algoritmo de Ordenación por Burbuja)         │
└──────────────────────────────────────────────────┘

Pulseras ordenadas alfabéticamente por apellido:
- Fernández, José (Pulsera: MEDICQR_STU901VWX234)
- González, Ana (Pulsera: MEDICQR_ABC123DEF456)
- López, Pedro (Pulsera: MEDICQR_XYZ789GHI012)
- Martínez, María (Pulsera: MEDICQR_MNO345PQR678)
```

**Propósito académico:**
Esta funcionalidad demuestra la implementación de un algoritmo de ordenación básico (Bubble Sort) como parte de los requisitos académicos del proyecto.

---

### 0) Salir del Sistema

**Descripción:**  
Cierra la aplicación de forma segura, liberando recursos y cerrando conexiones.

**¿Qué hace?**
1. Solicita confirmación antes de salir
2. Cierra la conexión con la base de datos
3. Libera recursos del sistema
4. Finaliza la aplicación

**Mensaje de confirmación:**
```
¿Está seguro de que desea salir? (s/n):
```

---

## Ejecución del Proyecto

```

###  Ejecutar JAR compilado
```bash
mkdir -p bin
javac -cp "lib/mysql-connector-java-8.0.33.jar" -d bin \
  src/main/java/com/medicqr/model/*.java \
  src/main/java/com/medicqr/dao/*.java \
  src/main/java/com/medicqr/service/*.java \
  src/main/java/com/medicqr/MedicQRApplication.java
```

```bash
# Ejecutar el JAR con dependencias incluidas
java -cp "lib/mysql-connector-java-8.0.33.jar:MedicQR.jar" com.medicqr.MedicQRApplication
```