# Registro de Productos - Lab 03

**Estudiante:** Vila Ramos Daniela  
**Curso:** Desarrollo de Aplicaciones Móviles  

## Parte B: Mejora con IA (Rama `con-IA`)

En esta sección se detallan las iteraciones realizadas con **Gemini** para incorporar las validaciones de campos vacíos y la funcionalidad de limpieza de formulario, diferenciando el código generado automáticamente de las refactorizaciones manuales aplicadas sobre `MainActivity.kt` para cumplir con las reglas de diseño Material 3 de Jetpack Compose.

---

### Tabla de Prompts y Decisiones

| Prompt utilizado | Generación de Gemini | Correcciones y Ajustes Manuales (Commit B2) |
| :--- | :--- | :--- |
| **Prompt 1: Validaciones y Control de Estado**<br> *"Ayúdame a agregar validaciones a los campos vacíos en el formulario de registro y añade un botón Limpiar para borrar los datos ingresados."*[cite: 1] | Generó los estados `mensajeError` y `mostrarResumen`, la condición `isBlank()` al presionar el botón de acción y la estructura básica del botón **LIMPIAR**. | **Integración parcial:** Se aprovechó la lógica de reseteo de variables (`nombre = ""`, `precio = ""`, `cantidad = ""`), pero el mensaje de error usaba un color estático genérico y el botón principal mantenía la etiqueta predeterminada `"AGREGAR"` |
| **Prompt 2: Refinamiento de UI, UX y Formatos**<br> *"Ajusta el texto del botón de acción a AGREGAR PRODUCTO, mejora el estilo del mensaje de error usando el color de tema y asegura el correcto formato numérico."*[cite: 1] | Renombró la etiqueta del botón a `"AGREGAR PRODUCTO"` y sugirió el uso de `MaterialTheme.colorScheme.error` para adaptar las alertas visuales. | **Aprobado y refactorizado (Commit B2):** En `MainActivity.kt` se consolidó la distribución de los botones en una `Row` compartida con `Modifier.weight(1f)`. Se forzó el uso de `Locale.US` en `String.format("%.2f", ...)` para garantizar que el cálculo del `importe` use punto decimal en la `Card`, y se aseguró que el mensaje de confirmación utilice `Color(0xFF2E7D32)` |

---

### Captura de la Mejora con IA
<img width="366" height="721" alt="parte 2" src="https://github.com/user-attachments/assets/7d9fc0cd-92c8-4aec-b15e-a84737fcae40" />


---

### Historial de Commits - Rama `con-IA`

* **`B1`**: *Aplica mejora generada con IA: validacion y boton limpiar*
* **`B2`**: *Corrige codigo de la IA: ajusta labels, colores M3 y formato Locale.US*
* **`B3`**: *Documenta prompts y decisiones en README*
