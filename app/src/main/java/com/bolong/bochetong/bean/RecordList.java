package com.bolong.bochetong.bean;

import java.util.List;

/**
 * Created by admin on 2017/5/4.
 */

public class RecordList {

    /**
     * errCode : 1000
     * errMessage : 请求成功
     * content : {"totalCount":3,"pageNo":1,"totalPage":1,"date":[{"id":"FCC1189D727D4156BB2A1315C280DBCE","parkName":"惠康家园六区停车场","carNumber":"冀G98888","recordIntime":"2017-04-13 17:08:04","durationTime":"8天0小时6分","recordPaymoney":null,"originalPrice":null,"preferentialPrice":null},{"id":"03E7C945865148D3902C97C7F42880E8","parkName":"惠康家园六区停车场","carNumber":"苏B78789","recordIntime":"2017-04-13 17:09:57","durationTime":"1小时8分","recordPaymoney":null,"originalPrice":null,"preferentialPrice":null},{"id":"FCC1189D727D4156BB2A1315C280DBC4","parkName":"惠康家园六区停车场","carNumber":"冀G98888","recordIntime":"2017-04-15 17:09:00","durationTime":"11小时30分","recordPaymoney":null,"originalPrice":null,"preferentialPrice":null}]}
     */

    private String errCode;
    private String errMessage;
    private ContentBean content;

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

    public ContentBean getContent() {
        return content;
    }

    public void setContent(ContentBean content) {
        this.content = content;
    }

    public static class ContentBean {
        /**
         * totalCount : 3
         * pageNo : 1
         * totalPage : 1
         * date : [{"id":"FCC1189D727D4156BB2A1315C280DBCE","parkName":"惠康家园六区停车场","carNumber":"冀G98888","recordIntime":"2017-04-13 17:08:04","durationTime":"8天0小时6分","recordPaymoney":null,"originalPrice":null,"preferentialPrice":null},{"id":"03E7C945865148D3902C97C7F42880E8","parkName":"惠康家园六区停车场","carNumber":"苏B78789","recordIntime":"2017-04-13 17:09:57","durationTime":"1小时8分","recordPaymoney":null,"originalPrice":null,"preferentialPrice":null},{"id":"FCC1189D727D4156BB2A1315C280DBC4","parkName":"惠康家园六区停车场","carNumber":"冀G98888","recordIntime":"2017-04-15 17:09:00","durationTime":"11小时30分","recordPaymoney":null,"originalPrice":null,"preferentialPrice":null}]
         */

        private int totalCount;
        private int pageNo;
        private int totalPage;
        private List<DateBean> date;

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public int getPageNo() {
            return pageNo;
        }

        public void setPageNo(int pageNo) {
            this.pageNo = pageNo;
        }

        public int getTotalPage() {
            return totalPage;
        }

        public void setTotalPage(int totalPage) {
            this.totalPage = totalPage;
        }

        public List<DateBean> getDate() {
            return date;
        }

        public void setDate(List<DateBean> date) {
            this.date = date;
        }

        public static class DateBean {
            /**
             * id : FCC1189D727D4156BB2A1315C280DBCE
             * parkName : 惠康家园六区停车场
             * carNumber : 冀G98888
             * recordIntime : 2017-04-13 17:08:04
             * durationTime : 8天0小时6分
             * recordPaymoney : null
             * originalPrice : null
             * preferentialPrice : null
             */

            private String id;
            private String parkName;
            private String carNumber;
            private String recordIntime;
            private String durationTime;
            private Object recordPaymoney;
            private Object originalPrice;
            private Object preferentialPrice;

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

            public String getCarNumber() {
                return carNumber;
            }

            public void setCarNumber(String carNumber) {
                this.carNumber = carNumber;
            }

            public String getRecordIntime() {
                return recordIntime;
            }

            public void setRecordIntime(String recordIntime) {
                this.recordIntime = recordIntime;
            }

            public String getDurationTime() {
                return durationTime;
            }

            public void setDurationTime(String durationTime) {
                this.durationTime = durationTime;
            }

            public Object getRecordPaymoney() {
                return recordPaymoney;
            }

            public void setRecordPaymoney(Object recordPaymoney) {
                this.recordPaymoney = recordPaymoney;
            }

            public Object getOriginalPrice() {
                return originalPrice;
            }

            public void setOriginalPrice(Object originalPrice) {
                this.originalPrice = originalPrice;
            }

            public Object getPreferentialPrice() {
                return preferentialPrice;
            }

            public void setPreferentialPrice(Object preferentialPrice) {
                this.preferentialPrice = preferentialPrice;
            }

            @Override
            public String toString() {
                return "DateBean{" +
                        "id='" + id + '\'' +
                        ", parkName='" + parkName + '\'' +
                        ", carNumber='" + carNumber + '\'' +
                        ", recordIntime='" + recordIntime + '\'' +
                        ", durationTime='" + durationTime + '\'' +
                        ", recordPaymoney=" + recordPaymoney +
                        ", originalPrice=" + originalPrice +
                        ", preferentialPrice=" + preferentialPrice +
                        '}';
            }
        }
    }
}
