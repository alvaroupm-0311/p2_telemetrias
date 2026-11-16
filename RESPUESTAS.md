# Respuestas — Práctica 2: Hilos y concurrencia
**Pareja:** (nombre estudiante A) y (nombre estudiante B)  
**Organización git:** `<nombre-organización>`

---

## Sesión 1: entender la concurrencia

### Ejercicio 1 — El contador compartido

**Pregunta 1.1**
> ¿Qué resultados has obtenido con un millón de iteraciones? ¿Y con mil?
> ¿Por qué con mil el resultado suele ser correcto, aunque el programa siga teniendo el mismo error?

*Respuesta:*

---

**Pregunta 1.2**
> En el intercalado de la tabla, ¿en qué paso exacto se pierde el incremento?
> Describe otro intercalado distinto que también pierda un incremento.

*Respuesta:*

---

### Ejercicio 2 — Tres formas de exclusión mutua

**Tabla de resultados** (tiempo en ms y valor final obtenido):

| Versión          | 2 hilos | 4 hilos | 8 hilos |
|------------------|---------|---------|---------|
| Sin protección   |         |         |         |
| `synchronized`   |         |         |         |
| `AtomicInteger`  |         |         |         |
| `ReentrantLock`  |         |         |         |

**Pregunta 2.1**
> ¿Qué versiones dan siempre el resultado correcto?
> De ellas, ¿cuál es más rápida y por qué crees que lo es?
> La versión sin protección es la más rápida: ¿por qué eso no sirve de nada?

*Respuesta:*

---

**Pregunta 2.2**
> En la versión con `ReentrantLock`, ¿qué ocurriría si `unlock()` no estuviera
> en un bloque `finally` y `valor++` lanzara una excepción?

*Respuesta:*

---

### Ejercicio 3 — Comprobar y después actuar

**Pregunta 3.1**
> ¿Por qué el saldo puede quedar negativo si `get()` y `addAndGet()` son atómicas?
> ¿Cuál es aquí la sección crítica?

*Respuesta:*

---

**Pregunta 3.2**
> Corrige `retirar` de dos formas: con `synchronized` y con un bucle de `compareAndSet`.
> ¿Qué ventaja e inconveniente tiene cada una?

*Respuesta:*

---

**Pregunta 3.3**
> ¿Qué tamaños finales y qué excepciones has observado en `ListaCompartida`?
> Corrígelo usando `Collections.synchronizedList` y explica por qué ahora funciona.

*Respuesta:*

---

### Ejercicio 4 — Visibilidad entre hilos

**Pregunta 4.1**
> ¿Qué ocurre sin `volatile` y qué ocurre con `volatile`?
> ¿Por qué `volatile` basta aquí pero no habría bastado en el ejercicio 1?

*Respuesta:*

---

### Ejercicio 5 — Interbloqueo

**Pregunta 5.1**
> Según el volcado, ¿qué cerrojo tiene cada hilo y cuál espera?
> ¿En qué estado (anexo D) están los dos hilos? Dibuja el grafo de espera.

*Respuesta:*

---

**Pregunta 5.2**
> ¿Cuál de las cuatro condiciones necesarias para el interbloqueo (Tema 2)
> es más fácil de romper aquí? Corrige `transferir` para que todos los hilos
> bloqueen las cuentas en el mismo orden (por ejemplo, por su identificador)
> y comprueba que el programa termina y que la suma de los saldos se conserva.

*Respuesta:*

---

### Ejercicio 6 — Una sección crítica demasiado grande

**Pregunta 6.1**
> ¿Cuánto ha tardado `Contencion`? Según JFR/JMC, ¿qué objeto concentra
> las esperas y cuánto tiempo, en total, han pasado los hilos bloqueados?

*Respuesta:*

---

**Pregunta 6.2**
> Reduce la sección crítica a lo imprescindible (solo `entradas.add`),
> graba de nuevo en `despues.jfr` y compara el tiempo total, los eventos
> `jdk.JavaMonitorEnter` y la vista Threads.
> ¿Sigue siendo correcto el programa? Justifícalo.

