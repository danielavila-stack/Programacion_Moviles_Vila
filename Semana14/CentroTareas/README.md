# CentroTareas

## Observación del Paso 1

**Qué pasa con la pantalla:** al tocar **Tarea larga (MAL)** y enseguida **Tócame**, durante unos 4 segundos nada responde: el contador no sube, los botones no reaccionan y la pantalla se queda congelada. Después de los 4 segundos aparece directamente **Terminó** y recién ahí se cuentan los toques.

**Por qué no se ve "Trabajando...":** `tareaLargaMala()` corre en el **hilo principal**, que es el mismo que dibuja la pantalla. El código cambia el mensaje a "Trabajando...", pero `Thread.sleep(4000)` bloquea ese hilo, así que no puede redibujar. Cuando el hilo se libera, el mensaje ya cambió a "Terminó", y solo se dibuja ese último valor.

**Si la espera fuera de más de 10 segundos:** Android detecta que la app no responde (después de unos 5 segundos sin atender un toque) y muestra el cuadro **"La aplicación no responde" (ANR)**, con las opciones de esperar o cerrar la app.