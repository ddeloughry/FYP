package fyp.model;

public class ParkDistance {
    private String name;
    private double distance;
    private boolean isFull;

    public ParkDistance(String name, double distance, boolean isFull) {
        this.name = name;
        this.distance = distance;
        this.isFull = isFull;
    }

    public double getDistance() {
        return distance;
    }

    public String getName() {
        return name;
    }

    public boolean isFull() {
        return isFull;
    }
}
