package src.cyberassembly.model;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Cinta {
    private final Queue<String> bateriaQueue = new LinkedList<>();
    private final int capacitatMaxima;
    
    //Bloqueo explíxito y variables de condición
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition cintaNoPlena =  lock.newCondition();
    private final Condition cintaNoBuida = lock.newCondition();

    public Cinta(int capacitatMaxima) {
        this.capacitatMaxima = capacitatMaxima;
    }

    //Invocado por los AGVs (productores) para depositar una batería.
    public void afegirBateria(String bateriaId, int agvId) throws InterruptedException {
        lock.lock();
        try {
            while (bateriaQueue.size() == capacitatMaxima) {
                System.out.println("[AGV #" + agvId + "] Cinta llena. Esperando vaciado...");
                cintaNoPlena.await(); // Pasa a estado de espera si la cinta está llena
            }
            bateriaQueue.add(bateriaId);
            System.out.println("[AGV #" + agvId + "] Batería '" + bateriaId + "' depositada. Capacidad: " + bateriaQueue.size() + "/" + capacitatMaxima);

            cintaNoBuida.signal(); // Despierta a un robot consumidor esperándolo
        } finally {
            lock.unlock(); // Asegura la liberación del lock en cualquier escenario
        }
    }

    /**
     * Invocado por los Robots (Consumidores) para retirar una batería.
     */
    public String retirarBateria(int robotId) throws InterruptedException {
        lock.lock();
        try {
            while (bateriaQueue.isEmpty()) {
                System.out.println("[Robot #" + robotId + "] Cinta vacía. Esperando baterías...");
                cintaNoBuida.await(); // Pasa a estado de espera si la cinta está vacía
            }
            String bateria = bateriaQueue.poll();
            System.out.println("[Robot #" + robotId + "] Batería '" + bateria + "' retirada. Capacidad: " + bateriaQueue.size() + "/" + capacitatMaxima);

            cintaNoPlena.signal(); // Despierta a un AGV productor esperándolo
            return bateria;
        } finally {
            lock.unlock();
        }
    }

    public int getOcupacioActual() {
        lock.lock();
        try {
            return bateriaQueue.size();
        } finally {
            lock.unlock();
        }
    }
}
