# TecsupFit — Programación en Móviles (Rama `sin-ia`)

Aplicación móvil desarrollada de forma nativa para Android utilizando **Kotlin** y **Jetpack Compose** para la reserva y gestión de clases de gimnasio (Fase 1).

## Estructura del proyecto

```text
app/src/main/java/com/daniela/tecsupfit/
├── MainActivity.kt
├── model/           → Clase.kt, Reserva.kt
├── data/            → DatosMock.kt (listaClases, reservasAgendadas)
├── navigation/      → AppNavegacion.kt (NavHost con rutas)
├── screens/         → InicioScreen, DetalleScreen, ReservarScreen, ConfirmacionScreen, ReservasScreen, RutinasScreen, PerfilScreen, BottomBar
└── ui/theme/        → Temas, colores y tipografías TecsupFit
```
## Requerimientos Funcionales

| RF | Descripción | Archivo | Cómo verificarlo |
|---|---|---|---|
| **RF1** | Filtrar clases por "Hoy" / "Esta semana" | `screens/InicioScreen.kt` | Toca un chip de filtro → se resalta la opción elegida |
| **RF2** | Listar clases disponibles | `screens/InicioScreen.kt` | Al abrir la app se ven las clases con nombre y horario |
| **RF3** | Ver detalle de la clase seleccionada | `screens/DetalleScreen.kt` | Toca una tarjeta → se abre la pantalla de detalle con la información completa |
| **RF4** | Reservar cupo eligiendo horario | `screens/ReservarScreen.kt` | En el detalle, toca "Reservar cupo" → selecciona uno de los horarios disponibles |
| **RF5** | Confirmar reserva con resumen | `screens/ConfirmacionScreen.kt` | Tras confirmar, se muestra el nombre de la clase, el horario y la sala asignada |
| **RF6** | Navegar entre secciones con BottomBar | `screens/BottomBar.kt` | Cambia fluidamente entre las pestañas Inicio, Reservas, Rutinas y Perfil |
| **RF7** | Listar reservas con estado | `screens/ReservasScreen.kt` | En la pestaña "Reservas" → visualiza la lista con etiquetas "Confirmada" o "Completada" |
| **RF8** | Ver perfil con estadísticas | `screens/PerfilScreen.kt` | En la pestaña "Perfil" → se muestra el avatar del usuario, clases asistidas y rachas |
| **RF9** | Ver rutinas de entrenamiento | `screens/RutinasScreen.kt` | En la pestaña "Rutinas" → se muestra el catálogo básico de rutinas disponibles |

## Capturas de Pantalla

| Pantalla Inicio | Detalle de Clase | Reservar Cupo | Confirmación |
| :---: | :---: | :---: | :---: |
| <img src="https://github.com/user-attachments/assets/dd485b4e-36ac-4917-99a1-08e8866826df" width="250" /> | <img src="https://github.com/user-attachments/assets/795b57d6-48f6-4ab2-a136-22913a7617f8" width="250" /> | <img src="https://github.com/user-attachments/assets/def08ebf-bc22-42da-840a-bc51949dc53e" width="250" /> | <img src="https://github.com/user-attachments/assets/c1f29fc2-abe9-47f1-958e-0d652968999c" width="250" /> |

| Reservas | Rutinas | Perfil |
| :---: | :---: | :---: |
| <img src="https://github.com/user-attachments/assets/e3b7e518-1013-45d5-8816-f9025db58790" width="250" /> | <img src="https://github.com/user-attachments/assets/f94fe8e6-518a-447b-b840-dcfc7f718d1b" width="250" /> | <img src="https://github.com/user-attachments/assets/3ace5236-b266-4288-9f63-d69b61db4268" width="250" /> |
