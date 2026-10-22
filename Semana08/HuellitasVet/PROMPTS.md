# Prompts utilizados

Fase 2 del Laboratorio 08 (HuellitasVet) en la rama `con-ia`. La IA usada fue **Claude** (Anthropic), a través de Claude Code, y no Gemini. Al inicio le entregué la guía del laboratorio en PDF y un mensaje con las reglas de trabajo, que también valían para la Fase 2. De ese mensaje, la parte que aplica a la Fase 2 es esta:

> Luego haz los commits A, B y C con los mensajes exactos de la guía, implementando las mejoras que esos mensajes indican. Para cada una respeta las "capas que cambian" de la tabla de la guía y la condición obligatoria: toda función nueva del Repository se declara primero en la interface; si cambias o agregas una entidad, sube la version de la base con una Migration para no perder datos. Si una mejora no se puede ver con los datos iniciales, agrega datos de prueba al final, sin cambiar los ids de los registros que ya existen. Mismo flujo: un commit, push, resumen y esperar mi "sigue con el commit X".

Después de cada commit revisé el resumen y la app en el emulador antes de pedir el siguiente.

## Prompt 1

**Texto enviado a Claude:** "Sigue con el commit A". Con las reglas de arriba y la guía, la mejora pedida fue la de la tabla de la Fase 2: *"Gráfico de peso de la mascota: la ficha muestra una línea con el peso registrado en cada atención (reutiliza GraficoLineas del laboratorio de Canvas). Capas que cambian: DAO + Repository + ViewModel + View"*. Antes de empezar, Claude me explicó el plan: una consulta nueva en `CitaDao`, la función declarada primero en `VetRepository`, los puntos como `StateFlow` en `MascotaViewModel` y la View que solo dibuja con Canvas.

**Que genero la IA:**
- `model/PuntoPeso.kt`: data class con `fecha` y `peso`.
- `CitaDao.observarPesos(mascotaId)`: consulta con `Flow` que devuelve el peso de cada cita ATENDIDA que tiene peso, de la más antigua a la más reciente (los alias del SELECT coinciden con los campos de `PuntoPeso`).
- `VetRepository.observarPesos` declarada primero en la interface, y `VetRepositoryRoom` que solo delega en el DAO.
- `MascotaViewModel.pesos: StateFlow<List<PuntoPeso>>`.
- `ui/components/graficos/GraficoLineas.kt`: gráfico de línea con Canvas (grilla, área sombreada, línea, puntos, peso encima y fecha debajo con `drawText`). Como mi proyecto no tenía el `GraficoLineas` del laboratorio de Canvas, lo creó nuevo.
- En `FichaMascotaScreen`, una tarjeta "Evolución del peso" arriba del historial clínico con el cambio total (por ejemplo "+0.2 kg"). Si hay menos de 2 pesos muestra un mensaje en lugar del gráfico.
- No cambió ninguna entidad, así que la base sigue en la versión 1 y no hizo falta Migration.

**Que tuve que corregir yo:** no tuve que corregir código. Revisé que la conversión de `PuntoPeso` a los puntos del gráfico (con la fecha en formato "21 set") quedara en la View, porque es solo formato de texto, y que el ViewModel no importara nada de Compose. Lo probé en el emulador: Luna mostró 4.0, 4.1 y 4.2 kg; cuando el veterinario atendió a Luna con 4.4 kg, el gráfico agregó el cuarto punto solo; Max, que tiene una sola atención, mostró el mensaje.

**Respeta MVVM?:** Sí. La consulta está en el DAO, la función se declaró primero en la interface `VetRepository`, el ViewModel solo conoce la interface y expone un `StateFlow`, y la View solo dibuja lo que recibe.

## Prompt 2

**Texto enviado a Claude:** "Sigue con el commit B". La mejora de la guía: *"Reprogramar cita: el dueño cambia fecha y hora de una cita PENDIENTE o CONFIRMADA respetando horarios libres y la regla de 2 horas. Capas que cambian: DAO + Repository + ViewModel + View"*.

**Que genero la IA:**
- `CitaDao.reprogramar(id, fecha, hora)`: `UPDATE` de fecha y hora que deja la cita en PENDIENTE.
- `VetRepository.reprogramarCita` declarada primero en la interface, y su implementación en `VetRepositoryRoom`.
- `ReprogramarCitaViewModel` (nuevo) con las reglas:
  - solo citas PENDIENTE o CONFIRMADA y si faltan más de 2 horas (la misma regla que cancelar);
  - nueva fecha de hoy a 7 días y, si es hoy, solo horas futuras;
  - horarios ocupados en gris (incluido el actual de la propia cita) y un horario distinto al actual;
  - respeta el ajuste "Aceptar citas";
  - antes de guardar vuelve a consultar si el horario sigue libre.
