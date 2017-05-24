package com.bolong.bochetong.bean;

import java.io.Serializable;

/**
 * Created by admin on 2017/5/3.
 */

public class Fee implements Serializable {


    /**
     * id : 12311
     * parkName : 惠康家园六区
     * carNumber : 苏79237
     * startstopTime : 2017-03-02 12:12:09 - 2017-03-05 13:22:00
     * durationTime : 1天0小时14分
     * shoudPay : 0.02
     * delivered : 0.01
     * arrearageCost : 0.01
     */

    private String id;
    private String parkName;
    private String carNumber;
    private String startstopTime;
    private String durationTime;
    private double shoudPay;
    private double delivered;
    private double arrearageCost;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParkName() {
        return parkName;
    }

    public void setParkName(String parkName) {
        this.parkName = parkName;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getStartstopTime() {
        return startstopTime;
    }

    public void setStartstopTime(String startstopTime) {
        this.startstopTime = startstopTime;
    }

    public String getDurationTime() {
        return durationTime;
    }

    public void setDurationTime(String durationTime) {
        this.durationTime = durationTime;
    }

    public double getShoudPay() {
        return shoudPay;
    }

    public void setShoudPay(double shoudPay) {
        this.shoudPay = shoudPay;
    }

    public double getDelivered() {
        return delivered;
    }

    public void setDelivered(double delivered) {
        this.delivered = delivered;
    }

    public double getArrearageCost() {
        return arrearageCost;
    }

    public void setArrearageCost(double arrearageCost) {
        this.arrearageCost = arrearageCost;
    }
}
