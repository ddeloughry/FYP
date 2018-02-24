package fyp.model;


public class ParkDateTime {
    private String openingTime, closingTime;

    public ParkDateTime(String openingTime, String closingTime) {
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }

    public String getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(String openingTime) {
        this.openingTime = openingTime;
    }

    public String getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(String closingTime) {
        this.closingTime = closingTime;
    }

    @Override
    public String toString() {
        return openingTime + " - " + closingTime + "\n";
    }
}
