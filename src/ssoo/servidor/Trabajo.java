package ssoo.servidor;

import ssoo.telemetría.Telemetría;

public class Trabajo {

    private final Telemetría t_original;
    private Telemetría t_analizada;

    public Trabajo (Telemetría telemetría){

        t_original = telemetría;
        t_analizada = null;

    }

    //Obtener para hilo Analizador la telemetría original
    public Telemetría getTelemetriaOriginal(){
        return t_original;
    }
    // Analizador introduce la telemetria analizada
    public synchronized void setTelemetríaAnalizada(Telemetría t){
        t_analizada = t;
        notifyAll();
    }
    // Peticion obtine la telemetria analizada
    public synchronized Telemetría getTelemetríaAnalizada() throws InterruptedException{
        while(t_analizada == null){
            this.wait();
        }
        return t_analizada;
    }
}
