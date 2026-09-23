# NavLab-05 - Navegación en Jetpack Compose

**Estudiante:** Daniela Vila Ramos  
**Curso:** Programación Móvil  

---

## Descripción

Este laboratorio demuestra la implementación de la navegación centralizada en Android utilizando **Jetpack Compose** y el componente **Navigation Compose**, rediseñado y modernizado mediante asistencia de Inteligencia Artificial (Gemini Agent) para adaptar la interfaz a **Material Design 3** con una paleta cromática en tonos morados (`#5B4097`) y una arquitectura modular estricta de 11 archivos.

---

## Requerimientos Funcionales

1. **RF1 - Navegación Principal y Autenticación:** La aplicación gestiona el acceso mediante `LoginScreen` procesando las credenciales del usuario y transfiriendo dinámicamente el nombre ingresado (`userName`) hacia la pantalla de inicio (`HomeScreen`).
2. **RF2 - Listado Dinámico de Alumnos:** La pantalla de directorio (`DirectoryScreen`) muestra una colección estructurada de estudiantes navegables mediante un componente optimizado `LazyColumn`.
3. **RF3 - Transferencia de Argumentos y Expediente:** Al seleccionar un alumno de la lista, la aplicación envía su identificador (`studentId`) como argumento hacia la pantalla de detalle (`DetailScreen`) para cargar su expediente académico completo.
4. **RF4 - Gestión de Perfil y Rediseño Modular:** La pantalla de Perfil (`ProfileScreen`) muestra los datos personales y académicos del estudiante, permitiendo el retorno o cierre de sesión bajo una arquitectura modular limpia de 11 archivos en Material Design 3.

---

## Prompt Utilizado con IA

