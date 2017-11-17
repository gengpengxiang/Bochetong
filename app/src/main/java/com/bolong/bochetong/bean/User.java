package com.bolong.bochetong.bean;

import java.io.Serializable;

/**
 * Created by admin on 2017/5/8.
 */

public class User implements Serializable{


    /**
     * userId : 9D2BDD3D9A40491BA7A80B5EED01A11C
     * token : 17173B1BC97E412982CDE924FD2E2577
     * userName : 水电费
     * userPhone : 18800909091
     * userBalance : 0
     * userRegistTime : 2017-06-28 14:51
     * freeTime : {"id":4,"minutes":60,"channel":"1","receiveTime":"2017.06.28 14:51","expireTime":"2017.07.28 14:51","useRange":"1,1","userId":"9D2BDD3D9A40491BA7A80B5EED01A11C"}
     */

    private String userId;
    private String token;
    private String userName;
    private String userPhone;
    private double userBalance;
    private String userRegistTime;
    private FreeTimeBean freeTime;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public double getUserBalance() {
        return userBalance;
    }

    public void setUserBalance(double userBalance) {
        this.userBalance = userBalance;
    }

    public String getUserRegistTime() {
        return userRegistTime;
    }

    public void setUserRegistTime(String userRegistTime) {
        this.userRegistTime = userRegistTime;
    }

    public FreeTimeBean getFreeTime() {
        return freeTime;
    }

    public void setFreeTime(FreeTimeBean freeTime) {
        this.freeTime = freeTime;
    }

    public static class FreeTimeBean implements Serializable{
        /**
         * id : 4
         * minutes : 60
         * channel : 1
         * receiveTime : 2017.06.28 14:51
         * expireTime : 2017.07.28 14:51
         * useRange : 1,1
         * userId : 9D2BDD3D9A40491BA7A80B5EED01A11C
         */

        private int id;
        private int minutes;
        private String channel;
        private String receiveTime;
        private String expireTime;
        private String useRange;
        private String userId;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getMinutes() {
            return minutes;
        }

        public void setMinutes(int minutes) {
            this.minutes = minutes;
        }

        public String getChannel() {
            return channel;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }

        public String getReceiveTime() {
            return receiveTime;
        }

        public void setReceiveTime(String receiveTime) {
            this.receiveTime = receiveTime;
        }

        public String getExpireTime() {
            return expireTime;
        }

        public void setExpireTime(String expireTime) {
            this.expireTime = expireTime;
        }

        public String getUseRange() {
            return useRange;
        }

        public void setUseRange(String useRange) {
            this.useRange = useRange;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }
}
