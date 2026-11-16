package ssoo.concurrencia;

/**
 * Ejercicio 1: el contador compartido.
 *
 * Dos hilos incrementan un millón de veces cada uno la misma variable
 * compartida. Predice qué valor se imprimirá y ejecútalo cinco veces.
 *
 * Después cambia ITERACIONES a 1000 y repite.
 *
 * @author Sistemas Operativos (DTE - ETSIST - UPM)
 */
public class ContadorCompartido {

    private static final int ITERACIONES = 1_000_000;
    private static int contador = 0;

    public static void main(String[] args) throws InterruptedException {
        Runnable tarea = () -> {
            for (int i = 0; i < ITERACIONES; i++) {
                contador++; // ¿es una operación atómica?
            }
        };
        Thread h1 = new Thread(tarea, "Hilo-1");
        Thread h2 = new Thread(tarea, "Hilo-2");
        h1.start();
        h2.start();
        h1.join(); // espera a que termine Hilo-1
        h2.join(); // espera a que termine Hilo-2
        System.out.println("Esperado: " + 2 * ITERACIONES);
        System.out.println("Obtenido: " + contador);
    }
}
