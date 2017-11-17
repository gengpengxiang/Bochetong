package com.bolong.bochetong.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bolong.bochetong.bean2.MsgEvent;

import org.greenrobot.eventbus.EventBus;

import static com.bolong.bochetong.activity.NewMainActivity.ACTION_LOCATION;
import static com.bolong.bochetong.activity.NewMainActivity.ACTION_LOCATION_SUCCESS;

public class LocationUtil {

    public static final int ACTION_LOCATION_SECOND = 44444;

    public static final int ACTION_LOCATION_NEW = 4474456;

    public static final int ACTION_LOCATION_NEWMAP = 4484456;

    public static final int ACTION_LOCATION_FAIL = 4444457;

    private static LocationClient mLocationClient = null;
    private static BDLocationListener myListener = new MyLocationListener();

    public static void start(Context context) {
        mLocationClient = new LocationClient(context);
        mLocationClient.registerLocationListener(myListener);

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");
//        int span = 1000;
//        option.setScanSpan(span);
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setLocationNotify(true);
        option.setIsNeedLocationDescribe(true);
        option.setIsNeedLocationPoiList(true);
        option.setIsNeedAddress(true);
        option.setIgnoreKillProcess(false);
        option.SetIgnoreCacheException(false);
        option.setEnableSimulateGps(false);
        mLocationClient.setLocOption(option);

        mLocationClient.start();
    }

    public static void stop() {
        if (mLocationClient != null) {
            mLocationClient.unRegisterLocationListener(myListener);
            mLocationClient.stop();
            mLocationClient = null;
            Log.e("定位关闭","close");
        }
    }

    private static double errorLnglat = 4.9E-324;
    private static class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            Log.e("工具类定位",bdLocation.getLatitude()+"");

            double longitude = bdLocation.getLongitude();
            double latitude = bdLocation.getLatitude();
            float radius = bdLocation.getRadius();
            String cityName = bdLocation.getCity();
            String addrStr = bdLocation.getCity();
            String cityCode = bdLocation.getCityCode();
            if(longitude==errorLnglat){
                EventBus.getDefault().post(new MsgEvent(ACTION_LOCATION_FAIL));
                Log.e("定位失败",errorLnglat+"");
            }else {
                    EventBus.getDefault().post(new MsgEvent(ACTION_LOCATION_SUCCESS, longitude,latitude,radius,cityName,cityCode));
                    //EventBus.getDefault().post(new MsgEvent(ACTION_LOCATION_NEWMAP, longitude,latitude,radius,cityName,cityCode));
                    Log.e("定位信息已发送", cityName + longitude + "==" + latitude+"cityId"+bdLocation.getCityCode());
                SharedPreferenceUtil.putString("cityCode",cityCode);
                SharedPreferenceUtil.putString("currentCityName",cityName);
                    stop();
            }

        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }
}
