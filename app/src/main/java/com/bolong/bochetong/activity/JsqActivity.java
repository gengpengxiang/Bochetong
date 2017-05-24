package com.bolong.bochetong.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bolong.bochetong.bean.CarPlate;
import com.bolong.bochetong.bean.CarPlateDao;
import com.bolong.bochetong.bean.Timer;
import com.bolong.bochetong.bean.User;
import com.bolong.bochetong.utils.DateUtils;
import com.bolong.bochetong.utils.HttpUtils;
import com.bolong.bochetong.utils.JsonValidatorUtils;
import com.bolong.bochetong.utils.NetBroadcastReceiver;
import com.bolong.bochetong.utils.NetUtil;
import com.bolong.bochetong.utils.NetworkAvailableUtils;
import com.bolong.bochetong.utils.Param;
import com.bolong.bochetong.utils.SharedPreferenceUtil;
import com.bolong.bochetong.utils.ToastUtil;
import com.bolong.bochetong.view.TimerView;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class JsqActivity extends BaseActivity {
    private Unbinder unbinder;
    @BindView(R.id.timerView)
    TimerView timerView;
    @BindView(R.id.tv_jsq_chepai)
    TextView tvJsqChepai;
    @BindView(R.id.tv_diffTime)
    TextView tvDiffTime;
    @BindView(R.id.switch_lock)
    Switch switchLock;
    @BindView(R.id.switch_pay)
    Switch switchPay;
    @BindView(R.id.tv_jsq_park)
    TextView tvJsqPark;
    @BindView(R.id.tv_jsq_money)
    TextView tvJsqMoney;
    private Date endDate;
    private long endTime;
    private long startTime;
    private long timeDiff;

    private long diffMinutes;
    private long diffHours;
    private long diffDays;
    private CarPlateDao mCardPlateDao;


    @Override
    public void onBaseCreate(Bundle bundle) {
        setTheme(R.style.Color1SwitchStyle);
        setContentViewId(R.layout.activity_jsq);

        unbinder = ButterKnife.bind(this);
        //进入后判断有无网络
        checkedSwitch();

        doPost();
        //timerView.start();

    }

    @Override
    public void initView() {
        setTitle("计时器");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    private void checkedSwitch() {

        switchLock.setChecked(SharedPreferenceUtil.getBoolean("lock",false));
        switchLock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.e("Switch", "Switch开启");
                    switchChecked(lockUrl, switchLock, "锁车", isChecked,"lock");

                } else {
                    Log.e("Switch", "Switch关闭");
                    switchChecked(lockUrl, switchLock, "锁车", isChecked,"lock");

                }
            }
        });

        switchPay.setChecked(SharedPreferenceUtil.getBoolean("pay",false));
        switchPay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    switchChecked(payUrl, switchPay, "自动付费", isChecked,"pay");

                } else {
                    switchChecked(payUrl, switchPay, "自动付费", isChecked,"pay");

                }
            }
        });

        switchLock.setEnabled(false);
        switchPay.setEnabled(false);
    }


    /**
     * 获取时间信息
     */
    private void doPost() {
        String uid = null;
        String token = null;
        if (SharedPreferenceUtil.getBean(getApplicationContext(), "userInfo") != null) {
            User user = (User) SharedPreferenceUtil.getBean(getApplicationContext(), "userInfo");
            uid = user.getUserId();
            token = user.getToken();
        } else {
            uid = Param.UID;
            token = Param.TOKEN;
        }

        Map<String, String> map = new HashMap<>();
        map.put("uid", uid);
        map.put("token", token);
        HttpUtils.post(Param.TIMER, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("onFailure", "onFailure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonDatas = response.body().string();
                Log.e("停车时间", jsonDatas);
                try {
                    try {
                        JSONObject jsonObject = new JSONObject(jsonDatas);
                        String content = jsonObject.optString("content");
                        JSONObject jb = new JSONObject(content);
                        String status = jb.optString("status");

                        final String lockCar = jb.optString("lockCar");
                        final String autoPay = jb.optString("autoPay");

                        if (status.equals("2")) {
                            String timer = jb.optString("timer");
                            Gson gson = new Gson();
                            final Timer mTimer = gson.fromJson(timer, Timer.class);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tvJsqChepai.setText(mTimer.getCarCard() + "");
                                    tvJsqPark.setText(mTimer.getParkName() + "");

                                    switchLock.setEnabled(true);
                                    switchPay.setEnabled(true);


                                    long enterTime = Long.valueOf(mTimer.getEnterTime());
                                    //保存进场时间
                                    SharedPreferenceUtil.putLong("enterTime", enterTime);
                                    //获取当前时间
                                    long nowTime = Long.valueOf(mTimer.getNowTime());
                                    long diffTime = (nowTime - enterTime) / (1000 * 60);//单位分钟

                                    Log.e("服务器当前时间", nowTime + "");
                                    Log.e("服务器时间差", diffTime + "");

                                    SharedPreferenceUtil.putLong("diffTime", diffTime);
                                    //开启计时器
                                    timerView.start((int) diffTime);

                                    long diffMinute = timeDiff % 60;
                                    long diffHour = timeDiff / (60) % 24;
                                    long diffDay = timeDiff / (24 * 60);
                                    tvDiffTime.setText(diffDay + "天" + diffHour + "小时" + diffMinute + "分钟");
                                    tvJsqMoney.setText(mTimer.getNowPrice() + "");

                                    //获取锁车车牌id
                                    SharedPreferenceUtil.putString("carRecordId", mTimer.getCarRecordId());
                                }
                            });
                        } else if (status.equals("1")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    switchLock.setEnabled(false);
                                    switchPay.setEnabled(false);
                                    Toast.makeText(JsqActivity.this, "当前未停车", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else if (status.equals("0")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    switchLock.setEnabled(false);
                                    switchPay.setEnabled(false);
                                    Toast.makeText(JsqActivity.this, "未绑定车牌", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    Log.e("数据解析失败", "数据解析异常");
                }
            }
        }, map);
    }

    private String payUrl = Param.IP + "/app/autopay";
    private String lockUrl = Param.IP + "/app/lockCar";

    /**
    *锁车和自动付费
    */
    private void switchChecked(String url, final Switch switchButton, final String str, final boolean isChecked, final String key) {
        String uid = null;
        String token = null;
        if (SharedPreferenceUtil.getBean(getApplicationContext(), "userInfo") != null) {
            User user = (User) SharedPreferenceUtil.getBean(getApplicationContext(), "userInfo");
            uid = user.getUserId();
            token = user.getToken();
        } else {
            uid = Param.UID;
            token = Param.TOKEN;
        }
        Map<String, String> map = new HashMap<>();
        map.put("uid", uid);
        map.put("token", token);
        map.put("carRecordId", SharedPreferenceUtil.getString("carRecordId", null));
        HttpUtils.post(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                Log.e("onFailure", "failure");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(JsqActivity.this, str + "功能开启失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonDatas = response.body().string();
                Log.e(str, jsonDatas);
                try {
                    JSONObject jsonObject = new JSONObject(jsonDatas);
                    String content = jsonObject.optString("content");
                    JSONObject jb = new JSONObject(content);
                    String status = jb.optString("status");
                    Log.e(str, status);
                    if (status.equals("1")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (isChecked) {
                                    SharedPreferenceUtil.putBoolean(key,true);
                                    Toast.makeText(JsqActivity.this, str + "功能开启成功", Toast.LENGTH_SHORT).show();
                                } else {
                                    SharedPreferenceUtil.putBoolean(key,false);
                                    Toast.makeText(JsqActivity.this, str + "功能关闭成功", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("访问失败", "failure");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(JsqActivity.this, str + "功能开启失败", Toast.LENGTH_SHORT).show();


                        }
                    });
                }
            }
        }, map);
    }

    
}
