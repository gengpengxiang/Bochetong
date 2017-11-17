package com.bolong.bochetong.bean2;

import java.util.List;


public class Bill {


    /**
     * accountBalance : 0.08
     * Pagenation : {"totalCount":7,"pageNo":1,"totalPage":2,"data":[{"billType":"","createTime":"2017-06-06 11:17","billMoney":"+20.0","payType":"支付宝","billId":"53a66495dbd741d795338a5d8d05a4f3"},{"billType":"停车缴费","createTime":"2017-06-06 09:44","billMoney":"-3.8","payType":"支付宝","billId":"5"},{"billType":"月卡续费","createTime":"2017-05-12 12:47","billMoney":"-0.0","payType":"支付宝","billId":"11df904ce8ed41e5a4548750305a78ab"},{"billType":"账户充值","createTime":"2017-05-02 09:43","billMoney":"+2.3","payType":"支付宝","billId":"4"},{"billType":"","createTime":"2017-05-02 09:43","billMoney":"+3.1","payType":"支付宝","billId":"3"},{"billType":"","createTime":"2017-05-02 09:42","billMoney":"+3.1","payType":"支付宝","billId":"2"}]}
     */

    private double accountBalance;
    private PagenationBean Pagenation;

    public double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(double accountBalance) {
        this.accountBalance = accountBalance;
    }

    public PagenationBean getPagenation() {
        return Pagenation;
    }

    public void setPagenation(PagenationBean Pagenation) {
        this.Pagenation = Pagenation;
    }

    public static class PagenationBean {
        /**
         * totalCount : 7
         * pageNo : 1
         * totalPage : 2
         * data : [{"billType":"","createTime":"2017-06-06 11:17","billMoney":"+20.0","payType":"支付宝","billId":"53a66495dbd741d795338a5d8d05a4f3"},{"billType":"停车缴费","createTime":"2017-06-06 09:44","billMoney":"-3.8","payType":"支付宝","billId":"5"},{"billType":"月卡续费","createTime":"2017-05-12 12:47","billMoney":"-0.0","payType":"支付宝","billId":"11df904ce8ed41e5a4548750305a78ab"},{"billType":"账户充值","createTime":"2017-05-02 09:43","billMoney":"+2.3","payType":"支付宝","billId":"4"},{"billType":"","createTime":"2017-05-02 09:43","billMoney":"+3.1","payType":"支付宝","billId":"3"},{"billType":"","createTime":"2017-05-02 09:42","billMoney":"+3.1","payType":"支付宝","billId":"2"}]
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
             * billType :
             * createTime : 2017-06-06 11:17
             * billMoney : +20.0
             * payType : 支付宝
             * billId : 53a66495dbd741d795338a5d8d05a4f3
             */

            private String billType;
            private String createTime;
            private String billMoney;
            private String payType;
            private String billId;

            public String getBillType() {
                return billType;
            }

            public void setBillType(String billType) {
                this.billType = billType;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public String getBillMoney() {
                return billMoney;
            }

            public void setBillMoney(String billMoney) {
                this.billMoney = billMoney;
            }

            public String getPayType() {
                return payType;
            }

            public void setPayType(String payType) {
                this.payType = payType;
            }

            public String getBillId() {
                return billId;
            }

            public void setBillId(String billId) {
                this.billId = billId;
            }
        }
    }
}
