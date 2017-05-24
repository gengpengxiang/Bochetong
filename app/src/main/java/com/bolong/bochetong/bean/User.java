package com.bolong.bochetong.bean;

import java.io.Serializable;

/**
 * Created by admin on 2017/5/8.
 */

public class User implements Serializable{


    /**
     * userId : E98A3FAFC28446E097366118FDC53449
     * token : B81D41C7DE8A4FCA81AD00EF09E39E7A
     * userName : 孙金龙
     * userPhone : 15812341234
     * userPass : 2398LS023KLSAFDL23042
     * userBalance : null
     * userRegistTime : 1493788906
     */

    private String userId;
    private String token;
    private String userName;
    private String userPhone;
    private String userPass;
    private String userBalance;
    private String userRegistTime;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public String getUserBalance() {
        return userBalance;
    }

    public void setUserBalance(String userBalance) {
        this.userBalance = userBalance;
    }

    public String getUserRegistTime() {
        return userRegistTime;
    }

    public void setUserRegistTime(String userRegistTime) {
        this.userRegistTime = userRegistTime;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", token='" + token + '\'' +
                ", userName='" + userName + '\'' +
                ", userPhone='" + userPhone + '\'' +
                ", userPass='" + userPass + '\'' +
                ", userBalance='" + userBalance + '\'' +
                ", userRegistTime='" + userRegistTime + '\'' +
                '}';
    }
}
