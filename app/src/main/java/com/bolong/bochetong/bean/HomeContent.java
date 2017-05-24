package com.bolong.bochetong.bean;

import java.util.List;

/**
 * Created by admin on 2017/5/10.
 */

public class HomeContent {

    /**
     * adPositionId : [{"id":1,"positionId":80,"title":"滴滴","url":"http://china.huanqiu.com/article/2017-05/10582600.html?from=bdwz","picture":"https://imgsa.baidu.com/news/q%3D100/sign=1236f908753e6709b80041ff0bc69fb8/ac4bd11373f08202ef1f941041fbfbedab641b0b.jpg","height":400,"width":300},{"id":2,"positionId":80,"title":"快递","url":"http://china.huanqiu.com/article/2017-05/10581474.html?from=bdwz","picture":"https://imgsa.baidu.com/news/q%3D100/sign=5ddf1b32de2a283445a6320b6bb4c92e/2e2eb9389b504fc2e8e4bb2aefdde71190ef6d61.jpg","height":400,"width":300}]
     * weatherContent : {"city":"北京","temp":"14~26℃","weather":"浮尘","quality":"严重污染","weatherImg":"29"}
     */

    private WeatherContentBean weatherContent;
    private List<AdPositionIdBean> adPositionId;

    public WeatherContentBean getWeatherContent() {
        return weatherContent;
    }

    public void setWeatherContent(WeatherContentBean weatherContent) {
        this.weatherContent = weatherContent;
    }

    public List<AdPositionIdBean> getAdPositionId() {
        return adPositionId;
    }

    public void setAdPositionId(List<AdPositionIdBean> adPositionId) {
        this.adPositionId = adPositionId;
    }

    public static class WeatherContentBean {
        /**
         * city : 北京
         * temp : 14~26℃
         * weather : 浮尘
         * quality : 严重污染
         * weatherImg : 29
         */

        private String city;
        private String temp;
        private String weather;
        private String quality;
        private String weatherImg;

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getTemp() {
            return temp;
        }

        public void setTemp(String temp) {
            this.temp = temp;
        }

        public String getWeather() {
            return weather;
        }

        public void setWeather(String weather) {
            this.weather = weather;
        }

        public String getQuality() {
            return quality;
        }

        public void setQuality(String quality) {
            this.quality = quality;
        }

        public String getWeatherImg() {
            return weatherImg;
        }

        public void setWeatherImg(String weatherImg) {
            this.weatherImg = weatherImg;
        }
    }

    public static class AdPositionIdBean {
        /**
         * id : 1
         * positionId : 80
         * title : 滴滴
         * url : http://china.huanqiu.com/article/2017-05/10582600.html?from=bdwz
         * picture : https://imgsa.baidu.com/news/q%3D100/sign=1236f908753e6709b80041ff0bc69fb8/ac4bd11373f08202ef1f941041fbfbedab641b0b.jpg
         * height : 400
         * width : 300
         */

        private int id;
        private int positionId;
        private String title;
        private String url;
        private String picture;
        private int height;
        private int width;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getPositionId() {
            return positionId;
        }

        public void setPositionId(int positionId) {
            this.positionId = positionId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }
    }
}
