package com.bolong.bochetong.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bolong.bochetong.activity.R;
import com.bolong.bochetong.bean.User;
import com.bolong.bochetong.bean2.YearCard;
import com.bolong.bochetong.view.CustomPopDialog;
import com.bolong.bochetong.view.TypesetTextView;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class DialogUtil {

    private static String parkName;

    public static void showDialog(final Context context, final String url, String content) {

        CustomPopDialog.Builder dialogBuild = new CustomPopDialog.Builder(context);
        final CustomPopDialog dialog = dialogBuild.create(R.layout.layout_dialog_update, 0.6, 0.75);
        dialog.setCanceledOnTouchOutside(false);
        TextView tv_update_version = (TextView) dialog.findViewById(R.id.tv_update_version);
        tv_update_version.setMovementMethod(ScrollingMovementMethod.getInstance());
        tv_update_version.setText(content);
        //dialog.show();
        QueueUtil.showDialog(dialog);

        TextView bt_update_cancel = (TextView) dialog.findViewById(R.id.bt_update_cancel);
        TextView bt_update = (TextView) dialog.findViewById(R.id.bt_update);
        bt_update_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ToastUtil.showShort(context, "取消");
                dialog.dismiss();
            }
        });
        bt_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                //add
                String versionNumber = SharedPreferenceUtil.getString("versionNumber","1.0.1");

                if (FileUtil.isExists(Param.FILEPATH+versionNumber+".apk")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setDataAndType(Uri.fromFile(new File(Param.FILEPATH+versionNumber+".apk")),
                            "application/vnd.android.package-archive");
                    context.startActivity(intent);
                } else {
                    new DownloadAnsy().execute(url);
                }
            }
        });
    }

    public static void showMonthCardDialog(Context context, int layoutId,String str) {

        CustomPopDialog.Builder dialogBuild = new CustomPopDialog.Builder(context);
        CustomPopDialog dialog = dialogBuild.create2(layoutId);
        dialog.setCanceledOnTouchOutside(true);
        QueueUtil.showDialog(dialog);
//        TextView tv = (TextView) dialog.findViewById(R.id.tv_monthcard_info);
        TypesetTextView tv = (TypesetTextView) dialog.findViewById(R.id.tv_monthcard_info);
//        tv.setText(StringFilter(s0));
        tv.setText(str);


    }

    public static void showMonthCardDialog2(Context context, int layoutId,String str) {
        CustomPopDialog.Builder dialogBuild = new CustomPopDialog.Builder(context);
        final CustomPopDialog dialog = dialogBuild.create2(layoutId);
        dialog.setCanceledOnTouchOutside(true);
        QueueUtil.showDialog(dialog);
        TextView tv = (TextView) dialog.findViewById(R.id.tv_monthcard_date);

        String s1 = context.getResources().getString(R.string.monthcard_date1);
        String s0 = str;
        String s2 = context.getResources().getString(R.string.monthcard_date2);

        SpannableStringBuilder spannable = new SpannableStringBuilder(toDBC(s1 + s0 + s2));
        spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#5084ed")), 15, 26, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(spannable);

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                dialog.dismiss();
//            }
//        }, 4000);
    }

    public static String toDBC(String str) {
        char[] c = str.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375) {
                c[i] = (char) (c[i] - 65248);
            }

        }
        return new String(c);
    }

    public static String StringFilter(String str) throws PatternSyntaxException {
        str = str.replaceAll("【", "[").replaceAll("】", "]").replaceAll("！", "!");//替换中文标号
        String regEx = "[『』]"; // 清除掉特殊字符
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

}
