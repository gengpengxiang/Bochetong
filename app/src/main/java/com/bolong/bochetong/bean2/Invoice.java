package com.bolong.bochetong.bean2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/6/27.
 */

public class Invoice {


    /**
     * errCode : 1000
     * errMessage : 请求成功
     * content : [{"id":"ca357119488849cfac3aa030cd644dfa","parkName":"惠康嘉园(二区,三区,四区,五区,六区)地上停车场","intime":"2017.06.26 11:05","outtime":"2017.06.26 15:18","price":5},{"id":"2ffe731ebc9f4b9ebef0feb649e47b7d","parkName":"惠康嘉园(二区,三区,四区,五区,六区)地上停车场","intime":"2017.06.25 07:43","outtime":"2017.06.25 10:00","price":3},{"id":"b07d22f971f64f7499b432a959affc45","parkName":"惠康嘉园(二区,三区,四区,五区,六区)地上停车场","intime":"2017.06.21 12:12","outtime":"2017.06.21 14:31","price":3},{"id":"522a42bdf66f4977959e2e1d0f9f926c","parkName":"惠康嘉园(二区,三区,四区,五区,六区)地上停车场","intime":"2017.06.12 13:42","outtime":"2017.06.21 08:16","price":166},{"id":"6d0dae36f88848dea87c853f929d2c65","parkName":"惠康嘉园(二区,三区,四区,五区,六区)地上停车场","intime":"2017.06.11 18:14","outtime":"2017.06.12 10:58","price":8},{"id":"043502eab24a40b0a0f686b8d16ed1d8","parkName":"惠康嘉园(二区,三区,四区,五区,六区)地上停车场","intime":"2017.06.11 08:23","outtime":"2017.06.11 14:48","price":3.5},{"id":"1fd2ba4aced84dc4951571d4616fc195","parkName":"惠康嘉园(二区,三区,四区,五区,六区)地上停车场","intime":"2017.06.10 08:14","outtime":"2017.06.10 23:11","price":7.5},{"id":"ee1643c0cfff40f39613af1eb61e0b3d","parkName":"惠康嘉园(二区,三区,四区,五区,六区)地上停车场","intime":"2017.06.08 23:42","outtime":"2017.06.09 07:01","price":4.5},{"id":"d1ac88c94bc74990b4c8a2a0479bbdc8","parkName":"惠康嘉园(二区,三区,四区,五区,六区)地上停车场","intime":"2017.06.08 09:49","outtime":"2017.06.08 13:26","price":4},{"id":"81005f86b4f042e8b07e39b457012588","parkName":"惠康嘉园(二区,三区,四区,五区,六区)地上停车场","intime":"2017.06.07 23:40","outtime":"2017.06.08 06:57","price":4.5},{"id":"04c3b40dbd0b4518905620f36252e7f6","parkName":"惠康嘉园(二区,三区,四区,五区,六区)地上停车场","intime":"2017.06.07 09:32","outtime":"2017.06.07 15:18","price":6},{"id":"d232e41f965b421cb871d03a162dc3bc","parkName":"惠康嘉园(二区,三区,四区,五区,六区)地上停车场","intime":"2017.06.06 16:15","outtime":"2017.06.07 08:13","price":10.5}]
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

    //add
    public static class ContentBean {
        /**
         * id : ca357119488849cfac3aa030cd644dfa
         * parkName : 惠康嘉园(二区,三区,四区,五区,六区)地上停车场
         * intime : 2017.06.26 11:05
         * outtime : 2017.06.26 15:18
         * price : 5.0
         */

        private String id;
        private String parkName;
        private String intime;
        private String outtime;
        private double price;
        public boolean isSelect;


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

        public String getIntime() {
            return intime;
        }

        public void setIntime(String intime) {
            this.intime = intime;
        }

        public String getOuttime() {
            return outtime;
        }

        public void setOuttime(String outtime) {
            this.outtime = outtime;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean isSelect) {
            this.isSelect = isSelect;
        }

        public boolean getSelect() {
            return isSelect;
        }
    }
}
