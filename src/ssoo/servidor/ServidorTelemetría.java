package ssoo.servidor;

import java.util.concurrent.Semaphore;

import ssoo.telemetría.estación.Receptor;
import ssoo.telemetría.panel.PanelVisualizador;

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

        Thread hilo_receptor = new Thread(new HiloReceptor(receptor, estadísticas,cola, semaforo));
        hilo_receptor.start();

        //Pool de hilos en base a la cantidad de proccesos de mi CPU
        final int max = Runtime.getRuntime().availableProcessors();
        Thread[] hilos = new Thread[max];

        for (int i = 0; i < max; i++) {
            hilos[i] = new Thread(new HiloAnalizador(cola, estadísticas));
            hilos[i].setName("Hilo Analizador- " + i);
            hilos[i].start();
        }

    }
}
