package car;

import cityinfrastructure.InfrastructureInfo;
import cityinfrastructure.Road;
import cityinfrastructure.TerribleRoad;
import enviromentcenter.EnviromentCenter;

import java.util.ArrayList;
import java.util.List;

public class ServiceCar extends Car implements Runnable {

    private List<Road> roadBack;
    private boolean carRepaired;

    public ServiceCar(Engine engine, InfrastructureInfo infrastructureInfo, EnviromentCenter enviromentCenter) {
        super(engine, infrastructureInfo, enviromentCenter);
        this.roadBack = new ArrayList<>();
        this.carRepaired = false;
    }

    private Road driveBack() {
        List<Road> roads = currentIntersection.getConnectedRoads();
        for (Road road : roads) {
            if (road == roadBack.get(0)) {
                roadBack = new ArrayList<>();
                return road;
            } else if (roadBack.contains(road)) {
                roadBack.remove(road);
                return road;
            }
        }
        int choiceNumber = driver.nextInt(currentIntersection.getConnectedRoads().size());
        return currentIntersection.getConnectedRoads().get(choiceNumber);
    }

    private void drive() {
        if (currentIntersection.repairCar()) {
            carRepaired = true;
            return;
        }

        Road choice;
        if(carRepaired) {
            choice = driveBack();
        } else {
            int choiceNumber = driver.nextInt(currentIntersection.getConnectedRoads().size());
            choice = currentIntersection.getConnectedRoads().get(choiceNumber);
            roadBack.add(choice);
        }


        int timeToDrive = driver.nextInt(20 - 3 + 1) + 3;
        try {
            Thread.sleep(timeToDrive);
        } catch (InterruptedException e) {
            return;
        }

        if (choice.getConnectedIntersections().get(0) == currentIntersection) {
            currentIntersection = choice.getConnectedIntersections().get(1);
        } else {
            currentIntersection = choice.getConnectedIntersections().get(0);
        }

    }

    private void enterTown() {
        currentIntersection = infrastructureInfo.getEntranceIntersections().get(driver.nextInt(infrastructureInfo.getEntranceIntersections().size()));
    }


    @Override
    public void run() {
        enterTown();
        while (!Thread.interrupted()) {
            if (enviromentCenter.isClearCity()) {
                return;
            }
            drive();
            if (carRepaired && roadBack.size() == 0) {
                enviromentCenter.removeServiceCar(this);
                //System.out.println("DESTROY THE SERVICE CAR!");
                return;
            }
        }
    }

}
