package com.bolong.bochetong.bean2;

import java.util.List;

public class ParkTimer {

    /**
     * status : 2
     * timer : [{"enterTime":"1500513480","enterTimeStr":"2017.07.20 09:18","nowTime":"1501571379","carCard":"京A00000","carRecordId":"1d1cb60369d742d2944b2e2185169aa1","parkName":"新桥家园地上停车场","parkId":"1","lockcar":0,"nowPrice":"147.0"}]
     */

    private int status;
    private List<TimerBean> timer;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<TimerBean> getTimer() {
        return timer;
    }

    public void setTimer(List<TimerBean> timer) {
        this.timer = timer;
    }

    public static class TimerBean {
        /**
         * enterTime : 1500513480
         * enterTimeStr : 2017.07.20 09:18
         * nowTime : 1501571379
         * carCard : 京A00000
         * carRecordId : 1d1cb60369d742d2944b2e2185169aa1
         * parkName : 新桥家园地上停车场
         * parkId : 1
         * lockcar : 0
         * nowPrice : 147.0
         */

        private String enterTime;
        private String enterTimeStr;
        private String nowTime;
        private String carCard;
        private String carRecordId;
        private String parkName;
        private String parkId;
        private String lockcar;
        private String nowPrice;


        public TimerBean(String enterTime, String enterTimeStr, String nowTime, String carCard, String carRecordId, String parkName, String parkId, String lockcar, String nowPrice) {
            this.enterTime = enterTime;
            this.enterTimeStr = enterTimeStr;
            this.nowTime = nowTime;
            this.carCard = carCard;
            this.carRecordId = carRecordId;
            this.parkName = parkName;
            this.parkId = parkId;
            this.lockcar = lockcar;
            this.nowPrice = nowPrice;
        }

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

        public String getParkId() {
            return parkId;
        }

        public void setParkId(String parkId) {
            this.parkId = parkId;
        }

        public String getLockcar() {
            return lockcar;
        }

        public void setLockcar(String lockcar) {
            this.lockcar = lockcar;
        }

        public String getNowPrice() {
            return nowPrice;
        }

        public void setNowPrice(String nowPrice) {
            this.nowPrice = nowPrice;
        }
    }
}
