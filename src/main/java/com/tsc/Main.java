
package com.tsc;

import com.tsc.conf.PLCConfig;
import com.tsc.conf.PLCConfigManager;
import com.tsc.PLC.PLCConnector;
import com.tsc.PLC.PLCCommandManager;
import com.tsc.program.programMain;
import com.tsc.conf.motorConfig;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        // Create config directory
        motorConfig.createConfigDirectory();
        PLCConfigManager.createConfigDirectory();
        //Load config
        List<PLCConfig> plcConfigs = PLCConfigManager.loadConfig(Paths.get("config", "PLC.json").toString());

        for (PLCConfig config : plcConfigs) {
            PLCConnector plcConnector = new PLCConnector(config);
            PLCCommandManager commandManager = new PLCCommandManager(plcConnector);

            int result = plcConnector.connect();
            if (result == 0) {
                System.out.println("Connected to PLC at " + config.getAddress());
                try {
                    String data = commandManager.readStringFromDataBlock(1, 256, 0, 2, 10);
                    System.out.println("Read data: " + data);
                } catch (IOException e) {
                    System.err.println("Error reading data: " + e.getMessage());
                }

            } else {
                System.out.println("Failed to connect to PLC at " + config.getAddress() + ": " + plcConnector.getErrorText(result));
            }
        }

        // Load motor configurations
        motorConfig motorConfigInstance = motorConfig.loadConfig(Paths.get("config", "motorConfig.json").toString());

        programMain.main();
    }
}