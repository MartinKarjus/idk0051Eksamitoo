package cityinfrastructure;

import car.*;
import enviromentcenter.EnviromentCenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Intersection {
    private CarServiceCenter carServiceCenter;
    private List<Road> connectedRoads;
    private String intersectionName;
    protected EnviromentCenter enviromentCenter;
    private List<Car> brokenDownCars;

    public Intersection(String intersectionName, EnviromentCenter enviromentCenter) {
        this.intersectionName = intersectionName;
        this.connectedRoads = new ArrayList<>();
        this.brokenDownCars = new ArrayList<>();
        this.enviromentCenter = enviromentCenter;
    }

    public boolean repairCar() {
        synchronized (this) {
            if (brokenDownCars.size() > 0) {
                Car brokenCar = brokenDownCars.get(0);
                if (brokenCar.getEngine() instanceof LimonadeEngine || brokenCar.getEngine() instanceof ElectricEngine) {
                    brokenCar.setTires(new MarmeladeTires());
                } else {
                    brokenCar.setTires(new Tires());
                }
                brokenDownCars.remove(brokenCar);
                this.notify();
                return true;
            } else {
                return false;
            }
        }
    }

    public synchronized void waitForRepairs(Car car) {
        brokenDownCars.add(car);

        while(brokenDownCars.contains(car)) {
            try {
                wait();
            } catch (InterruptedException e) {
                return;
            }
        }
    }


    public String getName() {
        return intersectionName;
    }

    public void setCarServiceCenter(CarServiceCenter carServiceCenter) {
        this.carServiceCenter = carServiceCenter;
    }

    public void addRoadConnection(Road road) {
        connectedRoads.add(road);
    }


    public Optional<CarServiceCenter> getCarServiceCenter() {
        return Optional.ofNullable(carServiceCenter);
    }

    public void informEnviromentCenter(Car car, Engine engine) {
        return;
    }

    public List<Road> getConnectedRoads() {
        return connectedRoads;
    }

    public EnviromentCenter getEnviromentCenter() {
        return enviromentCenter;
    }
}
