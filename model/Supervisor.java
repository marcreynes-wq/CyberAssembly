package model;

public class Supervisor {
    private boolean alertaSuperescalfament = false;

    public synchronized void verificaEstatAlerta(int robotId) throws InterruptedException {

        while (alertaSuperescalfament) {
            System.out.println("[SUPERVISOR] Robot #" + robotId + "pausado por alerta de superescalfament.");
            wait();
        }
    }

    public synchronized void activarSuperescalfament() {
        this.alertaSuperescalfament = true;
        System.out.println("\n[SUPERVISOR] 🚨 ALERTA DE SUPERESCALFAMIENTO ACTIVADA. Pausando producción...\n");
    }

    
    
}
