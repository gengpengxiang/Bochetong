package com.bolong.bochetong.bean2;

import java.util.List;

public class CarRecord {


    /**
     * totalCount : 13
     * pageNo : 1
     * totalPage : 3
     * data : [{"id":"12344","parkName":"惠康嘉园(二区,三区,四区,五区,六区)地上停车场","parkPicture":"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1495126912371&di=e88932cd645638f17e685908f4b769a1&imgtype=0&src=http%3A%2F%2Fwww.mzsky.cc%2Fdata%2Fattachment%2Fforum%2F201510%2F01%2F094051yfhpz8zuspkzazqq.jpg","carNumber":"冀G98888","recordIntime":"2017-06-09 06:19:26","durationTime":"14天11小时1分","recordPaymoney":0,"originalPrice":0,"preferentialPrice":0},{"id":"7692b1be261f4a9c944f525220c30a42","parkName":"惠康嘉园(二区,三区,四区,五区,六区)地上停车场","parkPicture":"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1495126912371&di=e88932cd645638f17e685908f4b769a1&imgtype=0&src=http%3A%2F%2Fwww.mzsky.cc%2Fdata%2Fattachment%2Fforum%2F201510%2F01%2F094051yfhpz8zuspkzazqq.jpg","carNumber":"京N11YG8","recordIntime":"2017-06-23 16:00:17","durationTime":"1小时20分","recordPaymoney":0,"originalPrice":0,"preferentialPrice":0},{"id":"043502eab24a40b0a0f686b8d16ed1d8","parkName":"惠康嘉园(二区,三区,四区,五区,六区)地上停车场","parkPicture":"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1495126912371&di=e88932cd645638f17e685908f4b769a1&imgtype=0&src=http%3A%2F%2Fwww.mzsky.cc%2Fdata%2Fattachment%2Fforum%2F201510%2F01%2F094051yfhpz8zuspkzazqq.jpg","carNumber":"京N11YG8","recordIntime":"2017-06-11 08:23:15","durationTime":"6小时25分","recordPaymoney":3.5,"originalPrice":3.5,"preferentialPrice":0},{"id":"04c3b40dbd0b4518905620f36252e7f6","parkName":"惠康嘉园(二区,三区,四区,五区,六区)地上停车场","parkPicture":"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1495126912371&di=e88932cd645638f17e685908f4b769a1&imgtype=0&src=http%3A%2F%2Fwww.mzsky.cc%2Fdata%2Fattachment%2Fforum%2F201510%2F01%2F094051yfhpz8zuspkzazqq.jpg","carNumber":"京N11YG8","recordIntime":"2017-06-07 09:32:32","durationTime":"5小时45分","recordPaymoney":6,"originalPrice":6,"preferentialPrice":0},{"id":"0e6c1990d5f54e6fa4ff13c1787c4011","parkName":"惠康嘉园(二区,三区,四区,五区,六区)地上停车场","parkPicture":"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1495126912371&di=e88932cd645638f17e685908f4b769a1&imgtype=0&src=http%3A%2F%2Fwww.mzsky.cc%2Fdata%2Fattachment%2Fforum%2F201510%2F01%2F094051yfhpz8zuspkzazqq.jpg","carNumber":"京N11YG8","recordIntime":"2017-06-08 14:47:27","durationTime":"7分","recordPaymoney":0,"originalPrice":0,"preferentialPrice":0},{"id":"1fd2ba4aced84dc4951571d4616fc195","parkName":"惠康嘉园(二区,三区,四区,五区,六区)地上停车场","parkPicture":"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1495126912371&di=e88932cd645638f17e685908f4b769a1&imgtype=0&src=http%3A%2F%2Fwww.mzsky.cc%2Fdata%2Fattachment%2Fforum%2F201510%2F01%2F094051yfhpz8zuspkzazqq.jpg","carNumber":"京N11YG8","recordIntime":"2017-06-10 08:14:23","durationTime":"14小时57分","recordPaymoney":7.5,"originalPrice":7.5,"preferentialPrice":0}]
     */

    private int totalCount;
    private int pageNo;
    private int totalPage;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 12344
         * parkName : 惠康嘉园(二区,三区,四区,五区,六区)地上停车场
         * parkPicture : https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1495126912371&di=e88932cd645638f17e685908f4b769a1&imgtype=0&src=http%3A%2F%2Fwww.mzsky.cc%2Fdata%2Fattachment%2Fforum%2F201510%2F01%2F094051yfhpz8zuspkzazqq.jpg
         * carNumber : 冀G98888
         * recordIntime : 2017-06-09 06:19:26
         * durationTime : 14天11小时1分
         * recordPaymoney : 0.0
         * originalPrice : 0.0
         * preferentialPrice : 0.0
         */

        private String id;
        private String parkName;
        private String parkPicture;
        private String carNumber;
        private String recordIntime;
        private String durationTime;
        private double recordPaymoney;
        private double originalPrice;
        private double preferentialPrice;

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

        public String getParkPicture() {
            return parkPicture;
        }

        public void setParkPicture(String parkPicture) {
            this.parkPicture = parkPicture;
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

        public double getRecordPaymoney() {
            return recordPaymoney;
        }

        public void setRecordPaymoney(double recordPaymoney) {
            this.recordPaymoney = recordPaymoney;
        }

        public double getOriginalPrice() {
            return originalPrice;
        }

        public void setOriginalPrice(double originalPrice) {
            this.originalPrice = originalPrice;
        }

        public double getPreferentialPrice() {
            return preferentialPrice;
        }

        public void setPreferentialPrice(double preferentialPrice) {
            this.preferentialPrice = preferentialPrice;
        }
    }
}
