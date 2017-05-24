package com.bolong.bochetong.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/4/26.
 */

public class ParkOfMap implements Serializable {


    private static final long serialVersionUID = -758459502806858414L;
    private double longitude;
    private double latitude;
    private String name;
    private String distance;
    private int carPort;

    //模拟数据
    public static List<ParkOfMap> parkinfos = new ArrayList<ParkOfMap>();

    static {
        parkinfos.add(new ParkOfMap(39.982866,116.172892,  "1号停车场", "距离6km", 120));
        parkinfos.add(new ParkOfMap(39.952866,116.142892,  "2号停车场", "距离20km", 100));
    }

    public ParkOfMap(double latitude, double longitude, String name, String distance, int carPort) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.name = name;
        this.distance = distance;
        this.carPort = carPort;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public int getCarPort() {
        return carPort;
    }

    public void setCarPort(int carPort) {
        this.carPort = carPort;
    }

    @Override
    public String toString() {
        return "ParkOfMap{" +
                "longitude=" + longitude +
                ", latitude=" + latitude +
                ", name='" + name + '\'' +
                ", distance='" + distance + '\'' +
                ", carPort=" + carPort +
                '}';
    }
}
