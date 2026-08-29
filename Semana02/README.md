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
> "Actúa como un desarrollador experto en Kotlin. Necesito refactorizar un Carrito de Compras en consola para un laboratorio académico en la rama `con-ia`. Guíame en 6 etapas para realizar 6 commits en Git etiquetados como `POO: ...`. El código en `Carrito.kt` debe aplicar Abstracción, Encapsulamiento, Herencia y Polimorfismo. Además, la salida en consola debe ser detallada, mostrando la lista de productos, subtotal, IGV (18%), producto más caro, descuento y una sugerencia para el cliente."

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

<img width="785" height="777" alt="Carrito" src="https://github.com/user-attachments/assets/d6a7ad83-a5e2-4f14-9deb-ea6129930c14" />


---

# Sistema de Tarifas de Estacionamiento en Kotlin

---

## Descripción del Proyecto
Este módulo consiste en el desarrollo de un programa de consola en Kotlin para gestionar el cobro de un **Sistema de Estacionamiento**. El programa procesa múltiples vehículos. La lógica evalúa el tipo de vehículo, el tiempo de permanencia, las tarifas por hora y aplica reglas de negocio para calcular descuentos especiales (como descuentos para clientes frecuentes o por tiempo prolongado) y recargos adicionales.

---

## Reglas del Negocio e Implementación

### 1. Tarifado Base por Categoría
| Tipo de Vehículo | Tarifa por Hora (S/) |
| :--- | :---: |
| **Moto** | S/ 2.00 |
| **Auto** | S/ 4.00 |
| **Camioneta** | S/ 10.00 |

### 2. Condición de Entrada
* **Tiempo mínimo:** Se establece una permanencia mínima obligatoria de 1 hora por servicio (no se procesan registros de 0 horas o fraccionales menores a 1).

### 3. Escala Progresiva de Recargos (Por Tiempo Prolongado)
* **De 1 a 2 horas:** Cobro estándar a tarifa base (0% adicional).
* **De 3 a 5 horas:** Se aplica un **20% de recargo** adicional sobre la tarifa base de esas horas.
* **A partir de la 6 hora en adelante:** Las horas excedentes a la quinta aplican un **50% de recargo** sobre la tarifa base.

### 4. Beneficios y Dinámica
* **Descuento Cliente Frecuente:** **10% de reducción** sobre el monto total calculado para usuarios frecuentes.
* **Procesamiento en Conjunto:** El sistema solicita al inicio el número exacto de vehículos a procesar en la sesión.

---

## Prompts Utilizados

### Prompt General (Prompt Base)
> *"Actúa como un desarrollador experto en Kotlin. Necesito construir un sistema de cobro de tarifas de estacionamiento dentro del archivo `Estacionamiento.kt` en el paquete `com.daniela.lab02carritokotlin.lab02tarifas`. El programa debe resolver la problemática del cálculo de tarifas según el tipo de vehículo (Moto: S/ 2.00, Auto: S/ 4.00, Camioneta: S/ 10.00) y aplicar las siguientes reglas: considerar un tiempo mínimo de 1 hora de estancia, mantener tarifa base normal en las primeras 2 horas, aplicar un 20% de recargo entre las horas 3 y 5, aplicar un 50% de recargo para las horas a partir de la 6.ª, y descontar un 10% si el usuario es cliente frecuente. Todo esto solicitando cuántos vehículos se van a procesar e imprimiendo la liquidación formateada."*

---

## Historial de Prompts Utilizados (Paso a Paso)

#### Prompt 1: Definición de Clases e Ingreso de Datos
> *"Ayúdame a crear la clase en Kotlin para representar el estacionamiento. Debe recibir el tipo de vehículo (Moto, Auto, Camioneta), el número de horas de estancia (validando que no sea menor a 1 hora) y la condición de cliente frecuente."*

#### Prompt 2: Lógica de Tarifas Base, Recargos y Descuento
> *"Agrega los métodos de cálculo en la clase: la tarifa base por hora según el tipo de vehículo (Moto: S/ 2, Auto: S/ 4, Camioneta: S/ 10), la escala de recargos por tiempo (20% en horas 3 a 5, y 50% para las horas a partir de la 6.ª) y el descuento del 10% si el cliente es frecuente."*

#### Prompt 3: Organización de Commits y Subida a Git
> *"Indícame los comandos exactos en Git Bash para registrar mi avance en los 3 commits requeridos (`Ingreso de datos`, `Calculos` y `Mostrar Resultado`) asegurando que solo suba los archivos de la carpeta `lab02tarifas`."*

#### Prompt 4: Generación del Reporte Formateado en Consola
> *"Por último, ajusta la función principal para solicitar la cantidad de vehículos a procesar, iterar cada registro y mostrar en consola un comprobante detallado con subtotal, recargos, descuento aplicado y total a pagar."*

---

## Commits Realizados

* **Commit 1:** `Ingreso de datos`
  * Creación del modelo principal para representar los datos del vehículo, horas consumidas y tarifa base.
* **Commit 2:** `Calculos`
  * Implementación de la lógica para evaluar el 10% de descuento por cliente frecuente y promociones por tiempo.
* **Commit 3:** `Mostrar Resultado`
  * Configuración del flujo principal en `main()` para iterar la lista de vehículos y mostrar el desglose final por consola.

---

## Captura de la Ejecución
<img width="410" height="661" alt="Estacionamiento" src="https://github.com/user-attachments/assets/286e61de-3652-45ed-ac14-6fae985476e2" />
