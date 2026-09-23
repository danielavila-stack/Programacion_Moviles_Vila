# Laboratorio 03 — Registro de Producto (Jetpack Compose)

**Estudiante:** Daniela Vila Ramos  
**Curso:** Desarrollo de Aplicaciones Móviles  
**Docente:** Juan José León Suiyon  

---

##  Descripción del Proyecto
Aplicación desarrollada en Android Studio con Jetpack Compose que permite ingresar los datos de un producto (nombre, precio y cantidad) y visualizarlos en una tarjeta de resumen con el importe total calculado.

---

## Capturas de Pantalla

### 1. Pantalla Inicial (Vacía)
<img width="382" height="787" alt="image" src="https://github.com/user-attachments/assets/995cc080-23c8-4504-b7e1-1f869b8f7879" />

### 2. Producto Registrado
<img width="372" height="637" alt="image" src="https://github.com/user-attachments/assets/75cd241b-8200-4435-94bf-e5d46ad437d9" />

---

## Pregunta de Reflexión

> **¿Qué pasaría si declaras las variables de los campos SIN `remember`?**

Si se declaran las variables sin la función `remember`, los valores introducidos se reinician al valor inicial en cada proceso de recomposición (*recomposition*) de la pantalla. Esto provoca que, al escribir cualquier letra o interactuar con la interfaz, el texto introducido en los campos de entrada se borre automáticamente y no se conserve el estado del formulario.

---

## Historial de Commits

1. `Estructura inicial del proyecto`
2. `Agrega encabezado con jerarquia tipografica`
3. `Agrega campos de ingreso con estado`
4. `Agrega boton de accion y card de resumen`
5. `Aplica reglas de diseno y mensaje de confirmacion`
6. `Agrega README con capturas y respuesta sobre remember`


