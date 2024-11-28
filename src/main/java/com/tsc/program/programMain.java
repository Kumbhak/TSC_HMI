package com.tsc.program;

import com.tsc.PLC.PLCCommandManager;
import com.tsc.PLC.PLCConnector;
import com.tsc.conf.Motor;
import com.tsc.conf.motorConfig;
import com.tsc.conf.PLCConfig;
import com.tsc.conf.PLCConfigManager;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class programMain {
    private static int currentPage = 0;
    private static List<Motor> motors;
    private static PLCCommandManager commandManager;

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
        navigationPanel.add(prevButton);
        navigationPanel.add(nextButton);
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

        updateMotorPanel(motorPanel);

        frame.setVisible(true);
    }

    private static void updateMotorPanel(JPanel motorPanel) {
        motorPanel.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        int start = currentPage * 2;
        int end = Math.min(start + 2, motors.size());

        for (int i = start; i < end; i++) {
            Motor motor = motors.get(i);

            // Motor name label
            JLabel nameLabel = new JLabel("Motor: " + motor.getName());
            nameLabel.setForeground(new Color(200, 200, 200));
            gbc.gridx = i % 2 == 0 ? 0 : 1;
            gbc.gridy = 0;
            motorPanel.add(nameLabel, gbc);

            // Motor position label
            JLabel positionLabel = new JLabel("Position: " + motor.getDatablock());
            positionLabel.setForeground(new Color(200, 200, 200));
            gbc.gridy = 1;
            motorPanel.add(positionLabel, gbc);

            // Set name text field and button
            JTextField nameField = new JTextField(10);
            JButton setNameButton = new JButton("Set Name");
            setNameButton.addActionListener(e -> motor.setName(nameField.getText()));
            gbc.gridy = 2;
            motorPanel.add(nameField, gbc);
            gbc.gridy = 3;
            motorPanel.add(setNameButton, gbc);

            // Up, Down, and Stop buttons
            JToggleButton upButton = new JToggleButton("Up");
            JToggleButton downButton = new JToggleButton("Down");
            JButton stopButton = new JButton("Stop");

            upButton.addActionListener(e -> {
                if (upButton.isSelected()) {
                    downButton.setSelected(false);
                    System.out.println("Up button clicked for motor: " + motor.getDatablock());
                    try {
                        commandManager.writeBooleanToDataBlock(1, motor.getDatablock(), 0, true);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            downButton.addActionListener(e -> {
                if (downButton.isSelected()) {
                    upButton.setSelected(false);
                }
            });

            gbc.gridy = 4;
            motorPanel.add(upButton, gbc);
            gbc.gridy = 5;
            motorPanel.add(downButton, gbc);
            gbc.gridy = 6;
            motorPanel.add(stopButton, gbc);

            // Read data from PLC and display
            try {
                String plcData = commandManager.readStringFromDataBlock(1, 256, 0, 2, 10);
                JLabel plcDataLabel = new JLabel("PLC Data: " + plcData);
                plcDataLabel.setForeground(new Color(200, 200, 200));
                gbc.gridy = 7;
                motorPanel.add(plcDataLabel, gbc);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        motorPanel.revalidate();
        motorPanel.repaint();
    }
}