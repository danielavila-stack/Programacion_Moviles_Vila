# NavLab-05 - Navegación en Jetpack Compose

**Estudiante:** Daniela Vila Ramos  
**Curso:** Programación Móvil  
 
## Descripción
Este laboratorio demuestra la implementación de la navegación centralizada en Android utilizando **Jetpack Compose** y el componente **Navigation Compose**. 

## Requerimientos Funcionales
1. **RF1 - Navegación Principal:** La aplicación debe permitir la transición fluida desde la pantalla de Inicio (`HomeScreen`) hacia las pantallas de Lista (`ListScreen`) y Perfil (`ProfileScreen`).
2. **RF2 - Listado Dinámico de Elementos:** La pantalla de Lista (`ListScreen`) debe mostrar una colección estructurada de ítems navegables mediante `LazyColumn`.
3. **RF3 - Transferencia de Argumentos:** Al seleccionar un ítem de la lista, la aplicación debe enviar su identificador numérico (`itemId`) como argumento tipado (`Int`) hacia la pantalla de Detalle (`DetailScreen`).
4. **RF4 - Gestión de Perfil y BackStack:** La pantalla de Perfil (`ProfileScreen`) debe mostrar los datos del usuario y permitir el retorno al inicio restableciendo la pila de navegación mediante la propiedad `popUpTo`.

## Capturas de Pantalla

| Pantalla Inicio | Lista de Elementos | Detalle de Elemento | Pantalla Perfil |
| :---: | :---: | :---: | :---: |
| <img src="https://github.com/user-attachments/assets/8eddd49b-f679-4159-944b-9416a2bbaaa0" width="200" /> | <img src="https://github.com/user-attachments/assets/5b0c4b1a-0055-4568-a871-68010ed60fbf" width="200" /> | <img src="https://github.com/user-attachments/assets/1d4066e3-b71e-45bf-a55e-ae8dcb0d90d1" width="200" /> | <img src="https://github.com/user-attachments/assets/c47bfe24-67aa-42f5-b941-7fafd51e9920" width="200" /> |

## Estructura del Proyecto
- **`navigation/Screen.kt`**: Clase sellada (`sealed class`) que define las rutas fuertemente tipadas y los argumentos de la aplicación.
- **`navigation/AppNavigation.kt`**: Configuración central del `NavHost` y la lógica de enrutamiento entre pantallas.
- **`screens/HomeScreen.kt`**: Pantalla principal de bienvenida y navegación hacia la lista o el perfil.
- **`screens/ListScreen.kt`**: Lista navegable construida con `LazyColumn` que envía argumentos a la pantalla de detalle.
- **`screens/DetailScreen.kt`**: Pantalla que recibe y procesa argumentos tipados (`Int`) enviados desde la lista.
- **`screens/ProfileScreen.kt`**: Pantalla de perfil de usuario que implementa la limpieza del BackStack mediante `popUpTo`.
