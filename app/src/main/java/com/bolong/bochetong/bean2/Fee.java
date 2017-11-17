package com.bolong.bochetong.bean2;

/**
 * Created by admin on 2017/6/23.
 */

public class Fee {

    /**
     * id : TNO2017061109234804800003576
     * price : 56.5
     * carNumber : 京P19A80
     * parkPlace : 惠康嘉园(二区,三区,四区,五区,六区)地上停车场
     * TimeSlot : 2017.06.06 17:38 - 2017.06.11 09:23
     */

    private String id;
    private double price;
    private String carNumber;
    private String parkPlace;
    private String TimeSlot;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getParkPlace() {
        return parkPlace;
    }

    public void setParkPlace(String parkPlace) {
        this.parkPlace = parkPlace;
    }

    public String getTimeSlot() {
        return TimeSlot;
    }

    public void setTimeSlot(String TimeSlot) {
        this.TimeSlot = TimeSlot;
    }
}
