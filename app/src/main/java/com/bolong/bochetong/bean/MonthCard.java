package com.bolong.bochetong.bean;

import java.util.List;

/**
 * Created by admin on 2017/5/14.
 */

public class MonthCard {


    /**
     * status : 1
     * monthCard : [{"cardId":"2","parkName":"惠康家园六区停车场","userPhoneNumber":"13520031276","carportNum":"A03","carNumber":"冀G98888","createTime":"2017-04-28","cardTermofvalidity":"2017-05-28","monthlyPrice":"150.0"}]
     */

    private String status;
    private List<MonthCardBean> monthCard;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<MonthCardBean> getMonthCard() {
        return monthCard;
    }

    public void setMonthCard(List<MonthCardBean> monthCard) {
        this.monthCard = monthCard;
    }

    public static class MonthCardBean {
        /**
         * cardId : 2
         * parkName : 惠康家园六区停车场
         * userPhoneNumber : 13520031276
         * carportNum : A03
         * carNumber : 冀G98888
         * createTime : 2017-04-28
         * cardTermofvalidity : 2017-05-28
         * monthlyPrice : 150.0
         */

        private String cardId;
        private String parkName;
        private String userPhoneNumber;
        private String carportNum;
        private String carNumber;
        private String createTime;
        private String cardTermofvalidity;
        private String monthlyPrice;

        public String getCardId() {
            return cardId;
        }

        public void setCardId(String cardId) {
            this.cardId = cardId;
        }

        public String getParkName() {
            return parkName;
        }

        public void setParkName(String parkName) {
            this.parkName = parkName;
        }

        public String getUserPhoneNumber() {
            return userPhoneNumber;
        }

        public void setUserPhoneNumber(String userPhoneNumber) {
            this.userPhoneNumber = userPhoneNumber;
        }

        public String getCarportNum() {
            return carportNum;
        }

        public void setCarportNum(String carportNum) {
            this.carportNum = carportNum;
        }

        public String getCarNumber() {
            return carNumber;
        }

        public void setCarNumber(String carNumber) {
            this.carNumber = carNumber;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getCardTermofvalidity() {
            return cardTermofvalidity;
        }

        public void setCardTermofvalidity(String cardTermofvalidity) {
            this.cardTermofvalidity = cardTermofvalidity;
        }

        public String getMonthlyPrice() {
            return monthlyPrice;
        }

        public void setMonthlyPrice(String monthlyPrice) {
            this.monthlyPrice = monthlyPrice;
        }
    }
}
