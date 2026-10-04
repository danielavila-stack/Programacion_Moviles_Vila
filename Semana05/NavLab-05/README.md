# NavLab-05 - Navegación en Jetpack Compose

**Estudiante:** Daniela Vila Ramos  
**Curso:** Programación Móvil

## Descripción
En este laboratorio se desarrolló e implementó el **Portal Académico**, una aplicación móvil para Android construida con **Jetpack Compose** y **Navigation Compose**. Durante el proceso, se estructuró un flujo de navegación completo que abarca desde la autenticación del usuario hasta la consulta detallada de información académica.

El proyecto fue optimizado mediante asistencia de Inteligencia Artificial (Gemini AI) para integrar la gestión de datos dinámicos mediante `StudentProvider`, un control de sesión seguro con limpieza del historial de navegación (*backstack*) y una interfaz moderna bajo los lineamientos de **Material Design 3**.

## Requerimientos Funcionales
1. **RF1 - Navegación Principal:** La aplicación debe permitir la transición fluida desde la pantalla de Inicio (`HomeScreen`) hacia las pantallas de Lista (`ListScreen`) y Perfil (`ProfileScreen`).
2. **RF2 - Listado Dinámico de Elementos:** La pantalla de Lista (`ListScreen`) debe mostrar una colección estructurada de ítems navegables mediante `LazyColumn`.
3. **RF3 - Transferencia de Argumentos:** Al seleccionar un ítem de la lista, la aplicación debe enviar su identificador numérico (`itemId`) como argumento tipado (`Int`) hacia la pantalla de Detalle (`DetailScreen`).
4. **RF4 - Gestión de Perfil y BackStack:** La pantalla de Perfil (`ProfileScreen`) debe mostrar los datos del usuario y permitir el retorno al inicio restableciendo la pila de navegación mediante la propiedad `popUpTo`.

## Prompt Utilizado con Gemini
> 1. ROL
     > Actúa como un desarrollador Senior Android experto en Jetpack Compose, Material Design 3 y arquitectura modular de software.
