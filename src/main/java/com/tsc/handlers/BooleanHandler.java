package com.tsc.handlers;

import com.sourceforge.snap7.moka7.S7;
import com.sourceforge.snap7.moka7.S7Client;

public class BooleanHandler {
    public static void main(String[] args) {
        String address = "192.168.0.1"; // Replace with your PLC's IP address
        int rack = 0;
        int slot = 1;
        int dbNumber = 1; // Replace with your DB number
        int byteIndex = 2;  // Byte index where the boolean is stored
        int bitIndex = 3;   // Bit index in the byte where the boolean is stored

        // Create a client and connect to the PLC
        S7Client plc = new S7Client();
        int result = plc.ConnectTo(address, rack, slot);

        if (result == 0) {
            System.out.println("Connected to PLC at " + address);

            // Read data block
            byte[] buffer = new byte[1];
            result = plc.ReadArea(S7.S7AreaDB, dbNumber, byteIndex, 1, buffer);

            if (result == 0) {
                // Read the boolean value
                boolean booleanValue = (buffer[0] & (1 << bitIndex)) != 0;
                System.out.println("Boolean value at DB" + dbNumber + " byte " + byteIndex + " bit " + bitIndex + ": " + booleanValue);

                // Example: Flip the boolean value and write it back
                buffer[0] ^= (1 << bitIndex); // Flip the specific bit
                result = plc.WriteArea(S7.S7AreaDB, dbNumber, byteIndex, 1, buffer);

                if (result == 0) {
                    System.out.println("Successfully wrote new boolean value.");
                } else {
                    System.out.println("Failed to write boolean value: " + S7Client.ErrorText(result));
                }
            } else {
                System.out.println("Failed to read boolean value: " + S7Client.ErrorText(result));
            }

            plc.Disconnect();
        } else {
            System.out.println("Failed to connect to PLC at " + address + ": " + S7Client.ErrorText(result));
        }
    }
}