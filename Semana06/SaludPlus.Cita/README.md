# ClinicaSaludPlus
**Estudiante:** Vila Ramos Daniela Fransua

**ClinicaSaludPlus** es una aplicación móvil desarrollada en Android con Kotlin y Jetpack Compose para la gestión integral de citas médicas, consulta de exámenes y recordatorios para pacientes.

---

## Retos Implementados (Fase 1 - Rama `sin-ia`)

En esta fase del proyecto se completó la arquitectura base de la aplicación junto con los 4 retos requeridos:

1. **Detalle de Cita y Cancelación (`AlertDialog`):**
    - Pantalla interactiva `DetalleCitaScreen` que despliega la información completa del médico y la consulta.
    - Implementación de `AlertDialog` para confirmación previa antes de eliminar o cancelar la cita en el `Repositorio`.

2. **Resultados Médicos (Modelo Propio):**
    - Creación del modelo de datos `ResultadoMedico`.
    - Pantalla `ResultadosScreen` con lista estática de exámenes de laboratorio (Hemograma, Perfil Lipídico, Radiografía) y estado del informe.

3. **Notificaciones Dinámicas (`.map`):**
    - Pantalla `NotificacionesScreen` que toma la lista de citas del usuario y genera tarjetas de recordatorio dinámicas usando el operador de transformación `.map` de Kotlin.

4. **Términos y Condiciones (`verticalScroll`):**
    - Vista `TerminosScreen` accesible desde el flujo de Registro.
    - Habilitada para lectura accesible de texto extenso mediante el modificador `.verticalScroll(rememberScrollState())`.

---

## Capturas de los Retos

| Reto 1: Detalle | Reto 2: Resultados | Reto 3: Notificaciones | Reto 4: Términos |
| :---: | :---: | :---: | :---: |
| ![Detalle](https://github.com/user-attachments/assets/7d947698-d64d-4a90-b619-8e3a2e8ce562) | ![Resultados](https://github.com/user-attachments/assets/553fb1fe-d396-48e1-b55c-0d3cfdd0eb52) | ![Notificaciones](https://github.com/user-attachments/assets/cd23ebf0-e0a7-447b-b487-b12262208a9e) | ![Términos](https://github.com/user-attachments/assets/524eb1b0-7b1b-488d-be19-c7f0a7da7e26) |
-----

## Estructura del Proyecto

```text
com.daniela.saludpluscitas/
├── data/
│   ├── model/         # Modelos de datos (Cita, Medico, Usuario, Especialidad, Resultado)
│   └── repository/    # Repositorio estático global y gestión de datos de sesión
├── navigation/        # Definición de rutas (Rutas.kt) y control de navegación (GrafoNavegacion.kt)
└── ui/
    ├── components/    # Componentes reutilizables (Botones, Avatares, Barra Inferior, Campos de texto)
    ├── screens/       # Pantallas de la aplicación organizadas por dominio
    │   ├── auth/      # Flujo de autenticación (Splash, Login, Registro, Términos)
    │   ├── citas/     # Gestión de reservas (Agendar, Confirmar, Mis Citas, Detalle, Resultados)
    │   ├── main/      # Flujo principal (Home, Especialidades, Médicos, Notificaciones)
    │   └── perfil/    # Pantalla de usuario (Perfil y cierre de sesión)
    └── theme/         # Colores y tipografía del diseño base (Material 3)