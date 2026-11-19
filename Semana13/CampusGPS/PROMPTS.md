# PROMPTS.md · Fase 2 con IA

**Proyecto:** CampusGPS · Laboratorio 13
**Alumna:** Daniela Fransua Vila Ramos
**Rama:** `con-ia`
**IA usada:** Claude (en lugar de Gemini en Android Studio)

> Regla que seguí: la IA propone, yo decido. Leí todo lo que generó, lo probé en mi celular Xiaomi y revisé que no rompiera MVVM: que no haya `Context` en el ViewModel, que no se calcule nada en los Composables y que `FusedLocationProviderClient` siga solo en el Repository.

| Prompt que escribí | Qué respondió la IA | Qué cambié o descarté | Por qué |
|---|---|---|---|
| «Quiero agregar un filtro por categoría con chips (Estudio, Comida, Eventos, Servicios) sin romper MVVM.» | Propuso un `StateFlow` con la categoría elegida en el ViewModel y una lista `lugaresVisibles` hecha con `combine(_lugares, _categoria)`, un Composable `FiltroCategorias` con `FilterChip` sin lógica, y que el mapa y la lista dibujen `lugaresVisibles`. | Acepté que el más cercano se calcule sobre los lugares visibles (el más cercano de la categoría) y que al cambiar de filtro se borre la selección. Las categorías salen de los datos del Repository y no están escritas a mano en los chips. | Si el filtro estuviera en el Composable, la pantalla haría lógica y rompería la regla de oro. Borrar la selección evita que quede seleccionado un lugar que ya no se ve. |
| «Quiero mejorar la parte visual: modo oscuro del mapa e íconos por categoría en las tarjetas.» | Propuso un botón en la barra superior, `MapProperties(mapStyleOptions = ...)` con un JSON en `res/raw`, íconos por categoría y la tarjeta del más cercano en verde. | Los íconos de `material-icons` ya no vienen incluidos con Compose, así que usé vector drawables en `res/drawable`. El JSON del mapa oscuro es el estilo "Night" de la documentación de Google. Ajusté `DarkColorScheme` para que el modo oscuro no saliera morado. | El estado "modo oscuro" lo dejé en `MainActivity` con `rememberSaveable` y no en el ViewModel, porque es un estado de la interfaz y no del negocio. Así además no se pierde al rotar la pantalla. |
| «Como tercera mejora quiero mostrar el tiempo a pie y un botón "Cómo llegar".» | Propuso calcular los minutos en el ViewModel (metros ÷ 1.4 ÷ 60) y abrir Google Maps con un `Intent` y la ruta caminando. | **Lo descarté** y lo reemplacé por el seguimiento en vivo. | El seguimiento en vivo está en la lista de mejoras del profesor y además cambia el Repository, el ViewModel y la View, así que demuestra mejor la arquitectura. |
| «Hagamos el seguimiento en vivo.» | Propuso agregar `seguirPosicion(): Flow<Posicion>` a la interface `UbicacionRepository`, implementarlo con `requestLocationUpdates` dentro de `callbackFlow` y `awaitClose`, recogerlo en el ViewModel con un `Job` y un `StateFlow` `siguiendo`, y un botón Iniciar/Detener en la pantalla. | Revisé que el ViewModel solo use la interface y no sepa de Google Play Services. Ajusté la pantalla para que, si falta el permiso, después de aceptarlo empiece a seguir y no solo a buscar (`quiereSeguir`). Junté botones en una fila para que el mapa no quede muy pequeño. | `FusedLocationProviderClient` debe quedarse en el Repository. `awaitClose` quita las actualizaciones cuando se detiene o se cierra el ViewModel, para no gastar batería. Sin `quiereSeguir`, el permiso aceptado ejecutaba la acción equivocada. |

## Lo que revisé de la arquitectura en cada mejora

- **View** (`ui/`): solo dibuja según el estado y avisa los eventos (`vm.filtrar()`, `vm.iniciarSeguimiento()`). No usa Repository ni calcula distancias.
- **ViewModel**: solo conoce las interfaces `UbicacionRepository` y `LugaresRepository`. No recibe `Context`.
- **Repository**: es el único que usa `FusedLocationProviderClient` (`obtenerPosicion()` y `seguirPosicion()`).
- La clave de API sigue solo en `local.properties` y nunca la pegué en un prompt.
