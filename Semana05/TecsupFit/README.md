# TecsupFit (Fase 2 - Rama `con-ia`)

Aplicación móvil de entrenamiento físico desarrollada de forma nativa para Android utilizando **Kotlin**, **Jetpack Compose** y la asistencia de IA (**Gemini en Android Studio**).

---

## Estructura del Proyecto

```text
app/src/main/java/com/daniela/tecsupfit/
├── data/
│   └── DatosMock.kt           # Fuentes de datos para clases y rutinas
├── model/
│   └── Clase.kt               # Modelo de datos (incluye propiedad esFavorito)
├── screens/
│   ├── InicioScreen.kt        # Búsqueda dinámica de clases en tiempo real
│   ├── DetalleScreen.kt       # Vista detallada e interacción de favoritos
│   └── RutinasScreen.kt       # Catálogo y filtrado por nivel de dificultad
└── MainActivity.kt            # Contenedor principal y navegación
```
## Requerimientos Funcionales

| RF       | Descripción                              | Archivo                                | Cómo verificarlo                                                                                                              |
| -------- | ---------------------------------------- | -------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------- |
| **RF1**  | Filtrar clases por "Hoy" / "Esta semana" | `screens/InicioScreen.kt`              | Toca un chip de filtro → se resalta la opción elegida                                                                         |
| **RF2**  | Listar clases disponibles                | `screens/InicioScreen.kt`              | Al abrir la app se ven las clases con nombre y horario                                                                        |
| **RF3**  | Ver detalle de la clase seleccionada     | `screens/DetalleScreen.kt`             | Toca una tarjeta → se abre la pantalla de detalle con la información completa                                                 |
| **RF4**  | Confirmar reserva con resumen            | `screens/ConfirmacionScreen.kt`        | Tras confirmar, se muestra el nombre de la clase, el horario y la sala asignada                                               |
| **RF5**  | Navegar entre secciones con BottomBar    | `screens/BottomBar.kt`                 | Cambia fluidamente entre las pestañas Inicio, Reservas, Rutinas y Perfil                                                      |
| **RF6**  | Listar reservas con estado               | `screens/ReservasScreen.kt`            | En la pestaña "Reservas" → visualiza la lista con etiquetas "Confirmada" o "Completada"                                       |
| **RF7**  | Ver perfil con estadísticas              | `screens/PerfilScreen.kt`              | En la pestaña "Perfil" → se muestra el avatar del usuario, clases asistidas y rachas                                          |
| **RF8**  | Ver rutinas de entrenamiento             | `screens/RutinasScreen.kt`             | En la pestaña "Rutinas" → se muestra el catálogo básico de rutinas disponibles                                                |
| **RF9**  | Búsqueda dinámica                        | `screens/InicioScreen.kt`              | Campo de texto interactivo (`OutlinedTextField`) para filtrar las clases en tiempo real según el nombre ingresado             |
| **RF10** | Sistema de favoritos                     | `screens/DetalleScreen.kt`, `Clase.kt` | Botón de corazón en la `TopAppBar` → permite marcar o desmarcar una clase como favorita y conserva el estado                  |
| **RF11** | Filtro por dificultad                    | `screens/RutinasScreen.kt`             | Botones de selección (`FilterChip`) → permiten filtrar las rutinas según el nivel: Todos, Principiante, Intermedio y Avanzado |

## Prompts Utilizados en Gemini (Android Studio)

### 1. Prompt RF9 — Búsqueda Dinámica (`InicioScreen.kt`)

> "Añade un campo de texto `OutlinedTextField` en la parte superior de `InicioScreen.kt` para buscar clases. Usa un estado mutable para almacenar el texto de búsqueda y filtra la lista de clases en tiempo real según el nombre ingresado."

### 2. Prompt RF10 — Sistema de Favoritos (`DetalleScreen.kt`)

> "En `DetalleScreen.kt`, añade un estado mutable para gestionar si la clase es favorita usando `var esFavorito by remember { mutableStateOf(clase?.esFavorito ?: false) }`. Agrega un `IconButton` dentro de las `actions` de la `TopAppBar`. Si `esFavorito` es `true`, muestra `Icons.Filled.Favorite` de color `Color.Red`. Si es `false`, muestra `Icons.Outlined.FavoriteBorder`. Al hacer clic, alterna el valor de `esFavorito` y actualiza la propiedad `clase?.esFavorito = esFavorito`."

### 3. Prompt RF11 — Filtro por Dificultad (`RutinasScreen.kt`)

> "En `RutinasScreen.kt`, implementa un sistema de filtrado por nivel de dificultad utilizando fichas de selección (`FilterChip`). Crea una fila horizontal con las opciones (Todos, Principiante, Intermedio, Avanzado). Mantén la opción seleccionada en una variable de estado y filtra en tiempo real la lista de rutinas (`DatosMock.listaRutinas`) según el nivel seleccionado. Si la opción es Todos, muestra la lista completa."

## Capturas de los Requerimientos Adicionales

|                                             Búsqueda Dinámica                                             |                                            Sistema de Favoritos                                           |                                           Filtro por Dificultad                                           |                                           Filtro por Dificultad                                           |
| :-------------------------------------------------------------------------------------------------------: | :-------------------------------------------------------------------------------------------------------: | :-------------------------------------------------------------------------------------------------------: | :-------------------------------------------------------------------------------------------------------: |
| <img src="https://github.com/user-attachments/assets/232e29e0-fbf2-4e70-8495-fe13f6c7ada3" width="250" /> | <img src="https://github.com/user-attachments/assets/30a20767-2511-47ba-84d2-ae0225546c4d" width="250" /> | <img src="https://github.com/user-attachments/assets/ea61f1ec-a686-4060-b793-a6e0e73ff9a6" width="250" /> | <img src="https://github.com/user-attachments/assets/d32d53ae-ecbc-42fa-b542-f18b1dd6b283" width="250" /> |


