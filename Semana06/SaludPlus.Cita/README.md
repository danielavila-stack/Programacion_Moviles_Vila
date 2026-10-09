# SaludPlus Citas: App Paciente

App Android (Jetpack Compose) para agendar citas médicas en la Clínica SaludPlus. Los datos viven
en memoria dentro de `Repositorio` (sin base de datos), por lo que se pierden al cerrar la app.

**Estudiante:** Daniela Vila, Programación en Móviles (TECSUP)

## Fase 2: Calendario dinámico (con IA)

- Muestra los próximos 5 días hábiles desde hoy con `java.time.LocalDate` (sin sábados, domingos ni días pasados).
- Las flechas `<` y `>` cambian de semana; no se puede retroceder antes de la semana actual.
- El mes y año del encabezado cambian según la semana mostrada.
- Al cambiar de día se recalculan los horarios y se reinicia la hora seleccionada.
- La pantalla de confirmación muestra la fecha en español ("Martes 13 de octubre 2026").
- Se mantiene el bloqueo de horarios ya reservados.
- Splash rediseñado con la imagen del doctor.

## Prompts usados
### Prompt 1: Calendario dinámico

**Prompt:** Pedí reemplazar los días fijos de la Pantalla 6 por un calendario con `java.time.LocalDate`
(5 días hábiles, flechas por semana, mes y año dinámicos, reinicio de la hora y horarios recalculados),
sin romper el bloqueo de horarios.

**Respuesta resumida:** La IA creó `FechaUtils.kt`, agregó `horariosDisponibles` al `Repositorio` y
reescribió `AgendarCitaScreen.kt` con los estados `semanaOffset`, `fechaSeleccionada` y `horaSeleccionada`.

**Qué tuve que corregir:**
- Mi minSdk es 24 y `java.time` necesita API 26, así que activé `coreLibraryDesugaring` en `build.gradle.kts`.
- `FechaUtils.kt` tenía `package ...util` y el import decía `...utils`; eso causó 8 errores hasta que igualé el nombre.
- Un `Cita.kt` de otro proyecto rompió la compilación en `estado`; lo restauré a mis 6 campos.

### Prompt 2: Fecha en español

**Prompt:** Pedí mostrar la fecha como "Martes 13 de octubre 2026" en Confirmación, Mis Citas, Detalle y
Notificaciones, sin cambiar la clase `Cita`.

**Respuesta resumida:** La IA indicó guardar la fecha en formato ISO (`2026-10-13`) y usar
`FechaUtils.fechaLarga()` solo al mostrarla.

