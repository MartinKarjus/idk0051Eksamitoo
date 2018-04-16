package controller;

import cityinfrastructure.*;
import enviromentcenter.EnviromentCenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CityMaker {

    private ExecutorService serviceCenterExecutor;

    public InfrastructureInfo makeInfrastructureInfo(EnviromentCenter enviromentCenter) {
        List<Intersection> intersections = new ArrayList<>();
        List<EntranceIntersection> entranceIntersections = new ArrayList<>();
        List<Road> roads = new ArrayList<>();
        serviceCenterExecutor = Executors.newFixedThreadPool(4);

        Random random = new Random();
        int serviceCenterCount = 0;
        int badRoadCount = 0;

        for(int row = 0; row < 4; row++) {
            for(int collumn = 1; collumn < 5; collumn++) {
                char first = (char) (row+65);
                if (row == 0) {
                    EntranceIntersection entrance = new EntranceIntersection(String.valueOf(first) + String.valueOf(collumn), enviromentCenter);

                    entranceIntersections.add(entrance);
                    intersections.add(entrance);
                } else {
                    intersections.add(new Intersection(String.valueOf(first) + String.valueOf(collumn), enviromentCenter));
                }
            }
        }

        while(serviceCenterCount < 4) {
            for (Intersection intersection : intersections) {
                if(random.nextInt(100) < 5) {
                    serviceCenterCount += 1;
                    CarServiceCenter center = new CarServiceCenter(enviromentCenter);
                    serviceCenterExecutor.execute(center);

                    if(!intersection.getCarServiceCenter().isPresent()) {
                        intersection.setCarServiceCenter(center);
                    }
                }
            }
        }

        int increaseChanceForBadRoad = 10;

        for (Intersection intersection : intersections) {
            for (Intersection otherIntersection : intersections) {
                if(intersection == otherIntersection) {
                    break;
                }

                if(Math.abs(otherIntersection.getName().charAt(0) - intersection.getName().charAt(0)) + Math.abs(otherIntersection.getName().charAt(1) - intersection.getName().charAt(1)) == 1) {
                    boolean add = true;
                    for (Road road : otherIntersection.getConnectedRoads()) {
                        if(road.getConnectedIntersections().contains(intersection) && road.getConnectedIntersections().contains(otherIntersection)) {
                            add = false;
                            break;
                        }
                    }
                    if(add) {
                        List<Intersection> connectionIntersection = new ArrayList<>();
                        connectionIntersection.add(intersection);
                        connectionIntersection.add(otherIntersection);
                        Road connection;

                        if(badRoadCount < 2 && random.nextInt(100) < increaseChanceForBadRoad) {
                            connection = new TerribleRoad(connectionIntersection, intersection.getName() + otherIntersection.getName());
                            badRoadCount += 1;
                        } else {
                            increaseChanceForBadRoad += 10;
                            connection = new Road(connectionIntersection, intersection.getName() + otherIntersection.getName());
                        }
                        intersection.addRoadConnection(connection);
                        otherIntersection.addRoadConnection(connection);
                    }
                }
            }
        }

        return new InfrastructureInfo(entranceIntersections, intersections, roads);
    }

    public ExecutorService getServiceCenterExecutor() {
        return serviceCenterExecutor;
    }
}
