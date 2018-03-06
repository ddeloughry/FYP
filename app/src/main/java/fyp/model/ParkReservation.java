package fyp.model;

import java.util.Date;


public class ParkReservation  {
    private long startTime, endTime;
    private String carParkName, licencePlate;

    public ParkReservation() {
    }

    public ParkReservation(long startTime, long endTime, String carParkName, String licencePlate) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.carParkName = carParkName;
        this.licencePlate = licencePlate;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getCarParkName() {
        return carParkName;
    }

    public void setCarParkName(String carParkName) {
        this.carParkName = carParkName;
    }

    public String getLicencePlate() {
        return licencePlate;
    }

    public void setLicencePlate(String licencePlate) {
        this.licencePlate = licencePlate;
    }

    @Override
    public String toString() {
        return (new Date(startTime)).toString() +
                "\n" + (new Date(endTime)).toString() +
                "\n" + carParkName +
                "\n" + licencePlate;
    }
}
