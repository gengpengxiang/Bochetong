package com.bolong.bochetong.bean2;

public class MonthCardCharge {

    /**
     * monthcardtype : {"id":"shimenyingb1wugudingcheweiyueka","monthcardtypename":"无固定车位月卡","unitPrice":150,"quantities":1,"unit":"月","status":1,"parkId":"005"}
     * amount : 0
     * status : 1
     */

    private MonthcardtypeBean monthcardtype;
    private int amount;
    private String status;

    public MonthcardtypeBean getMonthcardtype() {
        return monthcardtype;
    }

    public void setMonthcardtype(MonthcardtypeBean monthcardtype) {
        this.monthcardtype = monthcardtype;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class MonthcardtypeBean {
        /**
         * id : shimenyingb1wugudingcheweiyueka
         * monthcardtypename : 无固定车位月卡
         * unitPrice : 150
         * quantities : 1
         * unit : 月
         * status : 1
         * parkId : 005
         */

        private String id;
        private String monthcardtypename;
        private int unitPrice;
        private int quantities;
        private String unit;
        private int status;
        private String parkId;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMonthcardtypename() {
            return monthcardtypename;
        }

        public void setMonthcardtypename(String monthcardtypename) {
            this.monthcardtypename = monthcardtypename;
        }

        public int getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(int unitPrice) {
            this.unitPrice = unitPrice;
        }

        public int getQuantities() {
            return quantities;
        }

        public void setQuantities(int quantities) {
            this.quantities = quantities;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getParkId() {
            return parkId;
        }

        public void setParkId(String parkId) {
            this.parkId = parkId;
        }
    }
}
