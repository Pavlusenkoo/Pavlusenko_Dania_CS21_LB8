import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

public class RegistrationGUI extends JFrame {
    private JTextField nameField, lastNameField, workplaceField, reportTitleField, emailField;
    private JTextArea participantsArea;
    private RegistrationService service;

    public RegistrationGUI() {
        super("Conference Registration");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Створення компонентів GUI
        JPanel inputPanel = new JPanel(new GridLayout(5, 2));
        inputPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Last Name:"));
        lastNameField = new JTextField();
        inputPanel.add(lastNameField);
        inputPanel.add(new JLabel("Workplace:"));
        workplaceField = new JTextField();
        inputPanel.add(workplaceField);
        inputPanel.add(new JLabel("Report Title:"));
        reportTitleField = new JTextField();
        inputPanel.add(reportTitleField);
        inputPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        inputPanel.add(emailField);

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(new RegisterButtonListener());

        JButton getParticipantsButton = new JButton("Get Participants");
        getParticipantsButton.addActionListener(new GetParticipantsButtonListener());

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(registerButton);
        buttonsPanel.add(getParticipantsButton);

        participantsArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(participantsArea);

        add(inputPanel, BorderLayout.NORTH);
        add(buttonsPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        // Підключення до сервера
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            service = (RegistrationService) registry.lookup("RegistrationService");
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }

    private class RegisterButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = nameField.getText();
            String lastName = lastNameField.getText();
            String workplace = workplaceField.getText();
            String reportTitle = reportTitleField.getText();
            String email = emailField.getText();

            Participant participant = new Participant(name, lastName, workplace, reportTitle, email);

            try {
                int participantsCount = service.registerParticipant(participant);
                JOptionPane.showMessageDialog(RegistrationGUI.this, "Participant registered. Total participants: " + participantsCount);
            } catch (RemoteException ex) {
                JOptionPane.showMessageDialog(RegistrationGUI.this, "Error registering participant: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class GetParticipantsButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                List<Participant> participants = service.getRegisteredParticipants();
                StringBuilder sb = new StringBuilder();
                for (Participant participant : participants) {
                    sb.append("Name: ").append(participant.getName()).append("\n");
                    sb.append("Last Name: ").append(participant.getLastName()).append("\n");
                    sb.append("Workplace: ").append(participant.getWorkplace()).append("\n");
                    sb.append("Report Title: ").append(participant.getReportTitle()).append("\n");
                    sb.append("Email: ").append(participant.getEmail()).append("\n\n");
                }
                participantsArea.setText(sb.toString());
            } catch (RemoteException ex) {
                JOptionPane.showMessageDialog(RegistrationGUI.this, "Error getting participants: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        new RegistrationGUI();
    }
}