package com.tsc.conf;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Motor {
    private int datablock;
    private int nameoffset = 2;
    private int nameoffsetlength = 64;
    private int motornumberoffset = 68;
    private int upoffset = 0;
    private int upoffsetSub = 0;
    private int downoffset = 0;
    private int downoffsetSub = 1;
    private int p2offset = 0;
    private int p2offsetSub = 2;
    private int p3offset = 0;
    private int p3offsetSub = 3;
    private int uplimitoffset = 0;
    private int uplimitoffsetsub = 4;
    private int downlimitoffset = 0;
    private int downlimitoffsetsub = 5;
    private int motorselectedoffset = 0;
    private int motorselectedoffsetsub = 6;
    private int faultoffset = 1;
    private int faultoffsetsub = 0;
    private int scaleoffset = 70;
    private int countsupoffset = 74;
    private int countsdownoffset = 76;
    private int countsoffset = 78;
    private int ftoffset = 80;
    private int inoffset = 82;

    // Default constructor
    public Motor() {
    }

    // Parameterized constructor
    public Motor(int datablock, int nameoffset, int nameoffsetlength, int motornumberoffset,
                 int upoffset, int upoffsetSub, int downoffset, int downoffsetSub,
                 int p2offset, int p2offsetSub, int p3offset, int p3offsetSub,
                 int uplimitoffset, int uplimitoffsetsub, int downlimitoffset, int downlimitoffsetsub,
                 int motorselectedoffset, int motorselectedoffsetsub,
                 int faultoffset, int faultoffsetsub, int scaleoffset, int countsupoffset, int countsdownoffset,
                 int countsoffset, int ftoffset, int inoffset) {
        this.datablock = datablock;
        this.nameoffset = nameoffset;
        this.nameoffsetlength = nameoffsetlength;
        this.motornumberoffset = motornumberoffset;
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
        this.motorselectedoffset = motorselectedoffset;
        this.motorselectedoffsetsub = motorselectedoffsetsub;
        this.faultoffset = faultoffset;
        this.faultoffsetsub = faultoffsetsub;
        this.scaleoffset = scaleoffset;
        this.countsupoffset = countsupoffset;
        this.countsdownoffset = countsdownoffset;
        this.countsoffset = countsoffset;
        this.ftoffset = ftoffset;
        this.inoffset = inoffset;
    }

    @JsonProperty
    public int getDatablock() {
        return datablock;
    }

    public void setDatablock(int datablock) {
        this.datablock = datablock;
    }

    @JsonProperty
    public int getNameoffset() {
        return nameoffset;
    }

    public void setNameoffset(int nameoffset) {
        this.nameoffset = nameoffset;
    }

    @JsonProperty
    public int getNameoffsetlength() {
        return nameoffsetlength;
    }

    public void setNameoffsetlength(int nameoffsetlength) {
        this.nameoffsetlength = nameoffsetlength;
    }

    @JsonProperty
    public int getMotornumberoffset() {
        return motornumberoffset;
    }

    public void setMotornumberoffset(int motornumberoffset) {
        this.motornumberoffset = motornumberoffset;
    }

    @JsonProperty
    public int getUpoffset() {
        return upoffset;
    }

    public void setUpoffset(int upoffset) {
        this.upoffset = upoffset;
    }

    @JsonProperty
    public int getUpoffsetSub() {
        return upoffsetSub;
    }

    public void setUpoffsetSub(int upoffsetSub) {
        this.upoffsetSub = upoffsetSub;
    }

    @JsonProperty
    public int getDownoffset() {
        return downoffset;
    }

    public void setDownoffset(int downoffset) {
        this.downoffset = downoffset;
    }

    @JsonProperty
    public int getDownoffsetSub() {
        return downoffsetSub;
    }

    public void setDownoffsetSub(int downoffsetSub) {
        this.downoffsetSub = downoffsetSub;
    }

    @JsonProperty
    public int getP2offset() {
        return p2offset;
    }

    public void setP2offset(int p2offset) {
        this.p2offset = p2offset;
    }

    @JsonProperty
    public int getP2offsetSub() {
        return p2offsetSub;
    }

    public void setP2offsetSub(int p2offsetSub) {
        this.p2offsetSub = p2offsetSub;
    }

    @JsonProperty
    public int getP3offset() {
        return p3offset;
    }

    public void setP3offset(int p3offset) {
        this.p3offset = p3offset;
    }

    @JsonProperty
    public int getP3offsetSub() {
        return p3offsetSub;
    }

    public void setP3offsetSub(int p3offsetSub) {
        this.p3offsetSub = p3offsetSub;
    }

    @JsonProperty
    public int getUplimitoffset() {
        return uplimitoffset;
    }

    public void setUplimitoffset(int uplimitoffset) {
        this.uplimitoffset = uplimitoffset;
    }

    @JsonProperty
    public int getUplimitoffsetsub() {
        return uplimitoffsetsub;
    }

    public void setUplimitoffsetsub(int uplimitoffsetsub) {
        this.uplimitoffsetsub = uplimitoffsetsub;
    }

    @JsonProperty
    public int getDownlimitoffset() {
        return downlimitoffset;
    }

    public void setDownlimitoffset(int downlimitoffset) {
        this.downlimitoffset = downlimitoffset;
    }

    @JsonProperty
    public int getDownlimitoffsetsub() {
        return downlimitoffsetsub;
    }

    public void setDownlimitoffsetsub(int downlimitoffsetsub) {
        this.downlimitoffsetsub = downlimitoffsetsub;
    }

    @JsonProperty
    public int getMotorselectedoffset() {
        return motorselectedoffset;
    }

    public void setMotorselectedoffset(int motorselectedoffset) {
        this.motorselectedoffset = motorselectedoffset;
    }

    @JsonProperty
    public int getMotorselectedoffsetsub() {
        return motorselectedoffsetsub;
    }

    public void setMotorselectedoffsetsub(int motorselectedoffsetsub) {
        this.motorselectedoffsetsub = motorselectedoffsetsub;
    }

    @JsonProperty
    public int getFaultoffset() {
        return faultoffset;
    }

    public void setFaultoffset(int faultoffset) {
        this.faultoffset = faultoffset;
    }

    @JsonProperty
    public int getFaultoffsetsub() {
        return faultoffsetsub;
    }

    public void setFaultoffsetsub(int faultoffsetsub) {
        this.faultoffsetsub = faultoffsetsub;
    }

    @JsonProperty
    public int getScaleoffset() {
        return scaleoffset;
    }

    public void setScaleoffset(int scaleoffset) {
        this.scaleoffset = scaleoffset;
    }

    @JsonProperty
    public int getCountsupoffset() {
        return countsupoffset;
    }

    public void setCountsupoffset(int countsupoffset) {
        this.countsupoffset = countsupoffset;
    }

    @JsonProperty
    public int getCountsdownoffset() {
        return countsdownoffset;
    }

    public void setCountsdownoffset(int countsdownoffset) {
        this.countsdownoffset = countsdownoffset;
    }

    @JsonProperty
    public int getCountsoffset() {
        return countsoffset;
    }

    public void setCountsoffset(int countsoffset) {
        this.countsoffset = countsoffset;
    }

    @JsonProperty
    public int getFtoffset() {
        return ftoffset;
    }

    public void setFtoffset(int ftoffset) {
        this.ftoffset = ftoffset;
    }

    @JsonProperty
    public int getInoffset() {
        return inoffset;
    }

    public void setInoffset(int inoffset) {
        this.inoffset = inoffset;
    }
}