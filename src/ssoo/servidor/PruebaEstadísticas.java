package ssoo.servidor;

public class PruebaEstadísticas {
    public static void main(String[] args) throws Exception {
        Estadísticas estadísticas = new Estadísticas();

        Thread[] hilos = new Thread[8];

        for (int i = 0; i < 8; i++) {
            hilos[i] = new Thread(new HiloPruebasEstadísticas(estadísticas));
            hilos[i].setName("Hilo Estadisticas- " + i);
            hilos[i].start();
        }

        // Esperar a que terminen todos los hilos
        for (int i = 0; i < 8; i++) {
            hilos[i].join();
        }

        estadísticas.mostrar();
    }
}