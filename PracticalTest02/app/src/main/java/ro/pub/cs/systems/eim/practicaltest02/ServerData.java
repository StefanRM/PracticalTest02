package ro.pub.cs.systems.eim.practicaltest02;

public class ServerData {
    private String hour;
    private String minute;

    public ServerData() {
        this.hour = null;
        this.minute = null;
    }

    public ServerData(String hour, String minute) {
        this.hour = hour;
        this.minute = minute;
    }

    public int getHour() {
        return Integer.parseInt(hour);
    }

    public int getMinute() {
        return Integer.parseInt(minute);
    }

    @Override
    public String toString() {
        return "Alarm: " + hour + ":" + minute;
    }
}
