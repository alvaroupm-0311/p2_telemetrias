package ssoo.servidor;

public class HiloPruebasEstadísticas implements Runnable {

    private final Estadísticas e;

    public HiloPruebasEstadísticas(Estadísticas estadísticas){
        e = estadísticas;
    }

    @Override
    public void run() {
        for(int i=0; i<100000;i++){

            e.registrarTelemetría();
            e.registrarPetición();
            e.registrarInforme();
        }

    }
    
}
