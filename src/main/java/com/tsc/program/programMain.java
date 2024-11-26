package com.tsc.program;

import com.tsc.PLC.PLCCommandManager;
import com.tsc.PLC.PLCConnector;

import javax.swing.*;
import javax.swing.text.StyledEditorKit;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class programMain {
    public static void main() {
        JFrame frame = new JFrame("PLC Interface");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(true); // Remove window borders
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10); // Padding

        JTextField textField = new JTextField(20);
        textField.setPreferredSize(new Dimension(400, 200));
        textField.setFont(new Font("Arial", Font.PLAIN, 20));
        JButton getDataButton = new JButton("Get Data");
        getDataButton.setPreferredSize(new Dimension(400, 200));
        JButton sendDataButton = new JButton("Send Data");
        sendDataButton.setPreferredSize(new Dimension(400, 200));
        JLabel label = new JLabel("PLC Data will be displayed here");
        label.setPreferredSize(new Dimension(400, 200));
        label.setFont(new Font("Arial", Font.PLAIN, 20));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        frame.add(textField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        frame.add(getDataButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        frame.add(sendDataButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        frame.add(label, gbc);

        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        if (gd.isFullScreenSupported()) {
            gd.setFullScreenWindow(frame);
        } else {
            System.err.println("Fullscreen mode not supported");
            frame.setSize(800, 480); // Fallback to windowed mode
            frame.setVisible(true);
        }

        getDataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PLCConnector plcConnector = new PLCConnector();
                PLCCommandManager commandManager = new PLCCommandManager(plcConnector);
                try {
                    commandManager.executeConnectCommand("192.168.0.1", 0, 1);
                    if (plcConnector.isConnected()) {
                        String data = commandManager.readDataBlock(1, 516, 0, 0, 32);
                        label.setText(data);
                        commandManager.executeDisconnectCommand();
                    } else {
                        label.setText("Failed to connect to PLC");
                    }
                } catch (IOException ex) {
                    label.setText("Error: " + ex.getMessage());
                }
            }
        });

        sendDataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PLCConnector plcConnector = new PLCConnector();
                PLCCommandManager commandManager = new PLCCommandManager(plcConnector);
                try {
                    commandManager.executeConnectCommand("192.168.0.1", 0, 1);
                    if (plcConnector.isConnected()) {
                        String dataToSend = textField.getText();
                        commandManager.writeDataBlock(1, 0, "                               ");//clear the block
                        commandManager.writeDataBlock(1, 0, dataToSend);
                        label.setText("Data sent to PLC");
                        commandManager.executeDisconnectCommand();
                    } else {
                        label.setText("Failed to connect to PLC");
                    }
                } catch (IOException ex) {
                    label.setText("Error: " + ex.getMessage());
                }
            }
        });
    }
}