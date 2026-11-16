package ssoo.concurrencia;

import java.util.ArrayList;
import java.util.List;

/**
 * Ejercicio 6: banco de pruebas de contención.
 *
 * Lanza {@value #NUM_HILOS} hilos que registran {@value #MENSAJES_POR_HILO}
 * mensajes cada uno en un {@link RegistroLento} compartido y muestra el
 * número de entradas y el tiempo total.
 *
 * Graba con JFR para analizar la contención en JMC:
 *
 * <pre>
 * java -XX:StartFlightRecording=filename=antes.jfr,dumponexit=true,settings=profile \
 *      -cp bin ssoo.concurrencia.Contencion
 * </pre>
 *
 * @author Sistemas Operativos (DTE - ETSIST - UPM)
 */
public class Contencion {

    private static final int NUM_HILOS        = 8;
    private static final int MENSAJES_POR_HILO = 25;

    public static void main(String[] args) throws InterruptedException {
        RegistroLento registro = new RegistroLento();
        List<Thread> hilos = new ArrayList<>(NUM_HILOS);

        for (int i = 0; i < NUM_HILOS; i++) {
            final int id = i;
            hilos.add(new Thread(() -> {
                for (int j = 0; j < MENSAJES_POR_HILO; j++) {
                    registro.registrar("Hilo-" + id + " mensaje-" + j);
                }
            }, "Hilo-" + i));
        }

        long inicio = System.currentTimeMillis();
        hilos.forEach(Thread::start);
        for (Thread h : hilos) h.join();
        long tiempo = System.currentTimeMillis() - inicio;

        System.out.println("Entradas registradas: " + registro.tamaño()
                + " (esperado: " + NUM_HILOS * MENSAJES_POR_HILO + ")");
        System.out.println("Tiempo total:         " + tiempo + " ms");
    }
}
