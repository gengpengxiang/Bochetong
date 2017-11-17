package com.bolong.bochetong.bean2;

public class InvoiceDetail {

    /**
     * status : 1
     * invoice : {"orderNumber":"BL-INV.000000045","invoiceType":1,"invoiceCode":"031001770151","invoiceNumber":"10001369","buildDate":"2017.06.28 17:09","pdfFileUrl":"http://101.201.145.238/group1/M00/00/90/ZcmR7llTciSAaq1CAABs7nrneKE346.pdf","userId":"1","amount":33,"materielName":"停车费","buyerName":"地方官史蒂夫","buyerType":0,"buyerTaxCode":null,"remarks":"435345大润发给对方","openStatus":1}
     */

    private String status;
    private InvoiceBean invoice;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public InvoiceBean getInvoice() {
        return invoice;
    }

    public void setInvoice(InvoiceBean invoice) {
        this.invoice = invoice;
    }

    public static class InvoiceBean {
        /**
         * orderNumber : BL-INV.000000045
         * invoiceType : 1
         * invoiceCode : 031001770151
         * invoiceNumber : 10001369
         * buildDate : 2017.06.28 17:09
         * pdfFileUrl : http://101.201.145.238/group1/M00/00/90/ZcmR7llTciSAaq1CAABs7nrneKE346.pdf
         * userId : 1
         * amount : 33
         * materielName : 停车费
         * buyerName : 地方官史蒂夫
         * buyerType : 0
         * buyerTaxCode : null
         * remarks : 435345大润发给对方
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
        private int buyerType;
        private Object buyerTaxCode;
        private String remarks;
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

        public int getBuyerType() {
            return buyerType;
        }

        public void setBuyerType(int buyerType) {
            this.buyerType = buyerType;
        }

        public Object getBuyerTaxCode() {
            return buyerTaxCode;
        }

        public void setBuyerTaxCode(Object buyerTaxCode) {
            this.buyerTaxCode = buyerTaxCode;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        public int getOpenStatus() {
            return openStatus;
        }

        public void setOpenStatus(int openStatus) {
            this.openStatus = openStatus;
        }
    }
}
