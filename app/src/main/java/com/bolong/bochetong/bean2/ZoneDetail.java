package com.bolong.bochetong.bean2;

/**
 * Created by admin on 2017/6/16.
 */

public class ZoneDetail {


    /**
     * id : 003
     * parkName : 石担路东区
     * emptyPosition : 10
     * parkAddress : 石担路近新园路
     * parkPicture : http://101.201.145.238/group1/M00/00/53/ZcmR7llLGV-ABCpJAADt_Znut8I926.jpg
     * partLongitude : 116.113969
     * partLatitude : 39.899467
     * cityId : null
     * parkDistance : 2.30km
     * dayReferenceFee : 07:00-21:00 0.5元/15分钟
     * nightReferenceFee : 21:00-07:00 0.5元/1小时
     * chargeDetail : 15分钟内免费,15分钟后,一小时内0.5元/15分钟,第二小时起0.75元/15分钟,夜间晚9-早7点,0.5元/小时
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
