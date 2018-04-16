package cityinfrastructure;

import java.util.List;

public class Road {
    private List<Intersection> connectedIntersections;
    private String streetName;

    public Road(List<Intersection> connectedIntersections, String streetName) {
        this.connectedIntersections = connectedIntersections;
        this.streetName = streetName;
    }

    public List<Intersection> getConnectedIntersections() {
        return connectedIntersections;
    }

    public void setConnectedIntersections(List<Intersection> connectedIntersections) {
        this.connectedIntersections = connectedIntersections;
    }

    public String getStreetName() {
        return streetName;
    }
}
