package car;

public class Tires {
    private int badRoadsDrivenOn;

    public Tires() {
        this.badRoadsDrivenOn = 0;
    }

    public void badRoadCrossed() {
        badRoadsDrivenOn += 1;
    }

    public int getBadRoadsDrivenOn() {
        return badRoadsDrivenOn;
    }
}
