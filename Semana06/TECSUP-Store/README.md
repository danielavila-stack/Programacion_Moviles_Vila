# Laboratorio 06 - Fase 1: Desarrollo Manual sin IA (sin-ia)

**Estudiante:** Daniela Vila Ramos   
**Curso:** Programación Móvil


---

## Descripción de la Fase 1

En esta primera fase se construyó la estructura base de la aplicación **TECSUP Store** de manera completamente manual. Se implementó una arquitectura **MVVM** limpia con **Jetpack Compose**, cubriendo la navegación principal, la interfaz de inicio con tarjetas de productos, el menú lateral (`AppDrawer`) y la personalización del tema visual institucional.

---

## Estructura del Proyecto

```text
com.vila.daniela.tecsupstore/
│
├── components/
│   ├── AppDrawer.kt         # Menú de navegación lateral (Drawer)
│   └── TarjetaProducto.kt   # Componente reutilizable para cada producto
│
├── model/
│   └── Producto.kt          # Modelo de datos para los productos
│
├── screens/
│   ├── AppNavegacion.kt     # Configuración de NavHost y rutas de la app
│   └── HomeScreen.kt        # Pantalla principal con lista LazyColumn
│
└── ui.theme/
    ├── Color.kt             # Definición de colores principales
    ├── Theme.kt             # Configuración del tema Material 3
    └── Type.kt              # Tipografía de la aplicación
```
## Historial de Commits (Fase 1)

1. `feat: icono de 3 puntos y estado expanded en tarjeta de producto`
2. `feat: DropdownMenu contextual con opciones basicas funcionando`
3. `style: personalizacion del DropdownMenu con iconos y divisores`
4. `feat: estructura del NavigationDrawer con ModalDrawerSheet`
5. `feat: navegacion real desde los items del drawer`
6. `style: personalizacion del drawer, encabezado, item activo con datos de Daniela Vila`

---

## Evidencias de Ejecución

| Pantalla de Inicio | Menú Lateral (AppDrawer) |
| :---: | :---: |
| <img width="360" height="801" alt="Pantalla de Inicio" src="https://github.com/user-attachments/assets/cb29570a-3169-476c-8ab6-62e973df4d05"> | <img width="360" height="801" alt="Menú Lateral (AppDrawer)" src="https://github.com/user-attachments/assets/65992280-1e12-4425-9ea4-fcbe61177fb0"> |
