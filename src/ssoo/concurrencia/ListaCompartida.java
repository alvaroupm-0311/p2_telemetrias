package ssoo.concurrencia;

import java.util.ArrayList;
import java.util.List;

/**
 * Ejercicio 3b: colecciones no seguras frente a hilos.
 *
 * Dos hilos añaden 10 000 elementos cada uno al mismo ArrayList.
 * Observa el tamaño final y las excepciones que aparecen.
 *
 * Después corrígelo con Collections.synchronizedList (Pregunta 3.3).
 *
 * @author Sistemas Operativos (DTE - ETSIST - UPM)
 */
public class ListaCompartida {

    private static final int ELEMENTOS = 10_000;

    public static void main(String[] args) throws InterruptedException {
        List<Integer> lista = new ArrayList<>(); // ¡no es segura frente a hilos!

        Runnable tarea = () -> {
            for (int i = 0; i < ELEMENTOS; i++) {
                lista.add(i);
            }
        };

        Thread h1 = new Thread(tarea, "Hilo-1");
        Thread h2 = new Thread(tarea, "Hilo-2");
        h1.start();
        h2.start();
        h1.join();
        h2.join();

        System.out.println("Esperado: " + 2 * ELEMENTOS);
        System.out.println("Obtenido: " + lista.size());
    }
}
