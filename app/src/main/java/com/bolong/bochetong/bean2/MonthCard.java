package com.bolong.bochetong.bean2;


import java.util.ArrayList;
import java.util.List;

public class MonthCard {

    /**
     * status : 1
     * monthCard : [{"cardId":"682a588025ae47e9b9ea8300d3146c42","parkName":"门头沟区石门营B4小区停车场","userPhoneNumber":"15612770087","carportNum":"车位id解析失败","carNumbers":["京B77777"],"carNumber":"京B77777","createTime":"2017.11.10 16:38","cardTermofvalidity":"2017.11.22 00:00","monthlyPrice":"1.0","cardStatus":1,"cityName":"北京","residueDays":11,"renewalFee":null},{"cardId":"b26848f4f45d4f389c29b76bf9e4f81f","parkName":"门头沟区石门营B4小区停车场","userPhoneNumber":"15612770087","carportNum":"车位id解析失败","carNumbers":["鲁W22222"],"carNumber":"冀Bxxxxx","createTime":"2017.11.10 16:40","cardTermofvalidity":"2017.12.04 00:00","monthlyPrice":"1.0","cardStatus":1,"cityName":"北京","residueDays":23,"renewalFee":null}]
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
         * cardId : 682a588025ae47e9b9ea8300d3146c42
         * parkName : 门头沟区石门营B4小区停车场
         * userPhoneNumber : 15612770087
         * carportNum : 车位id解析失败
         * carNumbers : ["京B77777"]
         * carNumber : 京B77777
         * createTime : 2017.11.10 16:38
         * cardTermofvalidity : 2017.11.22 00:00
         * monthlyPrice : 1.0
         * cardStatus : 1
         * cityName : 北京
         * residueDays : 11
         * renewalFee : null
         */

        private String cardId;
        private String parkName;
        private String userPhoneNumber;
        private String carportNum;
        private String carNumber;
        private String createTime;
        private String cardTermofvalidity;
        private String monthlyPrice;
        private int cardStatus;
        private String cityName;
        private int residueDays;
        private Object renewalFee;
        private List<String> carNumbers;

        public MonthCardBean(String cardId, String parkName, String userPhoneNumber, String carportNum, String carNumber, String createTime, String cardTermofvalidity, String monthlyPrice, int cardStatus, String cityName, int residueDays, List<String> carNumbers) {
            this.cardId = cardId;
            this.parkName = parkName;
            this.userPhoneNumber = userPhoneNumber;
            this.carportNum = carportNum;
            this.carNumber = carNumber;
            this.createTime = createTime;
            this.cardTermofvalidity = cardTermofvalidity;
            this.monthlyPrice = monthlyPrice;
            this.cardStatus = cardStatus;
            this.cityName = cityName;
            this.residueDays = residueDays;

            this.carNumbers = carNumbers;
        }


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

        public int getCardStatus() {
            return cardStatus;
        }

        public void setCardStatus(int cardStatus) {
            this.cardStatus = cardStatus;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public int getResidueDays() {
            return residueDays;
        }

        public void setResidueDays(int residueDays) {
            this.residueDays = residueDays;
        }

        public Object getRenewalFee() {
            return renewalFee;
        }

        public void setRenewalFee(Object renewalFee) {
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
