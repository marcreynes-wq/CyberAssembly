package src.cyberassembly.model;

public class Supervisor {
    private boolean alertaSuperescalfament = false;

    public synchronized void verificaEstatAlerta(int robotId) throws InterruptedException {

        while (alertaSuperescalfament) {
            System.out.println("[SUPERVISOR] Robot #" + robotId + "pausado por alerta de superescalfament.");
            wait();
        }
    }

    public synchronized void activarAlerta() {
        this.alertaSuperescalfament = true;
        System.out.println("\n[SUPERVISOR]  ALERTA DE SUPERESCALFAMIENTO ACTIVADA. Pausando producción...\n");
    }

    public synchronized void desactivarAlerta() {
        this.alertaSuperescalfament = false;
        System.out.println("\n[SUPERVISOR]  ALERTA RESUELTA. Notificando a todos los hilos...\n");
        notifyAll();
    }

    public synchronized boolean isAlertaActiva() {
        return alertaSuperescalfament;
    }

    
    
}
