package ssoo.concurrencia;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Ejercicio 3a: comprobar y después actuar.
 *
 * Diez hilos intentan retirar 100 € de una cuenta con 100 €.
 * Aunque saldo es un AtomicInteger, el saldo puede quedar negativo.
 * El experimento se repite 200 veces.
 *
 * Predice: ¿cuántas retiradas deberían tener éxito en cada ronda?
 * ¿En cuántas rondas crees que el saldo quedará negativo?
 *
 * @author Sistemas Operativos (DTE - ETSIST - UPM)
 */
public class CuentaAtomica {

    private final AtomicInteger saldo = new AtomicInteger(100);

    public boolean retirar(int cantidad) {
        if (saldo.get() >= cantidad) { // 1. comprobar
            Thread.yield();            // (amplía la ventana de la carrera)
            saldo.addAndGet(-cantidad); // 2. actuar
            return true;
        }
        return false;
    }

    public int getSaldo() {
        return saldo.get();
    }

    public static void main(String[] args) throws InterruptedException {
        final int RONDAS   = 200;
        final int CLIENTES = 10;
        final int CANTIDAD = 100;

        int rondasNegativas     = 0;
        int saldoMínimoObservado = 0;

        for (int ronda = 0; ronda < RONDAS; ronda++) {
            CuentaAtomica cuenta = new CuentaAtomica();
            CountDownLatch listos = new CountDownLatch(1);
            List<Thread> clientes = new ArrayList<>(CLIENTES);

            for (int i = 0; i < CLIENTES; i++) {
                clientes.add(new Thread(() -> {
                    try {
                        listos.await(); // esperar a que todos estén listos
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    cuenta.retirar(CANTIDAD);
                }, "Cliente-" + i));
            }
            clientes.forEach(Thread::start);
            listos.countDown(); // suelta a todos a la vez
            for (Thread c : clientes) c.join();

            int saldoFinal = cuenta.getSaldo();
            if (saldoFinal < 0) {
                rondasNegativas++;
                saldoMínimoObservado = Math.min(saldoMínimoObservado, saldoFinal);
            }
        }
        System.out.println("Rondas con saldo negativo: " + rondasNegativas + " / " + RONDAS);
        System.out.println("Saldo mínimo observado:    " + saldoMínimoObservado);
    }
}
