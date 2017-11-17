package com.bolong.bochetong.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.bolong.bochetong.utils.FileUtil;
import com.bolong.bochetong.utils.Param;
import com.bolong.bochetong.utils.SharedPreferenceUtil;

import java.io.File;

public class UpdateReceiver extends BroadcastReceiver{


    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_PACKAGE_ADDED.equals(intent.getAction())) {

        }

        if (Intent.ACTION_PACKAGE_REMOVED.equals(intent.getAction())) {
        }

        if (Intent.ACTION_PACKAGE_REPLACED.equals(intent.getAction())) {

            String packageName = intent.getData().getSchemeSpecificPart();
            //add
            String versionNumber = SharedPreferenceUtil.getString("versionNumber","1.0.1");

            if (FileUtil.isExists(Param.FILEPATH+versionNumber+".apk")&&packageName.equals("com.bolong.bochetong.activity")) {
                File file = new File(Param.FILEPATH+versionNumber+".apk");
                file.delete();
            }

        }
    }
}
