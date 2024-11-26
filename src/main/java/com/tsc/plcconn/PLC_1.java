package com.tsc.plcconn;

import com.tsc.PLC.PLCCommandManager;
import com.tsc.PLC.PLCConnector;

import java.io.IOException;

public class PLC_1 {
    public void plc1Method() throws IOException {
        PLCConnector plc1Connector = new PLCConnector();
        PLCCommandManager command1Manager = new PLCCommandManager(plc1Connector);
        String address1 = "192.168.0.1";
        int rack = 0;
        int slot = 1;
        command1Manager.executeConnectCommand(address1, rack, slot);
        if (plc1Connector.isConnected()) {
            String data = command1Manager.readDataBlock(1, 516, 0, 0, 32);
            System.out.println("Data read from DB" + 1 + ": " + data);

            // Write string to data block
            command1Manager.writeDataBlock(1, 0, "                               ");//clear datablock
            System.out.println("Data written to DB");
            command1Manager.executeDisconnectCommand();
        } else {
            System.out.println("Failed to connect to PLC at " + address1 + ": " + plc1Connector.getErrorText(0));
        }
    }
}