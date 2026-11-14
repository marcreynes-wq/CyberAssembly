
import model.Cinta;
import model.Supervisor;


public class AGVTask implements Runnable{
    private final int agvId;
    private final Cinta cinta;
    private final Supervisor supervisor;
    private int comptadorBat = 0;

    public AGVTask(int agvId, Cinta cinta, Supervisor supervisor) {
        this.agvId = agvId;
        this.cinta = cinta;
        this.supervisor = supervisor;
    }

    @Override
    public void run() {
        try {
            while(!Thread.currentThread().isInterrupted()){
                supervisor.verificaEstatAlerta(agvId);

                String bateriaId = "BAT-AGV" + agvId + "_" + (++comptadorBat);
                cinta.afegirBateria(bateriaId, agvId);
                
                Thread.sleep(700);
            }
        } catch (InterruptedException e) {
            System.out.println("[AGV #" + agvId + "] Finalizado correctamente.");
            Thread.currentThread().interrupt();//Preservar estado de interrupción
        }
    }
}
