package com.tsc.program;

import com.tsc.PLC.PLCCommandManager;
import com.tsc.PLC.PLCConnector;
import com.tsc.conf.*;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class programMain {
    private static int currentPage = 0;
    private static List<Motor> motors;
    private static PLCCommandManager commandManager;
    private static JLabel statusBar;
    private static int motorBlocksPerPage;
    private static JFrame frame; // Declare frame as a static field

    public static void main() {
        try {
            motorConfig motorConfigInstance = motorConfig.loadConfig(Paths.get("config", "motorConfig.json").toString());
            motors = motorConfigInstance.getMotors();

            // Load PLC configurations
            List<PLCConfig> plcConfigs = PLCConfigManager.loadConfig(Paths.get("config", "PLC.json").toString());
            if (!plcConfigs.isEmpty()) {
                PLCConnector plcConnector = new PLCConnector(plcConfigs.get(0));
                commandManager = new PLCCommandManager(plcConnector);
                plcConnector.connect();
            }

            // Load HMI config or create it
            String hmiConfigPath = Paths.get("config", "HMIConfig.json").toString();
            File hmiConfigFile = new File(hmiConfigPath);
            if (!hmiConfigFile.exists()) {
                HMIConfig.generateDefaultConfig(hmiConfigPath);
            }
            HMIConfig hmiConfig = HMIConfig.loadConfig(hmiConfigPath);
            motorBlocksPerPage = hmiConfig.getMotorBlocksPerPage();

            createAndShowGUI();

            // Start a ScheduledExecutorService to update the GUI every 100 milliseconds
            ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
            executorService.scheduleAtFixedRate(() -> {
                SwingUtilities.invokeLater(() -> {
                    try {
                        updateMotorPanel((JPanel) frame.getContentPane().getComponent(0));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }, 0, 100, TimeUnit.MILLISECONDS);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createAndShowGUI() {
        SwingUtilities.invokeLater(() -> {
            frame = new JFrame("PLC Interface"); // Initialize frame
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setUndecorated(true);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setLayout(new BorderLayout());

            JPanel motorPanel = new JPanel(new GridBagLayout());
            motorPanel.setBackground(new Color(45, 45, 45));
            frame.add(motorPanel, BorderLayout.CENTER);

            JPanel navigationPanel = new JPanel();
            JButton prevButton = new JButton("<");
            JButton nextButton = new JButton(">");
            JButton faultsButton = new JButton("Faults");
            navigationPanel.add(prevButton);
            navigationPanel.add(nextButton);
            navigationPanel.add(faultsButton);
            frame.add(navigationPanel, BorderLayout.SOUTH);

            prevButton.addActionListener(e -> {
                if (currentPage > 0) {
                    currentPage--;
                    try {
                        updateMotorPanel(motorPanel);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });

            nextButton.addActionListener(e -> {
                if ((currentPage + 1) * 2 < motors.size()) {
                    currentPage++;
                    try {
                        updateMotorPanel(motorPanel);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });

            faultsButton.addActionListener(e -> {
                try {
                    showFaultsDialog();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });

            try {
                updateMotorPanel(motorPanel);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            frame.setVisible(true);
        });
    }

    private static void showFaultsDialog() throws IOException {
        JPanel faultsPanel = new JPanel(new BorderLayout());
        JTextArea faultsTextArea = new JTextArea(10, 30);
        faultsTextArea.setEditable(false);
        faultsTextArea.setText(getCurrentFaults());

        JButton resetButton = new JButton("Reset Faults");
        resetButton.addActionListener(e -> {
            try {
                faultsTextArea.setText(getCurrentFaults());
            } catch (Exception ex) {
                showErrorDialog("Failed to reset faults: " + ex.getMessage());
            }
        });

        faultsPanel.add(new JScrollPane(faultsTextArea), BorderLayout.CENTER);
        faultsPanel.add(resetButton, BorderLayout.SOUTH);

        JOptionPane.showMessageDialog(null, faultsPanel, "Current Faults", JOptionPane.INFORMATION_MESSAGE);
    }

    private static String getCurrentFaults() throws IOException {
        StringBuilder faults = new StringBuilder();
        for (Motor motor : motors) {
            try {
                if (commandManager.readBooleanFromDataBlock(motor.getDatablock(), motor.getFaultoffset(), motor.getFaultoffsetsub())) {
                    faults.append("Motor ").append(
                            commandManager.readStringFromDataBlock(motor.getDatablock(), 67, motor.getNameoffset(), motor.getNameoffset(), motor.getNameoffsetlength())
                    ).append(" has a fault.\n");
                }
            } catch (IOException e) {
                faults.append("Error reading motor ").append(
                        commandManager.readStringFromDataBlock(motor.getDatablock(), 67, motor.getNameoffset(), motor.getNameoffset(), motor.getNameoffsetlength())
                ).append(": ").append(e.getMessage()).append("\n");
            }
        }
        return faults.toString();
    }

    private static void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private static void updateMotorPanel(JPanel motorPanel) throws IOException {
        motorPanel.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        int start = currentPage * motorBlocksPerPage;
        int end = Math.min(start + motorBlocksPerPage, motors.size());

        for (int i = start; i < end; i++) {
            Motor motor = motors.get(i);

            JPanel motorSubPanel = new JPanel(new GridBagLayout());
            motorSubPanel.setBackground(new Color(45, 45, 45));
            try {
                motorSubPanel.setBorder(new LineBorder(getMotorStateColor(motor), 5));
            } catch (IOException e) {
                motorSubPanel.setBorder(new LineBorder(Color.RED, 2)); // Set border color to red if an exception occurs
            }
            GridBagConstraints subGbc = new GridBagConstraints();
            subGbc.fill = GridBagConstraints.HORIZONTAL;
            subGbc.insets = new Insets(10, 10, 10, 10);

            // Motor name label
            JLabel nameLabel = new JLabel("Motor: " +
                    commandManager.readStringFromDataBlock(motor.getDatablock(), 67, motor.getNameoffset(), motor.getNameoffset(), motor.getNameoffsetlength()));
            nameLabel.setForeground(new Color(200, 200, 200));
            subGbc.gridx = 0;
            subGbc.gridy = 0;
            motorSubPanel.add(nameLabel, subGbc);

            // Position panel
            JPanel positionPanel = new JPanel(new BorderLayout());
            positionPanel.setBackground(new Color(45, 45, 45));
            JLabel positionTitleLabel = new JLabel("Position");
            positionTitleLabel.setForeground(new Color(200, 200, 200));
            positionTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
            JLabel positionValueLabel = new JLabel(String.valueOf(commandManager.readIntFromDataBlock(motor.getDatablock(), motor.getFtoffset()) + " Ft "
                    + commandManager.readIntFromDataBlock(motor.getDatablock(), motor.getInoffset()) + " In"));
            positionValueLabel.setForeground(new Color(200, 200, 200));
            positionValueLabel.setHorizontalAlignment(SwingConstants.CENTER);
            positionPanel.add(positionTitleLabel, BorderLayout.NORTH);
            positionPanel.add(positionValueLabel, BorderLayout.CENTER);
            subGbc.gridy = 1;
            motorSubPanel.add(positionPanel, subGbc);

            // Up and Down buttons
            JToggleButton upButton = new JToggleButton("Up");
            JToggleButton downButton = new JToggleButton("Down");

            upButton.setBackground(Color.BLUE);
            downButton.setBackground(Color.GREEN);

            upButton.addActionListener(e -> {
                try {
                    if (commandManager.readBooleanFromDataBlock(motor.getDatablock(), motor.getUpoffset(), motor.getUpoffsetSub())) {
                        upButton.setSelected(true);
                        upButton.setBackground(Color.RED);
                        upButton.setText("Stop");
                        System.out.println("Up button pressed for : " + commandManager.readStringFromDataBlock(
                                motor.getDatablock(), 67, motor.getNameoffset(), motor.getNameoffset(), motor.getNameoffsetlength()
                        ));
                    } else {
                        upButton.setText("Up");
                        upButton.setBackground(Color.BLUE);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            });

            downButton.addActionListener(e -> {
                try {
                    if (commandManager.readBooleanFromDataBlock(motor.getDatablock(), motor.getDownoffset(), motor.getDownoffsetSub())) {
                        downButton.setText("Stop");
                        downButton.setBackground(Color.RED);
                        System.out.println("Down button pressed for : " + commandManager.readStringFromDataBlock(
                                motor.getDatablock(), 67, motor.getNameoffset(), motor.getNameoffset(), motor.getNameoffsetlength()));
                        downButton.setSelected(true);
                    } else {
                        downButton.setText("Down");
                        downButton.setBackground(Color.GREEN);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });

            subGbc.gridy = 2;
            motorSubPanel.add(upButton, subGbc);
            subGbc.gridy = 3;
            motorSubPanel.add(downButton, subGbc);

            // Vertical progress bar
            JProgressBar progressBar = new JProgressBar(JProgressBar.VERTICAL,
                    commandManager.readIntFromDataBlock(motor.getDatablock(), motor.getCountsdownoffset()),
                    commandManager.readIntFromDataBlock(motor.getDatablock(), motor.getCountsupoffset()));
            progressBar.setValue(commandManager.readIntFromDataBlock(motor.getDatablock(), motor.getCountsoffset()));
            progressBar.setForeground(new Color(100, 100, 255));
            subGbc.gridy = 0;
            subGbc.gridx = 1;
            subGbc.gridheight = 5;
            motorSubPanel.add(progressBar, subGbc);

            motorPanel.add(motorSubPanel, gbc);
        }

        motorPanel.revalidate();
        motorPanel.repaint();
    }

    private static Color getMotorStateColor(Motor motor) throws IOException {
        // Selected
        if (commandManager.readBooleanFromDataBlock(motor.getDatablock(), motor.getMotorselectedoffset(), motor.getMotorselectedoffsetsub())) {
            return Color.YELLOW;
        }
        // Faulted is not connected
        else if (!commandManager.isConnected()) {
            return Color.RED;
        }
        // Up limit
        else if (commandManager.readBooleanFromDataBlock(motor.getDatablock(), motor.getUplimitoffset(), motor.getUplimitoffsetsub())) {
            return Color.BLUE;
        }
        // Down limit
        else if (commandManager.readBooleanFromDataBlock(motor.getDatablock(), motor.getDownlimitoffset(), motor.getDownlimitoffsetsub())) {
            return Color.GREEN;
        }
        // Faulted
        else if (commandManager.readBooleanFromDataBlock(motor.getDatablock(), motor.getFaultoffset(), motor.getFaultoffsetsub())) {
            return Color.RED;
        }
        // Otherwise gray
        else if (commandManager.isConnected()) {
            return Color.LIGHT_GRAY;
        } else {
            return Color.LIGHT_GRAY;
        }
    }
}