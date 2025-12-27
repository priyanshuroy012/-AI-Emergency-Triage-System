package model;

public class Doctor implements Runnable {

    private String name;
    private boolean busy;
    private Patient currentPatient;

    public Doctor(String name) {
        this.name = name;
        this.busy = false;
    }

    public boolean isBusy() {
        return busy;
    }

    public String getName() {
        return name;
    }

    public void assignPatient(Patient p) {
        this.currentPatient = p;
        new Thread(this).start();
    }

    @Override
    public void run() {
        busy = true;
        try {
            Thread.sleep(4000); // simulate treatment time
        } catch (InterruptedException ignored) {}
        busy = false;
        currentPatient = null;
    }
}
