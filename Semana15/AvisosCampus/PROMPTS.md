# PROMPTS · Fase 2 de AvisosCampus

**Laboratorio 15** · Programación en Móviles
**Estudiante:** Daniela Fransua Vila Ramos
**IA utilizada:** Claude Code (modelo Claude Sonnet 5.5), en la app de escritorio de Claude
**Rama:** `con-ia`

> **Regla de trabajo: la IA propone, yo decido.**
> La IA escribió el código paso a paso, pero no hizo ningún commit. En cada paso me avisaba; yo probaba en el celular, revisaba los cambios y hacía el commit a mano. Si la IA proponía algo que rompía la arquitectura, debía corregirlo y anotarlo aquí.

## Registro de prompts

| N | Prompt que escribí | Qué respondió la IA | Qué cambié o descarté | Por qué |
|---|--------------------|---------------------|-----------------------|---------|
| 1 | Prompt inicial de la Fase 2: reglas de trabajo (la IA no hace commits, un paso por commit, esperar "continúa"), reglas de arquitectura y los pasos 1 a 4. El mensaje llegó cortado a mitad del Paso 3 | Hizo el Paso 1: campo `urgente: Boolean = false` en `Aviso`, canal "Urgentes" (IMPORTANCE_HIGH) junto a "Avisos", `mostrar` elige canal y prioridad según `aviso.urgente`, y el recordatorio emite `aviso.copy(urgente = true)`. Avisó que el mensaje estaba cortado y que había que desinstalar la app | Hice el Paso 1 porque estaba completo y dejé el Paso 3 en espera hasta tener el texto entero | La importancia de un canal no se puede cambiar después de creado. Y es mejor no adivinar un paso incompleto |
| 2 | "continúa" (Paso 2 · BigTextStyle) | Agregó `textoLargo` (texto + "Origen: … · Hora: …") y `setStyle(NotificationCompat.BigTextStyle().bigText(textoLargo))` en el Notificador, sin cambiar `setContentText` | Nada. Revisé que el ViewModel no se tocara | El formato de la notificación es un detalle visual de Android, así que le toca al Notificador |
| 3 | Volví a pegar el prompt completo, con los pasos 3 y 4 enteros y "Empieza por el PASO 1" | Confirmó que los pasos 1 y 2 ya estaban hechos, no los rehízo y repitió el aviso del Paso 2 esperando mi "continúa" | No se rehízo el Paso 1 aunque el prompt lo pedía | Rehacerlo duplicaría trabajo. Además, la regla 5 pide esperar "continúa" antes de avanzar |
| 4 | "continúa" (Paso 3 · Receptor del cargador) | Creó `CargadorReceiver` (POWER_CONNECTED → true, POWER_DISCONNECTED → false), el estado `escuchandoCargador` y `alCambiarCargador` en el ViewModel, el segundo Switch y un `DisposableEffect` propio con RECEIVER_NOT_EXPORTED y `unregisterReceiver` en `onDispose` | La IA agregó el color verde de "Cargador" en `HistorialAvisos`, que no estaba en el prompt | El `when` de colores debe cubrir todos los valores de `Origen`; sin esa línea no compilaba |
| 5 | "continúa" (Paso 4 · Documentación) | Revisó `viewmodel/`, agregó la sección "Fase 2 · Mejoras con IA" al final del README sin borrar nada y creó este PROMPTS.md | Revisar la columna OK con mis pruebas reales y agregar las capturas | El README solo debe marcar como OK lo que realmente probé en el celular |
| 6 | "¿Me diste el README para la Fase 2?" | Explicó que la Fase 2 no tiene un README aparte: es una sección nueva al final del mismo README.md, como pedía el Paso 4, y dijo qué contiene | Nada | El Paso 4 pedía no borrar la Fase 1 y agregar la Fase 2 al final del mismo archivo |

## Revisión del código generado

| Regla | Cómo se revisó | Cumple |
|-------|----------------|--------|
| El ViewModel no usa `Context` | Búsqueda en `viewmodel/`: solo aparece en comentarios; los únicos imports son de `androidx.lifecycle` y `kotlinx.coroutines` | Sí |
| El ViewModel no usa `NotificationManager` | Búsqueda en `viewmodel/`: solo aparece en un comentario | Sí |
| El ViewModel no usa `BroadcastReceiver` ni Composables | Búsqueda en `viewmodel/`: no hay `@Composable` y `BroadcastReceiver` solo aparece en comentarios; recibe `true`/`false` | Sí |
| El ViewModel solo conoce la interface `AvisosRepository` | Constructor `AvisosViewModel(private val repo: AvisosRepository)`; la clase concreta solo aparece en la Factory | Sí |
| Todo receptor se quita en `onDispose` | `PantallaAvisos.kt`: 2 `registerReceiver` (modo avión y cargador) y 2 `unregisterReceiver`, cada uno en el `onDispose` de su `DisposableEffect` | Sí |
| `PendingIntent` con `FLAG_IMMUTABLE` | `Notificador.kt`: el único `PendingIntent.getActivity` usa `PendingIntent.FLAG_IMMUTABLE` | Sí |
| `onReceive` corto | `ModoAvionReceiver` y `CargadorReceiver` solo leen la acción o el extra y llaman a `alCambiar(...)` | Sí |
| Receptores no exportados | Los dos se registran con `ContextCompat.RECEIVER_NOT_EXPORTED` | Sí |
| Sin dependencias nuevas ni material-icons | `build.gradle.kts` no cambió en la Fase 2 | Sí |

## Commits de la Fase 2

1. `feat(canales): agrega canal Urgentes para el recordatorio`
2. `feat(notificacion): muestra texto largo con BigTextStyle`
3. `feat(receptor): escucha el cargador con su propio switch`
4. `docs: actualiza README y agrega PROMPTS.md de la Fase 2`
