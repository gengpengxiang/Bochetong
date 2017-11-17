package com.bolong.bochetong.utils;

import android.os.Environment;

public class Param {

    public static String FILEPATH = Environment.getExternalStorageDirectory() + "/download/铂车通";
    //public static String path = Environment.getExternalStorageDirectory() + "/download/";

    public static final String UID = "1";

    public static final String TOKEN = "B81D41C7DE8A4FCA81AD00EF09E39E7A";

    public static final String WX_APP_ID = "wx21388637886dba0b";
    //正式地址
   // public static final String IP = "http://101.201.145.238";
    //测试地址
    public static final String IP = "http://101.201.145.238:8666";

//    public static final String IP = "http://192.168.1.220/bolong_web";

    public static final String HOMECONTENT = IP + "/app/getHomeContent";

    public static final String CARPARTLIST = IP + "/app/getcarparklist";

    public static final String TIMER = IP + "/app/gettimer";

    public static final String ADDCARCARD = IP + "/app/addcarcard";

    public static final String DELETECARCARD = IP + "/app/deletecarcard";

    public static final String MONTHCARDLIST = IP + "/app/getmonthcardlist";

    //public static final String ALIPAY = IP + "/app/alipay/pay";

    public static final String EXTENDPAY = IP + "/app/alipay/extendedPay";

    public static final String NOTECASE = IP + "/app/getnotecase";


    public static final String CARRECORDLIST = IP + "/app/getcarrecordlist";

    public static final String USERLOGIN = IP + "/app/userlogin";

    public static final String COMPLAINTSSUGGEST = IP + "/app/complaintsSuggest";

    public static final String AUTOPAY = IP + "/app/autopay";

    public static final String LOCKCAR = IP + "/app/lockCar";

    //2.0
//    public static final String ADWEAT = IP + "/app/getadweat";
    public static final String ADWEAT = IP + "/app/navigationInfo";
    public static final String NEARBYPARK = IP + "/app/getnearbypark";
    public static final String PARKDETAIL = IP + "/app/getparkdetail";
    public static final String NEARBYZONE = IP + "/app/getnearbyzone";
    public static final String ZONEDETAIL = IP + "/app/getzonedetail";

    public static final String LOGIN = IP + "/app/login";

    public static final String SENDSMS = IP + "/app/sendsms";

    public static final String CHECKSMS = IP + "/app/checksms";

    //public static final String HOMETIMER = IP + "/app/hometimer";

    public static final String CARCARDLIST = IP + "/app/getcarcardlist";

    public static final String PARKRECORDLIST = IP + "/app/getparkrecordlist";

    public static final String CASHCOUPON = IP + "/app/getcashcoupon";

    public static final String ARREARAGETABLE = IP + "/app/getarrearages";

    public static final String STAYOPENRECORD = IP + "/app/stayopenrecord";

    public static final String GETINVOICELIST = IP + "/app/getinvoicelist";

    public static final String BUILDINVOICE = IP + "/app/buildinvoice";

    public static final String GETINVOICE = IP + "/app/getinvoice";

    public static final String GETFREETIMELIST = IP + "/app/getfreetimelist";

    public static final String WXPAY = IP + "/app/wxpay/pay";

    public static final String ALIPAY = IP + "/app/alipay/pay";

    public static final String GETMONTHCARDLIST = IP + "/app/getmonthcardlist";

    public static final String UPDATECHECK = IP + "/app/updatecheck";

    public static final String MONTHCARDS = IP + "/app/monthCards";

    public static final String BANDMONTHCARDCAR = IP + "/app/bandMonthCardCar";

    public static final String DELETEMONTHCARDCAR = IP + "/app/deleteMonthCardCar";

    public static final String EDITPHONE = IP + "/app/editphone";

    public static final String HOMETIMERS = IP + "/app/hometimers";

    public static final String GETCITYS = IP + "/app/getcitys";

    public static final String BAOXIAN = "http://group.e-chinalife.com/ecs-online/web/sale/newcarsale/xintoubao_login.html";

    //月卡
    public static final String GETSTOPCARPARKLISTBYCITY = IP + "/app/getStopcarParkListByCity";
    public static final String CREATEBUYMONTHCARDORDER = IP + "/app/createBuyMonthCardOrder";
    public static final String GETMONTHCARDCHARGE = IP + "/app/getMonthCardCharge";
    public static final String REALMONTHCARDS = IP + "/app/realmonthCards";
        }