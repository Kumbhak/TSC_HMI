package com.tsc.conf;

public class PLCConfig {
    private String address;
    private int rack;
    private int slot;

    public PLCConfig() {
    }

    public PLCConfig(String address, int rack, int slot) {
        this.address = address;
        this.rack = rack;
        this.slot = slot;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getRack() {
        return rack;
    }

    public void setRack(int rack) {
        this.rack = rack;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }
}