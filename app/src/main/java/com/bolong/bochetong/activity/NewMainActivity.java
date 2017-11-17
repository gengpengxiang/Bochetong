package com.bolong.bochetong.activity;

import android.*;
import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bolong.bochetong.bean.User;
import com.bolong.bochetong.bean2.Bill;
import com.bolong.bochetong.bean2.MsgEvent;
import com.bolong.bochetong.bean2.YearCard;
import com.bolong.bochetong.fragment.MainFragment;
import com.bolong.bochetong.fragment.MapFragment;
import com.bolong.bochetong.push.ExampleUtil;
import com.bolong.bochetong.push.LocalBroadcastManager;
import com.bolong.bochetong.utils.DateUtils;
import com.bolong.bochetong.utils.DialogUtil;
import com.bolong.bochetong.utils.HttpUtils;
import com.bolong.bochetong.utils.LocationUtil;
import com.bolong.bochetong.utils.Param;
import com.bolong.bochetong.utils.SharedPreferenceUtil;
import com.bolong.bochetong.utils.StatusBarUtil;
import com.bolong.bochetong.view.PageIndicatorView;
import com.bolong.bochetong.view.ViewPagerCompat;
import com.google.gson.Gson;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.bolong.bochetong.activity.ChangeActivity.ACTION_EDITPHONE_SUCCESS;
import static com.bolong.bochetong.activity.WdActivity.ACTION_QUIT;
import static com.bolong.bochetong.utils.LocationUtil.ACTION_LOCATION_FAIL;


public class NewMainActivity extends AppCompatActivity {

    @BindView(R.id.viewPager)
    ViewPagerCompat viewPager;

