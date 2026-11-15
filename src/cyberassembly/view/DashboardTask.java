package src.cyberassembly.view;
import src.cyberassembly.model.Cinta;
import src.cyberassembly.model.Soldadura;

public class DashboardTask implements Runnable {
    private final Cinta cinta;
    private final Soldadura soldadura;

    public DashboardTask(Cinta cinta, Soldadura soldadura) {
        this.cinta = cinta;
        this.soldadura = soldadura;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Thread.sleep(2000);
                System.out.println("\n=========  ESTADO DE LA FÁBRICA CYBERASSEMBLY =========");
                System.out.println(" Baterías en Cinta: " + cinta.getOcupacioActual());
                System.out.println(" Estaciones Ocupadas: " + soldadura.getEstacionsOcupadesCount() + " / 3");
                System.out.println("=========================================================\n");
            }
        } catch (InterruptedException e) {
            System.out.println("[DASHBOARD] Monitoreo finalizado.");
        }
    }
}