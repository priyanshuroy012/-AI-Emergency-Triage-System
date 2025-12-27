#  AI Emergency Triage System

A Java-based emergency room simulation that prioritizes patients based on medical severity rather than arrival time.  
The system dynamically manages patient queues, simulates multiple doctors, and escalates patient severity if wait time exceeds safe thresholds.

---

##  Features

-  **Severity-based triage system**
-  **Automatic severity escalation for long-waiting patients**
-  **Multi-doctor simulation**
- **Live analytics dashboard**
- **Color-coded patient severity**
-  **Real-world hospital workflow simulation**
- **Polished Java Swing UI**

---

##  Data Structures & Algorithms Used

| Concept | Usage |
|------|------|
| Priority Queue | Severity-based patient ordering |
| Custom Comparator | Sorting by severity & arrival time |
| Queue | Patient flow management |
| Timers | Wait-time tracking & severity escalation |
| OOP Principles | Modular and scalable design |

---
<img width="1628" height="916" alt="Screenshot 2025-12-27 192104" src="https://github.com/user-attachments/assets/7e440af2-1673-4833-959f-f89b016177f8" />
<img width="1633" height="910" alt="Screenshot 2025-12-27 192146" src="https://github.com/user-attachments/assets/b62fc59e-1c99-425b-a48e-11dd6d60c2f0" />



## Project Structure
EmergencyTriageSystem/
│
├── Main.java # Application entry point
├── TriageUI.java # Swing-based UI
├── Patient.java # Patient model
├── PatientComparator.java # Custom priority comparator
├── TriageManager.java # Queue & doctor management
├── SeverityCalculator.java # Severity scoring logic
└── README.md

