
import ui.TriageUI;

public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new TriageUI().setVisible(true);
        });
    }
}