>
>2. CONTEXTO
    >El proyecto "NavLab-05" (paquete base: com.daniela.navlab_05) debe rediseñarse para convertirse en un Portal Académico en tonos morados (#5B4097). Debe tener una fidelidad >del 100% con las capturas de referencia del profesor tanto en diseño visual (tarjetas redondeadas, sombras, avatares) como en textos y flujo de datos.
>
>3. OBJETIVO
    >Generar todo el código Kotlin necesario siguiendo la estructura simplificada de 11 archivos (con componentes agrupados en Components.kt y modelo de datos en Student.kt), >asegurando que el nombre del usuario ingresado en el Login pase dinámicamente al Home ("Bienvenido,\n[Nombre]").
>
>4. TAREA
    >Paso a paso debes:
>1. Configurar la paleta de colores morada (#5B4097) y tema Material 3 en ui/theme/.
>2. Definir en models/Student.kt la data class Student y la lista ficticia de alumnos con los nombres exactos de la muestra (Juan León, Maria Garcia, Carlos Perez, Ana >Lopez, Luis Ramirez).
>3. Construir en components/Components.kt todos los elementos reutilizables (campos de texto, botones, tarjetas del menú, elementos de lista y bloques de información de >perfil).
>4. Configurar las rutas y el NavHost en navigation/ (Screen.kt y AppNavigation.kt), habilitando el argumento dinámico "home/{userName}".
>5. Generar las 5 pantallas en screens/ (LoginScreen, HomeScreen, DirectoryScreen, DetailScreen, ProfileScreen).
>6. Entregar el código Kotlin completo archivo por archivo con todos sus imports.
>
>5. INFORMACIÓN / DATOS
>- Paquete base: com.daniela.navlab_05
>- Colores: Primario Morado (#5B4097), Fondo lavanda claro (#F4EFFA), Cajas e Inputs en blanco/gris claro (#F0ECF5).
>- models/Student.kt: Contiene data class Student(id, name, career, email, faculty, bio) y la lista de estudiantes de ejemplo.
>- components/Components.kt: Contiene CustomTextField, PrimaryButton, LoginHeader, NavigationMenuCard, LogoutButton, StudentListItemCard, StudentInfoCard, ProfileTopHeader >y ProfileInfoGroup.
>- LoginScreen.kt: Fondo morado suave. Tarjeta blanca flotante con "Portal Académico", "Accede a tu cuenta", inputs para "Correo Institucional" y "Contraseña" (con botón >para mostrar/ocultar), botón morado "INICIAR SESIÓN", y enlace "¿Olvidaste tu contraseña?". Lógica: Si el usuario escribe su correo/nombre (ej. "Carla"), extrae y pasa ese >nombre a Home. Si el campo está vacío, envía por defecto "Daniela Vila Ramos".
>- HomeScreen.kt: Saludo grande en blanco/negrita: "Bienvenido,\n[userName]", subtítulo "¿Qué deseas gestionar hoy?", dos tarjetas blancas ("Directorio de Alumnos" / "Ver y >gestionar estudiantes" y "Mi Perfil Académico" / "Datos personales y progreso") y botón inferior "Cerrar Sesión Segura".
>- DirectoryScreen.kt: TopAppBar "← Directorio de Alumnos", muestra la lista de alumnos usando LazyColumn.
>- DetailScreen.kt: TopAppBar "← Expediente Académico", banner morado superior con avatar circular superpuesto en el centro, nombre y carrera. Tarjeta blanca inferior con >ID Estudiante ("2024-0001"), Correo ("juan.leon@example.com"), Facultad ("Ingeniería y Tecnología") y sección "Biografía".
>- ProfileScreen.kt: TopAppBar "← Configuración de Perfil", banner morado con avatar y nombre completo. Sección "INFORMACIÓN PERSONAL" (Nombre Completo, Correo, Teléfono) y >"ACADÉMICO" (Carrera, Ciclo Actual). Botón inferior rosa/rojo "Cerrar Sesión".
>
>6. RESTRICCIONES
>- Respetar estrictamente la estructura modular de 11 archivos:
   >  * components/Components.kt
> * models/Student.kt
> * navigation/AppNavigation.kt
> * navigation/Screen.kt
> * screens/LoginScreen.kt, HomeScreen.kt, DirectoryScreen.kt, DetailScreen.kt, ProfileScreen.kt
> * ui/theme/Color.kt, Theme.kt
>- Usar siempre el paquete com.daniela.navlab_05 en todos los archivos.
>- NO omitir ningún import ni dejar métodos inconclusos.
>
>7. FORMATO DE SALIDA
    >Entrega el código en bloques Kotlin organizados por ruta (ej. // Archivo: components/Components.kt).
>
>8. CRITERIOS DE CALIDAD
>- Código limpio, sin errores sintácticos y listo para copiar en Android Studio.
>- Transferencia dinámica fluida del nombre de usuario desde Login a Home.
>- Copia fiel del estilo visual, textos y distribución de las imágenes del profesor.
>
>9. VERIFICACIÓN
    >Antes de dar la respuesta, confirma que:
>- ¿Se crearon los componentes unificados en components/Components.kt?
>- ¿El modelo Student.kt contiene los estudiantes de la muestra?
>- ¿La navegación transfiere el parámetro userName desde LoginScreen hasta HomeScreen?
>- ¿Todos los textos del Login, Home, Perfil y Expediente coinciden con las imágenes del docente?

## Capturas de Pantalla

| Pantalla Inicio | Lista de Elementos | Directorio de Alumnos | Perfil de uno de los alumnos | Perfil del Alumno |
| :---: | :---: | :---: | :---: | :---: |
| <img src="https://github.com/user-attachments/assets/7081db15-8616-40ad-b004-5139cfdc1fd8" width="100%" /> | <img src="https://github.com/user-attachments/assets/135ef956-7165-4eba-a5c8-362cf2bfe480" width="100%" /> | <img src="https://github.com/user-attachments/assets/399906c4-babc-4a66-a875-36be23431832" width="100%" /> | <img src="https://github.com/user-attachments/assets/d01ff83c-085e-4a82-97be-c81f1969e31b" width="100%" /> | <img src="https://github.com/user-attachments/assets/a45e837b-4353-400e-9e04-f11e2f34b0bc" width="100%" /> |

## Estructura del Proyecto
- **`navigation/Screen.kt`**: Clase sellada (`sealed class`) que define las rutas fuertemente tipadas y los argumentos de la aplicación.
- **`navigation/AppNavigation.kt`**: Configuración central del `NavHost` y la lógica de enrutamiento entre pantallas.
- **`screens/HomeScreen.kt`**: Pantalla principal de bienvenida y navegación hacia la lista o el perfil.
- **`screens/ListScreen.kt`**: Lista navegable construida con `LazyColumn` que envía argumentos a la pantalla de detalle.
- **`screens/DetailScreen.kt`**: Pantalla que recibe y procesa argumentos tipados (`Int`) enviados desde la lista.
- **`screens/ProfileScreen.kt`**: Pantalla de perfil de usuario que implementa la limpieza del BackStack mediante `popUpTo`.
