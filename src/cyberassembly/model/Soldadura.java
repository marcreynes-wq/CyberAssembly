package src.cyberassembly.model;

import java.util.concurrent.Semaphore;

public class Soldadura {
    private final int numEstacions;
    private final boolean[] estacionsOcupades;
    private final boolean[] estacionsAvariades; // Soporte para Opción Extra B

    // 1. Semáforo contador para gestionar el aforo a las N estaciones
    private final Semaphore semaforEstacions;

    // 2. Semáforo binario / Mutex para proteger la firma del registro exclusivo
    private final Semaphore mutexRegistre;

    public Soldadura(int numEstacions) {
        this.numEstacions = numEstacions;
        this.estacionsOcupades = new boolean[numEstacions];
        this.estacionsAvariades = new boolean[numEstacions];
        this.semaforEstacions = new Semaphore(numEstacions, true); // Políticas de equidad (fairness)
        this.mutexRegistre = new Semaphore(1);                    // Mutex exclusivo
    }

    /**
     * Solicita una estación de soldadura libre.
     */
    public int sollicitarEstacio(int robotId) throws InterruptedException {
        semaforEstacions.acquire(); // Decrementa permisos o bloquea si no hay plazas

        int estacioAssignada = -1;
        // Búsqueda en exclusión mutua para evitar condiciones de carrera al elegir casilla del array
        synchronized (this) {
            for (int i = 0; i < numEstacions; i++) {
                if (!estacionsOcupades[i] && !estacionsAvariades[i]) {
                    estacionsOcupades[i] = true;
                    estacioAssignada = i;
                    break;
                }
            }
        }
        System.out.println("[Robot #" + robotId + "] Ocupa Estación de Soldadura #" + estacioAssignada);
        return estacioAssignada;
    }

    /**
     * Libera la estación de soldadura asignada.
     */
    public void alliberarEstacio(int estacioId, int robotId) {
        synchronized (this) {
            estacionsOcupades[estacioId] = false;
        }
        System.out.println("[Robot #" + robotId + "] Libera Estación de Soldadura #" + estacioId);
        semaforEstacions.release(); // Incrementa permisos del semáforo contador
    }

    /**
     * Firma el libro de registro central en estricta exclusión mutua.
     */
    public void signarRegistreCalitat(int robotId, String vinCotxe) throws InterruptedException {
        mutexRegistre.acquire(); // Adquiere el Mutex exclusivo
        try {
            System.out.println("[REGISTRO CENTRAL] ✍️ Robot #" + robotId + " firmando certificado de calidad VIN: " + vinCotxe);
            Thread.sleep(250); // Simula el tiempo de firma
            System.out.println("[REGISTRO CENTRAL] ✅ Firma registrada correctamente para VIN: " + vinCotxe);
        } finally {
            mutexRegistre.release(); // Garantiza la liberación del Mutex
        }
    }

    // Métodos auxiliares para la opción de mantenimiento (Opción B)
    public synchronized boolean marcarAvaria(int estacioId) {
        if (!estacionsAvariades[estacioId] && !estacionsOcupades[estacioId]) {
            estacionsAvariades[estacioId] = true;
            return true;
        }
        return false;
    }

    public synchronized void repararAvaria(int estacioId) {
        estacionsAvariades[estacioId] = false;
    }

    public synchronized int getEstacionsOcupadesCount() {
        int count = 0;
        for (boolean b : estacionsOcupades) if (b) count++;
        return count;
    }
}