```text
1. ROL
Actúa como un desarrollador Senior Android experto en Jetpack Compose, Material Design 3 y arquitectura modular de software.

2. CONTEXTO
El proyecto "NavLab-05" (paquete base: com.daniela.navlab_05) debe rediseñarse para convertirse en un Portal Académico en tonos morados (#5B4097). Debe tener una fidelidad del 100% con las capturas de referencia del profesor tanto en diseño visual (tarjetas redondeadas, sombras, avatares) como en textos y flujo de datos.

3. OBJETIVO
Generar todo el código Kotlin necesario siguiendo la estructura simplificada de 11 archivos (con componentes agrupados en Components.kt y modelo de datos en Student.kt), asegurando que el nombre del usuario ingresado en el Login pase dinámicamente al Home ("Bienvenido,\n[Nombre]").

4. TAREA
Paso a paso debes:
1. Configurar la paleta de colores morada (#5B4097) y tema Material 3 en ui/theme/.
2. Definir en models/Student.kt la data class Student y la lista ficticia de alumnos con los nombres exactos de la muestra (Juan León, Maria Garcia, Carlos Perez, Ana Lopez, Luis Ramirez).
3. Construir en components/Components.kt todos los elementos reutilizables (campos de texto, botones, tarjetas del menú, elementos de lista y bloques de información de perfil).
4. Configurar las rutas y el NavHost en navigation/ (Screen.kt y AppNavigation.kt), habilitando el argumento dinámico "home/{userName}".
5. Generar las 5 pantallas en screens/ (LoginScreen, HomeScreen, DirectoryScreen, DetailScreen, ProfileScreen).
6. Entregar el código Kotlin completo archivo por archivo con todos sus imports.

5. INFORMACIÓN / DATOS
- Paquete base: com.daniela.navlab_05
- Colores: Primario Morado (#5B4097), Fondo lavanda claro (#F4EFFA), Cajas e Inputs en blanco/gris claro (#F0ECF5).
- models/Student.kt: Contiene data class Student(id, name, career, email, faculty, bio) y la lista de estudiantes de ejemplo.
- components/Components.kt: Contiene CustomTextField, PrimaryButton, LoginHeader, NavigationMenuCard, LogoutButton, StudentListItemCard, StudentInfoCard, ProfileTopHeader y ProfileInfoGroup.
- LoginScreen.kt: Fondo morado suave. Tarjeta blanca flotante con "Portal Académico", "Accede a tu cuenta", inputs para "Correo Institucional" y "Contraseña" (con botón para mostrar/ocultar), botón morado "INICIAR SESIÓN", y enlace "¿Olvidaste tu contraseña?". Lógica: Si el usuario escribe su correo/nombre (ej. "Carla"), extrae y pasa ese nombre a Home. Si el campo está vacío, envía por defecto "Daniela Vila Ramos".
- HomeScreen.kt: Saludo grande en blanco/negrita: "Bienvenido,\n[userName]", subtítulo "¿Qué deseas gestionar hoy?", dos tarjetas blancas ("Directorio de Alumnos" / "Ver y gestionar estudiantes" y "Mi Perfil Académico" / "Datos personales y progreso") y botón inferior "Cerrar Sesión Segura".
- DirectoryScreen.kt: TopAppBar "← Directorio de Alumnos", muestra la lista de alumnos usando LazyColumn.
- DetailScreen.kt: TopAppBar "← Expediente Académico", banner morado superior con avatar circular superpuesto en el centro, nombre y carrera. Tarjeta blanca inferior con ID Estudiante ("2024-0001"), Correo ("juan.leon@example.com"), Facultad ("Ingeniería y Tecnología") y sección "Biografía".
- ProfileScreen.kt: TopAppBar "← Configuración de Perfil", banner morado con avatar y nombre completo. Sección "INFORMACIÓN PERSONAL" (Nombre Completo, Correo, Teléfono) y "ACADÉMICO" (Carrera, Ciclo Actual). Botón inferior rosa/rojo "Cerrar Sesión".

6. RESTRICCIONES
- Respetar estrictamente la estructura modular de 11 archivos:
  * components/Components.kt
  * models/Student.kt
  * navigation/AppNavigation.kt
  * navigation/Screen.kt
  * screens/LoginScreen.kt, HomeScreen.kt, DirectoryScreen.kt, DetailScreen.kt, ProfileScreen.kt
  * ui/theme/Color.kt, Theme.kt
- Usar siempre el paquete com.daniela.navlab_05 en todos los archivos.
- NO omitir ningún import ni dejar métodos inconclusos.

7. FORMATO DE SALIDA
Entrega el código en bloques Kotlin organizados por ruta (ej. // Archivo: components/Components.kt).

8. CRITERIOS DE CALIDAD
- Código limpio, sin errores sintácticos y listo para copiar en Android Studio.
- Transferencia dinámica fluida del nombre de usuario desde Login a Home.
- Copia fiel del estilo visual, textos y distribución de las imágenes del profesor.

9. VERIFICACIÓN
Antes de dar la respuesta, confirma que:
- ¿Se crearon los componentes unificados en components/Components.kt?
- ¿El modelo Student.kt contiene los estudiantes de la muestra?
- ¿La navegación transfiere el parámetro userName desde LoginScreen hasta HomeScreen?
- ¿Todos los textos del Login, Home, Perfil y Expediente coinciden con las imágenes del docente?

```
## Capturas de Pantalla

| Pantalla Login | Pantalla Inicio | Lista de Alumnos |
| :---: | :---: | :---: |
| <img src="https://github.com/user-attachments/assets/69858238-3548-4b23-b200-1b49a195fca5" width="200" /> | <img src="https://github.com/user-attachments/assets/7ac88fef-162f-4be1-83a0-8bdacd27db48" width="200" /> | <img src="https://github.com/user-attachments/assets/1130f6b4-ddd8-42d6-a724-2ab3beb3fc4b" width="200" /> |

| Detalle de Elemento | Pantalla Perfil |
| :---: | :---: |
| <img src="https://github.com/user-attachments/assets/748092a1-f3ba-4a65-9ed1-d7846e1db265" width="200" /> | <img src="https://github.com/user-attachments/assets/edaac1ec-c3e1-4c2b-890f-7b331e73b21e" width="200" /> |
