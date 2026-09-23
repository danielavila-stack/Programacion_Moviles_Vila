<<<<<<< HEAD
# ClinicaSalud + - (Fase 02)
=======
# Clínica Salud+ (Fase 1 - Rama `sin-ia`)

Aplicación móvil para la gestión y agendamiento de citas médicas, desarrollada de forma nativa para Android utilizando **Kotlin** y **Jetpack Compose**.
>>>>>>> 9de8e36 (Agregar README.md con documentacion de la Fase 1)

---

## Estructura del Proyecto

```text
<<<<<<< HEAD
com.daniela.clinicasalud/
├── model/
│   └── ClinicModels.kt         # Modelos de datos (Doctor, Cita, Especialidad)
├── navigation/
│   └── Screen.kt               # Definición de rutas de navegación
├── ui/
│   ├── screens/
│   │   ├── HomeScreen.kt          # Lista de doctores y especialidades
│   │   ├── DoctorDetailScreen.kt  # Detalle del médico seleccionado
│   │   ├── AgendarCitaScreen.kt   # Formulario de selección fecha/hora y cancelación
│   │   ├── ConfirmacionScreen.kt  # Pantalla de confirmación de reserva
│   │   └── MisCitasScreen.kt      # Historial, cancelación y eliminación de citas
│   └── theme/                     # Temas, colores y tipografías Material 3
└── MainActivity.kt                # Punto de entrada y NavHost principal

```
---

## Requerimientos Funcionales.

| Código | Requerimiento Funcional | Descripción |
| :---: | :--- | :--- |
| **RF1** | **Catálogo de Especialidades** | Selección y visualización dinámica de especialidades ("Todas", "Cardiología", "Pediatría", "Dermatología"). |
| **RF2** | **Filtrado de Médicos** | Filtrado en tiempo real del listado de doctores según la especialidad seleccionada. |
| **RF3** | **Perfil Detallado del Médico** | Vista de perfil con especialidad, años de experiencia, calificación y descripción detallada. |
| **RF4** | **Agendamiento de Cita** | Formulario interactivo para seleccionar la fecha y el horario disponible del médico. |
| **RF5** | **Confirmación de Agendamiento** | Pantalla de resumen con la validación final de los datos de la cita registrada. |
| **RF6** | **Historial de Citas** | Vista general ("Mis Citas") para consultar las citas agendadas y completadas. |
| **RF7** | **Sistema de Navegación** | Estructura de rutas mediante `NavHost` para un flujo continuo entre pantallas. |
| **RF8** | **Modelo de Datos Unificado** | Definición de estructuras de datos (`ClinicModels.kt`) para el manejo de médicos y citas. |
| **RF9** | **Cancelación al Agendar** | Botón 'Cancelar' en el formulario de agendamiento para abortar el proceso y limpiar la selección. | `AgendarCitaScreen.kt` |
| **RF10** | **Cancelación de Cita Confirmada** | Diálogo emergente de confirmación en el historial para cambiar citas 'Confirmadas' a 'Canceladas'. | `MisCitasScreen.kt` |
| **RF11** | **Eliminación del Historial** | Botón de papelera para remover definitivamente del historial las citas canceladas o pasadas. | `MisCitasScreen.kt`, `ClinicModels.kt` |

---

## Prompts Utilizados en Gemini (Android Studio)

Durante la Fase 2 en la rama `con-ia`, se empleó el panel de Gemini en Android Studio aplicando los siguientes prompts:

### 1. Prompt RF9 - Cancelación al Agendar (`AgendarCitaScreen.kt`)
> *"Modifica `AgendarCitaScreen.kt` para agregar un botón secundario de 'Cancelar' ubicado junto o debajo del botón 'Confirmar cita'. Al hacer clic en 'Cancelar', debe reiniciar/limpiar la selección de fecha y hora, y ejecutar `navController.popBackStack()` para regresar a la pantalla anterior (`DoctorDetailScreen`) sin guardar ningunos datos."*

### 2. Prompt RF10 - Diálogo de Cancelación (`MisCitasScreen.kt`)
> *"En `MisCitasScreen.kt`, agrega un botón llamado 'Cancelar cita' dentro de las tarjetas que tengan el estado 'Confirmada'. Al presionar este botón, debe desplegarse un `AlertDialog` preguntando: '¿Estás seguro de que deseas cancelar tu cita con [Nombre del Doctor]?'. Si el usuario confirma ('Sí, cancelar'), cambia el estado de esa cita a 'Cancelada' y actualiza el color de su etiqueta/badge a rojo o gris."*

### 3. Prompt RF11 - Eliminación del Historial (`MisCitasScreen.kt` / `ClinicModels.kt`)
> *"En `MisCitasScreen.kt` y `ClinicModels.kt`, actualiza el manejo de la lista de citas usando `remember { mutableStateListOf(...) }` para que las tarjetas de citas con estado 'Cancelada' o 'Completada' muestren un botón de ícono de papelera (Delete). Al hacer clic en la papelera, la cita debe removerse definitivamente de la lista visible del historial."*

---

## Correcciones Técnicas y Ajustes de Entorno

Durante el desarrollo se identificaron y se tuvo que resolver los siguientes imprevistos técnicos:

### 1. Error de Ruta del SDK (`SDK location not found`)
* **Problema:** Gradle presentaba fallos de sincronización debido a un formato con caracteres no escapados o barras invertidas dobles en la ruta del Android SDK.
* **Solución:** Tuve que ajustar manualmente el archivo `local.properties` asegurando la sintaxis correcta del sistema de archivos Windows:
  ```properties
  sdk.dir=C\:/Users/Daniela/AppData/Local/Android/Sdk
  ---
## 📱 Capturas de Pantalla

| Catálogo y Especialidades | Agendamiento y Cancelación  | Historial "Mis Citas"  |
| :---: | :---: | :---: |
| <img src="https://github.com/user-attachments/assets/ae417cf4-0775-485e-8868-fd895fe88871" width="220" /> | <img src="https://github.com/user-attachments/assets/7aea46e9-6c55-4df0-8c98-f4cb3a806e7a" width="220" /> | <img src="https://github.com/user-attachments/assets/a2dd1229-66f2-41ab-8f91-b5adf81dfc61" width="220" /> |

| Diálogo de Cancelación  | Eliminación del Historial |
| :---: | :---: |
| <img src="https://github.com/user-attachments/assets/e553b15b-ffba-4824-ac10-966e59bc4e15" width="220" /> | <img src="https://github.com/user-attachments/assets/d611ad5f-56c6-463b-8a83-e7337c583f8e" width="220" /> |
=======
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
>>>>>>> 9de8e36 (Agregar README.md con documentacion de la Fase 1)
