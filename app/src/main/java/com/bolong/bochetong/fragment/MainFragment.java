package com.bolong.bochetong.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviParaOption;
import com.bolong.bochetong.activity.BcxqActivity;
import com.bolong.bochetong.activity.CityActivity;
import com.bolong.bochetong.activity.JfActivity;
import com.bolong.bochetong.activity.LntcActivity;
import com.bolong.bochetong.activity.QfbjActivity;
import com.bolong.bochetong.activity.R;
import com.bolong.bochetong.activity.WdActivity;
import com.bolong.bochetong.activity.WebActivity;
import com.bolong.bochetong.adapter.BcAdapter;
import com.bolong.bochetong.adapter.MainAdapter;
import com.bolong.bochetong.bean.User;
import com.bolong.bochetong.bean2.AdWeat;
import com.bolong.bochetong.bean2.City;
import com.bolong.bochetong.bean2.Fee;
import com.bolong.bochetong.bean2.MsgEvent;
import com.bolong.bochetong.bean2.NearbyPark;
import com.bolong.bochetong.bean2.ParkTimer;
import com.bolong.bochetong.bean2.Version;
import com.bolong.bochetong.maputils.AMapUtil;
import com.bolong.bochetong.utils.AppUtils;
import com.bolong.bochetong.utils.CoordinateUtils;
import com.bolong.bochetong.utils.DialogUtil;
import com.bolong.bochetong.utils.HttpUtils;
import com.bolong.bochetong.utils.LocationUtil;
import com.bolong.bochetong.utils.Param;
import com.bolong.bochetong.utils.PinyinUtil;
import com.bolong.bochetong.utils.QueueUtil;
import com.bolong.bochetong.utils.SharedPreferenceUtil;
import com.bolong.bochetong.utils.ToastUtil;
import com.bolong.bochetong.view.CustomPopDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.stx.xhb.xbanner.XBanner;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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

import static com.bolong.bochetong.activity.CityActivity.ACTION_CITY_SELECTED;
import static com.bolong.bochetong.activity.LoginActivity.ACTION_LOGIN;
import static com.bolong.bochetong.activity.NewMainActivity.ACTION_CLOSE_DIALOG;
import static com.bolong.bochetong.activity.NewMainActivity.ACTION_LOCATION_SUCCESS;
import static com.bolong.bochetong.push.MyReceiver.ACTION_ARREARAGE;
import static com.bolong.bochetong.push.MyReceiver.ACTION_ENTER;
import static com.bolong.bochetong.push.MyReceiver.ACTION_EXIT;
import static com.bolong.bochetong.utils.DownloadAnsy.ACTION_DOWNLOAD_FINISH;
import static com.bolong.bochetong.utils.LocationUtil.ACTION_LOCATION_FAIL;

public class MainFragment extends Fragment {

    Unbinder unbinder;
    @BindView(R.id.et_park)
    EditText etPark;
    @BindView(R.id.icon_wd)
    ImageView iconWd;
    @BindView(R.id.banner)
    XBanner xBanner;
    @BindView(R.id.tv_weather)
    TextView tvWeather;
    @BindView(R.id.mRecyclerView)
    PullLoadMoreRecyclerView mRecyclerView;
    @BindView(R.id.tv_park_time)
    TextView tvParkTime;
    @BindView(R.id.tv_park_price)
    TextView tvParkPrice;
    @BindView(R.id.tv_park_entertime)
    TextView tvParkEntertime;
    @BindView(R.id.layout_timer)
    PercentRelativeLayout layoutTimer;
    @BindView(R.id.iv_view)
    ImageView ivView;
    @BindView(R.id.layout_view)
    RelativeLayout layoutView;
    @BindView(R.id.layout_wd)
    RelativeLayout layoutWd;
    @BindView(R.id.tv_city)
    TextView tvCity;
    @BindView(R.id.tv_day)
    TextView tvDay;
    @BindView(R.id.tv_hour)
    TextView tvHour;
    @BindView(R.id.tv_minute)
    TextView tvMinute;
    //定位
    private double startLongitude;
    private double startLatitude;
    private double stopLongitude;
    private double stopLatitude;
    //首页数据
    private static final int ACTION_YOUQIANFEI = 7695;
    private static final int ACTION_UPDATE = 4455;
    private static final int ACTION_ADWEAT = 0;
    private static final int ACTION_NEARBYPARK = 2;
    private static final int ACTION_HOMETIMER = 3;
    private static final int ACTION_PARKING = 4;
    public static final int ACTION_OVERLAY = 100;
    public static final int ACTION_FAILURE = 94;
    public static final int ACTION_NOINFO = 93;
    public static final int ACTION_NOPARKING = 69893;
    public static final int ACTION_SKIPQFBJ = 69898;

