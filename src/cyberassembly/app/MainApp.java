package src.cyberassembly.app;

import src.cyberassembly.model.Soldadura;
import src.cyberassembly.model.Cinta;
import src.cyberassembly.model.Supervisor;
import src.cyberassembly.tasks.AGVTask;
import src.cyberassembly.tasks.RobotTask;
import src.cyberassembly.tasks.SensorAvariesTask;
import src.cyberassembly.view.DashboardTask;

import java.util.ArrayList;
import java.util.List;

public class MainApp {
    public static void main(String[] args) {
        System.out.println("===  INICIANDO SIMULACIÓN CYBERASSEMBLY ===");

        // 1. Crear recursos compartidos (Paquete model)
        Supervisor supervisor = new Supervisor();
        Cinta cinta = new Cinta(5);
        Soldadura soldadura = new Soldadura(3);

        List<Thread> hilosActivos = new ArrayList<>();

        // 2. Iniciar hilos AGV (Productores)
        for (int i = 1; i <= 2; i++) {
            Thread tAGV = new Thread(new AGVTask(i, cinta, supervisor), "AGV-" + i);
            hilosActivos.add(tAGV);
            tAGV.start();
        }

        // 3. Iniciar hilos Robot (Consumidores)
        for (int i = 1; i <= 4; i++) {
            Thread tRobot = new Thread(new RobotTask(i, cinta, soldadura, supervisor), "Robot-" + i);
            hilosActivos.add(tRobot);
            tRobot.start();
        }

        // 4. Iniciar Dashboard
        Thread tDashboard = new Thread(new DashboardTask(cinta, soldadura), "Dashboard");
        hilosActivos.add(tDashboard);
        tDashboard.start();

        // 5. Iniciar Hilo Demonio (Opción Extra B)
        Thread tSensor = new Thread(new SensorAvariesTask(soldadura), "SensorAvaries");
        tSensor.setDaemon(true); // setDaemon(true) para cierre automático
        tSensor.start();

        // 6. Simular flujo de trabajo y prueba de parada de emergencia
        try {
            Thread.sleep(5000);
            supervisor.activarAlerta();

            Thread.sleep(3000);
            supervisor.desactivarAlerta();

            Thread.sleep(5000);
            System.out.println("\n===  INICIANDO TANCAMENT NET (CIERRE DE HILOS) ===");

            // Interrupción de hilos principales
            for (Thread t : hilosActivos) {
                t.interrupt();
            }

            // Sincronización de finalización con join()
            for (Thread t : hilosActivos) {
                t.join();
            }

        } catch (InterruptedException e) {
            System.err.println("Error en hilo principal: " + e.getMessage());
        }

        System.out.println("===  SIMULACIÓN FINALIZADA CON ÉXITO ===");
    }
}
