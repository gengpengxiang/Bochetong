package com.bolong.bochetong.bean;

import java.util.List;

public class Timer {

    /**
     * enterTime : 1496960366
     * enterTimeStr : 2017.06.09 06:19
     * nowTime : 1497249489
     * carCard : 冀G98888
     * carRecordId : 12344
     * parkName : 惠康嘉园(二区,三区,四区,五区,六区)地上停车场
     * nowPrice : 75.0
     */

    private String enterTime;
    private String enterTimeStr;
    private String nowTime;
    private String carCard;
    private String carRecordId;
    private String parkName;
    private String nowPrice;


    public String getEnterTime() {
        return enterTime;
    }

    public void setEnterTime(String enterTime) {
        this.enterTime = enterTime;
    }

    public String getEnterTimeStr() {
        return enterTimeStr;
    }

    public void setEnterTimeStr(String enterTimeStr) {
        this.enterTimeStr = enterTimeStr;
    }

    public String getNowTime() {
        return nowTime;
    }

    public void setNowTime(String nowTime) {
        this.nowTime = nowTime;
    }

    public String getCarCard() {
        return carCard;
    }

    public void setCarCard(String carCard) {
        this.carCard = carCard;
    }

    public String getCarRecordId() {
        return carRecordId;
    }

    public void setCarRecordId(String carRecordId) {
        this.carRecordId = carRecordId;
    }

    public String getParkName() {
        return parkName;
    }

    public void setParkName(String parkName) {
        this.parkName = parkName;
    }

    public String getNowPrice() {
        return nowPrice;
    }

    public void setNowPrice(String nowPrice) {
        this.nowPrice = nowPrice;
    }
}
