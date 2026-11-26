# PROMPTS · Fase 2 (rama con-ia)

**Alumna:** Daniela Fransua Vila Ramos

**Proyecto:** MuroCampus · Semana 09

**IA utilizada:** Claude (Anthropic)

Regla de trabajo: **la IA propone, yo decido**. Leí y probé cada respuesta en mi celular, y corregí lo que rompía la arquitectura MVVM + Repository o las reglas del laboratorio.

## Prompts por mejora

| N | Prompt que escribí | Qué respondió la IA | Qué cambié o descarté | Por qué |
| --- | --- | --- | --- | --- |
| 1 | «Agrega un Interceptor de OkHttp que ponga Accept: application/json en todas las peticiones» | Un `Interceptor` con `chain.request().newBuilder().header(...)` agregado al `OkHttpClient` en `RetrofitClient` | Verifiqué que el interceptor se registre **antes** del `HttpLoggingInterceptor` | Los interceptores se ejecutan en orden. Si el log va primero, registra la petición sin el encabezado y no se puede comprobar en Logcat |
| 2 | «¿En la app dónde se ve esa mejora?» | No se ve en la pantalla; se comprueba en Logcat con `tag:okhttp` | Nada. Tomé la captura del Logcat como evidencia | Es una mejora de red: la interfaz no cambia, pero todas las peticiones llevan el encabezado |
| 3 | «Agrega un campo de búsqueda que filtre por título las publicaciones ya descargadas» | `_busqueda` y `buscar()` en el ViewModel, `listaVisible` con `combine` + `stateIn`, y un campo de texto en la lista | Dejé el filtro en el **ViewModel**. La pantalla solo dibuja `listaVisible`. Comprobé que no se hagan peticiones nuevas al escribir | La pantalla no debe tener lógica. Filtrar dentro del Composable con `remember` mezclaría lógica con la vista. La búsqueda debe ser local, sin llamar a la API |
| 4 | «Hay que meterle un poco de Material 3 a la pantalla de lista» | Barra de búsqueda en forma de píldora con fondo tonal, chips con ✓, contador de publicaciones y estado *Sin resultados* con botón | **Quité la lupa** (🔍) del campo | Preferencia de diseño: el campo se ve más limpio sin el ícono |
| 5 | «El de sin resultados que esté al medio, no a un costado» | Agregar `fillMaxWidth()` al `Box` que contiene la lista | Lo apliqué | El `Box` solo tenía `weight(1f)` (alto completo) pero no ancho completo, por eso `Alignment.Center` no lo centraba |
| 6 | «Agrega un botón Eliminar en el detalle con @DELETE("posts/{id}") que revise el código de respuesta con Response<Unit>» | Endpoint `borrar()` con `Response<Unit>`, función `borrar` en el Repository y en el ViewModel, botón y diálogo de confirmación en el detalle, y un aviso (Snackbar) en `MainActivity` | La revisión de `isSuccessful` quedó en el **Repository**, que lanza `HttpException` si falla. El ViewModel solo recibe `Result<Int>`. La publicación se quita de la lista local | El ViewModel no debe conocer `Response` ni `HttpException` (regla de la Fase 2). JSONPlaceholder responde 200 pero no borra de verdad, por eso se actualiza la lista localmente |

## Revisión del código generado

Antes de cada commit revisé que el código de la IA no tuviera estos problemas:

| Regla | Resultado |
| --- | --- |
| No hay `catch` vacío que trague excepciones | Cumple: `llamar{}` convierte el error en `Result.failure` y vuelve a lanzar `CancellationException` |
| La pantalla nunca llama a la API | Cumple: los Composables solo llaman funciones del ViewModel |
| No se usa `GlobalScope` | Cumple: todas las corrutinas usan `viewModelScope` |
| La URL usa `https://` | Cumple: `https://jsonplaceholder.typicode.com/` |
| Sin Retrofit, `PostApi`, `Response` ni `HttpException` en `PostsViewModel` | Cumple: el ViewModel solo conoce la interface `PostRepository` |

## Commits de la Fase 2

| N | Commit |
| --- | --- |
| 1 | `chore: trae MuroCampus de la Fase 1` |
| 2 | `feat(red): agrega interceptor con encabezado Accept` |
| 3 | `feat(busqueda): filtra publicaciones por titulo` |
| 4 | `feat(borrar): elimina publicacion con DELETE y Response` |
| 5 | `docs: actualiza README y agrega PROMPTS.md de la Fase 2` |