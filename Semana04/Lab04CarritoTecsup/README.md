# Laboratorio 04: Aplicación de Carrito de Compras

**Estudiante:** Daniela  
**Curso:** Programación en Móviles  
**Institución:** TECSUP  

---

## Descripción del Proyecto

Esta aplicación es un prototipo funcional de **Carrito de Compras** desarrollado con **Android Studio** y **Jetpack Compose (Material 3)**. Permite registrar productos (nombre, precio y cantidad), calcular automáticamente el subtotal, el IGV (18%) y el total general, además de gestionar la eliminación individual de ítems y visualizar el estado vacío del carrito.

---

## Cuestionario / Respuestas Conceptuales

### 1. ¿Por qué la lista se declara con `val` y aun así podemos agregarle elementos?
En Kotlin, la palabra clave `val` significa que la **referencia** de la variable es inmutable (no se puede reasignar la variable `productos` a un nuevo objeto lista usando `=`). Sin embargo, el **contenido interno** de la lista sigue siendo mutable. Por lo tanto, podemos invocar métodos como `.add()`, `.remove()` o `.clear()` sobre el mismo objeto instanciado sin romper la regla de inmutabilidad de la variable.

### 2. ¿Por qué se utiliza `mutableStateListOf` y no una `MutableList` normal?
Una `MutableList` tradicional de Kotlin (como `mutableListOf()`) no está conectada con el sistema de rastreo de estado de Jetpack Compose. Si agregas o eliminas un producto de una `MutableList` normal, Compose no detecta el cambio y la pantalla no se actualiza.  
En cambio, **`mutableStateListOf`** crea una lista reactiva (`SnapshotStateList`) que notifica automáticamente a Compose cuando su contenido cambia, provocando la **recomposición** de la interfaz (`LazyColumn`) para reflejar los datos en tiempo real.

### 3. ¿Qué hace `remember` en la gestión del estado de la lista?
El bloque `remember` le indica a Compose que debe **preservar e ignorar la reinicialización** de esa lista durante las recomposiciones. Si no usáramos `remember`, cada vez que el usuario escriba una letra en cualquiera de los campos de texto (`TextField`), toda la función de la pantalla se ejecutaría de nuevo desde el inicio, creando una lista vacía nueva y borrando todos los productos que el usuario ya había agregado.

---

## Capturas de Pantalla (Evidencias)

<p align="center">
  <img src="https://github.com/user-attachments/assets/c0d18580-ec4f-48e6-943d-5b7bb9ebaf92" width="280" alt="Captura 1" />
  &nbsp;&nbsp;&nbsp;&nbsp;
  <img src="https://github.com/user-attachments/assets/dc7ea568-046b-4372-a458-2a3e315016a5" width="280" alt="Captura 2" />
</p>
