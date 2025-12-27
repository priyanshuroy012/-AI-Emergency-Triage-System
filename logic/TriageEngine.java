package logic;

import java.util.List;

public class TriageEngine {

    public static int calculateSeverity(List<String> symptoms) {
        int severity = 1;

        for (String s : symptoms) {
            switch (s) {
                case "Chest Pain":
                case "Bleeding":
                case "Unconscious":
                    severity += 3;
                    break;
                case "Shortness of Breath":
                case "High Fever":
                    severity += 2;
                    break;
                case "Headache":
                    severity += 1;
                    break;
            }
        }
        return Math.min(severity, 10);
    }
}
