package com.bolong.bochetong.bean2;

import java.util.List;

/**
 * Created by admin on 2017/6/27.
 */

public class Favor {

    /**
     * errCode : 1000
     * errMessage : 请求成功
     * content : [{"id":1,"minutes":60,"expflag":0,"title":"新用户注册","totalMinute":60,"receiveTime":"2017.06.30 09:15","useRange":"1,1"},{"id":3,"minutes":60,"expflag":1,"title":"春节送豪礼","totalMinute":60,"receiveTime":"2017.02.28 14:39","useRange":"1,1"}]
     */

    private String errCode;
    private String errMessage;
    private List<ContentBean> content;

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }

    public List<ContentBean> getContent() {
        return content;
    }

    public void setContent(List<ContentBean> content) {
        this.content = content;
    }

    public static class ContentBean {
        /**
         * id : 1
         * minutes : 60
         * expflag : 0
         * title : 新用户注册
         * totalMinute : 60
         * receiveTime : 2017.06.30 09:15
         * useRange : 1,1
         */

        private int id;
        private int minutes;
        private int expflag;
        private String title;
        private int totalMinute;
        private String receiveTime;
        private String useRange;

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

        public int getExpflag() {
            return expflag;
        }

        public void setExpflag(int expflag) {
            this.expflag = expflag;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getTotalMinute() {
            return totalMinute;
        }

        public void setTotalMinute(int totalMinute) {
            this.totalMinute = totalMinute;
        }

        public String getReceiveTime() {
            return receiveTime;
        }

        public void setReceiveTime(String receiveTime) {
            this.receiveTime = receiveTime;
        }

        public String getUseRange() {
            return useRange;
        }

        public void setUseRange(String useRange) {
            this.useRange = useRange;
        }
    }
}
