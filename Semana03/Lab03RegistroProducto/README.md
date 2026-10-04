# Laboratorio 03: Registro de Producto

**Estudiante:** Daniela Vila  
**Curso:** Programación en Móviles

## Descripción
Aplicación móvil desarrollada en Android Studio utilizando **Jetpack Compose** para el registro interactivo de productos. La interfaz permite ingresar el nombre del producto, su precio unitario y la cantidad, realizando el cálculo automático del importe total. Incluye validación de datos en tiempo real para evitar campos vacíos o valores no numéricos, desplegando un mensaje de error o una tarjeta de resumen según corresponda.

## Capturas de Pantalla

### Parte B: Mejora con IA (Validación y Botón Limpiar)

| Registro Exitoso del Producto | Validación de Campos Vacíos |
| :---: | :---: |
| <img src="https://github.com/user-attachments/assets/922685e6-39a8-4b09-abe0-f3df78fee16d" width="280" alt="Validación y Alerta de Error" /> | <img src="https://github.com/user-attachments/assets/26a9365f-57d9-445c-b5fc-d5d18966dad7" width="280" alt="Resumen de Registro" /> |


## Mejora con IA

| Prompt que usé | Qué generó Gemini | Qué acepté o corregí (y por qué) |
| :--- | :--- | :--- |
| Necesito optimizar la interfaz de `PantallaRegistro` en Jetpack Compose. Implementa una comprobación para prevenir el registro si algún campo está vacío, desplegando una alerta en texto rojo bajo los controles. Adicionalmente, incluye un botón secundario de tipo `OutlinedButton` al lado de AGREGAR para reiniciar todos los inputs a su estado inicial. | Modificó el bloque del composable agregando la variable de estado `mensajeError`, integró la comprobación condicional para validar campos en blanco antes de procesar el resumen, y añadió un `OutlinedButton` alineado en un `Row` horizontal para restablecer las variables. | Conservé la estructura del botón secundario de limpieza y la alerta visual en rojo. Sin embargo, complementé la lógica de validación añadiendo comprobaciones de conversión numéricas (`toDoubleOrNull()` y `toIntOrNull()`) para garantizar que precio y cantidad sean datos válidos antes de efectuar los cálculos del importe total. |