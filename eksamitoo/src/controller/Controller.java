package controller;

import car.Car;
import car.DieselEngine;
import cityinfrastructure.InfrastructureInfo;
import enviromentcenter.EnviromentCenter;
import enviromentcenter.EnviromentallyAwareBird;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Controller {

    public void generateTraffic() {
        CityMaker cityMaker = new CityMaker();
        EnviromentCenter enviromentCenter = new EnviromentCenter();
        InfrastructureInfo infrastructureInfo = cityMaker.makeInfrastructureInfo(enviromentCenter);
        enviromentCenter.setInfrastructureInfo(infrastructureInfo);
        EnviromentallyAwareBird bird = new EnviromentallyAwareBird(enviromentCenter);

        infrastructureInfo.printConnections();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ExecutorService executor = Executors.newFixedThreadPool(201);

        for(int i = 0; i < 200; i++) {
            executor.execute(new Car(new DieselEngine(), infrastructureInfo, enviromentCenter));
        }
        executor.execute(bird);
        /*
        try {
            Thread.sleep(120000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executor.shutdownNow();
        cityMaker.getServiceCenterExecutor().shutdownNow();
        enviromentCenter.setClearCity(true);
        */

    }


}
