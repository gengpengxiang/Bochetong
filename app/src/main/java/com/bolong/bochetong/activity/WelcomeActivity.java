package com.bolong.bochetong.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.bolong.bochetong.bean.User;
import com.bolong.bochetong.bean2.MsgEvent;
import com.bolong.bochetong.utils.HttpUtils;
import com.bolong.bochetong.utils.Param;
import com.bolong.bochetong.utils.SharedPreferenceUtil;
import com.bolong.bochetong.utils.StatusBarUtil;
import com.bolong.bochetong.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WelcomeActivity extends AppCompatActivity {

    private int ACTION_CHECK_STATUS = 871234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_welcome);
        EventBus.getDefault().register(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            StatusBarUtil.compat(this, Color.parseColor("#000000"));
        }

        checkStatus();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateUI(MsgEvent event) {
        if(event.getAction() == ACTION_CHECK_STATUS){
            String errCode = event.getStr();
            String status = event.getStr2();
            if(SharedPreferenceUtil.getBoolean("firstStart",true)){
                final Intent intent = new Intent(this, GuideActivity.class);
                final Timer timer = new Timer();
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        startActivityForResult(intent, 1);
                        SharedPreferenceUtil.putBoolean("firstStart",false);
                        timer.cancel();
                        finish();
                    }
                };
                timer.schedule(task, 500 * 1);
            }else {
                if (errCode.equals("1000")){
                    final Intent intent = new Intent(this, NewMainActivity.class);
                    Timer timer = new Timer();
                    TimerTask task = new TimerTask() {
                        @Override
                        public void run() {
                            startActivityForResult(intent, 1);
                            finish();
                        }
                    };
                    timer.schedule(task, 500 * 1);
                }else {
                    if(status.equals("请先登录")){
                        ToastUtil.showShort(WelcomeActivity.this,"系统检测到您长时间未操作，请重新登陆");
                    }

                    final Intent intent = new Intent(this, LoginActivity.class);
                    Timer timer = new Timer();
                    TimerTask task = new TimerTask() {
                        @Override
                        public void run() {
                            startActivityForResult(intent, 1);
                            finish();
                        }
                    };
                    timer.schedule(task, 500 * 1);
                }

            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void checkStatus() {
        String uid = "";
        String token = "";
        if (SharedPreferenceUtil.getBean(getApplicationContext(), "userInfo") != null) {
            User user = (User) SharedPreferenceUtil.getBean(getApplicationContext(), "userInfo");
            uid = user.getUserId();
            token = user.getToken();
        } else {
//            uid = Param.UID;
//            token = Param.TOKEN;
        }

        Map<String, String> map = new HashMap<>();
        map.put("uid", uid);
        map.put("token", token);
        Log.e("数据",uid+"==="+token);
        HttpUtils.post(Param.CARCARDLIST, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("检测","失败");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showShort(WelcomeActivity.this,"服务器异常");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonDatas = response.body().string();
                Log.e("绑定车牌", jsonDatas);
                try {
                    try {
                        JSONObject jsonObject = new JSONObject(jsonDatas);
                        String errCode = jsonObject.optString("errCode");
                        String errMessage = jsonObject.optString("content");

                        if(errCode.equals("1000")){
                            EventBus.getDefault().post(new MsgEvent(ACTION_CHECK_STATUS,errCode,""));

                        }else {
                            JSONObject jb = new JSONObject(errMessage);
                            String status = jb.optString("status");
                            EventBus.getDefault().post(new MsgEvent(ACTION_CHECK_STATUS,errCode,status));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("检测","1");

                    }
                } catch (Exception e) {
                    Log.e("检测","2");

                }
            }
        }, map);
    }
}