- `MisCitasViewModel.validarReprogramacion`, para aplicar la regla antes de abrir la pantalla.
- `ReprogramarCitaScreen` con la cita actual, chips de fecha y hora, resumen del nuevo horario y botón "Confirmar cambio", el botón "Reprogramar" en Mis citas y la ruta `reprogramar_cita/{id}`.
- Sin cambios de entidad: versión 1, sin Migration.

**Que tuve que corregir yo:** no corregí código a mano. Al revisar Mis citas, el aviso seguía diciendo "Puedes cancelar hasta 2 horas antes" y el título del diálogo decía "Cancelar cita" aunque el mensaje fuera de reprogramar. Se cambiaron a "Puedes reprogramar o cancelar hasta 2 horas antes" y "Mis citas". También revisé la decisión de que una cita reprogramada vuelva a PENDIENTE: la acepté porque la clínica debe confirmar el nuevo horario. Para probar la regla de las 2 horas se adelantó el reloj del emulador y luego se devolvió a la hora automática.

**Respeta MVVM?:** Sí. Las reglas (estados, 2 horas, fechas, horario libre) viven en el ViewModel; el DAO solo hace el `UPDATE`; la función se declaró primero en la interface; la pantalla solo muestra el estado y avisa los clics.

## Prompt 3

**Texto enviado a Claude:** "Sigue con el commit C". La mejora de la guía: *"Búsqueda de pacientes: buscador por nombre de mascota o dueño y filtro por especie al mismo tiempo. Capas que cambian: ViewModel + View"*. Antes de empezar, Claude me avisó que eso ya lo pedía el Paso 12 de la Fase 1 y me propuso una búsqueda avanzada. Le respondí: "Búsqueda avanzada entonces", con estos puntos: buscar también por raza sin importar tildes ni mayúsculas, contador de resultados, orden por nombre o por última visita, filtro "Sin visitas recientes" (6 meses) y botón "Limpiar filtros".

**Que genero la IA:**
- En `PacientesViewModel`, los cuatro filtros juntos en una data class `FiltrosPacientes` dentro de un solo `MutableStateFlow`, y la lista con `combine` de tres flujos (mascotas, últimas visitas y filtros).
- Una función `normalizar` con `java.text.Normalizer` para comparar sin tildes ni mayúsculas ("siames" encuentra "Siamés").
- Orden A–Z o por última visita (la más reciente primero y las que nunca tuvieron visita al final), filtro de visitas recientes y `limpiarFiltros()`.
- En `PacientesScreen`, una segunda fila de chips, el contador "N pacientes" y el botón "Limpiar filtros", que solo aparece si hay filtros activos.
- No tocó el DAO, el Repository ni las entidades, porque la última visita ya existía desde la Fase 1.

**Que tuve que corregir yo:** no corregí código. Al probar en el emulador, la barra flotante del teclado tapaba el chip "Gatos" y parecía que el filtro no funcionaba; no era un error de la app, y se probó con el teclado completo. Revisé los casos: "siames" → Luna, "LABRADOR" → Max, orden por última visita Luna, Michi, Max y Rocky, "Sin visitas recientes" → Rocky, con Gatos → 0 pacientes, y "Limpiar filtros" → los 4.

**Respeta MVVM?:** Sí. Todo el filtrado y el orden están en el ViewModel con `combine`; la View solo muestra la lista, el contador y los chips, y llama a funciones del ViewModel.

## Reflexion (5 lineas)

- Que mejoro la IA y que no? Hizo rápido las tres mejoras respetando las capas y probándolas en el emulador, pero no podía saber si yo quería que una cita reprogramada volviera a pendiente ni hasta dónde mejorar una búsqueda que ya existía: esas decisiones las tomé yo.
- Que error cometio? No rompió MVVM, pero dejó al principio textos que ya no coincidían con la nueva función (el aviso de cancelar en Mis citas) y algunos pasos de prueba fallaron por el teclado del emulador, así que tuve que revisar cada pantalla y no confiar solo en el resumen.
- Que aprendi sobre MVVM, Room y reglas de negocio? Que gracias a que el ViewModel solo conoce la interface del Repository, agregar una consulta nueva fue declarar la función, implementarla y exponerla como StateFlow, sin tocar las demás pantallas.
- También vi que las reglas de negocio en el ViewModel se pueden reutilizar: reprogramar usa la misma regla de 2 horas que cancelar.
- Y que si una mejora no cambia entidades no hace falta subir la versión de la base, pero si las cambia sí, con una Migration para no perder los datos del usuario.
