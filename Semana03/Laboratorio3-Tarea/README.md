# Laboratorio 03: Registro de Notas

**Estudiante:** Daniela Vila  
**Curso:** Programación en Móviles

---

## Descripción del Proyecto
Aplicación desarrollada en Android Studio con **Jetpack Compose** para el registro y cálculo del promedio ponderado de calificaciones. La interfaz utiliza controles interactivos `Slider` para seleccionar las notas de cuatro asignaturas, un `Switch` para alternar el redondeo del resultado final y un `Checkbox` de confirmación que habilita el botón de cálculo.

---
## Mejora con IA

| Prompt que usé | Qué generó Gemini | Qué acepté o corregí (y por qué) |
| :--- | :--- | :--- |
| En un composable de Jetpack Compose para cálculo de notas, ayúdame a integrar 4 Sliders para seleccionar notas de 0 a 20, un Switch para activar o desactivar el redondeo entero del promedio final, y un Checkbox de confirmación que habilite el botón de calcular. Muestra el resultado final resaltando el color según el estado de aprobación. | Construyó la disposición de los Sliders con estados `mutableFloatStateOf`, la condición del Checkbox para cambiar la propiedad `enabled` del botón, la lógica de redondeo con `roundToInt()` y la evaluación condicional para alternar el color del texto del resultado. | Acepté la gestión de estados de los componentes UI y la lógica de cambio de color dinámico. Corregí los coeficientes de ponderación asignados a cada curso para adaptarlos a la rúbrica requerida y reestructuré el maquetado con espaciados (`Spacer` y `padding`) para mejorar la distribución en pantalla. |

---
## Evidencia de Funcionamiento

| Estado Inicial | Resultado del Cálculo |
| :---: | :---: |
| <img src="https://github.com/user-attachments/assets/c5ca9bcf-eea8-4a69-8ccd-71a3d99f2468" width="280" alt="Estado Inicial" /> | <img src="https://github.com/user-attachments/assets/b75715e4-32b9-4ead-8e8e-f49346dca526" width="280" alt="Resultado del Cálculo" /> |

---
