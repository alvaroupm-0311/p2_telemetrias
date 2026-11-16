package ssoo.concurrencia;

import java.util.ArrayList;
import java.util.List;

/**
 * Ejercicio 6: una sección crítica demasiado grande.
 *
 * El método {@code registrar} está protegido correctamente, pero incluye
 * dentro de la sección crítica un trabajo lento ({@code formatear}) que
 * no accede a nada compartido.
 *
 * Observa la contención con JFR/JMC y después reduce la sección crítica
 * a lo imprescindible (solo {@code entradas.add}) — Pregunta 6.2.
 *
 * @author Sistemas Operativos (DTE - ETSIST - UPM)
 */
public class RegistroLento {

    private final List<String> entradas = new ArrayList<>();

    public synchronized void registrar(String texto) {
        String linea = formatear(texto); // lento: no toca nada compartido
        entradas.add(linea);             // lo único que de verdad se comparte
    }

    public synchronized int tamaño() {
        return entradas.size();
    }

    private static String formatear(String texto) {
        try {
            Thread.sleep(20); // simula un formateo o una E/S lenta
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return "[" + System.nanoTime() + "] " + texto;
    }
}
