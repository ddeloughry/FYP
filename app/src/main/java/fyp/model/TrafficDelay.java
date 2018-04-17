package fyp.model;

public class TrafficDelay {
    private String carParkName, direction;
    private long time;
    private int delay;

    public TrafficDelay(String carParkName, String direction, long time, int delay) {
        this.carParkName = carParkName;
        this.direction = direction;
        this.time = time;
        this.delay = delay;
    }

    public TrafficDelay() {

    }

    public String getCarParkName() {
        return carParkName;
    }

    public void setCarParkName(String carParkName) {
        this.carParkName = carParkName;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    @Override
    public String toString() {
        return "TrafficDelay{" +
                "carParkname='" + carParkName + '\'' +
                ", direction='" + direction + '\'' +
                ", time=" + time +
                ", delay=" + delay +
                '}';
    }
}
