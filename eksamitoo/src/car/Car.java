package car;


import cityinfrastructure.InfrastructureInfo;
import cityinfrastructure.Intersection;
import cityinfrastructure.Road;
import cityinfrastructure.TerribleRoad;
import enviromentcenter.EnviromentCenter;

import java.util.List;
import java.util.Random;

public class Car implements Runnable{
    private Engine engine;
    private int informEnviromentCenterCounter;
    private int queryEnviromentCenterCounter;
    private int stopCounter;
    protected Intersection currentIntersection;
    protected EnviromentCenter enviromentCenter;
    private boolean wantsToChangeEngine;
    protected InfrastructureInfo infrastructureInfo;
    private Tires tires;
    protected Random driver;
    private int lastConsideredSwitchAt;

    public Car(Engine engine, InfrastructureInfo infrastructureInfo, EnviromentCenter enviromentCenter) {
        this.engine = engine;
        this.infrastructureInfo = infrastructureInfo;
        this.enviromentCenter = enviromentCenter;
        this.informEnviromentCenterCounter = 0;
        this.queryEnviromentCenterCounter = 0;
        this.wantsToChangeEngine = false;
        this.tires = new Tires();
        this.driver = new Random();
        this.stopCounter = 0;
        this.lastConsideredSwitchAt = 0;
    }

    private void enterTown() {
        currentIntersection = infrastructureInfo.getEntranceIntersections().get(driver.nextInt(infrastructureInfo.getEntranceIntersections().size()));
        currentIntersection.informEnviromentCenter(this, engine);
    }

    private void updateEnviromentCenter() {
        informEnviromentCenterCounter += 1;
        queryEnviromentCenterCounter += 1;

        if (informEnviromentCenterCounter == 5) {
            enviromentCenter.notifyEnviromentCenter(engine);
            informEnviromentCenterCounter = 0;
        }

        if (queryEnviromentCenterCounter == 7) {
            enviromentCenter.allowDriving(engine, this);

            int currentCount = enviromentCenter.getTimesEngineStopped(engine);
            if(currentCount - lastConsideredSwitchAt >= 2) {
                lastConsideredSwitchAt = currentCount;

                if(driver.nextInt(6) == 0) {
                    wantsToChangeEngine = true;
                }
            }
            queryEnviromentCenterCounter = 0;
        }
    }

    private void drive() {
        //System.out.println("Car " + this + " at: " + currentIntersection.getName() + " Engine: " + engine.getClass() + " Tires: " + tires);
        if(tires.getBadRoadsDrivenOn() == 3) {
            //System.out.println("waiting for service car");
            enviromentCenter.requestServiceCar();
            currentIntersection.waitForRepairs(this);
            //System.out.println("carrying on");
        }

        int choiceNumber = driver.nextInt(currentIntersection.getConnectedRoads().size());
        Road choice = currentIntersection.getConnectedRoads().get(choiceNumber);

        if(choice instanceof TerribleRoad) {
            tires.badRoadCrossed();
        }

        int timeToDrive = driver.nextInt(20 - 3 + 1) + 3;
        try {
            Thread.sleep(timeToDrive);
        } catch (InterruptedException e) {
            return;
        }

        updateEnviromentCenter();

        if(choice.getConnectedIntersections().get(0) == currentIntersection) {
            currentIntersection = choice.getConnectedIntersections().get(1);
        } else {
            currentIntersection = choice.getConnectedIntersections().get(0);
        }

        if(currentIntersection.getCarServiceCenter().isPresent()) {
            currentIntersection.getCarServiceCenter().get().requestService(this);
        }
    }


    @Override
    public void run() {
        enterTown();
        while (!Thread.interrupted() && !enviromentCenter.isClearCity()) {
            drive();
        }
        System.out.println("Car stopping");
    }


    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public void setTires(Tires tires) {
        this.tires = tires;
    }

    public Engine getEngine() {
        return engine;
    }

    public boolean wantsToChangeEngine() {
        return wantsToChangeEngine;
    }

    public Intersection getCurrentIntersection() {
        return currentIntersection;
    }

    public Tires getTires() {
        return tires;
    }
}
