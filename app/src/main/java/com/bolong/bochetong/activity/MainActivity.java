package com.bolong.bochetong.activity;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bolong.bochetong.bean.HomeContent;
import com.bolong.bochetong.bean.User;
import com.bolong.bochetong.utils.HttpUtils;
import com.bolong.bochetong.utils.Param;
import com.bolong.bochetong.utils.SharedPreferenceUtil;
import com.bolong.bochetong.utils.StatusBarUtil;
import com.bolong.bochetong.view.CustomPopDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.stx.xhb.xbanner.XBanner;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private Unbinder unbinder;
    @BindView(R.id.banner)
    XBanner banner;
    @BindView(R.id.icon_bdcp)
    ImageView iconBdcp;
    @BindView(R.id.icon_kscz)
    ImageView iconKscz;
    @BindView(R.id.icon_qb)
    ImageView iconQb;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.iv_city)
    TextView ivCity;
    @BindView(R.id.iv_weather)
    ImageView ivWeather;
    @BindView(R.id.tv_templow)
    TextView tvTemplow;
    @BindView(R.id.tv_weather)
    TextView tvWeather;
    @BindView(R.id.tv_airquality)
    TextView tvAirquality;
    @BindView(R.id.icon_jsq)
    ImageView iconJsq;
    @BindView(R.id.icon_wode)
    ImageView iconWode;
    @BindView(R.id.icon_qfbj)
    ImageView iconQfbj;
    @BindView(R.id.icon_tcjl)
    ImageView iconTcjl;
    @BindView(R.id.icon_bc)
    ImageView iconBc;
    @BindView(R.id.icon_location)
    TextView iconLocation;
    @BindView(R.id.icon_kq)
    ImageView iconKq;

    private List<HomeContent.AdPositionIdBean> adList = new ArrayList<>();
    private List<String> picList = new ArrayList<>();
    private List<String> urlList = new ArrayList<>();
    private TextView tv_noweather;
    private SwipeRefreshLayout refresh_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        //banner = (XBanner) findViewById(R.id.banner);
        unbinder = ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            StatusBarUtil.compat(this, Color.parseColor("#2081d1"));
        }
        //StatusBarUtil.compat(this, Color.parseColor("#2081d1"));
        doPost();
        //刷新相关
        tv_noweather = (TextView) findViewById(R.id.tv_no_weather);
        //ScrollView scrollView = (ScrollView) findViewById(R.id.scrollview);
        refresh_layout = (SwipeRefreshLayout) this.findViewById(R.id.refresh_layout);
        refresh_layout.setColorSchemeResources(R.color.colorPrimary);
        refresh_layout.setOnRefreshListener(this);//设置下拉的监听



    }

    private boolean flag;

    private void doPost() {
        Map<String, String> map = new HashMap<>();
        map.put("citycode", "101010100");
        HttpUtils.post(Param.HOMECONTENT, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                flag = false;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        tv_noweather.setVisibility(View.VISIBLE);

                        tv_noweather.setVisibility(View.VISIBLE);
                        ivCity.setVisibility(View.INVISIBLE);
                        ivWeather.setVisibility(View.INVISIBLE);
                        tvTemplow.setVisibility(View.INVISIBLE);
                        tvWeather.setVisibility(View.INVISIBLE);
                        tvAirquality.setVisibility(View.INVISIBLE);
                    }
                });
                Log.e("onFailure", "onFailure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonDatas = response.body().string();
                Log.e("首页数据", jsonDatas);
                try {
                    try {
                        JSONObject jsonObject = new JSONObject(jsonDatas);
                        String content = jsonObject.optString("content");
                        Gson gson = new Gson();
                        //天气相关
                        JSONObject jb = new JSONObject(content);
                        String weatherContent = jb.optString("weatherContent");
                        final HomeContent.WeatherContentBean weather = gson.fromJson(weatherContent, HomeContent.WeatherContentBean.class);

                        //广告相关
                        HomeContent homeContent = gson.fromJson(content, HomeContent.class);
                        List<HomeContent.AdPositionIdBean> adPositionId = homeContent.getAdPositionId();
                        picList.clear();
                        urlList.clear();
                        for (int i = 0; i < adPositionId.size(); i++) {
                            picList.add(adPositionId.get(i).getPicture());
                            urlList.add(adPositionId.get(i).getUrl());
                        }
                        final int img = Integer.parseInt(weather.getWeatherImg());
                        ApplicationInfo appInfo = getApplicationInfo();
                        final int resID = getResources().getIdentifier(("icon" + String.valueOf(img)), "drawable", appInfo.packageName);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startBanner(picList, urlList);
                                progressBar.setVisibility(View.GONE);

                                //
                                tv_noweather.setVisibility(View.INVISIBLE);
                                ivCity.setVisibility(View.VISIBLE);
                                ivWeather.setVisibility(View.VISIBLE);
                                tvTemplow.setVisibility(View.VISIBLE);
                                tvWeather.setVisibility(View.VISIBLE);
                                tvAirquality.setVisibility(View.VISIBLE);
                                //
                                ivCity.setText(weather.getCity());
                                ivWeather.setImageResource(resID);
                                tvTemplow.setText(weather.getTemp());
                                tvWeather.setText(weather.getWeather());
                                tvAirquality.setText(weather.getQuality());

                                flag = true;
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                            }
                        });
                        flag = false;
                    }
                } catch (Exception e) {
                    flag = false;
                }

            }
        }, map);

    }



    private void startBanner(final List<String> picList, final List<String> urlList) {
        Log.d("startBanner", "startBanner");
        //获取广告实体类中的图片

        banner.setPointsIsVisible(true);
        banner.setmPointContainerPosition(XBanner.CENTER);
//        banner.setData(BannerUrl.getImgesUrl(), null);
        banner.setData(picList, null);
        banner.setmAdapter(new XBanner.XBannerAdapter() {
            @Override
            public void loadBanner(XBanner banner, Object model, View view, int position) {
//                Glide.with(MainActivity.this).load(BannerUrl.getImgesUrl().get(position)).into((ImageView) view);
                //Glide.with(MainActivity.this).load(picList.get(position)).into((ImageView) view);
                //add
                Glide.with(MainActivity.this).load(picList.get(position)).diskCacheStrategy(DiskCacheStrategy.ALL).into((ImageView) view);
            }
        });

        banner.setOnItemClickListener(new XBanner.OnItemClickListener() {
            @Override
            public void onItemClick(XBanner banner, int position) {
                switch (position) {
                    case 0:
                        Toast.makeText(MainActivity.this, "第一张", Toast.LENGTH_SHORT).show();


//                        Intent intent = new Intent();
//                        intent.setData(Uri.parse(urlList.get(position)));//Url 就是你要打开的网址
//                        intent.setAction(Intent.ACTION_VIEW);
//                        startActivity(intent);
                        break;
                    case 1:
                        Toast.makeText(MainActivity.this, "第二张", Toast.LENGTH_SHORT).show();
//                        Intent intent2 = new Intent();
//                        intent2.setData(Uri.parse(urlList.get(position)));//Url 就是你要打开的网址
//                        intent2.setAction(Intent.ACTION_VIEW);
//                        startActivity(intent2);

                        break;
                    case 2:
                        Toast.makeText(MainActivity.this, "第三张", Toast.LENGTH_SHORT).show();
//                        Intent intent3 = new Intent();
//                        intent3.setData(Uri.parse(urlList.get(position)));//Url 就是你要打开的网址
//                        intent3.setAction(Intent.ACTION_VIEW);
//                        startActivity(intent3);
//                        break;
                }
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        banner.startAutoPlay();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        banner.stopAutoPlay();
        unbinder.unbind();
    }

    @OnClick({R.id.icon_location, R.id.icon_bc, R.id.icon_bdcp, R.id.icon_kq, R.id.icon_kscz, R.id.icon_qb, R.id.icon_jsq, R.id.icon_wode, R.id.icon_qfbj, R.id.icon_tcjl})
    public void onViewClicked(View view) {

     //   boolean hasLogined = (SharedPreferenceUtil.getBean(getApplicationContext(), "userInfo") == null);

        switch (view.getId()) {
            case R.id.icon_location:
                Intent intent0 = new Intent();
                intent0.setClass(MainActivity.this, LocationActivity.class);
                startActivity(intent0);
                break;
            case R.id.icon_bc:

                    Intent intent1 = new Intent();
                    intent1.setClass(MainActivity.this, BcActivity.class);
                    startActivity(intent1);

                break;
            case R.id.icon_jsq:

                    Intent intent2 = new Intent();
                    intent2.setClass(MainActivity.this, JsqActivity.class);
                    startActivity(intent2);


                break;
            case R.id.icon_bdcp:

                    Intent intent3 = new Intent();
                    intent3.setClass(MainActivity.this, BdcpActivity.class);
                    startActivity(intent3);


                break;
            case R.id.icon_kscz:

                    Intent intent5 = new Intent();
                    intent5.setClass(MainActivity.this, KsczActivity.class);
                    startActivity(intent5);


                break;
            case R.id.icon_kq:

                    Intent intent4 = new Intent();
                    intent4.setClass(MainActivity.this, YkActivity.class);
                    startActivity(intent4);


                break;
            case R.id.icon_qb:

                    Intent intent6 = new Intent();
                    intent6.setClass(MainActivity.this, QbActivity.class);
                    startActivity(intent6);


                break;
            case R.id.icon_qfbj:

                    Intent intent7 = new Intent();
                    intent7.setClass(MainActivity.this, QfbjActivity.class);
                    startActivity(intent7);


                break;
            case R.id.icon_tcjl:

                    Intent intent8 = new Intent();
                    intent8.setClass(MainActivity.this, TcjlActivity.class);
                    startActivity(intent8);


                break;
            case R.id.icon_wode:
                Intent intent9 = new Intent();
                intent9.setClass(MainActivity.this, WodeActivity.class);
                startActivity(intent9);
                break;
        }
    }

    private void showDialog() {
        CustomPopDialog.Builder dialogBuild = new CustomPopDialog.Builder(this);
        final CustomPopDialog dialog = dialogBuild.create(R.layout.layout_dialog_quiterror);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, 1000);
    }

    private long exitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {

            if ((System.currentTimeMillis() - exitTime) > 2000)  //System.currentTimeMillis()无论何时调用，肯定大于2000
            {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                setResult(1);
                finish();
                //System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onRefresh() {
        doPost();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                refresh_layout.setRefreshing(false);
            }
        }, 500);
    }


}
