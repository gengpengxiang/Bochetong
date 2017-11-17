package com.bolong.bochetong.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DividerItemDecoration;
import android.util.Log;
import android.view.View;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviParaOption;
import com.bolong.bochetong.adapter.LntcAdapter;
import com.bolong.bochetong.bean2.MsgEvent;
import com.bolong.bochetong.bean2.Zone;
import com.bolong.bochetong.maputils.AMapUtil;
import com.bolong.bochetong.utils.CoordinateUtils;
import com.bolong.bochetong.utils.HttpUtils;
import com.bolong.bochetong.utils.Param;
import com.bolong.bochetong.utils.ToastUtil;
import com.google.gson.Gson;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LntcActivity extends BaseActivity {


    @BindView(R.id.mRecyclerView)
    PullLoadMoreRecyclerView mRecyclerView;
    private Unbinder unbind;
    private LntcAdapter adapter;
    private double startLongitude;
    private double startLatitude;
    private double stopLongitude;
    private double stopLatitude;
    private List<Zone.DataBean> zones = new ArrayList<>();
    private static final int ACTION_ZONE = 0;
    public List<Activity> activityList = new LinkedList<Activity>();
    public static final String ROUTE_PLAN_NODE = "routePlanNode";

    @Override
    public void onBaseCreate(Bundle bundle) {
        setContentViewId(R.layout.activity_zdtc);
        unbind = ButterKnife.bind(this);
        activityList.add(this);
        EventBus.getDefault().register(this);
        Intent intent = this.getIntent();
        startLongitude = intent.getDoubleExtra("startLongitude", 0.1);
        startLatitude = intent.getDoubleExtra("startLatitude", 0.1);

        getNearbyZones(String.valueOf(startLongitude), String.valueOf(startLatitude));
    }

    private void getNearbyZones(String longitude, String latitude) {
        Map<String, String> map = new HashMap<>();
        map.put("longitude", longitude);
        map.put("latitude", latitude);
        HttpUtils.post(Param.NEARBYZONE, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setContentViewId(R.layout.layout_nonet);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonDatas = response.body().string();
                Log.e("数据", jsonDatas);
                try {
                    try {
                        JSONObject jsonObject = new JSONObject(jsonDatas);
                        String content = jsonObject.optString("content");
                        Gson gson = new Gson();
                        Zone zone = gson.fromJson(content, Zone.class);
                        List<Zone.DataBean> dataBean = zone.getData();
                        zones.clear();
                        zones.addAll(dataBean);
                        EventBus.getDefault().post(new MsgEvent(ACTION_ZONE));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setContentViewId(R.layout.layout_noinfo);
                            }
                        });
                    }
                } catch (Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setContentViewId(R.layout.layout_noinfo);
                        }
                    });

                }
            }
        }, map);
    }

    @Override
    public void initView() {
        setTitle("路内停车");
        mRecyclerView.setLinearLayout();
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
        mRecyclerView.setColorSchemeResources(R.color.blue);
        refreshRecyclerView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        unbind.unbind();
    }

    private void refreshRecyclerView() {
        mRecyclerView.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getNearbyZones(String.valueOf(startLongitude), String.valueOf(startLatitude));
                        //adapter.notifyDataSetChanged();
                        mRecyclerView.setPullLoadMoreCompleted();
                    }
                }, 300);
            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //adapter.notifyDataSetChanged();
                        mRecyclerView.setPullLoadMoreCompleted();
                        ToastUtil.showShort(LntcActivity.this,"没有更多数据了");

                    }
                }, 300);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateUI(MsgEvent event) {
        if (event.getAction() == ACTION_ZONE) {
            adapter = new LntcAdapter(zones, mListener);
            mRecyclerView.setAdapter(adapter);
            adapter.setOnItemClickLitener(new LntcAdapter.OnItemClickLitener() {
                @Override
                public void onItemClick(View view, int position) {
                    String parkName = zones.get(position).getPark_name();
                    String parkId = zones.get(position).getId();
                    String distance = zones.get(position).getDistance();
                    Intent intent = new Intent();
                    intent.putExtra("action", "LntcActivity");
                    intent.putExtra("parkName", parkName);
                    intent.putExtra("parkId", parkId);
                    intent.putExtra("distance", distance);
                    intent.putExtra("startLongitude", startLongitude);
                    intent.putExtra("startLatitude", startLatitude);
                    intent.setClass(LntcActivity.this, BcxqActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    private LntcAdapter.NaviClickListener mListener = new LntcAdapter.NaviClickListener() {
        @Override
        public void myOnClick(int position, View v) {
            stopLongitude = zones.get(position).getPart_longitude();
            stopLatitude = zones.get(position).getPart_latitude();

            NaviParaOption naviParaOption = new NaviParaOption();
            naviParaOption.startPoint(new LatLng(startLatitude, startLongitude));
            naviParaOption.endPoint(new LatLng(stopLatitude, stopLongitude));

            LatLng startLL = new LatLng(startLatitude, startLongitude);
            LatLng endLL = new LatLng(stopLatitude, stopLongitude);

            if (AMapUtil.isInstallByRead("com.autonavi.minimap") && !AMapUtil.isInstallByRead("com.baidu.BaiduMap")) {
                double[] endPoint = CoordinateUtils.bdToGaoDe(stopLatitude, stopLongitude);
                Intent intent = new Intent("android.intent.action.VIEW", android.net.Uri.parse("androidamap://navi?sourceApplication=铂车通&lat=" + endPoint[0] + "&lon=" + endPoint[1] + "&dev=0"));
                intent.setPackage("com.autonavi.minimap");
                startActivity(intent);
            }
            if (AMapUtil.isInstallByRead("com.baidu.BaiduMap") && !AMapUtil.isInstallByRead("com.autonavi.minimap")) {

                BaiduMapNavigation.openBaiduMapNavi(naviParaOption, LntcActivity.this);
            }
            if (!AMapUtil.isInstallByRead("com.baidu.BaiduMap") && !AMapUtil.isInstallByRead("com.autonavi.minimap")) {
                BaiduMapNavigation.openWebBaiduMapNavi(naviParaOption, LntcActivity.this);
            } else {

                BaiduMapNavigation.openBaiduMapNavi(naviParaOption, LntcActivity.this);
            }
        }
    };


}