**Qué tuve que corregir:** En `NotificacionesScreen` escribí `\${...}` con barra invertida y se mostraba el
código literal; quité las `\`. Mantuve la fecha ISO en `guardarCita` para que el bloqueo siga funcionando.

### Prompt 3: Imagen en el Splash

**Prompt:** Pedí cómo poner la imagen del doctor del diseño de referencia en la pantalla Splash.

**Respuesta resumida:** La IA indicó copiar la imagen a `res/drawable` y mostrarla con `Image` y
`painterResource`, con `weight(1f)` y `ContentScale.Fit`.

**Qué tuve que corregir:** Agregué a mano los imports (`Image`, `CircleShape`, `ContentScale`,
`painterResource`, `R`) y usé `doc.png`, que tiene fondo blanco, con el nombre en minúsculas.

### Git

El primer commit subió solo `FechaUtils.kt` y dejó 64 archivos sin versionar. Los agregué, hice Amend del
commit y subí con Force Push (solo en `con-ia`) porque el push normal fue rechazado.

## Preguntas de reflexión

### 1. ¿Por qué los modelos, Rutas.kt y AppNavigation.kt se entregaron completos y las pantallas no? ¿Qué tienen en común los archivos que sí se dejaron como esqueleto?

Los modelos, las rutas y la navegación son como el plano de la app: definen qué datos existen y a qué
pantalla se puede ir desde cuál. Tienen que estar bien para que todo funcione, pero no eran lo que se
quería practicar. Los archivos que se dejaron como esqueleto (el Repositorio y las pantallas) tienen en
común que ahí hay que pensar: guardar y buscar datos, filtrar, mostrar listas y reaccionar a lo que toca
el usuario. Ahí es donde se aprende de verdad.

### 2. ¿Por qué el Repositorio es un object y no una clase normal? ¿Qué pasaría con las citas si cada pantalla creara su propia lista?

Porque un `object` es una sola copia de los datos que usan todas las pantallas. Si cada pantalla tuviera
su propia lista, sería como tener un cuaderno distinto en cada una: agendaría la cita en Confirmar, pero
Mis Citas miraría otro cuaderno vacío y nunca la vería. Con un solo `object`, todas leen y escriben en el
mismo lugar y los datos coinciden en toda la app.

### 3. ¿Cómo lograste que la búsqueda de especialidades y los horarios disponibles se actualicen solos?

Guardé el texto del buscador y las selecciones (`semanaOffset`, `fechaSeleccionada`, `horaSeleccionada`)
en variables que Compose vigila (`remember` con `mutableStateOf`). Cuando una cambia, Compose vuelve a
dibujar la pantalla y recalcula lo que depende de ella, por eso no tuve que decirle "actualiza la lista".
Los horarios se calculan con `Repositorio.horariosDisponibles()`, que descarta las horas de las citas ya
guardadas y las horas pasadas de hoy; así, una hora reservada ya no aparece al volver a elegir ese día.

### 4. ¿Qué diferencia notaste entre navigate() normal y el que usa popUpTo? ¿Qué pasa al presionar Atrás en cada caso?

Con `navigate()` normal (Especialidades → Médicos → Fecha y hora) las pantallas se apilan y, al presionar
Atrás, vuelvo a la anterior. Con `popUpTo` (al terminar en Confirmar cita y volver al Inicio) se borra todo
el recorrido del agendamiento. Entonces, al presionar Atrás desde el Inicio no regreso a Confirmar, donde
podría agendar la misma cita otra vez por error.

### 5. ¿Qué tuviste que corregir del código que te generó la IA para el calendario dinámico?

- Mi minSdk es 24 y `java.time` no funciona ahí sin configuración extra: activé `coreLibraryDesugaring`
  en `build.gradle.kts`.
- El paquete de `FechaUtils.kt` (`util`) no coincidía con el import (`utils`) y dio 8 errores; igualé los nombres.
- La fecha debía guardarse en formato ISO (`2026-10-13`) y la hora sin espacios para no romper la ruta de
  navegación y para que el bloqueo de horarios compare bien.
- Un `Cita.kt` de otro proyecto rompió la compilación; lo restauré a mis 6 campos originales.
- En las notificaciones había `\${...}` con barra invertida y mostraba el código literal; quité las `\`.

### 6. Compara el NavigationDrawer del Laboratorio 6 con el NavigationBar de esta tarea: ¿en qué caso usarías cada uno en un proyecto propio?

El NavigationBar (barra de abajo) lo usaría cuando la app tiene pocas secciones principales, de 3 a 5, que
la gente usa todo el tiempo, como Inicio, Citas, Resultados y Perfil aquí. Queda al alcance del pulgar y se
ve de un vistazo. El NavigationDrawer (menú lateral) lo usaría cuando hay muchas secciones o algunas que se
usan poco, porque se esconden en un menú y no llenan la pantalla. Para una app de citas médicas usaría la
barra de abajo; para una con muchas opciones de administración, el menú lateral, o los dos juntos.

## Capturas de pantalla
| <img src="https://github.com/user-attachments/assets/3826fd81-cc77-4539-8fb1-172f6db7a8f3" width="280" /> | <img src="https://github.com/user-attachments/assets/46b4f0c3-5ab8-4abf-887b-8789547116e2" width="280" /> | <img src="https://github.com/user-attachments/assets/a7dd4bff-7e69-4779-9b9c-bbc2bc4dd393" width="280" /> |
| :---: | :---: | :---: |
| <img src="https://github.com/user-attachments/assets/55c9c38d-371a-4ff6-a1a3-8a46704860f2" width="280" /> | <img src="https://github.com/user-attachments/assets/c92907ec-2f2b-47b5-819f-19975eb6c6ca" width="280" /> | <img src="https://github.com/user-attachments/assets/7362f137-0e34-4cc0-b710-5098188735fd" width="280" /> |
| <img src="https://github.com/user-attachments/assets/a5189572-5355-4028-be88-1fa5cd02597a" width="280" /> | <img src="https://github.com/user-attachments/assets/1ff41baf-2a3a-4aed-a18d-5ddc7a8a3861" width="280" /> | <img src="https://github.com/user-attachments/assets/a1bedca8-1b89-414a-86b1-969bfd3e0732" width="280" /> |
| <img src="https://github.com/user-attachments/assets/261b8e82-4a84-44c3-b24d-099b7d1bea6c" width="280" /> | <img src="https://github.com/user-attachments/assets/cf06659e-9ed0-4c5f-903c-e0836ecf5470" width="280" /> | |

