package com.bolong.bochetong.bean2;

import java.io.Serializable;
import java.util.List;

/**
 * Created by admin on 2017/6/26.
 */

public class FpHistory implements Serializable{


    /**
     * errCode : 1000
     * errMessage : 请求成功
     * content : [{"orderNumber":"BL-INV.000000024","invoiceType":1,"invoiceCode":"031001770151","invoiceNumber":"10001207","buildDate":"2017.06.27 16:33","pdfFileUrl":"http://101.201.145.238/group1/M00/00/88/ZcmR7llSGE-AYs4_AABs6EhKJB8851.pdf","userId":"1","amount":33,"materielName":"停车费","buyerName":null,"buyerType":null,"buyerTaxCode":null,"openStatus":1},{"orderNumber":"BL-INV.000000023","invoiceType":1,"invoiceCode":"031001770151","invoiceNumber":"10001189","buildDate":"2017.06.27 15:26","pdfFileUrl":"http://101.201.145.238/group1/M00/00/87/ZcmR7llSDm-ASE9UAABs3WMJtIc407.pdf","userId":"1","amount":33,"materielName":"停车费","buyerName":null,"buyerType":null,"buyerTaxCode":null,"openStatus":1},{"orderNumber":"BL-INV.000000016","invoiceType":1,"invoiceCode":"031001770151","invoiceNumber":"10001141","buildDate":"2017.06.26 17:28","pdfFileUrl":"http://101.201.145.238/group1/M00/00/86/ZcmR7llR9ByABrViAABs8HHPoow458.pdf","userId":"1","amount":33,"materielName":"停车费","buyerName":null,"buyerType":null,"buyerTaxCode":null,"openStatus":1},{"orderNumber":"BL-INV.000000022","invoiceType":null,"invoiceCode":null,"invoiceNumber":null,"buildDate":null,"pdfFileUrl":null,"userId":"1","amount":33,"materielName":"停车费","buyerName":null,"buyerType":null,"buyerTaxCode":null,"openStatus":0}]
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

    public static class ContentBean implements Serializable{
        /**
         * orderNumber : BL-INV.000000024
         * invoiceType : 1
         * invoiceCode : 031001770151
         * invoiceNumber : 10001207
         * buildDate : 2017.06.27 16:33
         * pdfFileUrl : http://101.201.145.238/group1/M00/00/88/ZcmR7llSGE-AYs4_AABs6EhKJB8851.pdf
         * userId : 1
         * amount : 33
         * materielName : 停车费
         * buyerName : null
         * buyerType : null
         * buyerTaxCode : null
         * openStatus : 1
         */

        private String orderNumber;
        private int invoiceType;
        private String invoiceCode;
        private String invoiceNumber;
        private String buildDate;
        private String pdfFileUrl;
        private String userId;
        private double amount;
        private String materielName;
        private String buyerName;
        private String buyerType;
        private String buyerTaxCode;
        private int openStatus;

        public String getOrderNumber() {
            return orderNumber;
        }

        public void setOrderNumber(String orderNumber) {
            this.orderNumber = orderNumber;
        }

        public int getInvoiceType() {
            return invoiceType;
        }

        public void setInvoiceType(int invoiceType) {
            this.invoiceType = invoiceType;
        }

        public String getInvoiceCode() {
            return invoiceCode;
        }

        public void setInvoiceCode(String invoiceCode) {
            this.invoiceCode = invoiceCode;
        }

        public String getInvoiceNumber() {
            return invoiceNumber;
        }

        public void setInvoiceNumber(String invoiceNumber) {
            this.invoiceNumber = invoiceNumber;
        }

        public String getBuildDate() {
            return buildDate;
        }

        public void setBuildDate(String buildDate) {
            this.buildDate = buildDate;
        }

        public String getPdfFileUrl() {
            return pdfFileUrl;
        }

        public void setPdfFileUrl(String pdfFileUrl) {
            this.pdfFileUrl = pdfFileUrl;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public String getMaterielName() {
            return materielName;
        }

        public void setMaterielName(String materielName) {
            this.materielName = materielName;
        }

        public String getBuyerName() {
            return buyerName;
        }

        public void setBuyerName(String buyerName) {
            this.buyerName = buyerName;
        }

        public String getBuyerType() {
            return buyerType;
        }

        public void setBuyerType(String buyerType) {
            this.buyerType = buyerType;
        }

        public String getBuyerTaxCode() {
            return buyerTaxCode;
        }

        public void setBuyerTaxCode(String buyerTaxCode) {
            this.buyerTaxCode = buyerTaxCode;
        }

        public int getOpenStatus() {
            return openStatus;
        }

        public void setOpenStatus(int openStatus) {
            this.openStatus = openStatus;
        }
    }
}
