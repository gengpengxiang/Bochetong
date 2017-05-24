package com.bolong.bochetong.bean;

import java.util.List;

/**
 * Created by admin on 2017/5/14.
 */

public class Card {

    /**
     * errCode : 1000
     * errMessage : 请求成功
     * content : [{"carId":"1","carCard":"冀G98888"},{"carId":"2","carCard":"苏B78789"}]
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
         * carId : 1
         * carCard : 冀G98888
         */

        private String carId;
        private String carCard;

        public String getCarId() {
            return carId;
        }

        public void setCarId(String carId) {
            this.carId = carId;
        }

        public String getCarCard() {
            return carCard;
        }

        public void setCarCard(String carCard) {
            this.carCard = carCard;
        }
    }
}