*Respuesta:*

---

## Sesión 2: de secuencial a multihilo

### Hito 0 — Medir el servidor secuencial

**Pregunta S2.1**
> ¿Cuántos hilos tuyos hay trabajando en el volcado?
> ¿Qué le ocurre a la segunda estación mientras se atiende a la primera?
> Anota el tiempo total de atender a las dos estaciones.

*Respuesta:*

---

### Hito 2 — El estado compartido

**Tabla de análisis del estado compartido:**

| Objeto                    | ¿Lo usan varios hilos? | ¿Alguno lo modifica? | ¿Necesita protección? |
|---------------------------|------------------------|----------------------|----------------------|
| `Receptor`                |                        |                      |                      |
| Cada `Petición`/`Encargo` |                        |                      |                      |
| Cada `Analizador`         |                        |                      |                      |
| Cada `Informe`            |                        |                      |                      |
| `Estadísticas`            |                        |                      |                      |

**Pregunta S2.2**
> Compara el tiempo total con el del Hito 0.
> ¿Por qué cada `HiloPetición` debe tener su propio `Analizador`
> en lugar de compartir uno? (Consulta el Javadoc).

*Respuesta:*

---

**Pregunta S2.3**
> En `Estadísticas`, `informesEnviados` nunca debería superar a `peticionesRecibidas`.
> Si sustituyes cada contador por su propio `AtomicLong` y `mostrar()` lee los tres
> contadores uno detrás de otro, ¿puede mostrar más informes que peticiones?
> Describe un intercalado que lo provoque.
> ¿Qué solución mantiene la coherencia entre los contadores?

*Respuesta:*

---

### Hito 3 — Observar el servidor multihilo

**Pregunta S2.4**
> En la vista Threads de JMC, ¿cuántos `HiloPetición` llegan a ejecutarse a la vez?
> ¿Ves esperas en el cerrojo de `Estadísticas`?
> ¿Son relevantes frente al tiempo de análisis?

*Respuesta:*

---

**Pregunta S2.5**
> ¿Qué pasaría si llegaran 500 peticiones a la vez?
> ¿Tiene sentido tener muchos más hilos analizando que núcleos?
> (Piensa en la Práctica 1 y en la Sesión 3).

*Respuesta:*

---

## Sesión 3: productores y consumidores

### Hito 2 — Esperar a los resultados

**Pregunta S3.1**
> Graba el servidor con JFR mientras atiende a dos simuladores.
> En JMC, ¿en qué estado están los `HiloPetición` mientras esperan (espera activa)?
> ¿Cuánta CPU consume la JVM (evento `jdk.CPULoad`)?
> ¿Por qué esto es un problema, aunque el servidor funcione?

*Respuesta:*

---

**Pregunta S3.2**
> ¿Qué ocurre si todos los trabajos de una petición pequeña terminan
> antes de que el `HiloPetición` llame a `esperarTodos()`?
> ¿Se queda bloqueado para siempre? Explica por qué no.

*Respuesta:*

---

**Pregunta S3.3**
> ¿Por qué la espera se hace con `while` y no con `if`?
> ¿Por qué ambos métodos deben ser `synchronized`?

*Respuesta:*

---

**Pregunta S3.4**
> Repite la grabación del paso 2a con la espera pasiva activa.
> ¿En qué estado están ahora los `HiloPetición` mientras esperan,
> y cuánta CPU consume la JVM?

*Respuesta:*

---

### Hito 3 — Diagnosticar el sistema completo

**Pregunta S3.5**
> En el volcado del servidor ocioso, ¿en qué estado están los `HiloAnalizador`
> y en qué método de la biblioteca están esperando?
> En el volcado con carga, ¿hay algún `HiloPetición` esperando para encolar? ¿Por qué?

*Respuesta:*

---

**Pregunta S3.6**
> Compara el tiempo total de atender a tres simuladores con el de la Sesión 2.
> ¿Mejora? Razónalo con el número de núcleos y de hilos de cada versión.

*Respuesta:*
