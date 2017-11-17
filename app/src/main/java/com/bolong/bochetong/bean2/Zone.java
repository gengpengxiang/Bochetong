package com.bolong.bochetong.bean2;

import java.io.Serializable;
import java.util.List;

/**
 * Created by admin on 2017/6/16.
 */

public class Zone {


    /**
     * totalCount : 0
     * pageNo : 1
     * totalPage : 1
     * data : [{"id":"003","distance":"2.30km","park_picture":"http://101.201.145.238/group1/M00/00/53/ZcmR7llLGV-ABCpJAADt_Znut8I926.jpg","standard":"0.5元/15分钟","park_address":"石担路近新园路","park_id":"003","park_name":"石担路东区","part_longitude":116.113969,"emptyPosition":10,"part_latitude":39.899467},{"id":"118","picture":null,"distance":"3.16km","standard":"0.5元/15分钟","park_address":"紫金北路","park_id":"003","park_name":"紫金北路","part_longitude":116.103969,"emptyPosition":17,"part_latitude":39.896467},{"id":"004","distance":"48.34km","park_picture":"http://101.201.145.238/group1/M00/00/53/ZcmR7llLGYqAZk3qAABLgpVSsrw418.jpg","standard":"0.5元/15分钟","park_address":"莲石湖西路","park_id":"003","park_name":"莲石湖西路","part_longitude":116.444444,"emptyPosition":13,"part_latitude":39.555444},{"id":"009","distance":"98.81km","park_picture":"http://101.201.145.238/group1/M00/00/53/ZcmR7llLGfCAK2fPAABu2mDYQew192.jpg","standard":"0.5元/15分钟","park_address":"小园桥","park_id":"003","park_name":"农林路小园桥","part_longitude":117.1139,"emptyPosition":22,"part_latitude":39.899463},{"id":"005","distance":"1068.34km","park_picture":"http://101.201.145.238/group1/M00/00/53/ZcmR7llLGa-AFM8zAADPh_-eSvc061.jpg","standard":"0.5元/15分钟","park_address":"永安路","park_id":"003","park_name":"永安路","part_longitude":117.888555,"emptyPosition":10,"part_latitude":50.456132}]
     */

    private int totalCount;
    private int pageNo;
    private int totalPage;
    private List<DataBean> data;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
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
         * picture : null
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
        private Object picture;

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

        public Object getPicture() {
            return picture;
        }

        public void setPicture(Object picture) {
            this.picture = picture;
        }
    }
}
