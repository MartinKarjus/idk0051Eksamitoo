package cityinfrastructure;

import car.Car;
import car.ElectricEngine;
import car.Engine;
import car.LimonadeEngine;
import enviromentcenter.EnviromentCenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CarServiceCenter implements Runnable {


    List<Car> carsToService;
    List<Car> carsServiced;
    EnviromentCenter enviromentCenter;
    Random randomEngine;

    public CarServiceCenter(EnviromentCenter enviromentCenter) {
        this.carsToService = new ArrayList<>();
        this.carsServiced = new ArrayList<>();
        this.randomEngine = new Random();
        this.enviromentCenter = enviromentCenter;
    }

    public List<Car> getByCondition(Predicate<Car> condition) {
        List<Car> carsToReturn;
        carsToReturn = carsServiced.stream()
                .filter(condition)
                .collect(Collectors.toList());
        return carsToReturn;
    }

    public void requestService(Car carToService) {
        synchronized (this) {
            carsToService.add(carToService);
            notifyAll();
        }
        //System.out.println("Entering service center");
        while(carsToService.contains(carToService)) {

            try {
                synchronized (this) {
                    this.wait();
                }
            } catch (InterruptedException e) {
                return;
            }

        }
        //System.out.println("Leaving service center");
    }

    private void serviceCars() throws InterruptedException {
        if(carsToService.size() > 0) {
            Thread.sleep(50);
            if(carsToService.get(0).wantsToChangeEngine()) {
                Engine oldEngine = carsToService.get(0).getEngine();
                Engine newEngine;
                if(randomEngine.nextBoolean()) {
                    newEngine = new LimonadeEngine();
                    carsToService.get(0).setEngine(newEngine);
                } else {
                    newEngine = new ElectricEngine();
                    carsToService.get(0).setEngine(newEngine);
                }
                enviromentCenter.replaceEngine(oldEngine, newEngine);
            }
            synchronized (this) {
                carsServiced.add(carsToService.get(0));
                carsToService.remove(0);
                notifyAll();
            }
        } else {
            synchronized (this) {
                wait();
            }
        }
    }


    @Override
    public void run() {
        while (!Thread.interrupted() && !enviromentCenter.isClearCity()) {
            try {
                serviceCars();
            } catch (InterruptedException e) {
                return;
            }

        }
    }

}
