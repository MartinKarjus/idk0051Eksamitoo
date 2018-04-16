package enviromentcenter;

public class EnviromentallyAwareBird implements Runnable{


    private EnviromentCenter enviromentCenter;

    public EnviromentallyAwareBird(EnviromentCenter enviromentCenter) {
        this.enviromentCenter = enviromentCenter;
    }

    private void sing() {
        if(enviromentCenter.getPollution() < 400) {
            System.out.println("Puhas õhk on puhas õhk on rõõmus linnu elu!”");
        } else {
            System.out.println("Inimene tark, inimene tark – saastet täis on linnapark");
        }
    }

    @Override
    public void run() {
        while (!Thread.interrupted() && !enviromentCenter.isClearCity()) {
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // System.out.println(enviromentCenter.getOverview());
            sing();
        }
    }
}
