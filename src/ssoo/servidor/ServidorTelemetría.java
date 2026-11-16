package ssoo.servidor;

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
        Thread hilo_receptor = new Thread(new HiloReceptor(receptor, estadísticas,cola));
        hilo_receptor.start();

        Thread[] hilos = new Thread[8];

        for (int i = 0; i < 8; i++) {
            hilos[i] = new Thread(new HiloAnalizador(cola));
            hilos[i].setName("Hilo Analizador- " + i);
            hilos[i].start();
        }

    }
}
