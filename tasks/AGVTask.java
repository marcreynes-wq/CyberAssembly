public class AGVTask implements Runnable{
    Cinta cinta;
    private Cinta id = 1;
    private int comptadorBat = 0;

    @Override
    public void run() {
        try {
            while(!Thread.currentThread().isInterrupted()){
                
                cinta.afergirBateria(id, comptadorBat);
                comptadorBat ++;
                Thread.sleep(900);

            }
        } catch {
            Thread.currentThread.interrupt();

        }
    }
    
}
