package com.bolong.bochetong.bean2;

import java.util.List;

/**
 * Created by admin on 2017/6/13.
 */

public class Coupon {


    private List<CouponBean> coupon;

    public List<CouponBean> getCoupon() {
        return coupon;
    }

    public void setCoupon(List<CouponBean> coupon) {
        this.coupon = coupon;
    }

    public static class CouponBean {
        /**
         * id : 1
         * faceValue : 5
         * createData : 2017-06-21
         * expirationDate : 2017-09-21
         * usedDate : 2017.06.21 09:03
         * useStatus : true
         * expirationStatus : false
         * orderId : TNO2017062109020190800004950
         * profiles : 满50元可抵
         */

        private String id;
        private int faceValue;
        private String createData;
        private String expirationDate;
        private String usedDate;
        private boolean useStatus;
        private boolean expirationStatus;
        private String orderId;
        private String profiles;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getFaceValue() {
            return faceValue;
        }

        public void setFaceValue(int faceValue) {
            this.faceValue = faceValue;
        }

        public String getCreateData() {
            return createData;
        }

        public void setCreateData(String createData) {
            this.createData = createData;
        }

        public String getExpirationDate() {
            return expirationDate;
        }

        public void setExpirationDate(String expirationDate) {
            this.expirationDate = expirationDate;
        }

        public String getUsedDate() {
            return usedDate;
        }

        public void setUsedDate(String usedDate) {
            this.usedDate = usedDate;
        }

        public boolean isUseStatus() {
            return useStatus;
        }

        public void setUseStatus(boolean useStatus) {
            this.useStatus = useStatus;
        }

        public boolean isExpirationStatus() {
            return expirationStatus;
        }

        public void setExpirationStatus(boolean expirationStatus) {
            this.expirationStatus = expirationStatus;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getProfiles() {
            return profiles;
        }

        public void setProfiles(String profiles) {
            this.profiles = profiles;
        }
    }
}
