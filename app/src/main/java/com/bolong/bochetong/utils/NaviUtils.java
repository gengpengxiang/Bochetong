package com.bolong.bochetong.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import com.baidu.navisdk.adapter.BNCommonSettingParam;
import com.baidu.navisdk.adapter.BNOuterTTSPlayerCallback;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNaviSettingManager;
import com.baidu.navisdk.adapter.BaiduNaviManager;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by admin on 2017/5/16.
 */

public class NaviUtils {


    public static final int authBaseRequestCode = 1;
    public static final int authComRequestCode = 2;

    public static boolean hasInitSuccess = false;
    public static boolean hasRequestComAuth = false;


    public static String mSDCardPath = null;
    public static final String APP_FOLDER_NAME = "BNSDKSimpleDemo";
    public static boolean initDirs() {
        mSDCardPath = getSdcardDir();
        if (mSDCardPath == null) {
            return false;
        }
        File f = new File(mSDCardPath, APP_FOLDER_NAME);
        if (!f.exists()) {
            try {
                f.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    /**
     * 内部TTS播报状态回传handler
     */
    public static Handler ttsHandler = new Handler() {
        public void handleMessage(Message msg) {
            int type = msg.what;
            switch (type) {
                case BaiduNaviManager.TTSPlayMsgType.PLAY_START_MSG: {
                    // showToastMsg("Handler : TTS play start");
                    break;
                }
                case BaiduNaviManager.TTSPlayMsgType.PLAY_END_MSG: {
                    // showToastMsg("Handler : TTS play end");
                    break;
                }
                default:
                    break;
            }
        }
    };

    /**
     * 内部TTS播报状态回调接口
     */
    public static BaiduNaviManager.TTSPlayStateListener ttsPlayStateListener = new BaiduNaviManager.TTSPlayStateListener() {

        @Override
        public void playEnd() {
            // showToastMsg("TTSPlayStateListener : TTS play end");
        }

        @Override
        public void playStart() {
            // showToastMsg("TTSPlayStateListener : TTS play start");
        }
    };

    public static final String[] authBaseArr = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION};
    public static final String[] authComArr = {Manifest.permission.READ_PHONE_STATE};
    public static boolean hasBasePhoneAuth(Activity activity) {
        // TODO Auto-generated method stub

        PackageManager pm = activity.getPackageManager();
        for (String auth : authBaseArr) {
            if (pm.checkPermission(auth, activity.getPackageName()) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public static boolean hasCompletePhoneAuth(Activity activity) {
        // TODO Auto-generated method stub

        PackageManager pm = activity.getPackageManager();
        for (String auth : authComArr) {
            if (pm.checkPermission(auth, activity.getPackageName()) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public static void initNavi(final Activity activity) {

        BNOuterTTSPlayerCallback ttsCallback = null;

        // 申请权限
        if (android.os.Build.VERSION.SDK_INT >= 23) {

            if (!hasBasePhoneAuth(activity)) {

                activity.requestPermissions(authBaseArr, authBaseRequestCode);
                return;

            }
        }

        BaiduNaviManager.getInstance().init( activity, mSDCardPath, APP_FOLDER_NAME, new BaiduNaviManager.NaviInitListener() {
            String authinfo = null;
            @Override
            public void onAuthResult(int status, String msg) {
                if (0 == status) {
   //                 authinfo = "key校验成功!";
                } else {
   //                 authinfo = "key校验失败, " + msg;
                }
                activity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                       // Toast.makeText(activity, authinfo, Toast.LENGTH_LONG).show();
                    }
                });
            }

            public void initSuccess() {
   //             Toast.makeText(activity, "百度导航引擎初始化成功", Toast.LENGTH_SHORT).show();
                hasInitSuccess = true;
                initSetting();
            }

            public void initStart() {
   //             Toast.makeText(activity, "百度导航引擎初始化开始", Toast.LENGTH_SHORT).show();
            }

            public void initFailed() {
 //               Toast.makeText(activity, "百度导航引擎初始化失败", Toast.LENGTH_SHORT).show();
            }

        }, null, ttsHandler, ttsPlayStateListener);

    }

    public static String getSdcardDir() {
        if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().toString();
        }
        return null;
    }

    public static void routeplanToNavi(Activity activity,double startLongitude,double startLatitude,double stopLongitude,double stopLatitude,BaiduNaviManager.RoutePlanListener listener) {
        if (!hasInitSuccess) {
            //Toast.makeText(BNDemoMainActivity.this, "还未初始化!", Toast.LENGTH_SHORT).show();
        }
        // 权限申请
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            // 保证导航功能完备
            if (!hasCompletePhoneAuth(activity)) {
                if (!hasRequestComAuth) {
                    hasRequestComAuth = true;
                    activity.requestPermissions(authComArr, authComRequestCode);
                    return;
                } else {
                    //Toast.makeText(BNDemoMainActivity.this, "没有完备的权限!", Toast.LENGTH_SHORT).show();
                }
            }

        }
        //自定义起点与终点
        //mStartLocationData;
        //mDestLocationData;
        BNRoutePlanNode.CoordinateType coType = BNRoutePlanNode.CoordinateType.BD09LL;
        BNRoutePlanNode sNode = null;
        BNRoutePlanNode eNode = null;

//        sNode = new BNRoutePlanNode(116.30142, 40.05087, "百度大厦", null, coType);
//        eNode = new BNRoutePlanNode(116.39750, 39.90882, "北京天安门", null, coType);
        sNode = new BNRoutePlanNode(startLongitude, startLatitude, "起点", null, coType);
        eNode = new BNRoutePlanNode(stopLongitude, stopLatitude, "目的地", null, coType);


        if (sNode != null && eNode != null) {
            List<BNRoutePlanNode> list = new ArrayList<BNRoutePlanNode>();
            list.add(sNode);
            list.add(eNode);
            //此方法boolean参数代表真实导航还是虚拟
            BaiduNaviManager.getInstance().launchNavigator(activity, list, 1, true, listener);
        }
    }

    public static List<Activity> activityList = new LinkedList<Activity>();


    /**
     * 初始化tts语音
     */
    public static void initSetting() {
        // BNaviSettingManager.setDayNightMode(BNaviSettingManager.DayNightMode.DAY_NIGHT_MODE_DAY);
        BNaviSettingManager
                .setShowTotalRoadConditionBar(BNaviSettingManager.PreViewRoadCondition.ROAD_CONDITION_BAR_SHOW_ON);
        BNaviSettingManager.setVoiceMode(BNaviSettingManager.VoiceMode.Veteran);
        // BNaviSettingManager.setPowerSaveMode(BNaviSettingManager.PowerSaveMode.DISABLE_MODE);
        BNaviSettingManager.setRealRoadCondition(BNaviSettingManager.RealRoadCondition.NAVI_ITS_ON);
        Bundle bundle = new Bundle();
        // 必须设置APPID，否则会静音
        bundle.putString(BNCommonSettingParam.TTS_APP_ID, "9569088");
        BNaviSettingManager.setNaviSdkParam(bundle);
    }



}
