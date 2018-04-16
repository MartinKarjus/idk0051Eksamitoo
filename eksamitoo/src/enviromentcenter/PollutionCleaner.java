package enviromentcenter;

public class PollutionCleaner implements Runnable{
    private EnviromentCenter enviromentCenter;


    public PollutionCleaner(EnviromentCenter enviromentCenter) {
        this.enviromentCenter = enviromentCenter;
    }

    @Override
    public void run() {
        System.out.println("Pollution cleaner started");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        enviromentCenter.resetCounter();
    }

}
