package ssoo.servidor;

import ssoo.telemetría.Analizador;

public class HiloAnalizador implements Runnable{

    private final Analizador a;
    private final ColaDeTrabajos colita;

    public HiloAnalizador(ColaDeTrabajos cola){
        a = new Analizador();
        colita = cola;
    }

    @Override
    public void run() {
        try {
            while(true){
                Trabajo t = colita.extraerTrabajo();
                t.setTelemetríaAnalizada(a.analizar(t.getTelemetriaOriginal()));
            }
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
    }
    
}
