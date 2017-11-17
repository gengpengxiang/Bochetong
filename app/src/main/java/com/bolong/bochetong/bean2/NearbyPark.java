package com.bolong.bochetong.bean2;

import java.io.Serializable;
import java.util.List;

/**
 * Created by admin on 2017/6/14.
 */

public class NearbyPark implements Serializable{


    private List<ParksBean> parks;
    private List<ZonesBean> zones;

    public List<ParksBean> getParks() {
        return parks;
    }

    public void setParks(List<ParksBean> parks) {
        this.parks = parks;
    }

    public List<ZonesBean> getZones() {
        return zones;
    }

    public void setZones(List<ZonesBean> zones) {
        this.zones = zones;
    }

    public static class ParksBean implements Serializable{
        /**
         * park_picture : http://101.201.145.238/group1/M00/00/00/ZcmR7lkJfy-AAztkAADlKHZkygg013.jpg
         * standard : 1.0元/30分钟
         * park_name : 惠润嘉园小区(九区,十区)地上停车场
         * part_longitude : 116.125207
         * emptyPosition : 11
         * part_latitude : 39.919764
         * id : 2
         * distance : 1.85km
         * park_address : 门头沟区永定镇莲石路
         */

        private String park_picture;
        private String standard;
        private String park_name;
        private double part_longitude;
        private int emptyPosition;
        private double part_latitude;
        private String id;
        private String distance;
        private String park_address;

        public String getPark_picture() {
            return park_picture;
        }

        public void setPark_picture(String park_picture) {
            this.park_picture = park_picture;
        }

        public String getStandard() {
            return standard;
        }

        public void setStandard(String standard) {
            this.standard = standard;
        }

        public String getPark_name() {
            return park_name;
        }

        public void setPark_name(String park_name) {
            this.park_name = park_name;
        }

        public double getPart_longitude() {
            return part_longitude;
        }

        public void setPart_longitude(double part_longitude) {
            this.part_longitude = part_longitude;
        }

        public int getEmptyPosition() {
            return emptyPosition;
        }

        public void setEmptyPosition(int emptyPosition) {
            this.emptyPosition = emptyPosition;
        }

        public double getPart_latitude() {
            return part_latitude;
        }

        public void setPart_latitude(double part_latitude) {
            this.part_latitude = part_latitude;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getPark_address() {
            return park_address;
        }

        public void setPark_address(String park_address) {
            this.park_address = park_address;
        }
    }

    public static class ZonesBean implements Serializable{
        /**
         * id : 003
         * distance : 2.30km
         * park_picture : http://101.201.145.238/group1/M00/00/53/ZcmR7llLGV-ABCpJAADt_Znut8I926.jpg
         * standard : 0.5元/15分钟
         * park_address : 石担路近新园路
         * park_id : 003
         * park_name : 石担路东区
         * part_longitude : 116.113969
         * emptyPosition : 10
         * part_latitude : 39.899467
         */

        private String id;
        private String distance;
        private String park_picture;
        private String standard;
        private String park_address;
        private String park_id;
        private String park_name;
        private double part_longitude;
        private int emptyPosition;
        private double part_latitude;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getPark_picture() {
            return park_picture;
        }

        public void setPark_picture(String park_picture) {
            this.park_picture = park_picture;
        }

        public String getStandard() {
            return standard;
        }

        public void setStandard(String standard) {
            this.standard = standard;
        }

        public String getPark_address() {
            return park_address;
        }

        public void setPark_address(String park_address) {
            this.park_address = park_address;
        }

        public String getPark_id() {
            return park_id;
        }

        public void setPark_id(String park_id) {
            this.park_id = park_id;
        }

        public String getPark_name() {
            return park_name;
        }

        public void setPark_name(String park_name) {
            this.park_name = park_name;
        }

        public double getPart_longitude() {
            return part_longitude;
        }

        public void setPart_longitude(double part_longitude) {
            this.part_longitude = part_longitude;
        }

        public int getEmptyPosition() {
            return emptyPosition;
        }

        public void setEmptyPosition(int emptyPosition) {
            this.emptyPosition = emptyPosition;
        }

        public double getPart_latitude() {
            return part_latitude;
        }

        public void setPart_latitude(double part_latitude) {
            this.part_latitude = part_latitude;
        }
    }
}
