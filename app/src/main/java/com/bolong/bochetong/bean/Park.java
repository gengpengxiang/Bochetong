package com.bolong.bochetong.bean;

import java.io.Serializable;
import java.util.List;


public class Park implements Serializable {


    /**
     * errCode : 1000
     * errMessage : 请求成功
     * content : {"totalCount":2,"pageNo":1,"totalPage":1,"date":[{"parkId":"1","parkName":"惠康家园六区停车场","parkAddress":"门头沟门头沟区永定镇石龙北路52号","parkedPeopleNum":"0","emptyPosition":"30","parkDistance":"22km","parkPhoto":"http://101.201.145.238/group1/M00/00/00/ZcmR7lkJfVmAFTwZAADfy2CWDTQ543.jpg"},{"parkId":"2","parkName":"门头沟十块地停车场","parkAddress":"北京市门头沟区龙林路","parkedPeopleNum":"0","emptyPosition":"25","parkDistance":"28km","parkPhoto":"http://101.201.145.238/group1/M00/00/00/ZcmR7lkJfy-AAztkAADlKHZkygg013.jpg"}]}
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

    public static class ContentBean implements Serializable{
        /**
         * totalCount : 2
         * pageNo : 1
         * totalPage : 1
         * date : [{"parkId":"1","parkName":"惠康家园六区停车场","parkAddress":"门头沟门头沟区永定镇石龙北路52号","parkedPeopleNum":"0","emptyPosition":"30","parkDistance":"22km","parkPhoto":"http://101.201.145.238/group1/M00/00/00/ZcmR7lkJfVmAFTwZAADfy2CWDTQ543.jpg"},{"parkId":"2","parkName":"门头沟十块地停车场","parkAddress":"北京市门头沟区龙林路","parkedPeopleNum":"0","emptyPosition":"25","parkDistance":"28km","parkPhoto":"http://101.201.145.238/group1/M00/00/00/ZcmR7lkJfy-AAztkAADlKHZkygg013.jpg"}]
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

        public static class DateBean implements Serializable{
            /**
             * parkId : 1
             * parkName : 惠康家园六区停车场
             * parkAddress : 门头沟门头沟区永定镇石龙北路52号
             * parkedPeopleNum : 0
             * emptyPosition : 30
             * parkDistance : 22km
             * parkPhoto : http://101.201.145.238/group1/M00/00/00/ZcmR7lkJfVmAFTwZAADfy2CWDTQ543.jpg
             * "partLongitude":"116.1252070",
             * "partLatitude":"39.9197640"
             */

            private String parkId;
            private String parkName;
            private String parkAddress;
            private String parkedPeopleNum;
            private String emptyPosition;
            private String parkDistance;
            private String parkPhoto;
            private String partLongitude;
            private String partLatitude;
            private String parkMaxnum;

            public String getParkMaxnum() {
                return parkMaxnum;
            }

            public void setParkMaxnum(String parkMaxnum) {
                this.parkMaxnum = parkMaxnum;
            }

            public String getParkId() {
                return parkId;
            }

            public void setParkId(String parkId) {
                this.parkId = parkId;
            }

            public String getParkName() {
                return parkName;
            }

            public void setParkName(String parkName) {
                this.parkName = parkName;
            }

            public String getParkAddress() {
                return parkAddress;
            }

            public void setParkAddress(String parkAddress) {
                this.parkAddress = parkAddress;
            }

            public String getParkedPeopleNum() {
                return parkedPeopleNum;
            }

            public void setParkedPeopleNum(String parkedPeopleNum) {
                this.parkedPeopleNum = parkedPeopleNum;
            }

            public String getEmptyPosition() {
                return emptyPosition;
            }

            public void setEmptyPosition(String emptyPosition) {
                this.emptyPosition = emptyPosition;
            }

            public String getParkDistance() {
                return parkDistance;
            }

            public void setParkDistance(String parkDistance) {
                this.parkDistance = parkDistance;
            }

            public String getParkPhoto() {
                return parkPhoto;
            }

            public void setParkPhoto(String parkPhoto) {
                this.parkPhoto = parkPhoto;
            }

            public String getPartLongitude() {
                return partLongitude;
            }

            public void setPartLongitude(String partLongitude) {
                this.partLongitude = partLongitude;
            }

            public String getPartLatitude() {
                return partLatitude;
            }

            public void setPartLatitude(String partLatitude) {
                this.partLatitude = partLatitude;
            }
        }
    }
}
