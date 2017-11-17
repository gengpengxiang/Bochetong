package com.bolong.bochetong.activity;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bolong.bochetong.bean2.Version;
import com.bolong.bochetong.utils.AppUtils;
import com.bolong.bochetong.utils.DialogUtil;
import com.bolong.bochetong.utils.HttpUtils;
import com.bolong.bochetong.utils.Param;
import com.bolong.bochetong.utils.SharedPreferenceUtil;
import com.bolong.bochetong.utils.ToastUtil;
import com.google.gson.Gson;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class GywmActivity extends BaseActivity {


    @BindView(R.id.tv_website)
    TextView tvWebsite;
    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(R.id.tv_phoneNumber)
    TextView tvPhoneNumber;
    @BindView(R.id.tv_version_prompt)
    TextView tvVersionPrompt;
    @BindView(R.id.layout_update)
    RelativeLayout layoutUpdate;
    private Unbinder unbind;

    public static Activity activity;

    @Override
    public void onBaseCreate(Bundle bundle) {

        setContentViewId(R.layout.activity_gywm);
        unbind = ButterKnife.bind(this);

        activity = this;
        MPermissions.requestPermissions(this, REQUECT_CODE_SDCARD, Manifest.permission.CALL_PHONE);
    }

    @Override
    public void initView() {

        setTitle("关于我们");
        tvVersion.setText(AppUtils.getAppName(this) + AppUtils.getVersionName(this));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbind.unbind();
    }

    private static final int REQUECT_CODE_SDCARD = 2;

    @OnClick({R.id.tv_website, R.id.tv_phoneNumber, R.id.layout_update})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_website:
                Intent intent = new Intent();
                intent.setClass(GywmActivity.this,WebActivity.class);
                intent.putExtra("url","http://www.blpark.com");
                startActivity(intent);
                break;
            case R.id.tv_phoneNumber:

                Intent intent2 = new Intent("android.intent.action.CALL", Uri.parse("tel:010-60865161"));
                startActivity(intent2);
                break;
            case R.id.layout_update:
                updateCheck();
                break;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @PermissionGrant(REQUECT_CODE_SDCARD)
    public void requestSdcardSuccess()
    {
        //Toast.makeText(this, "已开启!", Toast.LENGTH_SHORT).show();
    }

    @PermissionDenied(REQUECT_CODE_SDCARD)
    public void requestSdcardFailed()
    {
        //Toast.makeText(this, "拒绝!", Toast.LENGTH_SHORT).show();
    }


    public void updateCheck() {
        Map<String, String> map = new HashMap<>();
        map.put("appType", "1");
        HttpUtils.post(Param.UPDATECHECK, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonDatas = response.body().string();
                Log.e("版本", jsonDatas);
                try {
                    try {
                        JSONObject jsonObject = new JSONObject(jsonDatas);
                        String content = jsonObject.optString("content");
                        Gson gson = new Gson();
                        Version version = gson.fromJson(content, Version.class);
                        final String versionNumber = version.getVersionNumber();
                        SharedPreferenceUtil.putString("versionNumber",versionNumber);
                        final String downloadAddress = version.getDownloadAddress();
                        String updateContent = version.getUpdateContent();
                        final String totolContent = "最新版本:" + versionNumber + "\n" + "更新内容: \n" + updateContent;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    if (versionNumber.equals(AppUtils.getVersionName(getApplicationContext()))) {
                                        ToastUtil.showShort(getApplicationContext(), "已经是最新版本");
                                    } else {

                                        DialogUtil.showDialog(GywmActivity.this,downloadAddress,totolContent);
                                    }
                                } catch (Exception e) {

                                }
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {

                }
            }
        },map);
    }

}
