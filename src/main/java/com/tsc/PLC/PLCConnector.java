// src/main/java/com/tsc/PLC/PLCConnector.java
package com.tsc.PLC;

import com.sourceforge.snap7.moka7.S7Client;
import com.tsc.conf.PLCConfig;

public class PLCConnector {
    private S7Client plc;
    private PLCConfig config;
    private boolean isConnected;

    public PLCConnector(PLCConfig config) {
        this.plc = new S7Client();
        this.config = config;
        this.isConnected = false;
    }

    public void setAddress(String address) {
        this.config.setAddress(address);
    }

    public void setRack(int rack) {
        this.config.setRack(rack);
    }

    public void setSlot(int slot) {
        this.config.setSlot(slot);
    }

    public int connect() {
        int result = plc.ConnectTo(config.getAddress(), config.getRack(), config.getSlot());
        if (result == 0) {
            isConnected = true;
        }
        return result;
    }

    public void disconnect() {
        if (isConnected) {
            plc.Disconnect();
            isConnected = false;
        }
    }

    public boolean isConnected() {
        return isConnected;
    }

    public String getErrorText(int errorCode) {
        return plc.ErrorText(errorCode);
    }

    public S7Client getPlc() {
        return plc;
    }
}