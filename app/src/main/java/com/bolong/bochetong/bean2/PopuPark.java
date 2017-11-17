package com.bolong.bochetong.bean2;


import java.util.List;

public class PopuPark {


    /**
     * status : 1
     * parkLis : [{"id":"005","parkName":"门头沟区石门营B1小区停车场"},{"id":"006","parkName":"门头沟区石门营B2小区停车场"},{"id":"007","parkName":"门头沟区石门营B3小区停车场"},{"id":"008","parkName":"门头沟区石门营B4小区停车场"}]
     */

    private String status;
    private List<ParkLisBean> parkLis;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ParkLisBean> getParkLis() {
        return parkLis;
    }

    public void setParkLis(List<ParkLisBean> parkLis) {
        this.parkLis = parkLis;
    }

    public static class ParkLisBean {
        /**
         * id : 005
         * parkName : 门头沟区石门营B1小区停车场
         */

        private String id;
        private String parkName;

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
    }
}
