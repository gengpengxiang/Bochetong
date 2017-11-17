package com.bolong.bochetong.pay;

import com.google.gson.annotations.SerializedName;

/**
 * Created by admin on 2017/6/29.
 */

public class WxEntity {


    /**
     * sign : 43DF218DE9F39FC63043CBD77874908A
     * timestamp : 1498784939
     * noncestr : WTOb9qzVbDBV82J1radQKJagJPF6dsn1
     * partnerid : 1480321732
     * prepayid : wx20170630090859025ea0e35c0875483462
     * package : Sign=WXPay
     * appid : wx21388637886dba0b
     */

    private String sign;
    private String timestamp;
    private String noncestr;
    private String partnerid;
    private String prepayid;
    @SerializedName("package")
    private String packageX;
    private String appid;

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getPackageX() {
        return packageX;
    }

    public void setPackageX(String packageX) {
        this.packageX = packageX;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }
}
