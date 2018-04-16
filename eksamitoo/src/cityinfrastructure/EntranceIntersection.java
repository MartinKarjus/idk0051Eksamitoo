package cityinfrastructure;

import car.Car;
import car.Engine;
import enviromentcenter.EnviromentCenter;

import java.util.List;

public class EntranceIntersection extends Intersection {
    public EntranceIntersection(String intersectionName, EnviromentCenter enviromentCenter) {
        super(intersectionName, enviromentCenter);
    }

    @Override
    public void informEnviromentCenter(Car car, Engine engine) {
        enviromentCenter.newCarEntering(car, engine);
    }
}
