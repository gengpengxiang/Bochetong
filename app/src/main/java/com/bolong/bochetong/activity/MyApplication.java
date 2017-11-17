package com.bolong.bochetong.activity;

import android.app.Application;
import com.baidu.mapapi.SDKInitializer;
import cn.jpush.android.api.JPushInterface;

public class MyApplication extends Application {
    private static MyApplication instance;

    public static MyApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        SDKInitializer.initialize(getApplicationContext());

//        JPushInterface.setDebugMode(false);    // 设置开启日志,发布时请关闭日志
//        JPushInterface.init(this);


    }



}
