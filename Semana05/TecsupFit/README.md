# TecsupFit — Programación en Móviles (Rama `con-ia`)

Aplicación móvil desarrollada de forma nativa para Android utilizando **Kotlin** y **Jetpack Compose** para la reserva y gestión de clases de gimnasio (Fase 2).

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
# Prompts Utilizados con Gemini AI
En este Fase 2 se utilizo 3 prompts de acuerdo a lo que se necesitaba agregar a este codigo base.
### Prompt 1
>"En la pantalla InicioScreen.kt de mi proyecto Jetpack Compose, agrega una barra de búsqueda en la parte superior utilizando OutlinedTextField con un ícono de lupa (Icons.Default.Search). Crea una variable de estado searchQuery para capturar el texto ingresado por el usuario y filtra en tiempo real la lista de clases (DatosMock.listaClases) de modo que coincida con el nombre de la clase (clase.nombre) o la sala (clase.sala), ignorando mayúsculas y minúsculas."

### Prompt 2
>En DetalleScreen.kt, añade un estado mutable para gestionar si la clase es favorita usando var esFavorito by remember { mutableStateOf(clase?.esFavorito ?: false) }. Agrega un IconButton dentro de las actions de la TopAppBar. Si esFavorito es true, muestra Icons.Filled.Favorite de color Color.Red. Si es false, muestra Icons.Outlined.FavoriteBorder. Al hacer clic, alterna el valor de esFavorito y actualiza la propiedad clase?.esFavorito = esFavorito. Usa los atributos existentes de mi modelo (nombre, hora, sala, duracion, cuposDisponibles, descripcion).
### Prompt 3
>"En RutinasScreen.kt, implementa un sistema de filtrado por nivel de dificultad utilizando fichas de selección (FilterChip). Crea una fila horizontal con las opciones (Todos, Principiante, Intermedio, Avanzado). Mantén la opción seleccionada en una variable de estado y filtra en tiempo real la lista de rutinas (DatosMock.listaRutinas) según el nivel seleccionado. Si la opción es Todos, muestra la lista completa."


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

| Pantalla Inicio (buscamos las clases) | Detalle de Clase | Rutinas de Entrenamiento (como Principiante) | Rutinas de Entrenamiento (Intermedio) |
| :---: | :---: | :---: | :---: |
| <img src="https://github.com/user-attachments/assets/97f2b962-0789-48f9-8dc4-9c5854d9ace9" width="220" height="460" alt="Pantalla Inicio (buscamos las clases)" /> | <img src="https://github.com/user-attachments/assets/0a4c66f8-e424-4612-b797-94e246511a80" width="220" height="460" alt="Detalle de Clase" /> | <img src="https://github.com/user-attachments/assets/c899e323-c642-4a2b-9b9d-abe78c3a71e0" width="220" height="460" alt="Rutinas de Entrenamiento (como Principiante)" /> | <img src="https://github.com/user-attachments/assets/dee375d6-d4d1-48ec-bf1b-b0471b6ce325" width="220" height="460" alt="Rutinas de Entrenamiento (Intermedio)" /> |