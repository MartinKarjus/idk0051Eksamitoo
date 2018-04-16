import car.Car;
import car.GassolineEngine;
import cityinfrastructure.CarServiceCenter;
import controller.Controller;

import java.util.function.Predicate;


public class Main {
    public static void main(String[] args) {
        Controller controller = new Controller();
        controller.generateTraffic();
    }

    public static void predicateExample() {
        CarServiceCenter carServiceCenter = new CarServiceCenter(null);
        carServiceCenter.getByCondition(c -> c.getEngine().getClass().equals(GassolineEngine.class));
    }
}
