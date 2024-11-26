package com.tsc.PLC;

import com.sourceforge.snap7.moka7.S7;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class PLCCommandManager {
    private final PLCConnector plcConnector;

    public PLCCommandManager(PLCConnector plcConnector) {
        this.plcConnector = plcConnector;
    }

    public void executeConnectCommand(String address, int rack, int slot) throws IOException {
        plcConnector.setAddress(address);
        plcConnector.setRack(rack);
        plcConnector.setSlot(slot);
        int result = plcConnector.connect();
        if (result != 0) {
            throw new IOException("Failed to connect to PLC: " + plcConnector.getErrorText(result));
        }
    }

    public void executeDisconnectCommand() throws IOException {
        plcConnector.disconnect();
    }

    public String readDataBlock(int dbNumber, int dbSize, int dbStart, int stringByte, int stringLength) throws IOException {
        if (!plcConnector.isConnected()) {
            throw new IOException("PLC is not connected");
        }

        byte[] buffer = new byte[dbSize];
        int result = plcConnector.getPlc().ReadArea(S7.S7AreaDB, dbNumber, dbStart, dbSize, buffer);
        if (result != 0) {
            throw new IOException("Failed to read data block: " + plcConnector.getErrorText(result));
        }

        return new String(buffer, stringByte, stringLength, StandardCharsets.UTF_8);
    }

    public void writeDataBlock(int dbNumber, int dbStart, String data) throws IOException {
        if (!plcConnector.isConnected()) {
            throw new IOException("PLC is not connected");
        }

        byte[] stringBytes = data.getBytes(StandardCharsets.UTF_8);
        byte[] buffer = new byte[stringBytes.length + 2];
        buffer[0] = (byte) 254; // Maximum length of the string (adjust as needed)
        buffer[1] = (byte) stringBytes.length; // Current length of the string
        System.arraycopy(stringBytes, 0, buffer, 2, stringBytes.length);

        int result = plcConnector.getPlc().WriteArea(S7.S7AreaDB, dbNumber, dbStart, buffer.length, buffer);
        if (result != 0) {
            throw new IOException("Failed to write data block: " + plcConnector.getErrorText(result));
        }
    }
}