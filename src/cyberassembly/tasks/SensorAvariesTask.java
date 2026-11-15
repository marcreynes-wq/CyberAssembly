package src.cyberassembly.tasks;


import java.util.Random;

import src.cyberassembly.model.Soldadura;

public class SensorAvariesTask implements Runnable {
    private final Soldadura soldadura;
    private final Random random = new Random();

    public SensorAvariesTask(Soldadura soldadura) {
        this.soldadura = soldadura;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Thread.sleep(3500);
                int estacio = random.nextInt(3);
                if (soldadura.marcarAvaria(estacio)) {
                    System.out.println("[SENSOR DEMONIO 😈] Avería generada en Estación #" + estacio);
                    Thread.sleep(2000); // Simulación de reparación
                    soldadura.repararAvaria(estacio);
                    System.out.println("[TÉCNICO MANTENIMIENTO 🛠️] Estación #" + estacio + " reparada.");
                }
            }
        } catch (InterruptedException e) {
            System.out.println("[SENSOR DEMONIO] Hilo demonio detenido.");
        }
    }
}