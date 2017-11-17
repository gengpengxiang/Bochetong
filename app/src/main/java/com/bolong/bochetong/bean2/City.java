package com.bolong.bochetong.bean2;

import java.util.List;

public class City {


    /**
     * errCode : 1000
     * errMessage : 请求成功
     * content : [{"id":"131","cityName":"北京","cityPinyin":"beijing","longtitude":116.133092,"latitude":39.902644,"provinceId":"110000"},{"id":"326","cityName":"烟台","cityPinyin":"yantai","longtitude":121.454223,"latitude":37.470841,"provinceId":"370000"},{"id":"42","cityName":"佳木斯","cityPinyin":"jiamusi","longtitude":130.325479,"latitude":46.806278,"provinceId":"230000"},{"id":"49","cityName":"牡丹江","cityPinyin":"mudanjiang","longtitude":129.640285,"latitude":44.558312,"provinceId":"230000"}]
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

    public static class ContentBean {
        /**
         * id : 131
         * cityName : 北京
         * cityPinyin : beijing
         * longtitude : 116.133092
         * latitude : 39.902644
         * provinceId : 110000
         */

        private String id;
        private String cityName;
        private String cityPinyin;
        private double longtitude;
        private double latitude;
        private String provinceId;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public String getCityPinyin() {
            return cityPinyin;
        }

        public void setCityPinyin(String cityPinyin) {
            this.cityPinyin = cityPinyin;
        }

        public double getLongtitude() {
            return longtitude;
        }

        public void setLongtitude(double longtitude) {
            this.longtitude = longtitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public String getProvinceId() {
            return provinceId;
        }

        public void setProvinceId(String provinceId) {
            this.provinceId = provinceId;
        }
    }
}
