package cityinfrastructure;

import java.util.ArrayList;
import java.util.List;

public class InfrastructureInfo {
    private List<EntranceIntersection> entranceIntersections;
    private List<Intersection> intersections;
    private List<Road> roads;


    public InfrastructureInfo(List<EntranceIntersection> entranceIntersections, List<Intersection> intersections, List<Road> roads) {
        this.entranceIntersections = entranceIntersections;
        this.intersections = intersections;
        this.roads = roads;
    }

    public void printConnections() {

        for (Intersection intersection : intersections) {
            System.out.println("----------------------------------------------------");
            System.out.println("Intersection name: " + intersection.getName());
            if(intersection instanceof EntranceIntersection) {
                System.out.println("Connection to the outside");
            }
            for (Road road : intersection.getConnectedRoads()) {
                System.out.println("Road name: " + road.getStreetName() + " ,type: " + road.getClass());
                for (Intersection crossing : road.getConnectedIntersections()) {
                    System.out.print(crossing.getName());
                    System.out.print(" ");
                }
                System.out.println();
            }
        }
    }

    public List<EntranceIntersection> getEntranceIntersections() {
        return entranceIntersections;
    }

    public List<Intersection> getIntersections() {
        return intersections;
    }

    public List<Road> getRoads() {
        return roads;
    }
}
