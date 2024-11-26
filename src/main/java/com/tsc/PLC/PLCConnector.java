package com.tsc.PLC;

import com.sourceforge.snap7.moka7.*;

import java.io.IOException;

public class PLCConnector {
    private S7Client plc;
    private String address;
    private int rack;
    private int slot;
    private boolean isConnected;

    public PLCConnector() {
        this.plc = new S7Client();
        this.isConnected = false;
        this.address = "192.168.0.1";
        this.rack = 0;
        this.slot = 1;

    }


    public void setAddress(String address) {
        this.address = address;
    }

    public void setRack(int rack) {
        this.rack = rack;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public int connect() {
        int result = plc.ConnectTo(address, rack, slot);
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