package com.bolong.bochetong.maputils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import java.io.File;

public class AMapUtil {
    public static  void goToNaviActivity(Context context, String sourceApplication , String poiname , String lat , String lon , String dev , String style){
        StringBuffer stringBuffer  = new StringBuffer("androidamap://navi?sourceApplication=")
                .append(sourceApplication);
        if (!TextUtils.isEmpty(poiname)){
            stringBuffer.append("&poiname=").append(poiname);
        }
        stringBuffer.append("&lat=").append(lat)
                .append("&lon=").append(lon)
                .append("&dev=").append(dev)
                .append("&style=").append(style);

        Intent intent = new Intent("android.intent.action.VIEW", android.net.Uri.parse(stringBuffer.toString()));
        intent.setPackage("com.autonavi.minimap");
        context.startActivity(intent);
    }

    /**
     * 根据包名检测某个APP是否安装
     * <h3>Version</h3> 1.0
     * <h3>CreateTime</h3> 2016/6/27,13:02
     * <h3>UpdateTime</h3> 2016/6/27,13:02
     * <h3>CreateAuthor</h3>
     * <h3>UpdateAuthor</h3>
     * <h3>UpdateInfo</h3> (此处输入修改内容,若无修改可不写.)
     *
     * @param packageName 包名
     * @return true 安装 false 没有安装
     */
    public static boolean isInstallByRead(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }


}
