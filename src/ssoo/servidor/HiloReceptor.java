package ssoo.servidor;

import java.io.IOException;

import ssoo.telemetría.estación.Petición;
import ssoo.telemetría.estación.Receptor;

public class HiloReceptor implements Runnable {

    private final Receptor r;
    private final Estadísticas e;
    private final ColaDeTrabajos colita;

    public HiloReceptor(Receptor receptor, Estadísticas estadísticas, ColaDeTrabajos cola){
        Thread.currentThread().setName("Receptor");
        r = receptor;
        e = estadísticas;
        colita = cola;
    }

	@Override
	public void run() {
        try {
            while(true){
                Petición petición = r.recibirPetición();
                Thread hilo_peticion = new HiloPeticion(petición, e, colita);
                hilo_peticion.start();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } // espera a una estación           
	}
    
}
