# Práctica 2 — Hilos y concurrencia: el servidor de telemetría

**Asignatura:** Sistemas Operativos  
**Curso:** 2026-2027  
**Pareja:** (nombre estudiante A) · (nombre estudiante B)  
**Organización:** `<nombre-organización>`

---

## Requisitos previos

Antes de la Sesión 1 comprueba que tienes instalado en el equipo con el que vayas a trabajar:

| Herramienta | Versión | Comprobación |
|---|---|---|
| **JDK** (no solo JRE) | 25 | `java -version` |
| **Eclipse IDE for Java and Web Developers** | 2026-06 | — |
| **JDK Mission Control (JMC)** | 9 o superior | Descarga en Moodle |

> **Importante:** `jcmd`, `jstack` y `jfr` deben pertenecer al **mismo** JDK que usa Eclipse.
> Si tienes varios JDK instalados, configura Eclipse para que use el JDK 25
> (*Preferences → Java → Installed JREs*).

---

## Configuración inicial

### 1. Clonar la plantilla y vincularla a vuestra organización

El **estudiante A** ejecuta:

```bash
git clone https://git.mucs.es/sist_ope/p2-telemetria
cd p2-telemetria
git remote remove origin
git remote add origin https://git.mucs.es/<organizacion>/p2-telemetria
git push -u origin main
```

El **estudiante B** clona directamente desde la organización:

```bash
git clone https://git.mucs.es/<organizacion>/p2-telemetria
```

### 2. Añadir el JAR al proyecto

1. Descarga `p2-telemetria.jar` desde Moodle.
2. Cópialo en la carpeta `lib/` de este proyecto.
3. En Eclipse: clic derecho sobre el proyecto → *Build Path → Configure Build Path →
   Libraries → Add JARs…* → selecciona `lib/p2-telemetria.jar`.

> El JAR está excluido del repositorio por el `.gitignore`. **No lo subas.**

### 3. Configurar la codificación en Eclipse

*Preferences → General → Workspace → Text file encoding → UTF-8*

Comprueba que el proyecto compila sin errores antes de empezar la Sesión 1.

---

## Estructura del repositorio

```
p2-telemetria/
├── src/
│   ├── ssoo/concurrencia/      ← ejemplos ya escritos (Sesión 1)
│   └── ssoo/servidor/          ← servidor que irás construyendo (Sesiones 2 y 3)
├── lib/                        ← aquí va p2-telemetria.jar (no se sube al repo)
├── RESPUESTAS.md               ← tus respuestas a las preguntas del enunciado
└── README.md                   ← este fichero
```

### Paquete `ssoo.concurrencia` (Sesión 1)

Seis programas cortos ya escritos, cada uno con un problema de concurrencia:

| Clase | Problema |
|---|---|
| `ContadorCompartido` | Condición de carrera en `contador++` |
| `ComparativaContadores` | Comparativa: sin protección, `synchronized`, `AtomicInteger`, `ReentrantLock` |
| `CuentaAtomica` | Comprobar y después actuar (*check-then-act*) |
| `ListaCompartida` | `ArrayList` no es segura frente a hilos |
| `Parada` | Visibilidad entre hilos sin `volatile` |
| `Transferencias` | Interbloqueo por orden de cerrojos |
| `RegistroLento` + `Contencion` | Sección crítica demasiado grande (contención) |

### Paquete `ssoo.servidor` (Sesiones 2 y 3)

| Clase | Estado inicial |
|---|---|
| `ServidorTelemetría` | Servidor secuencial funcional — punto de partida |
| `Estadísticas` | Sin sincronizar — la protegerás en la Sesión 2, Hito 2 |

El resto de clases (`HiloRecepción`, `HiloPetición`, `ColaTrabajos`, etc.)
las crearás tú a lo largo de las sesiones.

---

## Flujo de trabajo por sesión

### Sesión 1 — Entender la concurrencia

1. Lee la Sección 3 del enunciado.
2. Para cada ejercicio: **predice**, ejecuta (varias veces), explica y corrige.
3. Responde las preguntas 1.1 a 6.2 en `RESPUESTAS.md`.
4. Sube las correcciones de los ejercicios 3, 5 y 6.
5. Etiqueta el repositorio:

```bash
git tag sesion1
git push --tags
```

### Sesión 2 — De secuencial a multihilo

1. Lee la Sección 5 del enunciado.
2. Hito 0: mide el servidor secuencial.
3. Hito 1: crea `HiloRecepción` y `HiloPetición`.
4. Hito 2: protege `Estadísticas` y demuéstralo con `PruebaEstadísticas`.
5. Hito 3: observa con JFR y JMC.
6. Responde S2.1 a S2.5 en `RESPUESTAS.md`.
7. Etiqueta:

```bash
git tag sesion2
git push --tags
```

### Sesión 3 — Productores y consumidores

1. Lee la Sección 6 del enunciado.
2. Hito 1: crea `ColaTrabajos`, `Trabajo` y `HiloAnalizador`.
3. Hito 2: implementa la espera pasiva (`Pendientes` o `CountDownLatch`).
4. Hito 3: diagnostica el sistema completo con volcados de hilos.
5. Responde S3.1 a S3.6 en `RESPUESTAS.md`.
6. Etiqueta:

```bash
git tag sesion3
git push --tags
```

---

## Entrega

> **Fecha límite: viernes 27 de noviembre a las 23:55**

La entrega es **obligatoriamente en Moodle**. Lo que haya en el repositorio git
no cuenta como entrega por sí solo.

1. Repasa la lista de comprobación de la Sección 7.1 del enunciado.
2. Asegúrate de que el repositorio remoto está actualizado con las tres etiquetas.
3. Comprime la carpeta del proyecto en un `.zip` **incluyendo** la carpeta `.git`
   pero **sin** `lib/`, `bin/` ni ficheros `.jfr`:

```bash
# Desde el directorio padre de p2-telemetria:
zip -r p2-telemetria.zip p2-telemetria \
    --exclude "p2-telemetria/lib/*.jar" \
    --exclude "p2-telemetria/bin/*" \
    --exclude "*.jfr"
```

4. Sube ese `.zip` a la tarea de Moodle y confirma que aparece como enviado.

---

## Normas de estilo de código

- **Nombres:** `UpperCamelCase` para clases, `lowerCamelCase` para métodos y variables,
  `MAYÚSCULAS_CON_GUIONES` para constantes.
- **Indentación:** consistente en todo el proyecto.
- **Comentarios de sincronización:** cada `synchronized`, `volatile`, cerrojo o cola
  debe llevar un comentario que indique **qué protege** y **frente a qué hilos**.
  Ejemplo:
  ```java
  // Protege los tres contadores frente a accesos concurrentes de varios HiloPetición.
  public synchronized void registrarPetición() { ... }
  ```
- **Nombres de hilos:** usa siempre `super("NombreDescriptivo")` o
  `new Thread(r, "NombreDescriptivo")`. Un hilo sin nombre aparece en los volcados
  como `Thread-17`.

---

## Actividad complementaria voluntaria

El servidor de eco en mayúsculas de la Práctica 1, ahora con hilos (Java).
Se trabaja en un repositorio separado, `p2-servidor`, y tiene su propia tarea en Moodle.
Fecha límite: **domingo 29 de noviembre a las 23:55**.
Consulta la Sección 8 del enunciado.
