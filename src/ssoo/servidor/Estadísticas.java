package ssoo.servidor;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Lleva la cuenta de las peticiones recibidas, las telemetrías analizadas
 * y los informes enviados por el servidor.
 *
 * En la versión secuencial solo la usa un hilo, así que no necesita
 * ninguna protección… por ahora.
 *
 * En el Hito 2 de la Sesión 2 tendrás que protegerla: varios HiloPetición
 * la modificarán a la vez y sus contadores tienen la misma condición de
 * carrera que el contador del Ejercicio 1.
 *
 * @author Sistemas Operativos (DTE - ETSIST - UPM)
 */
public class Estadísticas {

    private AtomicLong peticionesRecibidas = new AtomicLong(0);
    private long telemetríasAnalizadas = 0;
    private AtomicLong informesEnviados      = new AtomicLong(0); // nunca puede superar a peticionesRecibidas


    public void registrarPetición() {
        peticionesRecibidas.getAndIncrement();
    }

    public synchronized void registrarTelemetría() {
       
        telemetríasAnalizadas ++;

       
    }

    public synchronized void registrarInforme() {

        informesEnviados.getAndIncrement();

    }

    public void mostrar() {
        System.out.printf("[Estadísticas] peticiones=%d  telemetrías=%d  informes=%d%n",
                peticionesRecibidas.get(), telemetríasAnalizadas, informesEnviados.get());
    }
}
