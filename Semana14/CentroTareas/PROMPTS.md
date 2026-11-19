# PROMPTS.md · Fase 2 con IA

**Proyecto:** CentroTareas · Laboratorio 14
**Alumna:** Daniela Fransua Vila Ramos
**Rama:** `con-ia`
**IA usada:** Claude (en lugar de Gemini en Android Studio)

> Regla que seguí: la IA propone, yo decido. Leí todo lo que generó, lo probé en mi celular y revisé que no rompiera las reglas: nada de `GlobalScope`, `runBlocking` ni `Thread.sleep`, la `CancellationException` siempre se relanza, el ViewModel no recibe `Context` y la pantalla no calcula nada.

| Prompt que escribí | Qué respondió la IA | Qué cambié o descarté | Por qué |
|---|---|---|---|
| «¿Qué mejoras de la lista me recomiendas para la Fase 2?» | Recomendó primos con progreso, tiempo máximo y comparación de tiempos, por ser visibles y fáciles de explicar. | Me quedé con las dos primeras. La tercera la cambié después por la barra de navegación. | Primos con progreso cambia el Repository y el ViewModel, así que cumple la regla de que al menos una mejora toque esas capas. |
| «Hagamos que los primos muestren su progreso.» | Propuso que `contarPrimos` reciba una función `alAvanzar` que se llama más o menos cada 1 % del recorrido, guardar el avance en un `StateFlow` `progresoPrimos` en el ViewModel y mostrar la barra y el porcentaje en la pantalla. | Acepté la función de aviso en lugar de devolver un `Flow`. Después cambié la barra por un círculo grande con el porcentaje al centro. | El cálculo ya devolvía la cantidad final, así que con la función de aviso el cambio en el Repository es pequeño. Mantuve `ensureActive()` dentro del ciclo para que Cancelar siga funcionando. La barra avanza un poco más lento al final porque revisar si un número grande es primo cuesta más. |
| «Agrega un tiempo máximo a las cargas con `withTimeout`.» | Propuso envolver las cargas con `withTimeout`, atrapar `TimeoutCancellationException` antes que `CancellationException` y mostrar **Tardó demasiado**. | **Descarté el límite fijo de 2 s** y lo puse como opciones: Sin límite, 2 s y 4 s. | Las cargas tardan 3 s («A la vez») y 5 s («Una por una»), así que con 2 s fijos ninguna pasaría nunca. Con 4 s se ve para qué sirve cargar en paralelo: «A la vez» pasa y «Una por una» no. El orden de los `catch` importa porque `TimeoutCancellationException` es una subclase de `CancellationException`. |
| «Como tercera mejora quiero la comparación de tiempos.» | Propuso guardar cada carga exitosa en una lista en el ViewModel y calcular los promedios de cada modo con `map` + `stateIn`. | **La descarté** y la reemplacé por una barra de navegación inferior. | Preferí una mejora que ordene la pantalla: con tres tarjetas juntas había que hacer mucho scroll. |
| «Quiero una barra de navegación abajo con Tarea, Primos y Cargas.» | Propuso un `NavigationBar` en el `bottomBar` del `Scaffold`, separar las tarjetas en tres funciones y mostrar solo la de la pestaña elegida. | Usé mis propios colores y los íconos como vector drawables. Agregué un círculo de progreso en Tarea, agrandé el de Primos e hice que Cargas ocupe toda la pantalla con `weight(1f)`. Quité el texto de ayuda de los tiempos y el «(ficticia)» del perfil. | La pestaña es estado de la interfaz, por eso vive en la pantalla con `rememberSaveable` y no en el ViewModel. El texto del campo y el contador se guardan en `PantallaTareas` y no dentro de cada tarjeta, porque si no se borrarían al cambiar de pestaña. En Cargas quité el scroll porque `weight` no funciona dentro de un scroll. |

## Lo que comprobé en cada mejora

- **Hilos:** al cambiar de pestaña, la tarea con progreso **sigue corriendo**, porque vive en `viewModelScope`.
- **Cancelación:** Cancelar sigue funcionando en la tarea y en los primos, y la `CancellationException` se relanza siempre.
- **Errores:** con *Simular error* sigue saliendo «No se pudo cargar: Sin conexión», y con el tiempo máximo sale «Tardó demasiado». Reintentar funciona en los dos casos.
- **Reglas de oro:** `git grep` no encuentra `GlobalScope`, `runBlocking` ni `Thread.sleep` en los archivos `.kt`.
