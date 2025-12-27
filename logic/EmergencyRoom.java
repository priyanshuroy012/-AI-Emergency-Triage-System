package logic;

import model.Doctor;
import model.Patient;

import java.util.*;

public class EmergencyRoom {

    private PriorityQueue<Patient> queue;
    private List<Doctor> doctors;
    private int treatedCount = 0;

    public EmergencyRoom() {
        queue = new PriorityQueue<>((a, b) -> {
            if (b.getSeverity() != a.getSeverity())
                return b.getSeverity() - a.getSeverity();
            return Long.compare(a.getArrivalTime(), b.getArrivalTime());
        });

        doctors = List.of(
                new Doctor("Dr. A"),
                new Doctor("Dr. B"),
                new Doctor("Dr. C")
        );

        startDoctorDispatcher();
        startSeverityAging();
    }

    public void addPatient(Patient p) {
        queue.offer(p);
    }

    public List<Patient> getAllPatients() {
        return new ArrayList<>(queue);
    }

    public int getTreatedCount() {
        return treatedCount;
    }

    public List<Doctor> getDoctors() {
        return doctors;
    }

    /* 👨‍⚕️ Auto assign patients to free doctors */
    private void startDoctorDispatcher() {
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                for (Doctor d : doctors) {
                    if (!d.isBusy() && !queue.isEmpty()) {
                        Patient p = queue.poll();
                        if (p != null) {
                            d.assignPatient(p);
                            treatedCount++;
                        }
                    }
                }
            }
        }, 0, 2000);
    }

    /* ⏱ Auto severity escalation */
    private void startSeverityAging() {
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                List<Patient> temp = new ArrayList<>(queue);
                queue.clear();
                for (Patient p : temp) {
                    if (System.currentTimeMillis() - p.getArrivalTime() > 60000) {
                        p.increaseSeverity();
                    }
                    queue.offer(p);
                }
            }
        }, 60000, 60000);
    }
}
