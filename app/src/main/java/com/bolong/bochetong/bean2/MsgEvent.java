package com.bolong.bochetong.bean2;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.poi.PoiResult;
import com.bolong.bochetong.bean.User;

import java.util.List;


public class MsgEvent {
    public int action;
    public boolean flag;
    public double arg1, arg2, arg3, arg4;
    public float radius;
    public List<NearbyPark.ParksBean> list;
    public LatLng latLng;
    public PoiResult result;
    public List<Bill.PagenationBean.DataBean> billsList;
    public String str;
    public String str2;
    public int position;
    public int position2;
    public User user;
    public Version version;
    public YearCard.MonthCardBean monthCardBean;
    public City.ContentBean city;


    public MsgEvent(int action, LatLng latLng) {
        this.action = action;
        this.latLng = latLng;
    }

    public MsgEvent(int action, City.ContentBean city) {
        this.action = action;
        this.city = city;
    }

    public MsgEvent(int action, int position, int position2) {
        this.action = action;
        this.position = position;
        this.position2 = position2;
    }

    public MsgEvent(int action, String str,int position,int position2) {
        this.action = action;
        this.str = str;
        this.position = position;
        this.position2 = position2;
    }

    public MsgEvent(int action, int position) {
        this.action = action;
        this.position = position;
    }

    public MsgEvent(int action, String str, String str2) {
        this.action = action;
        this.str = str;
        this.str2 = str2;
    }

    public MsgEvent(int action, String str, String str2,int position) {
        this.action = action;
        this.str = str;
        this.str2 = str2;
        this.position = position;
    }

    public MsgEvent(int action, PoiResult result, int position) {
        this.action = action;
        this.result = result;
        this.position = position;
    }

    public MsgEvent(int action, double arg1, double arg2, float radius, List<NearbyPark.ParksBean> list) {
        this.action = action;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.radius = radius;
        this.list = list;
    }

    public MsgEvent(int action, double arg1, double arg2, float radius,String str,String str2) {
        this.action = action;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.radius = radius;
        this.str = str;
        this.str2 = str2;
    }

    public MsgEvent(int action, double arg1, double arg2, float radius) {
        this.action = action;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.radius = radius;
    }

    public MsgEvent(int action, double arg1, double arg2, double arg3, double arg4) {
        this.action = action;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.arg3 = arg3;
        this.arg4 = arg4;
    }

    public MsgEvent(int action, List<NearbyPark.ParksBean> list) {
        this.action = action;
        this.list = list;
    }

    public MsgEvent(int action) {
        this.action = action;
    }

    public MsgEvent(int action, boolean flag) {
        this.action = action;
        this.flag = flag;
    }

    public MsgEvent(int action, String str) {
        this.action = action;
        this.str = str;
    }

    public String getStr2() {
        return str2;
    }

    public void setStr2(String str2) {
        this.str2 = str2;
    }

    public MsgEvent(int action, String str, List<Bill.PagenationBean.DataBean> billsList) {
        this.action = action;
        this.str = str;
        this.billsList = billsList;
    }

    public MsgEvent(int action, User user) {
        this.action = action;
        this.user = user;
    }

    public MsgEvent(int action, Version version) {
        this.action = action;
        this.version = version;
    }

    public MsgEvent(int action, YearCard.MonthCardBean monthCardBean) {
        this.action = action;
        this.monthCardBean = monthCardBean;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public boolean getFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public double getArg2() {
        return arg2;
    }

    public void setArg2(double arg2) {
        this.arg2 = arg2;
    }

    public double getArg1() {
        return arg1;
    }

    public void setArg1(double arg1) {
        this.arg1 = arg1;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public List<NearbyPark.ParksBean> getList() {
        return list;
    }

    public void setList(List<NearbyPark.ParksBean> list) {
        this.list = list;
    }

    public double getArg4() {
        return arg4;
    }

    public void setArg4(double arg4) {
        this.arg4 = arg4;
    }

    public double getArg3() {
        return arg3;
    }

    public void setArg3(double arg3) {
        this.arg3 = arg3;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public PoiResult getResult() {
        return result;
    }

    public void setResult(PoiResult result) {
        this.result = result;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition2() {
        return position2;
    }

    public void setPosition2(int position2) {
        this.position2 = position2;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    public YearCard.MonthCardBean getMonthCardBean() {
        return monthCardBean;
    }

    public void setMonthCardBean(YearCard.MonthCardBean monthCardBean) {
        this.monthCardBean = monthCardBean;
    }

    public City.ContentBean getCity() {
        return city;
    }

    public void setCity(City.ContentBean city) {
        this.city = city;
    }
}
