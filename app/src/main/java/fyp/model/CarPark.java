package fyp.model;


import java.text.ParseException;
import java.util.Calendar;
import java.util.HashMap;

public class CarPark {
    private int id, identifier, totalSpaces, freeSpaces;
    private Double latitude, longitude, pricePerHour, pricePerDay, vehicleHeight;
    private String name;
    private long lastUpdated;
    private HashMap<Integer, ParkDateTime> times = new HashMap<>();
    private String priceString;

    public CarPark() {
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdentifier() {
        return identifier;
    }

    public void setIdentifier(int identifier) {
        this.identifier = identifier;
    }

    public int getTotalSpaces() {
        return totalSpaces;
    }

    public void setTotalSpaces(int totalSpaces) {
        this.totalSpaces = totalSpaces;
    }

    public int getFreeSpaces() {
        return freeSpaces;
    }

    public void setFreeSpaces(int freeSpaces) {
        this.freeSpaces = freeSpaces;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getVehicleHeight() {
        return vehicleHeight;
    }

    public void setVehicleHeight(double vehicleHeight) {
        this.vehicleHeight = vehicleHeight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isFull() {
        return freeSpaces == 0;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public HashMap<Integer, ParkDateTime> getTimes() {
        return times;
    }

    public void setTimes(HashMap<Integer, ParkDateTime> times) {
        this.times = times;
    }

    public boolean isOpen() throws ParseException {
        Calendar now = Calendar.getInstance();
        int day = now.get(Calendar.DAY_OF_WEEK);
        if (day == 1) {
            if (times.get(day - 2 + 7) == null) {
                return false;
            } else {
                Calendar c1 = Calendar.getInstance();
                Calendar c2 = Calendar.getInstance();
                c1.set(Calendar.HOUR_OF_DAY, Integer.parseInt((times.get(day - 2 + 7).getOpeningTime()).substring(0, 2)));
                c1.set(Calendar.MINUTE, Integer.parseInt((times.get(day - 2 + 7).getOpeningTime()).substring(3, 5)));
                c2.set(Calendar.HOUR_OF_DAY, Integer.parseInt((times.get(day - 2 + 7).getClosingTime().substring(0, 2))));
                c2.set(Calendar.MINUTE, Integer.parseInt((times.get(day - 2 + 7).getClosingTime()).substring(3, 5)));
                return Calendar.getInstance().after(c1) && Calendar.getInstance().before(c2);
            }
        } else {
            if (times.get(day - 2) == null) {
                return false;
            } else {
                Calendar c1 = Calendar.getInstance();
                Calendar c2 = Calendar.getInstance();
                c1.set(Calendar.HOUR_OF_DAY, Integer.parseInt((times.get(day - 2).getOpeningTime()).substring(0, 2)));
                c1.set(Calendar.MINUTE, Integer.parseInt((times.get(day - 2).getOpeningTime()).substring(3, 5)));
                c2.set(Calendar.HOUR_OF_DAY, Integer.parseInt((times.get(day - 2).getClosingTime().substring(0, 2))));
                c2.set(Calendar.MINUTE, Integer.parseInt((times.get(day - 2).getClosingTime()).substring(3, 5)));
                return Calendar.getInstance().after(c1) && Calendar.getInstance().before(c2);
            }
        }
    }


    public double getPricePerHour() {
        return pricePerHour;
    }

    public void setPricePerHour(double pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

    public Double getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(Double pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public void setPriceString(String priceString) {
        this.priceString = priceString;
    }

    public String getPriceString() {
        return priceString;
    }
}


