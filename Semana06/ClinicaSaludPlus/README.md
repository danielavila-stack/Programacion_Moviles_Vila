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
| <img width="200" alt="Reto 1" src="https://github.com/user-attachments/assets/2ccb37e7-aec6-4619-90f9-e727be211699" /> | <img width="200" alt="Reto 2" src="https://github.com/user-attachments/assets/46aeab46-6c5d-4ab3-a581-6e68a02dd116" /> | <img width="200" alt="Reto 3" src="https://github.com/user-attachments/assets/15bbc6e3-4ef5-4586-91f6-c940adfae28a" /> | <img width="200" alt="Reto 4" src="https://github.com/user-attachments/assets/fe6c19a8-78db-4a5b-8779-465169bd07aa" /> |
---

## Estructura del Proyecto

```text
com.daniela.clinicasaludplus/
├── data/
│   ├── model/         # Modelos de datos (Cita, Usuario, ResultadoMedico)
│   └── repository/    # Repositorio estático y gestión de datos
├── ui/
│   ├── screens/       # Pantallas de la aplicación (Home, Citas, Perfil, Resultados, Notificaciones, Términos)
│   └── theme/         # Colores y tipografía de Material 3
└── navigation/        # Control de rutas y navegación (AppNavigation)