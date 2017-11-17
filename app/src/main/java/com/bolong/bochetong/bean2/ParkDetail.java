package com.bolong.bochetong.bean2;

/**
 * Created by admin on 2017/6/15.
 */

public class ParkDetail {

    /**
     * id : 1
     * parkName : 惠康嘉园(二区,三区,四区,五区,六区)地上停车场
     * emptyPosition : 9452
     * parkAddress : 门头沟区惠康嘉园
     * parkPicture : https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1495126912371&di=e88932cd645638f17e685908f4b769a1&imgtype=0&src=http%3A%2F%2Fwww.mzsky.cc%2Fdata%2Fattachment%2Fforum%2F201510%2F01%2F094051yfhpz8zuspkzazqq.jpg
     * partLongitude : 116.118269
     * partLatitude : 39.915135
     * cityId : null
     * parkDistance : 11.2km
     * dayReferenceFee : 07:00-21:00 1.0元/1小时
     * nightReferenceFee : 21:00-07:00 0.5元/1小时
     * chargeDetail : 首小时免费，第二小时起1元/小时；夜间(晚9点-早7点)0.5元/小时
     * "parkingProfile": "添加停车场简介后,在此处显示"
     * "firstPrice": "1.0元/2小时"
     */

    private String id;
    private String parkName;
    private int emptyPosition;
    private String parkAddress;
    private String parkPicture;
    private double partLongitude;
    private double partLatitude;
    private Object cityId;
    private String parkDistance;
    private String dayReferenceFee;
    private String nightReferenceFee;
    private String chargeDetail;
    private String parkingProfile;
    private String firstPrice;

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

    public int getEmptyPosition() {
        return emptyPosition;
    }

    public void setEmptyPosition(int emptyPosition) {
        this.emptyPosition = emptyPosition;
    }

    public String getParkAddress() {
        return parkAddress;
    }

    public void setParkAddress(String parkAddress) {
        this.parkAddress = parkAddress;
    }

    public String getParkPicture() {
        return parkPicture;
    }

    public void setParkPicture(String parkPicture) {
        this.parkPicture = parkPicture;
    }

    public double getPartLongitude() {
        return partLongitude;
    }

    public void setPartLongitude(double partLongitude) {
        this.partLongitude = partLongitude;
    }

    public double getPartLatitude() {
        return partLatitude;
    }

    public void setPartLatitude(double partLatitude) {
        this.partLatitude = partLatitude;
    }

    public Object getCityId() {
        return cityId;
    }

    public void setCityId(Object cityId) {
        this.cityId = cityId;
    }

    public String getParkDistance() {
        return parkDistance;
    }

    public void setParkDistance(String parkDistance) {
        this.parkDistance = parkDistance;
    }

    public String getDayReferenceFee() {
        return dayReferenceFee;
    }

    public void setDayReferenceFee(String dayReferenceFee) {
        this.dayReferenceFee = dayReferenceFee;
    }

    public String getNightReferenceFee() {
        return nightReferenceFee;
    }

    public void setNightReferenceFee(String nightReferenceFee) {
        this.nightReferenceFee = nightReferenceFee;
    }

    public String getChargeDetail() {
        return chargeDetail;
    }

    public void setChargeDetail(String chargeDetail) {
        this.chargeDetail = chargeDetail;
    }

    public String getParkingProfile() {
        return parkingProfile;
    }

    public void setParkingProfile(String parkingProfile) {
        this.parkingProfile = parkingProfile;
    }

    public String getFirstPrice() {
        return firstPrice;
    }

    public void setFirstPrice(String firstPrice) {
        this.firstPrice = firstPrice;
    }
}
