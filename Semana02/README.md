# Carrito de Compras en Kotlin utilizando POO

---

**Estudiante:** Daniela Vila Ramos  
**Curso:** Programación en Móviles  
**Profesor:** Juan José León Suiyon  

---

## Descripción del Proyecto
Este proyecto consiste en el desarrollo de una aplicación de consola en Kotlin que simula un **Carrito de Compras** para una tienda tecnológica. En esta rama (`con-ia`), el desarrollo se ha realizado aplicando los 4 principios fundamentales de la **Programación Orientada a Objetos (POO)** y optimizando la estructura del sistema con un módulo de recomendaciones y descuentos promocionales.

---

## Prompts Utilizados

### Prompt General
> "Actúa como un desarrollador senior en Kotlin. Necesito refactorizar un Carrito de Compras en consola para un laboratorio académico en la rama `con-ia`. Guíame en 6 etapas para realizar 6 commits en Git etiquetados como `POO: ...`. El código en `Carrito.kt` debe aplicar Abstracción, Encapsulamiento, Herencia y Polimorfismo. Además, la salida en consola debe ser detallada, mostrando la lista de productos, subtotal, IGV (18%), producto más caro, descuento y una sugerencia para el cliente."

---

### Prompts Secundarios

1. **Prompt para ajuste de nomenclatura en los commits:**
   > "Quiero que los mensajes de mis 6 commits utilicen la estructura `POO: ...` enfocada exactamente en los temas de Abstracción, Encapsulamiento, Herencia y Polimorfismo."

2. **Prompt para ajuste del enfoque del proyecto:**
   > "Quiero orientar los mensajes de los commits a las buenas prácticas de diseño orientado a objetos en lugar de referencias técnicas externas, enfocándome en los conceptos de Abstracción, Encapsulamiento, Herencia y Polimorfismo."

3. **Prompt para resolución de problemas con Git:**
   > "Al intentar hacer el commit me sale el mensaje `nothing added to commit but untracked files present` o `Everything up-to-date`. ¿Cómo lo soluciono para subir mis cambios desde la terminal?"

4. **Prompt para mejora del diseño de consola:**
   > "Quiero que la salida por consola se vea más completa y profesional (con detalle de lista, numeración, cálculo de IGV 18%, producto más caro, descuento y mensaje final personalizado)."

5. **Prompt para la documentación del proyecto:**
   > "Ayúdame a generar el archivo `README.md` explicando la aplicación de los 4 pilares de POO, la salida por consola y la tabla de commits del proyecto."

---

## Commits Realizados

| N.º | Commit | Mensaje del Commit | Descripción |
| :---: | :--- | :--- | :--- |
| **1** | `Commit 1` | `POO: Implementa abstraccion con clase base ItemCarrito` | Creación de la clase abstracta base `ItemCarrito` y modelo inicial de Producto. |
| **2** | `Commit 2` | `POO: Aplica encapsulamiento en calculo de descuentos` | Implementación de métodos privados para ocultar la lógica interna de descuentos. |
| **3** | `Commit 3` | `POO: Implementa herencia con ProductoFisico y ProductoImportado` | Extensión de clases para incorporar productos físicos y cálculo de aranceles. |
| **4** | `Commit 4` | `POO: Aplica polimorfismo en calculo de subtotales` | Sobrescritura de métodos para procesar subtotales de productos físicos y digitales. |
| **5** | `Commit 5` | `POO: Integra gestor para recomendaciones de productos` | Incorporación de la clase `GestorRecomendaciones` para sugerir productos dinámicamente. |
| **6** | `Commit 6` | `POO: Agrega calculo dinamico de descuentos por promocion` | Aplicación de descuentos promocionales por monto total y formato final por consola. |

---

## Captura de la Ejecución

<img width="816" height="887" alt="image" src="https://github.com/user-attachments/assets/1e73db45-fe48-4fba-97fa-e9d5386826f2" />

