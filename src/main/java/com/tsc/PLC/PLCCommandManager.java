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
    public boolean isConnected() {
        return plcConnector.isConnected();
    }

    public String readStringFromDataBlock(int dbNumber, int dbSize, int dbStart, int stringByte, int stringLength) throws IOException {
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

    public void writeStringToDataBlock(int dbNumber, int dbStart, String data) throws IOException {
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
    public void writeBooleanToDataBlock(int dbNumber, int dbStart, int bitIndex, boolean value) throws IOException {
        if (!plcConnector.isConnected()) {
            throw new IOException("PLC is not connected");
        }

        byte[] buffer = new byte[1];
        int result = plcConnector.getPlc().ReadArea(S7.S7AreaDB, dbNumber, dbStart, 1, buffer);
        if (result != 0) {
            throw new IOException("Failed to read data block: " + plcConnector.getErrorText(result));
        }

        if (value) {
            buffer[0] |= (1 << bitIndex);
        } else {
            buffer[0] &= ~(1 << bitIndex);
        }

        result = plcConnector.getPlc().WriteArea(S7.S7AreaDB, dbNumber, dbStart, 1, buffer);
        if (result != 0) {
            throw new IOException("Failed to write data block: " + plcConnector.getErrorText(result));
        }
    }
    public boolean readBooleanFromDataBlock(int dbNumber, int dbStart, int bitIndex) throws IOException {
        if (!plcConnector.isConnected()) {
            throw new IOException("PLC is not connected");
        }

        byte[] buffer = new byte[1];
        int result = plcConnector.getPlc().ReadArea(S7.S7AreaDB, dbNumber, dbStart, 1, buffer);
        if (result != 0) {
            throw new IOException("Failed to read data block: " + plcConnector.getErrorText(result));
        }

        return (buffer[0] & (1 << bitIndex)) != 0;
    }
    // Method to read an int value from a data block
    public int readIntFromDataBlock(int dbNumber, int dbStart) throws IOException {
        if (!plcConnector.isConnected()) {
            throw new IOException("PLC is not connected");
        }

        byte[] buffer = new byte[2]; // int is 2 bytes
        int result = plcConnector.getPlc().ReadArea(S7.S7AreaDB, dbNumber, dbStart, 2, buffer);
        if (result != 0) {
            throw new IOException("Failed to read data block: " + plcConnector.getErrorText(result));
        }

        return S7.GetShortAt(buffer, 0);
    }

    // Method to write an int value to a data block
    public void writeIntToDataBlock(int dbNumber, int dbStart, int value) throws IOException {
        if (!plcConnector.isConnected()) {
            throw new IOException("PLC is not connected");
        }

        byte[] buffer = new byte[2];
        S7.SetShortAt(buffer, 0, value);

        int result = plcConnector.getPlc().WriteArea(S7.S7AreaDB, dbNumber, dbStart, 2, buffer);
        if (result != 0) {
            throw new IOException("Failed to write data block: " + plcConnector.getErrorText(result));
        }
    }

    // Method to read a real value from a data block
    public float readRealFromDataBlock(int dbNumber, int dbStart) throws IOException {
        if (!plcConnector.isConnected()) {
            throw new IOException("PLC is not connected");
        }

        byte[] buffer = new byte[4]; // real is 4 bytes
        int result = plcConnector.getPlc().ReadArea(S7.S7AreaDB, dbNumber, dbStart, 4, buffer);
        if (result != 0) {
            throw new IOException("Failed to read data block: " + plcConnector.getErrorText(result));
        }

        return S7.GetFloatAt(buffer, 0);
    }

    // Method to write a real value to a data block
    public void writeRealToDataBlock(int dbNumber, int dbStart, float value) throws IOException {
        if (!plcConnector.isConnected()) {
            throw new IOException("PLC is not connected");
        }

        byte[] buffer = new byte[4];
        S7.SetFloatAt(buffer, 0, value);

        int result = plcConnector.getPlc().WriteArea(S7.S7AreaDB, dbNumber, dbStart, 4, buffer);
        if (result != 0) {
            throw new IOException("Failed to write data block: " + plcConnector.getErrorText(result));
        }
    }

}