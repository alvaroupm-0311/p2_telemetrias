package ssoo.servidor;

import ssoo.telemetría.Analizador;

public class HiloAnalizador implements Runnable{

    private final Analizador a;
    private final ColaDeTrabajos colita;
    private final Estadísticas e;

    public HiloAnalizador(ColaDeTrabajos cola, Estadísticas estadísticas){
        a = new Analizador();
        colita = cola;
        e = estadísticas;
    }

    @Override
    public void run() {
        try {
            while(true){
                Trabajo t = colita.extraerTrabajo();
                e.registrarTelemetría();
                t.setTelemetríaAnalizada(a.analizar(t.getTelemetriaOriginal()));
            }
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
    }
    
}
