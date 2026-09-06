# Laboratorio 02: Carrito de Compras en Kotlin

**Estudiante:** Daniela  
**Curso:** Programación en Móviles  
**Docente:** Juan León Suiyon  

---

## Descripción del Proyecto
Este programa implementa un sistema interactivo de carrito de compras en consola utilizando Kotlin. Permite gestionar un catálogo de productos estructurados con `data class`, realizar el cálculo automatizado de importes fiscales, aplicar reglas de negocio para descuentos condicionales y presentar un reporte detallado al cliente.

### Funcionalidades Principales
- **Cálculo de Subtotal:** Determinación del importe base sumando el resultado de `precio × cantidad` de cada ítem.
- **Cálculo de IGV:** Aplicación del impuesto general a las ventas (18%) sobre el subtotal acumulado.
- **Cálculo del Total General:** Determinación del monto final a pagar tras impuestos y bonificaciones.
- **Reporte Detallado:** Impresión formateada del desglose de productos, cantidades e importes individuales.
- **Lógica de Descuentos (`when`):** Identificación dinámica del producto de mayor valor para aplicar una bonificación especial sobre su precio final.

---

## Pregunta de Teoría: Diferencia entre `val` y `var` en Kotlin

- **`val` (Inmutable):**
  Declara una referencia de **solo lectura**. Una vez asignado el valor en memoria, este **no puede ser reasignado**. Promueve la inmutabilidad, la seguridad en entornos multihilo y previene efectos secundarios no deseados en la lógica del sistema.

- **`var` (Mutable):**
  Declara una variable con **permiso de reasignación**. Su valor puede cambiar a lo largo del ciclo de vida de la ejecución. Se utiliza únicamente cuando el estado de una variable necesita mutar constantemente.

---

## Captura de Pantalla de Ejecución

<img width="705" height="722" alt="image" src="https://github.com/user-attachments/assets/d4eda965-2f02-4389-a49e-8b99f8a4714b" />
