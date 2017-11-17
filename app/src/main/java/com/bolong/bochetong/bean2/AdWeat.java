package com.bolong.bochetong.bean2;

import java.util.List;

/**
 * Created by admin on 2017/6/14.
 */

public class AdWeat {

    /**
     * traf : 3和8
     * weat : 多云  18~27℃
     * blAds : [{"id":1,"positionId":80,"title":"滴滴","url":"http://china.huanqiu.com/article/2017-05/10582600.html?from=bdwz","picture":"http://101.201.145.238/group1/M00/00/02/ZcmR7lkdM8CAG67yAAC6RNV9Wbk360.png","height":800,"width":600},{"id":2,"positionId":80,"title":"快递","url":"http://china.huanqiu.com/article/2017-05/10581474.html?from=bdwz","picture":"http://101.201.145.238/group1/M00/00/02/ZcmR7lkdMy2ARrrfAAVmh-uXCBE428.png","height":800,"width":600},{"id":3,"positionId":80,"title":"铂隆","url":"http://www.blpark.com/","picture":"http://101.201.145.238/group1/M00/00/02/ZcmR7lkdMjuAVIjoAAU6hWipFUE043.png","height":800,"width":600}]
     */

    private String traf;
    private String weat;
    private List<BlAdsBean> blAds;

    public String getTraf() {
        return traf;
    }

    public void setTraf(String traf) {
        this.traf = traf;
    }

    public String getWeat() {
        return weat;
    }

    public void setWeat(String weat) {
        this.weat = weat;
    }

    public List<BlAdsBean> getBlAds() {
        return blAds;
    }

    public void setBlAds(List<BlAdsBean> blAds) {
        this.blAds = blAds;
    }

    public static class BlAdsBean {
        /**
         * id : 1
         * positionId : 80
         * title : 滴滴
         * url : http://china.huanqiu.com/article/2017-05/10582600.html?from=bdwz
         * picture : http://101.201.145.238/group1/M00/00/02/ZcmR7lkdM8CAG67yAAC6RNV9Wbk360.png
         * height : 800
         * width : 600
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