    @BindView(R.id.pageIndicatorView)
    PageIndicatorView pageIndicatorView;
    private double startLongitude;
    private double startLatitude;
    //add
    private FragmentPagerAdapter mAdapter;
    private List<Fragment> mFragments;
    private Unbinder unbind;
    private MainFragment mainFragment;
    private MapFragment mapFragment;
    public static final int ACTION_LOCATION = 1;
    public static final int ACTION_LOCATION_MAP = 22;
    public static final int ACTION_BACK = -1;
    private float radius;
    private long exitTime;
    public static boolean isForeground = false;
    public static final int ACTION_MONTHCARDIALOGINFO = 3656;
    public static final int ACTION_MONTHCARDIALOGDATE = 3657;
    private String cityName;
    public static final int ACTION_LOCATION_SUCCESS = 101047;
    public static final int ACTION_CLOSE_DIALOG = 183047;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);



        setContentView(R.layout.activity_new_main);
        unbind = ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            StatusBarUtil.compat(this, Color.parseColor("#000000"));
        }

        initViews();
        setSelect(0);



        setAlias();
        registerMessageReceiver();
        JPushInterface.init(getApplicationContext());

        getMonthCard();

        checkLocationPermission();

    }

    private static final int REQUECT_CODE_SDCARD = 2;
    private void checkLocationPermission() {
        MPermissions.requestPermissions(NewMainActivity.this, REQUECT_CODE_SDCARD, Manifest.permission.ACCESS_FINE_LOCATION);

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

    private void initViews() {
        mFragments = new ArrayList<Fragment>();
        mainFragment = new MainFragment();
        mapFragment = new MapFragment();

        mFragments.add(mainFragment);
        mFragments.add(mapFragment);

        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }
        };
        viewPager.setAdapter(mAdapter);
        pageIndicatorView.initIndicator(2);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                pageIndicatorView.setSelectedPage(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setSelect(int i) {
        viewPager.setCurrentItem(i);
    }


    @Override
    protected void onResume() {
        isForeground = true;
        super.onResume();
    }


    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        JPushInterface.clearAllNotifications(this);
        EventBus.getDefault().unregister(this);
        Log.e("onDestroy","onDestroy");
        unbind.unbind();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateUI(MsgEvent event) {
        if (event.getAction() == ACTION_BACK) {
            setSelect(0);
        }
        if (event.getAction() == ACTION_MONTHCARDIALOGINFO) {
            String s = event.getStr();

            if (SharedPreferenceUtil.getBoolean("isWarn", true)) {
                DialogUtil.showMonthCardDialog(this, R.layout.layout_monthcard_warn, s);
                SharedPreferenceUtil.putBoolean("isWarn", false);
            }

        }
        if (event.getAction() == ACTION_MONTHCARDIALOGDATE) {
            String s = event.getStr();
            if (SharedPreferenceUtil.getBoolean("isExpire", true)) {
                DialogUtil.showMonthCardDialog2(this, R.layout.layout_monthcard_date, s);
                SharedPreferenceUtil.putBoolean("isExpire", false);
            }
        }
        if (event.getAction() == ACTION_EDITPHONE_SUCCESS) {
            finish();
        }
        if (event.getAction() == ACTION_QUIT) {
            Intent intent = new Intent();
            intent.setClass(NewMainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, ""));

        }
        if(event.getAction() == ACTION_LOCATION_SUCCESS){
            //LocationUtil.stop();
            //Log.e("定位已关闭","close");
        }
        if(event.getAction() == ACTION_LOCATION_FAIL){
            //LocationUtil.stop();
            //Log.e("定位失败关闭","close");
        }

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (viewPager.getCurrentItem() == 1) {
            setSelect(0);
            return false;
        } else {

            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                EventBus.getDefault().post(new MsgEvent(ACTION_CLOSE_DIALOG));
                if ((System.currentTimeMillis() - exitTime) > 2000)
                {

                    Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    exitTime = System.currentTimeMillis();
                } else {
                    setResult(1);
                    finish();
                }
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.bolong.bochetong.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                    String messge = intent.getStringExtra(KEY_MESSAGE);
                    String extras = intent.getStringExtra(KEY_EXTRAS);
                    StringBuilder showMsg = new StringBuilder();
                    showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                    if (!ExampleUtil.isEmpty(extras)) {
                        showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                    }
                    //setCostomMsg(showMsg.toString());
                }
            } catch (Exception e) {
            }
        }
    }

    private void setAlias() {
        if (SharedPreferenceUtil.getBean(getApplicationContext(), "userInfo") != null) {
            User user = (User) SharedPreferenceUtil.getBean(getApplicationContext(), "userInfo");
            String alias = user.getUserId();
            mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
            Log.e("alias===", alias);
            JPushInterface.setLatestNotificationNumber(this, 2);
        }
    }

    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "可实时更新";
                    //Log.i(TAG, logs);
                    // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    //Log.i(TAG, logs);
                    // 延迟 60 秒来调用 Handler 设置别名
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
                    //Log.e(TAG, logs);
            }
            //ExampleUtil.showToast(logs, getApplicationContext());
        }
    };
    private static final int MSG_SET_ALIAS = 1001;
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    //Log.d(TAG, "Set alias in handler.");
                    // 调用 JPush 接口来设置别名。
                    JPushInterface.setAliasAndTags(getApplicationContext(),
                            (String) msg.obj,
                            null,
                            mAliasCallback);
                    break;
                default:
                    //Log.i(TAG, "Unhandled msg - " + msg.what);
            }
        }
    };


    private void getMonthCard() {
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
        Log.e("uid", uid);
        Map<String, String> map = new HashMap<>();
        map.put("uid", uid);
        map.put("token", token);
        HttpUtils.post(Param.MONTHCARDS, new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonDatas = response.body().string();
                Log.e("月卡", jsonDatas);
                try {
                    try {

                        JSONObject jsonObject = new JSONObject(jsonDatas);
                        String content = jsonObject.optString("content");
                        Gson gson = new Gson();


                        YearCard yearCard = gson.fromJson(content, YearCard.class);
                        String status = yearCard.getStatus();
                        if (status.equals("1")) {
                            List<YearCard.MonthCardBean> cardBean = yearCard.getMonthCard();
                            if (cardBean.size() != 0) {
                                Date date = new Date();
                                Date date2 = new Date();
                                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy.MM.dd HH:mm");
                                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
                                SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy年MM月dd日");
                                String times = null;
                                String times2 = null;
                                String times3 = null;
                                long diff = 0;
                                String info = "";

                                for (int i = 0; i < cardBean.size(); i++) {
                                    YearCard.MonthCardBean card = cardBean.get(i);
                                    try {
                                        date = sdf1.parse(card.getCardTermofvalidity());
                                        times = sdf2.format(date);

                                        long endTime = DateUtils.getStringToDate2(card.getCardTermofvalidity());
                                        long nowTime = System.currentTimeMillis();
                                        long diffTime = endTime - nowTime;
                                        diff = diffTime / (1000 * 60 * 60 * 24);
                                        times2 = DateUtils.getDateToString2(endTime);
                                        date2 = sdf1.parse(times2);
                                        times3 = sdf3.format(date2);

                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    info += "\n" + card.getParkName() + "，车位号" + card.getCarportNum() + "，有效期至" + times + "。";


                                    if (Integer.parseInt(String.valueOf(diff)) < 7&&Integer.parseInt(String.valueOf(diff))>0) {
                                        Log.e("diff",diff+"");
                                        EventBus.getDefault().post(new MsgEvent(ACTION_MONTHCARDIALOGDATE, times3));

                                    }
                                }
                                EventBus.getDefault().post(new MsgEvent(ACTION_MONTHCARDIALOGINFO, info));
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }catch (Exception e){

                }
            }
        }, map);

    }
}
