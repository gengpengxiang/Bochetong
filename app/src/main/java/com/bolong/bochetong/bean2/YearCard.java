package com.bolong.bochetong.bean2;

import java.util.List;

public class YearCard {

    /**
     * status : 1
     * monthCard : [{"cardId":"47a424d48ed7430d8183705d3138bfeb","parkName":"新桥家园地上停车场","userPhoneNumber":"18892207851","carportNum":"1283","carNumbers":["京M444444","京M97984"],"carNumber":"京M444444","createTime":"2017.07.10 16:32","cardTermofvalidity":"2018.01.12 00:00","monthlyPrice":"180.0","renewalFee":1380}]
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
         * cardId : 47a424d48ed7430d8183705d3138bfeb
         * parkName : 新桥家园地上停车场
         * userPhoneNumber : 18892207851
         * carportNum : 1283
         * carNumbers : ["京M444444","京M97984"]
         * carNumber : 京M444444
         * createTime : 2017.07.10 16:32
         * cardTermofvalidity : 2018.01.12 00:00
         * monthlyPrice : 180.0
         * renewalFee : 1380.0
         */

        private String cardId;
        private String parkName;
        private String userPhoneNumber;
        private String carportNum;
        private String carNumber;
        private String createTime;
        private String cardTermofvalidity;
        private String monthlyPrice;
        private double renewalFee;
        private List<String> carNumbers;

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

        public double getRenewalFee() {
            return renewalFee;
        }

        public void setRenewalFee(double renewalFee) {
            this.renewalFee = renewalFee;
        }

        public List<String> getCarNumbers() {
            return carNumbers;
        }

        public void setCarNumbers(List<String> carNumbers) {
            this.carNumbers = carNumbers;
        }
    }
}
