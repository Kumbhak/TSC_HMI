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
        this.nameoffset = nameoffset;
        this.nameoffsetlength = nameoffsetlength;
        this.upoffset = upoffset;
        this.upoffsetSub = upoffsetSub;
        this.downoffset = downoffset;
        this.downoffsetSub = downoffsetSub;
        this.p2offset = p2offset;
        this.p2offsetSub = p2offsetSub;
        this.p3offset = p3offset;
        this.p3offsetSub = p3offsetSub;
        this.uplimitoffset = uplimitoffset;
        this.uplimitoffsetsub = uplimitoffsetsub;
        this.downlimitoffset = downlimitoffset;
        this.downlimitoffsetsub = downlimitoffsetsub;
        this.motor1selectoffset = motor1selectoffset;
        this.motor1selectoffsetsub = motor1selectoffsetsub;
        this.motor2selectoffset = motor2selectoffset;
        this.motor2selectoffsetsub = motor2selectoffsetsub;

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