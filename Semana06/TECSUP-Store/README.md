# Laboratorio 06 - Fase 2: Desarrollo e Integración con IA

**Estudiante:** Daniela Vila Ramos  
**Curso:** Programación Móvil

---

## Descripción de la Fase 2

En esta fase se implementó la gestión de estado global de la aplicación utilizando la arquitectura MVVM en **Jetpack Compose**. Se integró un flujo dinámico de productos favoritos interactuando con el `DropdownMenu` de cada tarjeta y reflejando el conteo total en tiempo real mediante un componente `Badge` morado en el `AppDrawer`.

---

## Prompts Utilizados

### Prompt 1: Manejo de Estado Global
> *"Necesito crear un estado reactivo global en `AppNavegacion.kt` utilizando `mutableStateListOf<Int>()` para almacenar los IDs de los productos marcados como favoritos. Pasa el total de elementos de esta lista (`cantidadFavoritos`) como parámetro al componente `AppDrawer`."*

---

### Prompt 2: Interacción y Conexión de Eventos
> *"En `TarjetaProducto.kt`, conecta la opción 'Favoritos' del `DropdownMenu` con un evento `onFavoritoClick`. Pasa este callback a través de `HomeScreen.kt` hasta `AppNavegacion.kt` para agregar o quitar el ID del producto seleccionado dentro de la lista de favoritos."*

---

### Prompt 3: Componente Badge y Personalización Visual
> *"En `AppDrawer.kt`, actualiza la firma del composable para recibir el parámetro `cantidadFavoritos: Int`. Si `cantidadFavoritos > 0`, agrega un componente `Badge` con el número actualizado dentro de la propiedad `badge` del `NavigationDrawerItem` correspondiente a Favoritos."*

---

## Evidencias de Ejecución

<table>
  <tr>
    <td align="center"><b>Opciones DropdownMenu</b></td>
    <td align="center"><b>Contador Badge en Drawer</b></td>
  </tr>
  <tr>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/46fde276-69de-48a4-8e1a-17ea86bb2eb8" width="300" alt="DropdownMenu" />
    </td>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/ba64ebc3-0c88-49d6-b567-c5a482e90059" width="300" alt="Badge Drawer" />
    </td>
  </tr>
</table>