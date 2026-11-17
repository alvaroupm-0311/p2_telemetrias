package ssoo.servidor;

import java.util.concurrent.Semaphore;

import ssoo.telemetría.estación.Receptor;
import ssoo.telemetría.panel.PanelVisualizador;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Servidor de telemetría — versión secuencial de partida.
 *
 * Atiende una petición detrás de otra en el hilo main.
 * Mientras se atiende una estación, la siguiente espera en el Receptor.
 *
 * Punto de partida para las Sesiones 2 y 3:
 *   - Sesión 2, Hito 1: divide esto en ServidorTelemetría + HiloRecepción + HiloPetición.
 *   - Sesión 2, Hito 2: protege Estadísticas frente a varios HiloPetición.
 *   - Sesión 3:         añade ColaTrabajos, Trabajo y HiloAnalizador.
 *
 * @author Sistemas Operativos (DTE - ETSIST - UPM)
 */
public class ServidorTelemetría {

    public static void main(String[] args) throws Exception {
        Receptor     receptor     = new Receptor();
        Estadísticas estadísticas = new Estadísticas();
        ColaDeTrabajos cola = new ColaDeTrabajos(20);
        PanelVisualizador.getPanel().registrarColaTrabajos(cola);

        //Semaforo
        Semaphore semaforo = new Semaphore(20);

        //Guardo referencia del hilo Receptor
        HiloReceptor hR = new HiloReceptor(receptor, estadísticas,cola, semaforo);

        Thread hilo_receptor = new Thread(hR);
        hilo_receptor.start();

        //Pool de hilos en base a la cantidad de proccesos de mi CPU
        final int max = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(max);

        for (int i = 0; i < max; i++) {
            executor.execute(new HiloAnalizador(cola, estadísticas));
        }


        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                System.out.println("Iniciando cierre ordenado...");

                // 1. Impedir nuevas peticiones
                hR.detener();
                hilo_receptor.interrupt();

                // 2. Esperar a que terminen todas las peticiones
                semaforo.acquire(20);

                // Enviar una píldora venenosa por analizador
                for (int i = 0; i < max; i++) {
                    cola.encolarTrabajo(new Trabajo());
                }

                // Indicar al executor que debe cerrarse
                executor.shutdown();

                // Esperar a que terminen todos los analizadores
                executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

                // 5. Mostrar estadísticas finales
                System.out.println("Servidor detenido.");
                estadísticas.mostrar();

            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }));

    }
}
