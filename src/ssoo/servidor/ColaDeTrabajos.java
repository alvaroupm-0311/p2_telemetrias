package ssoo.servidor;

import java.util.concurrent.ArrayBlockingQueue;

import ssoo.telemetría.Numerable;

public class ColaDeTrabajos extends ArrayBlockingQueue<Trabajo> implements Numerable{

    public ColaDeTrabajos(int capacity) {
        super(capacity);
    }
    
    //Meto trabajos en la cola
    public void encolarTrabajo(Trabajo t) throws InterruptedException{
        this.put(t);
    }
    //Saco trabajos de la cola
    public Trabajo extraerTrabajo() throws InterruptedException{
        return this.take();
    }


    @Override
    public int numTrabajos() {
        return this.size();
    }
    
}
