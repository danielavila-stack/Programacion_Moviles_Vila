# Clínica Salud+ (Fase 1 - Rama `sin-ia`)

Aplicación móvil para la gestión y agendamiento de citas médicas, desarrollada de forma nativa para Android utilizando **Kotlin** y **Jetpack Compose**.

---

## Estructura del Proyecto

```text
app/src/main/java/com/daniela/clinicasalud/
├── model/
│   └── ClinicModels.kt           # Modelos de datos (Doctor, Appointment, DateOption) y LocalData
├── navigation/
│   └── Screen.kt                 # Definición de rutas y argumentos de navegación
├── ui/
│   ├── screens/
│   │   ├── AgendarCitaScreen.kt  # Selección única de fecha y hora
│   │   ├── ConfirmacionScreen.kt # Vista de éxito y resumen de la cita
│   │   ├── DoctorDetailScreen.kt # Perfil detallado del médico
│   │   ├── HomeScreen.kt         # Lista de médicos y filtros por especialidad
│   │   └── MisCitasScreen.kt     # Historial y estado de las citas
│   └── theme/
│       ├── Color.kt              # Paleta de colores morados y estados
│       ├── Theme.kt              # Configuración de MaterialTheme
│       └── Type.kt               # Tipografías de la aplicación
└── MainActivity.kt               # Contenedor principal, NavHost y ModalNavigationDrawer

```

## Requerimientos Funcionales

| RF | Descripción | Archivo | Cómo verificarlo |
| :---: | :--- | :--- | :--- |
| **RF1** | Filtrar médicos por especialidad | `ui/screens/HomeScreen.kt` | Toca un chip "Cardiología" → la lista se filtra |
| **RF2** | Listar médicos disponibles | `ui/screens/HomeScreen.kt` | Al abrir la app se ven médicos con nombre, especialidad y calificación |
| **RF3** | Ver perfil del médico seleccionado | `ui/screens/DoctorDetailScreen.kt` | Toca una tarjeta → se abre el perfil con datos completos |
| **RF4** | Agendar cita eligiendo fecha y hora | `ui/screens/AgendarCitaScreen.kt` | En el perfil, toca "Agendar cita" → elige chip de fecha y hora |
| **RF5** | Confirmar cita con resumen | `ui/screens/ConfirmacionScreen.kt` | Tras confirmar, se muestra ícono verde + nombre del médico + fecha + hora |
| **RF6** | Navegar entre secciones con menú lateral | `MainActivity.kt` | Toca el ícono ☰ → se abre el drawer con las opciones de navegación |
| **RF7** | Listar citas agendadas con estado | `ui/screens/MisCitasScreen.kt` | Drawer → "Mis citas" → lista con chip verde "Confirmada" o gris "Completada" |
| **RF8** | Ir a "Mis citas" desde la confirmación | `ui/screens/ConfirmacionScreen.kt` | Toca "Ver mis citas" → navega a la lista con la cita recién agendada |

---

## Capturas del Resultado

| Inicio (`HomeScreen`) | Perfil (`DoctorDetailScreen`) | Agendar (`AgendarCitaScreen`) |
| :---: | :---: | :---: |
| ![HomeScreen](https://github.com/user-attachments/assets/0f2bf3b4-cac7-421b-83a5-800891969874) | ![DoctorDetail](https://github.com/user-attachments/assets/2ea2a201-f34e-4811-9410-695743cb2957) | ![AgendarCita](https://github.com/user-attachments/assets/baa6f437-124d-4c3d-9b22-7c5825b54a61) |

| Confirmación (`ConfirmacionScreen`) | Mis Citas (`MisCitasScreen`) | Menú Lateral (`ModalDrawer`) |
| :---: | :---: | :---: |
| ![Confirmacion](https://github.com/user-attachments/assets/cb70ecc1-27a7-4eba-be8a-9ae294be1ed3) | ![MisCitas](https://github.com/user-attachments/assets/26fee688-0b36-481f-bbc3-55255b14aeed) | ![Drawer](https://github.com/user-attachments/assets/8e9ec5f2-458b-4db8-8a10-a6896d708ac4) |
