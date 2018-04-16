package enviromentcenter;

public class Counter {

    private int timesDieselStopped;
    private int timesGassollineStopped;

    public Counter() {
        timesGassollineStopped = 0;
        timesDieselStopped = 0;
    }

    public int getTimesDieselStopped() {
        return timesDieselStopped;
    }

    public int getTimesGassollineStopped() {
        return timesGassollineStopped;
    }

    public void incrementDieselStopped() {
        timesDieselStopped += 1;
    }

    public void incrementGassollineStopped() {
        timesGassollineStopped += 1;
    }
}
