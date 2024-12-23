package com.tsc.conf;

public class Motor {
    private String name;
    private int datablock;

    // Default constructor
    public Motor() {
    }

    // Parameterized constructor
    public Motor(String name, int datablock) {
        this.name = name;
        this.datablock = datablock;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDatablock() {
        return datablock;
    }

    public void setDatablock(int datablock) {
        this.datablock = datablock;
    }


}