    private String weat;
    private String traf;
    private List<String> picList = new ArrayList<>();
    private List<String> urlList = new ArrayList<>();
    private MainAdapter adapter;
    private List<NearbyPark.ZonesBean> zones = new ArrayList<>();
    private List<NearbyPark.ParksBean> parks = new ArrayList<>();
    //停车数据
    //private String status;
    private String lockcar;
    private ParkTimer.TimerBean mTimer;
    private String parkName;
    private String carCard;
    private String enterTimeStr;
    private String nowPrice;
    //搜索相关
    private String parkStr;
    private List<NearbyPark.ParksBean> searchList = new ArrayList<>();
    private List<NearbyPark.ZonesBean> zones2 = new ArrayList<>();
    private List<NearbyPark.ParksBean> parks2 = new ArrayList<>();
    private CustomPopDialog dialogQfbj;
    private int n = 0;
    private String cityId = "";
    private String cityName;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        LocationUtil.start(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.layout_viewpager_main, container, false);
        unbinder = ButterKnife.bind(this, view);

        updateCheck();
        initViews();

        getParkTime();
        mRecyclerView.setRefreshing(true);
        searchPark();

        return view;
    }


    private void updateCheck() {
        Map<String, String> map = new HashMap<>();
        map.put("appType", "1");
        HttpUtils.post(Param.UPDATECHECK, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonDatas = response.body().string();

                try {
                    try {
                        JSONObject jsonObject = new JSONObject(jsonDatas);
                        String content = jsonObject.optString("content");
                        Gson gson = new Gson();
                        Version version = gson.fromJson(content, Version.class);
                        EventBus.getDefault().post(new MsgEvent(ACTION_UPDATE, version));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {

                }
            }
        }, map);
    }


    private void searchPark() {
        etPark.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                parkStr = s.toString();
                if (TextUtils.isEmpty(parkStr)) {
                    mRecyclerView.setPullRefreshEnable(true);
                    refreshAdapter(zones, parks, mListener);
                } else {
                    mRecyclerView.setPullRefreshEnable(false);
                    searchList.clear();
                    for (NearbyPark.ParksBean parkBean : parks) {
                        if (parkBean.getPark_name().contains(parkStr)) {
                            searchList.add(parkBean);
                        }
                    }
                    parks2.clear();
                    parks2.addAll(searchList);
                    refreshAdapter(zones2, parks2, mListener);

                    for (int i = 0; i < parks2.size(); i++) {
                        Log.e("测试数据", parks2.get(i).getStandard() + i);
                    }
                }

            }
        });

       /* etPark.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER) {
                    refreshAdapter(zones2, parks2, mListener);
                }

                return false;
            }
        });*/

    }

    private void initViews() {
        mRecyclerView.setLinearLayout();
        mRecyclerView.setColorSchemeResources(R.color.blue);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));
        mRecyclerView.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (tvCity.getText().equals("定位失败")) {
                            LocationUtil.start(getActivity());
                        } else {
                            getNearbyPark(cityId, startLongitude, startLatitude);
                        }
                        mRecyclerView.setPullLoadMoreCompleted();
                    }
                }, 300);
            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        mRecyclerView.setPullLoadMoreCompleted();
                        ToastUtil.showShort(getActivity(), "没有更多数据了");
                    }
                }, 300);
            }
        });

    }


    @OnClick({R.id.layout_wd, R.id.layout_timer, R.id.tv_city})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_wd:
                skip(WdActivity.class);
                //skip(JfActivity.class);
                break;
            case R.id.layout_timer:
                skip(JfActivity.class);
                break;
            case R.id.tv_city:
                Intent intent = new Intent();
                intent.setClass(getActivity(), CityActivity.class);
                intent.putExtra("currentCity", cityName);
                startActivity(intent);
                //skip(CityActivity.class);
                break;
        }
    }

    private void skip(Class c) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), c);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        getParkTime();
        getBill();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("onStop", "Activity停止状态");
        handler.removeCallbacks(runnable);
        n = 0;
    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            getParkTime();
            handler.postDelayed(this, 1000 * 60);
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void getAdWeat(String cityName, double longitude, double latitude) {
        Map<String, String> map = new HashMap<>();
        //map.put("citycode", "1");
        map.put("cityname", cityName);
        map.put("longtitude", String.valueOf(longitude));
        map.put("latitude", String.valueOf(latitude));
        HttpUtils.post(Param.ADWEAT, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonDatas = response.body().string();
                Log.e("天气", jsonDatas);
                try {
                    try {
                        JSONObject jsonObject = new JSONObject(jsonDatas);
                        String content = jsonObject.optString("content");
                        Gson gson = new Gson();
                        //天气相关
                        JSONObject jb = new JSONObject(content);
                        weat = jb.optString("weat");
                        //今日限行
                        traf = jb.optString("traf");
                        //广告图片
                        AdWeat adWeat = gson.fromJson(content, AdWeat.class);
                        List<AdWeat.BlAdsBean> blads = adWeat.getBlAds();
                        picList.clear();
                        urlList.clear();
                        for (int i = 0; i < blads.size(); i++) {
                            picList.add(blads.get(i).getPicture());
                            urlList.add(blads.get(i).getUrl());
                        }
                        EventBus.getDefault().post(new MsgEvent(ACTION_ADWEAT));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {

                }
            }
        }, map);
    }

    /**
     * 获取附近车场信息
     */
    private void getNearbyPark(String cityId, double startLongitude, double startLatitude) {

        Map<String, String> map = new HashMap<>();
        map.put("cityId", cityId);
        map.put("longitude", String.valueOf(startLongitude));
        map.put("latitude", String.valueOf(startLatitude));
        HttpUtils.post(Param.NEARBYPARK, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                EventBus.getDefault().post(new MsgEvent(ACTION_FAILURE));
                Log.e("请求车场信息失败", "failure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonDatas = response.body().string();
                Log.e("附近车场信息", jsonDatas);
                try {
                    try {
                        JSONObject jsonObject = new JSONObject(jsonDatas);
                        String content = jsonObject.optString("content");
                        Gson gson = new Gson();
                        //占道停车
                        NearbyPark nearbyPark = gson.fromJson(content, NearbyPark.class);
                        List<NearbyPark.ZonesBean> zones3 = nearbyPark.getZones();
                        //社区停车
                        List<NearbyPark.ParksBean> parks3 = nearbyPark.getParks();
                        zones.clear();
                        parks.clear();
                        zones.addAll(zones3);
                        parks.addAll(parks3);

                        //搜索相关
                        zones2.clear();
                        parks2.clear();
                        zones2.addAll(zones3);
                        parks2.addAll(parks3);

                        EventBus.getDefault().post(new MsgEvent(ACTION_NEARBYPARK));
                        EventBus.getDefault().post(new MsgEvent(ACTION_OVERLAY, parks));

                    } catch (JSONException e) {
                        EventBus.getDefault().post(new MsgEvent(ACTION_FAILURE));
                    }
                } catch (Exception e) {
                    EventBus.getDefault().post(new MsgEvent(ACTION_FAILURE));
                }
            }
        }, map);

    }

    /**
     * 获取停车信息
     */
    private void getParkTime() {
        String uid = null;
        String token = null;
        if (SharedPreferenceUtil.getBean(getActivity().getApplicationContext(), "userInfo") != null) {
            User user = (User) SharedPreferenceUtil.getBean(getActivity().getApplicationContext(), "userInfo");
            uid = user.getUserId();
            token = user.getToken();
        }
        Map<String, String> map = new HashMap<>();
//        map.put("uid", "C62AED409A63443496D5057CEF35A7EA");
        map.put("uid", uid);
        map.put("token", token);
        if (uid != null && token != null) {
            HttpUtils.post(Param.HOMETIMERS, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("主页获取停车信息失败", "failure");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String jsonDatas = response.body().string();
                    Log.e("主页正在停车", jsonDatas);
                    try {
                        try {
                            JSONObject jsonObject = new JSONObject(jsonDatas);
                            String content = jsonObject.optString("content");

                            Gson gson = new Gson();
                            ParkTimer parkTimer = gson.fromJson(content, ParkTimer.class);

                            int status = parkTimer.getStatus();
                            //EventBus.getDefault().post(new MsgEvent(ACTION_HOMETIMER));

                            if (status == 3) {
                                EventBus.getDefault().post(new MsgEvent(ACTION_YOUQIANFEI));
                            }

                            if (status == 2) {

                                List<ParkTimer.TimerBean> timers = parkTimer.getTimer();
                                mTimer = timers.get(0);
                                lockcar = timers.get(0).getLockcar();
                                EventBus.getDefault().post(new MsgEvent(ACTION_PARKING));
                                Log.e("发送通知", "success");
                            }
                            if(status == 1){
                                EventBus.getDefault().post(new MsgEvent(ACTION_NOPARKING));
                            }
                            if(status == 0){
                                EventBus.getDefault().post(new MsgEvent(ACTION_NOPARKING));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {

                    }
                }
            }, map);
        }
    }

    private void startBanner(final List<String> picList, final List<String> urlList) {
        //获取广告实体类中的图片
        xBanner.setPointsIsVisible(true);
        xBanner.setmPointContainerPosition(XBanner.CENTER);
        xBanner.setData(picList, null);
        xBanner.setmAdapter(new XBanner.XBannerAdapter() {
            @Override
            public void loadBanner(XBanner banner, Object model, View view, int position) {
                Glide.with(getActivity()).load(picList.get(position)).diskCacheStrategy(DiskCacheStrategy.ALL).into((ImageView) view);
            }
        });

        xBanner.setOnItemClickListener(new XBanner.OnItemClickListener() {
            @Override
            public void onItemClick(XBanner banner, int position) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), WebActivity.class);
                switch (position) {
                    case 0:
                        intent.putExtra("url", urlList.get(position));

                        break;
                    case 1:
                        intent.putExtra("url", urlList.get(position));
                        break;
                    case 2:
                        intent.putExtra("url", urlList.get(position));
                        break;
                }
                if (intent.getStringExtra("url") != null) {
                    startActivity(intent);
                } else {
                    ToastUtil.showShort(getActivity(), "敬请期待");
                }
            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateUI(MsgEvent event) throws BadHanyuPinyinOutputFormatCombination {
        if (event.getAction() == ACTION_YOUQIANFEI) {
            CustomPopDialog.Builder dialogBuild = new CustomPopDialog.Builder(getActivity());
            if (dialogQfbj == null) {
                dialogQfbj = dialogBuild.create(R.layout.layout_dialog_qfbj);
            }

            dialogQfbj.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK)
                    {
                        dialogQfbj.dismiss();
                        getActivity().finish();
                        return true;
                    }
                    else
                    {
                        return false;
                    }
                }
            });


            dialogQfbj.setCanceledOnTouchOutside(false);

            if (!dialogQfbj.isShowing()) {
                QueueUtil.showDialog(dialogQfbj);
                TextView btnCancel = (TextView) dialogQfbj.findViewById(R.id.btn_cancel);
                TextView btnConFirm = (TextView) dialogQfbj.findViewById(R.id.btn_confirm);
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogQfbj.dismiss();
                        getActivity().finish();
                    }
                });
                btnConFirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogQfbj.dismiss();
                        Intent intent = new Intent();
                        intent.putExtra("symbol","arrearage");
                        intent.setClass(getActivity(), QfbjActivity.class);
                        startActivity(intent);
                    }
                });
            }
        }

        if (event.getAction() == ACTION_ADWEAT) {
            tvWeather.setText("今日限行：" + traf + "   " + "天气："+weat);
            startBanner(picList, urlList);
        }
        if (event.getAction() == ACTION_LOCATION_FAIL) {
            mRecyclerView.setRefreshing(false);
            tvCity.setText("定位失败");
            refreshAdapter(zones, parks, mListener);
        }
        if (event.getAction() == ACTION_NEARBYPARK) {
            refreshAdapter(zones, parks, mListener);
        }
        //正在停车
        if (event.getAction() == ACTION_PARKING) {
            n++;
            Log.e("主页n的数值", n + "");
            layoutTimer.setVisibility(View.VISIBLE);
            xBanner.setVisibility(View.GONE);

            //tvParkTime.setText();
            tvParkPrice.setText(mTimer.getNowPrice());
            tvParkEntertime.setText("入场时间 : " + mTimer.getEnterTimeStr());

            long enterTime = Long.valueOf(mTimer.getEnterTime());
            //获取当前时间
            final long nowTime = Long.valueOf(mTimer.getNowTime());
            long diffTime = (nowTime - enterTime) / 60;
            long diffMinute = diffTime % 60;
            long diffHour = diffTime / (60) % 24;
            long diffDay = diffTime / (24 * 60);
            //tvParkTime.setText("—" + diffDay + "天" + diffHour + "小时" + diffMinute + "分钟" + "—");

            //add
            tvDay.setText(diffDay+"");
            tvHour.setText(diffHour+"");
            tvMinute.setText(diffMinute+"");

            carCard = mTimer.getCarCard();
            parkName = mTimer.getParkName();
            nowPrice = mTimer.getNowPrice();
            enterTimeStr = mTimer.getEnterTimeStr();

            if (n == 1) {
                handler.postDelayed(runnable, 1000 * 60);
            }
        }

        if (event.getAction() == ACTION_LOGIN) {
            try {
                if (event.getStr().equals("denglu")) {
                    getParkTime();
                    if (SharedPreferenceUtil.getBean(getActivity(), "userInfo") != null) {
                        User user = (User) SharedPreferenceUtil.getBean(getActivity(), "userInfo");

                        if (user.getFreeTime() != null) {
                            //显示赠送提示
                            CustomPopDialog.Builder dialogBuild = new CustomPopDialog.Builder(getActivity());
                            final CustomPopDialog dialog = dialogBuild.create(R.layout.layout_dialog_zssc, 0.6, 0.72);
                            dialog.setCanceledOnTouchOutside(false);
                            //dialog.show();
                            QueueUtil.showDialog(dialog);
                            ImageView btClose = (ImageView) dialog.findViewById(R.id.bt_close);
                            btClose.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                        }
                    }
                }
            } catch (Exception e) {

            }
        }
        if (event.getAction() == ACTION_UPDATE) {
            Version version = event.getVersion();
            String versionNumber = version.getVersionNumber();
            String downloadAddress = version.getDownloadAddress();
            String updateContent = version.getUpdateContent();
            String totolContent = "最新版本:" + versionNumber + "\n" + "更新内容: \n" + updateContent;
            if (versionNumber.equals(AppUtils.getVersionName(getActivity()))) {
                //ToastUtil.showShort(getActivity(), "已经是最新版本");
            } else {
                SharedPreferenceUtil.putString("versionNumber", versionNumber);
                DialogUtil.showDialog(getActivity(), downloadAddress, totolContent);
            }
        }
        if (event.getAction() == ACTION_DOWNLOAD_FINISH) {
            String versionNumber = SharedPreferenceUtil.getString("versionNumber", "1.0.1");
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(new File(Param.FILEPATH + versionNumber + ".apk")), "application/vnd.android.package-archive");
            startActivity(intent);
        }
        if (event.getAction() == ACTION_CITY_SELECTED) {
            City.ContentBean city = event.getCity();
            tvCity.setText(city.getCityName() + "市");
            cityId = city.getId();
            Log.e("cityId===", cityId + "经度==" + city.getLongtitude() + "纬度==" + city.getLatitude());
            startLongitude = city.getLongtitude();
            startLatitude = city.getLatitude();

            getNearbyPark(cityId, startLongitude, startLatitude);
            Log.e("天气数据", city.getCityPinyin() + startLongitude + startLatitude);
            getAdWeat(city.getCityPinyin(), startLongitude, startLatitude);
        }
        if (event.getAction() == ACTION_LOCATION_SUCCESS) {
            mRecyclerView.setRefreshing(false);
            Log.e("主页接收广播", "true");
            startLongitude = event.getArg1();
            startLatitude = event.getArg2();

            getNearbyPark(cityId, startLongitude, startLatitude);

            cityName = event.getStr();
            if (cityName.contains("市")) {
                cityName = cityName.replace("市", "");
            }
            String newCityName = PinyinUtil.getPinyinWithoutBlank(cityName);
            getAdWeat(newCityName, startLongitude, startLatitude);
            tvCity.setText(event.getStr());

            //EventBus.getDefault().post(new MsgEvent(ACTION_LOCATION_MAP, startLongitude,startLatitude,event.getRadius(),cityName,event.getStr2()));

        }
        if (event.getAction() == ACTION_EXIT) {
            layoutTimer.setVisibility(View.GONE);
            xBanner.setVisibility(View.VISIBLE);
        }
        if (event.getAction() == ACTION_ENTER) {
                getParkTime();
//            layoutTimer.setVisibility(View.VISIBLE);
//            xBanner.setVisibility(View.GONE);
        }
        if(event.getAction() == ACTION_NOPARKING){
            layoutTimer.setVisibility(View.GONE);
            xBanner.setVisibility(View.VISIBLE);
        }
        if(event.getAction() == ACTION_ARREARAGE){
            getBill();
        }
        if(event.getAction() == ACTION_SKIPQFBJ){
            CustomPopDialog.Builder dialogBuild = new CustomPopDialog.Builder(getActivity());
            if (dialogQfbj == null) {
                dialogQfbj = dialogBuild.create(R.layout.layout_dialog_qfbj);
            }
            dialogQfbj.setCanceledOnTouchOutside(false);

            //add
            dialogQfbj.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK)
                    {
                        dialogQfbj.dismiss();
                        getActivity().finish();
                        return true;
                    }
                    else
                    {
                        return false;
                    }
                }
            });


            if (!dialogQfbj.isShowing()) {
                QueueUtil.showDialog(dialogQfbj);
                TextView btnCancel = (TextView) dialogQfbj.findViewById(R.id.btn_cancel);
                TextView btnConFirm = (TextView) dialogQfbj.findViewById(R.id.btn_confirm);
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogQfbj.dismiss();
                        getActivity().finish();
                    }
                });
                btnConFirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogQfbj.dismiss();
                        Intent intent = new Intent();
                        intent.setClass(getActivity(), QfbjActivity.class);
                        startActivity(intent);
                    }
                });
            }
        }
        if(event.getAction() == ACTION_CLOSE_DIALOG){
            Log.e("收到先下了","true");
            if(dialogQfbj.isShowing()){
                dialogQfbj.dismiss();
            }
        }

    }

    private void refreshAdapter(final List<NearbyPark.ZonesBean> zones, final List<NearbyPark.ParksBean> parks, MainAdapter.NaviClickListener mListener) {
        mRecyclerView.setRefreshing(false);
        adapter = new MainAdapter(zones, parks, mListener);
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickLitener(new BcAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, final int position) {
                if (zones.size() != 0) {
                    if (position == 0) {
                        Intent intent = new Intent();
                        intent.setClass(getActivity(), LntcActivity.class);
                        intent.putExtra("startLongitude", startLongitude);
                        intent.putExtra("startLatitude", startLatitude);
                        startActivity(intent);
                    } else {
                        String parkName = parks.get(position - 1).getPark_name();
                        String parkId = parks.get(position - 1).getId();
                        String distance = parks.get(position - 1).getDistance();
                        Intent intent = new Intent();
                        intent.putExtra("action", "MainFragment");
                        intent.putExtra("parkName", parkName);
                        intent.putExtra("parkId", parkId);
                        intent.putExtra("distance", distance);
                        intent.putExtra("startLongitude", startLongitude);
                        intent.putExtra("startLatitude", startLatitude);
                        intent.setClass(getActivity(), BcxqActivity.class);
                        startActivity(intent);
                    }
                } else {
                    String parkName = parks.get(position).getPark_name();
                    String parkId = parks.get(position).getId();
                    String distance = parks.get(position).getDistance();
                    Intent intent = new Intent();
                    intent.putExtra("action", "MainFragment");
                    intent.putExtra("parkName", parkName);
                    intent.putExtra("parkId", parkId);
                    intent.putExtra("distance", distance);
                    intent.putExtra("startLongitude", startLongitude);
                    intent.putExtra("startLatitude", startLatitude);
                    intent.setClass(getActivity(), BcxqActivity.class);
                    startActivity(intent);

                }

            }
        });
    }

    //实现Adapter的点击方法,点击导航按钮实现导航功能
    private MainAdapter.NaviClickListener mListener = new MainAdapter.NaviClickListener() {

        @Override
        public void myOnClick(int position, View v) {
            if (zones.size() != 0) {
                if (position == 0) {
                    stopLongitude = zones.get(position).getPart_longitude();
                    stopLatitude = zones.get(position).getPart_latitude();
                } else {
                    stopLongitude = parks.get(position - 1).getPart_longitude();
                    stopLatitude = parks.get(position - 1).getPart_latitude();
                }
            } else {
                stopLongitude = parks.get(position).getPart_longitude();
                stopLatitude = parks.get(position).getPart_latitude();
            }
            //navi(startLongitude, startLatitude, stopLongitude, stopLatitude);
            // openBaiduMap(startLongitude,startLatitude,SNAME,stopLatitude,stopLongitude,DNAME,CITY);

            NaviParaOption naviParaOption = new NaviParaOption();
            naviParaOption.startPoint(new LatLng(startLatitude, startLongitude));
            naviParaOption.endPoint(new LatLng(stopLatitude, stopLongitude));

            //BaiduMapNavigation.openBaiduMapNavi(naviParaOption,getActivity());


            LatLng startLL = new LatLng(startLatitude, startLongitude);
            LatLng endLL = new LatLng(stopLatitude, stopLongitude);
            //baiduClientNavi(startLL,endLL);

            if (AMapUtil.isInstallByRead("com.autonavi.minimap") && !AMapUtil.isInstallByRead("com.baidu.BaiduMap")) {
                double[] endPoint = CoordinateUtils.bdToGaoDe(stopLatitude, stopLongitude);
                Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("androidamap://navi?sourceApplication=铂车通&lat=" + endPoint[0] + "&lon=" + endPoint[1] + "&dev=0"));
                intent.setPackage("com.autonavi.minimap");
                startActivity(intent);
            }
            if (AMapUtil.isInstallByRead("com.baidu.BaiduMap") && !AMapUtil.isInstallByRead("com.autonavi.minimap")) {

                BaiduMapNavigation.openBaiduMapNavi(naviParaOption, getActivity());


            }
            if (!AMapUtil.isInstallByRead("com.baidu.BaiduMap") && !AMapUtil.isInstallByRead("com.autonavi.minimap")) {
                BaiduMapNavigation.openWebBaiduMapNavi(naviParaOption, getActivity());
            } else {

                BaiduMapNavigation.openBaiduMapNavi(naviParaOption, getActivity());
            }
        }
    };

    private void getBill() {
        String uid = null;
        String token = null;
        if (SharedPreferenceUtil.getBean(getActivity(), "userInfo") != null) {
            User user = (User) SharedPreferenceUtil.getBean(getActivity(), "userInfo");
            uid = user.getUserId();
            token = user.getToken();
        } else {
            uid = Param.UID;
            token = Param.TOKEN;
        }

        Map<String, String> map = new HashMap<>();
        map.put("uid", uid);
        map.put("token", token);
        HttpUtils.post(Param.ARREARAGETABLE, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonDatas = response.body().string();
                Log.e("欠费补缴", jsonDatas);
                try {
                    try {
                        JSONObject jsonObject = new JSONObject(jsonDatas);
                        String errMessage = jsonObject.optString("errMessage");
                        String content = jsonObject.optString("content");
                        Log.e("errMessage", errMessage);
                        Log.e("content", content);

                        if (!errMessage.equals("没有欠费单")&&!errMessage.equals("请求失败")) {
                           EventBus.getDefault().post(new MsgEvent(ACTION_SKIPQFBJ));
                        }

                    } catch (JSONException e) {

                    }
                } catch (Exception e) {

                }
            }
        }, map);

    }

}
