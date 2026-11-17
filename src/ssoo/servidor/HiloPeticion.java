package ssoo.servidor;

import java.util.ArrayList;
import java.util.List;

import ssoo.telemetría.Encargo;
import ssoo.telemetría.Informe;
import ssoo.telemetría.Telemetría;
import ssoo.telemetría.Índice;
import ssoo.telemetría.estación.Petición;

public class HiloPeticion extends Thread{

    private final Petición p;
    private final Estadísticas e;
    private final ColaDeTrabajos colilla;

    public HiloPeticion(Petición petición, Estadísticas estadísticas, 
        ColaDeTrabajos colita){
        super("Petición-" + petición.getEncargo().getTítulo()); // nombre en los volcados
        p = petición;
        e = estadísticas;
        colilla = colita;
    }

    @Override
	public void run() {
        try {

        e.registrarPetición();
        Encargo encargo = p.getEncargo();

        List<Trabajo> trabajos = new ArrayList<>();
        Trabajo trabajo;
        for (Telemetría t : encargo.getTelemetrías()) {
            trabajo = new Trabajo(t);
            trabajos.add(trabajo);
            colilla.encolarTrabajo(trabajo);
        }
        List<Telemetría> analizadas = new ArrayList<>();
        for (Trabajo t : trabajos){
            analizadas.add(t.getTelemetríaAnalizada());
        }
            
        Informe informe = new Informe("informe-" + encargo.getTítulo(),
                                new Índice(analizadas), analizadas);
        p.getEstación().enviar(informe); // método correcto: enviar()
        e.registrarInforme();
        e.mostrar();

        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }


	}
}
