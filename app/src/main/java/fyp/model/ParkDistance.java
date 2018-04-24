package fyp.model;

public class ParkDistance {
    private String name;
    private double distance;

    public ParkDistance(String name, double distance) {
        this.name = name;
        this.distance = distance;
    }

    public double getDistance() {
        return distance;
    }

    public String getName() {
        return name;
    }
}
