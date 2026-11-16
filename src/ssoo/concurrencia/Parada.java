package ssoo.concurrencia;

/**
 * Ejercicio 4: visibilidad entre hilos.
 *
 * El hilo main pide al hilo Trabajador que se detenga cambiando una
 * variable compartida. Es muy probable que el programa no termine nunca,
 * aunque main ponga {@code parar} a true.
 *
 * Prueba después con {@code volatile boolean parar} y observa la diferencia.
 *
 * Si en tu equipo termina siempre, aumenta la espera de main a 3 segundos.
 *
 * @author Sistemas Operativos (DTE - ETSIST - UPM)
 */
public class Parada {

    private static boolean parar = false; // prueba después con volatile

    public static void main(String[] args) throws InterruptedException {
        Thread trabajador = new Thread(() -> {
            long vueltas = 0;
            while (!parar) {
                vueltas++;
            }
            System.out.println("Detenido tras " + vueltas + " vueltas");
        }, "Trabajador");

        trabajador.start();
        Thread.sleep(1000);
        parar = true;
        System.out.println("main: parar = true");
        trabajador.join();
    }
}
