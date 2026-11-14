import model.Supervisor;

public class RobotTask implements Runnable {
    int id;

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()){
            try {

                Supervisor.verificaEstatAlerta(id);
                String bat = cinta.extreureBat(id);
                int numEstacio = soldadura.agafa(id);
                Sleep(random);
                soldadura.allibera(id, numEstacio);
                soldadura.registreCentral(id);
            } catch {
                Thread.currentThread().interrupt();
            }
            

        }

        synchronized verificaEsta(id){
            if (activaAlerta) {
                wait();
            }
        }
    }

}