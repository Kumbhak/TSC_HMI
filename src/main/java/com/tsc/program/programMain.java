package com.tsc.program;

import com.tsc.PLC.PLCCommandManager;
import com.tsc.PLC.PLCConnector;
import com.tsc.conf.Motor;
import com.tsc.conf.motorConfig;
import com.tsc.conf.PLCConfig;
import com.tsc.conf.PLCConfigManager;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class programMain {
    private static int currentPage = 0;
    private static List<Motor> motors;
    private static PLCCommandManager commandManager;
    private static JLabel statusBar;

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

            createAndShowGUI();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void createAndShowGUI() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("PLC Interface");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setUndecorated(true); // Remove window borders and title bar
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximize the window to cover the entire screen
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
                    updateMotorPanel(motorPanel);
                }
            });

            nextButton.addActionListener(e -> {
                if ((currentPage + 1) * 2 < motors.size()) {
                    currentPage++;
                    updateMotorPanel(motorPanel);
                }
            });

            faultsButton.addActionListener(e -> showFaultsDialog());

            updateMotorPanel(motorPanel);

            // Timer to update motor panel every 500 milliseconds
            Timer timer = new Timer(500, e -> {
                updateMotorPanel(motorPanel);
            });
            timer.start();

            frame.setVisible(true);
        });
    }

    private static void showFaultsDialog() {
        JPanel faultsPanel = new JPanel(new BorderLayout());
        JTextArea faultsTextArea = new JTextArea(10, 30);
        faultsTextArea.setEditable(false);
        faultsTextArea.setText(getCurrentFaults());

        JButton resetButton = new JButton("Reset Faults");
        resetButton.addActionListener(e -> {
            try {
                commandManager.executeDisconnectCommand();
                commandManager.executeConnectCommand("PLC_ADDRESS", 0, 1); // Replace with actual PLC address, rack, and slot
                faultsTextArea.setText(getCurrentFaults());
            } catch (IOException ex) {
                showErrorDialog("Failed to reset PLC connection: " + ex.getMessage());
            }
        });

        faultsPanel.add(new JScrollPane(faultsTextArea), BorderLayout.CENTER);
        faultsPanel.add(resetButton, BorderLayout.SOUTH);

        JOptionPane.showMessageDialog(null, faultsPanel, "Current Faults", JOptionPane.INFORMATION_MESSAGE);
    }

    private static String getCurrentFaults() {
        StringBuilder faults = new StringBuilder();
        for (Motor motor : motors) {
            try {
                if (commandManager.readBooleanFromDataBlock(motor.getDatablock(), 0, 3)) {
                    faults.append("Motor ").append(motor.getName()).append(" has a fault.\n");
                }
            } catch (IOException e) {
                faults.append("Error reading motor ").append(motor.getName()).append(": ").append(e.getMessage()).append("\n");
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

        int start = currentPage * 2;
        int end = Math.min(start + 2, motors.size());

        for (int i = start; i < end; i++) {
            Motor motor = motors.get(i);

            JPanel motorSubPanel = new JPanel(new GridBagLayout());
            motorSubPanel.setBackground(new Color(45, 45, 45));
            motorSubPanel.setBorder(new LineBorder(getMotorStateColor(motor), 2)); // Set border color based on motor state
            GridBagConstraints subGbc = new GridBagConstraints();
            subGbc.fill = GridBagConstraints.HORIZONTAL;
            subGbc.insets = new Insets(10, 10, 10, 10);

            // Motor name label
            JLabel nameLabel = new JLabel("Motor: " + motor.getName());
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
            JLabel positionValueLabel = new JLabel(String.valueOf(motor.getDatablock()));
            positionValueLabel.setForeground(new Color(200, 200, 200));
            positionValueLabel.setHorizontalAlignment(SwingConstants.CENTER);
            JLabel positionUnitLabel = new JLabel("feet");
            positionUnitLabel.setForeground(new Color(200, 200, 200));
            positionUnitLabel.setHorizontalAlignment(SwingConstants.CENTER);
            positionPanel.add(positionTitleLabel, BorderLayout.NORTH);
            positionPanel.add(positionValueLabel, BorderLayout.CENTER);
            positionPanel.add(positionUnitLabel, BorderLayout.SOUTH);
            subGbc.gridy = 1;
            motorSubPanel.add(positionPanel, subGbc);

            // Up and Down buttons
            JToggleButton upButton = new JToggleButton("Up");
            JToggleButton downButton = new JToggleButton("Down");

            upButton.setBackground(Color.BLUE);
            downButton.setBackground(Color.GREEN);

            upButton.addActionListener(e -> {
                if (upButton.isSelected()) {
                    downButton.setSelected(false);
                    upButton.setBackground(Color.RED);
                    upButton.setText("Stop");
                    System.out.println("Up button clicked for motor: " + motor.getDatablock());
                    try {
                        commandManager.writeBooleanToDataBlock(motor.getDatablock(), 0, 0, true);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    upButton.setBackground(Color.BLUE);
                    upButton.setText("Up");
                    try {
                        commandManager.writeBooleanToDataBlock(motor.getDatablock(), 0, 0, false);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });

            downButton.addActionListener(e -> {
                if (downButton.isSelected()) {
                    upButton.setSelected(false);
                    downButton.setBackground(Color.RED);
                    downButton.setText("Stop");
                    System.out.println("Down button clicked for motor: " + motor.getDatablock());
                    try {
                        commandManager.writeBooleanToDataBlock(motor.getDatablock(), 0, 1, true);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    downButton.setBackground(Color.GREEN);
                    downButton.setText("Down");
                    try {
                        commandManager.writeBooleanToDataBlock(motor.getDatablock(), 0, 1, false);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });

            subGbc.gridy = 2;
            motorSubPanel.add(upButton, subGbc);
            subGbc.gridy = 3;
            motorSubPanel.add(downButton, subGbc);

            // Vertical progress bar
            JProgressBar progressBar = new JProgressBar(JProgressBar.VERTICAL, 0, 100);
            progressBar.setValue(getMotorPosition(motor));
            progressBar.setForeground(new Color(0, 113, 206));
            subGbc.gridy = 0;
            subGbc.gridx = 1;
            subGbc.gridheight = 5;
            motorSubPanel.add(progressBar, subGbc);

            motorPanel.add(motorSubPanel, gbc);
        }

        motorPanel.revalidate();
        motorPanel.repaint();
    }

    private static int getMotorPosition(Motor motor) {
        // Placeholder logic for motor position
        return motor.getDatablock() * 10;
    }
    private static Color getMotorStateColor(Motor motor) throws IOException {
        //selected
        if (commandManager.readBooleanFromDataBlock(motor.getDatablock(), 0, 0)) {
            return Color.YELLOW;
            //up limit
        }
        else if(!commandManager.isConnected()) {
            return Color.RED;
        }
        else if(commandManager.readBooleanFromDataBlock(motor.getDatablock(), 0, 1)) {
            return Color.BLUE;
            //down limit
        } else if(commandManager.readBooleanFromDataBlock(motor.getDatablock(), 0, 2)) {
            return Color.GREEN;
            //faulted
        } else if (commandManager.readBooleanFromDataBlock(motor.getDatablock(), 0, 3)) {
            return Color.RED;
            //otherwise gray
        } else {
            return Color.LIGHT_GRAY;
        }
    }
}