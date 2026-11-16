package ssoo.concurrencia;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Ejercicio 2: tres formas de exclusión mutua.
 *
 * Ejecuta con 2, 4 y 8 hilos (se pasa como argumento) y completa
 * la tabla de RESPUESTAS.md con el tiempo y el valor final de cada versión.
 *
 * Uso: java ssoo.concurrencia.ComparativaContadores <numHilos>
 *
 * @author Sistemas Operativos (DTE - ETSIST - UPM)
 */
public class ComparativaContadores {

    private static final int TOTAL_INCREMENTOS = 4_000_000;

    // Interfaz común para los cuatro contadores.
    interface Contador {
        void incrementar();
        int valor();
    }

    // Versión 0: sin protección (incorrecta).
    static class ContadorInseguro implements Contador {
        private int valor = 0;
        public void incrementar() { valor++; }
        public int valor()        { return valor; }
    }

    // a) Monitor de Java: synchronized.
    static class ContadorSynchronized implements Contador {
        private int valor = 0;
        public synchronized void incrementar() { valor++; }
        public synchronized int valor()        { return valor; }
    }

    // b) Variable atómica (java.util.concurrent.atomic).
    static class ContadorAtomico implements Contador {
        private final AtomicInteger valor = new AtomicInteger();
        public void incrementar() { valor.incrementAndGet(); }
        public int valor()        { return valor.get(); }
    }

    // c) Cerrojo explícito (java.util.concurrent.locks).
    static class ContadorCerrojo implements Contador {
        private final ReentrantLock cerrojo = new ReentrantLock();
        private int valor = 0;
        public void incrementar() {
            cerrojo.lock();
            try {
                valor++;
            } finally {
                cerrojo.unlock(); // se libera incluso si hay excepción
            }
        }
        public int valor() {
            cerrojo.lock();
            try {
                return valor;
            } finally {
                cerrojo.unlock();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        if (args.length != 1) {
            System.err.println("Uso: java ssoo.concurrencia.ComparativaContadores <numHilos>");
            return;
        }
        int numHilos  = Integer.parseInt(args[0]);
        int porHilo   = TOTAL_INCREMENTOS / numHilos;
        int esperado  = numHilos * porHilo;

        System.out.printf("Hilos: %d   Esperado: %d%n%n", numHilos, esperado);
        System.out.printf("%-20s  %10s  %12s  %s%n", "Versión", "Tiempo(ms)", "Valor final", "¿Correcto?");
        System.out.println("-".repeat(60));

        medir("Sin protección",  new ContadorInseguro(),    numHilos, porHilo, esperado);
        medir("synchronized",    new ContadorSynchronized(), numHilos, porHilo, esperado);
        medir("AtomicInteger",   new ContadorAtomico(),      numHilos, porHilo, esperado);
        medir("ReentrantLock",   new ContadorCerrojo(),      numHilos, porHilo, esperado);
    }

    private static void medir(String nombre, Contador contador,
                               int numHilos, int porHilo, int esperado)
            throws InterruptedException {
        List<Thread> hilos = new ArrayList<>(numHilos);
        for (int i = 0; i < numHilos; i++) {
            hilos.add(new Thread(() -> {
                for (int j = 0; j < porHilo; j++) contador.incrementar();
            }, nombre + "-" + i));
        }
        long inicio = System.currentTimeMillis();
        hilos.forEach(Thread::start);
        for (Thread h : hilos) h.join();
        long ms    = System.currentTimeMillis() - inicio;
        int  valor = contador.valor();
        System.out.printf("%-20s  %10d  %12d  %s%n",
                nombre, ms, valor, valor == esperado ? "OK" : "ERROR");
    }
}
