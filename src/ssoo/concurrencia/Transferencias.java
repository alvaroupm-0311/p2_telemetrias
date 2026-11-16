package ssoo.concurrencia;

/**
 * Ejercicio 5: interbloqueo.
 *
 * Dos hilos transfieren dinero entre las mismas dos cuentas en sentidos
 * opuestos. Cada transferencia bloquea primero la cuenta de origen y después
 * la de destino.
 *
 * Predice: ¿terminará el programa? Si no termina, ¿qué estará esperando
 * cada hilo? Obtén el volcado de hilos con jstack cuando se detenga.
 *
 * Después corrige {@code transferir} para que todos los hilos bloqueen
 * las cuentas en el mismo orden (Pregunta 5.2).
 *
 * @author Sistemas Operativos (DTE - ETSIST - UPM)
 */
public class Transferencias {

    private static final int VECES = 100_000;

    static class Cuenta {
        final int id;
        int saldo;

        Cuenta(int id, int saldo) {
            this.id    = id;
            this.saldo = saldo;
        }
    }

    static void transferir(Cuenta origen, Cuenta destino, int cantidad) {
        synchronized (origen) {
            synchronized (destino) {
                origen.saldo  -= cantidad;
                destino.saldo += cantidad;
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Cuenta a = new Cuenta(1, 1000);
        Cuenta b = new Cuenta(2, 1000);

        Thread hiloA = new Thread(() -> {
            for (int i = 0; i < VECES; i++) {
                transferir(a, b, 1);
            }
        }, "Hilo-A");

        Thread hiloB = new Thread(() -> {
            for (int i = 0; i < VECES; i++) {
                transferir(b, a, 1);
            }
        }, "Hilo-B");

        hiloA.start();
        hiloB.start();
        hiloA.join();
        hiloB.join();

        System.out.println("Saldo A: " + a.saldo + "   Saldo B: " + b.saldo);
        System.out.println("Total:   " + (a.saldo + b.saldo) + "  (esperado: 2000)");
    }
}
