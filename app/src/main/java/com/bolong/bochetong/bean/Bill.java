package com.bolong.bochetong.bean;

import java.util.List;

/**
 * Created by admin on 2017/5/3.
 */

public class Bill {


    /**
     * accountBalance : 32.0
     * pagenation : {"pageNo":1,"totalPage":1,"totalCount":4,"date":[{"id":4,"payType":"账户余额","billType":"停车缴费","billTime":"1491981129","billMoney":"-30.0"},{"id":3,"payType":"微信","billType":"账户充值","billTime":"1491962409","billMoney":"+30.0"},{"id":2,"payType":"账户余额","billType":"停车缴费","billTime":"1491876549","billMoney":"-18.0"},{"id":1,"payType":"支付宝","billType":"账户充值","billTime":"1491818941","billMoney":"+50.0"}]}
     */

    private String accountBalance;
    private PagenationBean pagenation;

    public String getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(String accountBalance) {
        this.accountBalance = accountBalance;
    }

    public PagenationBean getPagenation() {
        return pagenation;
    }

    public void setPagenation(PagenationBean pagenation) {
        this.pagenation = pagenation;
    }

    public static class PagenationBean {
        /**
         * pageNo : 1
         * totalPage : 1
         * totalCount : 4
         * date : [{"id":4,"payType":"账户余额","billType":"停车缴费","billTime":"1491981129","billMoney":"-30.0"},{"id":3,"payType":"微信","billType":"账户充值","billTime":"1491962409","billMoney":"+30.0"},{"id":2,"payType":"账户余额","billType":"停车缴费","billTime":"1491876549","billMoney":"-18.0"},{"id":1,"payType":"支付宝","billType":"账户充值","billTime":"1491818941","billMoney":"+50.0"}]
         */

        private int pageNo;
        private int totalPage;
        private int totalCount;
        private List<DateBean> date;

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

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public List<DateBean> getDate() {
            return date;
        }

        public void setDate(List<DateBean> date) {
            this.date = date;
        }

        public static class DateBean {
            /**
             * id : 4
             * payType : 账户余额
             * billType : 停车缴费
             * billTime : 1491981129
             * billMoney : -30.0
             */

            private int id;
            private String payType;
            private String billType;
            private String billTime;
            private String billMoney;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getPayType() {
                return payType;
            }

            public void setPayType(String payType) {
                this.payType = payType;
            }

            public String getBillType() {
                return billType;
            }

            public void setBillType(String billType) {
                this.billType = billType;
            }

            public String getBillTime() {
                return billTime;
            }

            public void setBillTime(String billTime) {
                this.billTime = billTime;
            }

            public String getBillMoney() {
                return billMoney;
            }

            public void setBillMoney(String billMoney) {
                this.billMoney = billMoney;
            }
        }
    }
}
