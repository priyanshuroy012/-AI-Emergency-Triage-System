package model;

public class Patient {
    private String name;
    private int severity;
    private long arrivalTime;

    public Patient(String name, int severity) {
        this.name = name;
        this.severity = severity;
        this.arrivalTime = System.currentTimeMillis();
    }

    public String getName() {
        return name;
    }

    public int getSeverity() {
        return severity;
    }

    public void increaseSeverity() {
        if (severity < 10) severity++;
    }

    public long getArrivalTime() {
        return arrivalTime;
    }
}

