import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

public class RegistrationServer extends JFrame {
    private JTextField hostField, portField;
    private JTextArea participantsArea;
    private RegistrationService registrationService;

    public RegistrationServer() {
        super("Conference Server");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Створення компонентів GUI
        JPanel inputPanel = new JPanel(new GridLayout(2, 2));
        inputPanel.add(new JLabel("Host:"));
        hostField = new JTextField("localhost");
        inputPanel.add(hostField);
        inputPanel.add(new JLabel("Port:"));
        portField = new JTextField("1099");
        inputPanel.add(portField);

        JButton startButton = new JButton("Start");
        startButton.addActionListener(new StartButtonListener());

        JButton stopButton = new JButton("Stop");
        stopButton.addActionListener(new StopButtonListener());

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new SaveButtonListener());

        JButton loadButton = new JButton("Load");
        loadButton.addActionListener(new LoadButtonListener());

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(new ExitButtonListener());

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(startButton);
        buttonsPanel.add(stopButton);
        buttonsPanel.add(saveButton);
        buttonsPanel.add(loadButton);
        buttonsPanel.add(exitButton);

        participantsArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(participantsArea);

        add(inputPanel, BorderLayout.NORTH);
        add(buttonsPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private class StartButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String host = hostField.getText();
                int port = Integer.parseInt(portField.getText());
                Registry registry = LocateRegistry.createRegistry(port);
                registrationService = new RegistrationServiceImpl();
                registry.rebind("RegistrationService", registrationService);
                updateParticipantsArea();
                JOptionPane.showMessageDialog(RegistrationServer.this, "Server started successfully.");
            } catch (RemoteException ex) {
                JOptionPane.showMessageDialog(RegistrationServer.this, "Error starting server: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class StopButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String host = hostField.getText();
                int port = Integer.parseInt(portField.getText());
                Registry registry = LocateRegistry.getRegistry(host, port);
                registry.unbind("RegistrationService");
                JOptionPane.showMessageDialog(RegistrationServer.this, "Server stopped successfully.");
            } catch (RemoteException ex) {
                JOptionPane.showMessageDialog(RegistrationServer.this, "Error stopping server: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(RegistrationServer.this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class SaveButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showSaveDialog(RegistrationServer.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    List<Participant> participants = registrationService.getRegisteredParticipants();
                    XMLUtils.saveParticipantsToXML(participants, file);
                    JOptionPane.showMessageDialog(RegistrationServer.this, "Data saved to " + file.getAbsolutePath());
                }
            } catch (RemoteException ex) {
                JOptionPane.showMessageDialog(RegistrationServer.this, "Error getting participants: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(RegistrationServer.this, "Error saving data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class LoadButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(RegistrationServer.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    List<Participant> participants = XMLUtils.loadParticipantsFromXML(file);
                    for (Participant participant : participants) {
                        registrationService.registerParticipant(participant);
                    }
                    updateParticipantsArea();
                    JOptionPane.showMessageDialog(RegistrationServer.this, "Data loaded from " + file.getAbsolutePath());
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(RegistrationServer.this, "Error loading data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class ExitButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }

    private void updateParticipantsArea() {
        try {
            List<Participant> participants = registrationService.getRegisteredParticipants();
            StringBuilder sb = new StringBuilder();
            int count = 1;
            for (Participant participant : participants) {
                sb.append(count).append(") name: ").append(participant.getName()).append(", familyName: ").append(participant.getLastName()).append(", placeOfWork: ").append(participant.getWorkplace()).append(", report: ").append(participant.getReportTitle()).append(", email: ").append(participant.getEmail()).append("\n");
                count++;
            }
            participantsArea.setText(sb.toString());
            participantsArea.setCaretPosition(0);
            setTitle("Conference Server - Host: " + hostField.getText() + ", Port: " + portField.getText() + ", participants: " + participants.size());
        } catch (RemoteException ex) {
            JOptionPane.showMessageDialog(this, "Error getting participants: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new RegistrationServer();
    }
}