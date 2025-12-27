package ui;

import logic.EmergencyRoom;
import logic.TriageEngine;
import model.Patient;
import model.Doctor;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TriageUI extends JFrame {

    private JTextField nameField;
    private JCheckBox headache, chestPain, fever, unconscious, breathless, bleeding;

    private DefaultListModel<String> queueModel;
    private JList<String> queueList;

    private JLabel statsLabel;
    private EmergencyRoom er;

    public TriageUI() {
        er = new EmergencyRoom();

        /* -------- Global UI Polish -------- */
        UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 13));
        UIManager.put("Button.font", new Font("Segoe UI", Font.BOLD, 13));
        UIManager.put("CheckBox.font", new Font("Segoe UI", Font.PLAIN, 12));
        UIManager.put("Panel.background", Color.WHITE);

        setTitle("🏥 AI Emergency Triage System");
        setSize(1100, 620);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));

        add(createInputPanel(), BorderLayout.CENTER);
        add(createQueuePanel(), BorderLayout.EAST);
        add(createBottomPanel(), BorderLayout.SOUTH);

        startAnalyticsUpdater();
    }

    /* ================= CARD PANEL HELPER ================= */
    private JPanel cardPanel(String title) {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                new EmptyBorder(12, 12, 12, 12)
        ));

        if (title != null) {
            JLabel label = new JLabel(title);
            label.setFont(new Font("Segoe UI", Font.BOLD, 16));
            panel.add(label, BorderLayout.NORTH);
        }
        return panel;
    }

    /* ================= ADD PATIENT PANEL ================= */
    private JPanel createInputPanel() {
        JPanel outer = cardPanel("➕ Add Patient");

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        form.add(new JLabel("Patient Name"), gbc);

        gbc.gridx = 1;
        nameField = new JTextField(16);
        form.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        form.add(new JLabel("Symptoms"), gbc);

        JPanel symptoms = new JPanel(new GridLayout(3, 2, 6, 6));
        symptoms.setBorder(new EmptyBorder(6, 6, 6, 6));

        headache = new JCheckBox("Headache");
        chestPain = new JCheckBox("Chest Pain");
        fever = new JCheckBox("High Fever");
        unconscious = new JCheckBox("Unconscious");
        breathless = new JCheckBox("Breathless");
        bleeding = new JCheckBox("Bleeding");

        symptoms.add(headache);
        symptoms.add(chestPain);
        symptoms.add(fever);
        symptoms.add(unconscious);
        symptoms.add(breathless);
        symptoms.add(bleeding);

        gbc.gridy = 2;
        form.add(symptoms, gbc);

        outer.add(form, BorderLayout.CENTER);
        return outer;
    }

    /* ================= QUEUE + ANALYTICS ================= */
    private JPanel createQueuePanel() {
        JPanel outer = cardPanel("🕒 Waiting Queue");
        outer.setPreferredSize(new Dimension(350, 0));

        queueModel = new DefaultListModel<>();
        queueList = new JList<>(queueModel);
        queueList.setFont(new Font("Consolas", Font.PLAIN, 13));
        queueList.setCellRenderer(new SeverityRenderer());
        queueList.setBorder(new EmptyBorder(6, 6, 6, 6));

        outer.add(new JScrollPane(queueList), BorderLayout.CENTER);
        outer.add(createAnalyticsPanel(), BorderLayout.SOUTH);

        return outer;
    }

    private JPanel createAnalyticsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(230, 230, 230)),
                new EmptyBorder(8, 4, 4, 4)
        ));

        statsLabel = new JLabel();
        panel.add(statsLabel, BorderLayout.CENTER);
        return panel;
    }

    private void startAnalyticsUpdater() {
        Timer t = new Timer(1000, e -> updateStats());
        t.start();
    }

    private void updateStats() {
        int waiting = er.getAllPatients().size();
        int treated = er.getTreatedCount();
        long busyDoctors = er.getDoctors().stream().filter(Doctor::isBusy).count();

        statsLabel.setText(
                "<html>" +
                        "🧍 Waiting: <b>" + waiting + "</b><br>" +
                        "✅ Treated: <b>" + treated + "</b><br>" +
                        "👨‍⚕️ Doctors Busy: <b>" + busyDoctors + "</b>" +
                        "</html>"
        );
    }

    /* ================= BOTTOM BUTTONS ================= */
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton addBtn = new JButton("➕ Add Patient");
        JButton refreshBtn = new JButton("🔄 Refresh Queue");

        addBtn.setPreferredSize(new Dimension(160, 35));
        refreshBtn.setPreferredSize(new Dimension(160, 35));

        addBtn.setBackground(new Color(33, 150, 243));
        addBtn.setForeground(Color.WHITE);
        addBtn.setFocusPainted(false);

        refreshBtn.setFocusPainted(false);

        addBtn.addActionListener(e -> addPatient());
        refreshBtn.addActionListener(e -> updateQueue());

        panel.add(addBtn);
        panel.add(refreshBtn);
        return panel;
    }

    /* ================= LOGIC ================= */
    private void addPatient() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter patient name");
            return;
        }

        List<String> symptoms = new ArrayList<>();
        if (headache.isSelected()) symptoms.add("Headache");
        if (chestPain.isSelected()) symptoms.add("Chest Pain");
        if (fever.isSelected()) symptoms.add("High Fever");
        if (unconscious.isSelected()) symptoms.add("Unconscious");
        if (breathless.isSelected()) symptoms.add("Shortness of Breath");
        if (bleeding.isSelected()) symptoms.add("Bleeding");

        int severity = TriageEngine.calculateSeverity(symptoms);
        er.addPatient(new Patient(name, severity));

        clearForm();
        updateQueue();
    }

    private void clearForm() {
        nameField.setText("");
        headache.setSelected(false);
        chestPain.setSelected(false);
        fever.setSelected(false);
        unconscious.setSelected(false);
        breathless.setSelected(false);
        bleeding.setSelected(false);
    }

    private void updateQueue() {
        queueModel.clear();
        for (Patient p : er.getAllPatients()) {
            queueModel.addElement(p.getName() + " | Severity " + p.getSeverity());
        }
    }

    /* ================= SEVERITY COLOR ================= */
    static class SeverityRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(
                JList<?> list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {

            JLabel label = (JLabel) super.getListCellRendererComponent(
                    list, value, index, isSelected, cellHasFocus);

            String text = value.toString();
            int severity = Integer.parseInt(text.replaceAll("\\D+", ""));

            if (!isSelected) {
                if (severity >= 7) label.setForeground(Color.RED);
                else if (severity >= 4) label.setForeground(Color.ORANGE);
                else label.setForeground(new Color(0, 128, 0));
            }

            return label;
        }
    }
}
