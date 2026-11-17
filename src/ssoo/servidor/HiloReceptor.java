package ssoo.servidor;

import java.io.IOException;
import java.util.concurrent.Semaphore;

import ssoo.telemetría.estación.Petición;
import ssoo.telemetría.estación.Receptor;

public class HiloReceptor implements Runnable {

    private final Receptor r;
    private final Estadísticas e;
    private final ColaDeTrabajos colita;
    private final Semaphore s;
    private volatile boolean activo = true;

    public HiloReceptor(Receptor receptor, Estadísticas estadísticas,
         ColaDeTrabajos cola, Semaphore semaforo){
        Thread.currentThread().setName("Receptor");
        r = receptor;
        e = estadísticas;
        colita = cola;
        s = semaforo;
    }
    //Como esta activo, usar metodo para detener
    public void detener() {
        activo = false;
    }

	@Override
	public void run() {
        try {
            while(activo){
                Petición petición = r.recibirPetición();
                if(!activo)
                    return ;
                s.acquire(); //Reduce en 1 el numero del semaforo
                if (!activo) {
                     s.release();
                     break;
                }
                Thread hilo_peticion = new HiloPeticion(petición, e, colita, s);
                hilo_peticion.start();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } // espera a una estación           
            catch (InterruptedException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
	}
    
}
