package src.cyberassembly.tasks;

import src.cyberassembly.model.Cinta;
import src.cyberassembly.model.Soldadura;
import src.cyberassembly.model.Supervisor;

public class RobotTask implements Runnable {
    private final int robotId;
    private final Cinta cinta;
    private final Soldadura soldadura;
    private final Supervisor supervisor;
    private int cotxesCompletats = 0;

    public RobotTask(int robotId, Cinta cinta, Soldadura soldadura, Supervisor supervisor) {
        this.robotId = robotId;
        this.cinta = cinta;
        this.soldadura = soldadura;
        this.supervisor = supervisor;
    }

    @Override
    public void run() {
        
            try {
                while (!Thread.currentThread().isInterrupted()){
                    // 1. Verificación de alerta
                    supervisor.verificaEstatAlerta(robotId);

                    // 2. Retirar batería del búfer
                    String bateria = cinta.retirarBateria(robotId);

                    // 3. Solicitar estación de soldadura (Semaphore)
                    int estacioId = soldadura.sollicitarEstacio(robotId);
                    
                    // 4. Trabajo de soldadura
                    System.out.println("[Robot #" + robotId + "] Ensamblando vehículo con " + bateria + " en Estación #" + estacioId);
                    Thread.sleep(1000);

                    // 5. Firma en el libro de registro central (Mutex)
                    String vin = "CYBER-VIN-" + robotId + "-" + (++cotxesCompletats);
                    soldadura.signarRegistreCalitat(robotId, vin);

                    // 6. Liberación de la estación
                    soldadura.alliberarEstacio(estacioId, robotId);

                    Thread.sleep(400); // Descanso entre ciclos
                }
            } catch (InterruptedException e) {
                System.out.println("[Robot #" + robotId + "] Finalizado correctamente.");
                Thread.currentThread().interrupt();
            }
            

        

        /*synchronized verificaEsta(robotId){
            if (activaAlerta) {
                wait();
            }
        }*/
    }

}