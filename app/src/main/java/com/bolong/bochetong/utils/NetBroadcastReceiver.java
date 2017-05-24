package com.bolong.bochetong.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.bolong.bochetong.activity.BaseActivity;
import com.bolong.bochetong.activity.MyApplication;

import java.util.ArrayList;

/**
 * Created by admin on 2017/5/11.
 */

public class NetBroadcastReceiver extends BroadcastReceiver {



    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            int netWorkState = NetUtil.getNetWorkState(context);

        }
    }



}
