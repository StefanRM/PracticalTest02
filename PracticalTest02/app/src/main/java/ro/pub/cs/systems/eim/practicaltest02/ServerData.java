package ro.pub.cs.systems.eim.practicaltest02;

public class ServerData {
    private String country;
    private String timezone;

    public ServerData() {
        this.country = null;
        this.timezone = null;
    }

    public ServerData(String country, String timezone) {
        this.country = country;
        this.timezone = timezone;
    }

    public String getCountry() {
        return country;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    @Override
    public String toString() {
        return "Gathered Info: { country = " + country + ", timezone = " + timezone + " }";
    }
}
