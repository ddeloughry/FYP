package fyp.model;

public class TrafficStat {
    private double minutes;
    private int dayOfWeek;
    private String timeOfDay, weather, carParkName;

    public TrafficStat(String timeOfDay, double minutes, int dayOfWeek, String weather, String carParkName) {
        this.timeOfDay = timeOfDay;
        this.minutes = minutes;
        this.dayOfWeek = dayOfWeek;
        this.weather = weather;
        this.carParkName = carParkName;
    }

    public String getTimeOfDay() {
        return timeOfDay;
    }

    public void setTimeOfDay(String timeOfDay) {
        this.timeOfDay = timeOfDay;
    }

    public double getMinutes() {
        return minutes;
    }

    public void setMinutes(double minutes) {
        this.minutes = minutes;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getCarParkName() {
        return carParkName;
    }

    public void setCarParkName(String carParkName) {
        this.carParkName = carParkName;
    }
}
