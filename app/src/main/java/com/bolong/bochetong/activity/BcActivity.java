package com.bolong.bochetong.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.navisdk.adapter.BNOuterLogUtil;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.bolong.bochetong.adapter.BcAdapter;
import com.bolong.bochetong.bean.Park;
import com.bolong.bochetong.bean.User;
import com.bolong.bochetong.utils.HttpUtils;
import com.bolong.bochetong.utils.NaviUtils;
import com.bolong.bochetong.utils.Param;
import com.bolong.bochetong.utils.RecyclerViewRefreshUtils;
import com.bolong.bochetong.utils.SharedPreferenceUtil;
import com.google.gson.Gson;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

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

import static com.bolong.bochetong.activity.LocationActivity.ROUTE_PLAN_NODE;

public class BcActivity extends BaseActivity {

    private Unbinder unbinder;
    @BindView(R.id.iv_failure)
    ImageView ivFailure;
    @BindView(R.id.mRecyclerView)
    PullLoadMoreRecyclerView mRecyclerView;
    private BcAdapter adapter;
    private List<Park.ContentBean.DateBean> parkList = new ArrayList<>();
    //定位
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener;
    //public MyLocationListener listener;

    private double startLongitude;
    private double startLatitude;
    private double stopLongitude;
    private double stopLatitude;
    private LinearLayout ll;

    private boolean flag = false;
    private int totalPage;
    private int pageNo;
    private int page = 1;

    @Override
    public void onBaseCreate(Bundle bundle) {
        setContentViewId(R.layout.activity_bc);

        ll = (LinearLayout) findViewById(R.id.layout_json_failure);

        activityList.add(BcActivity.this);
        BNOuterLogUtil.setLogSwitcher(true);
//        if (NaviUtils.initDirs()) {
//            NaviUtils.initNavi(BcActivity.this);
//        }
        unbinder = ButterKnife.bind(this);

        //定位
        mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
        initLocation();
        myListener = new MyLocationListener();
        mLocationClient.registerLocationListener(myListener); // 注册监听函数

        mLocationClient.start();
        //add

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION);
        registerReceiver(myReceiver, intentFilter);

        //刷新相关
        mRecyclerView.setColorSchemeResources(R.color.blue);
        mRecyclerView.setRefreshing(true);
        mRecyclerView.setLinearLayout();
        refreshRecyclerView();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (NaviUtils.initDirs()) {
                    NaviUtils.initNavi(BcActivity.this);
                    Log.e("初始化成功","success");
                }
            }
        }, 500);//3秒后执行Runnable中的run方法


    }

    private void refreshRecyclerView() {
        mRecyclerView.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        parkList.clear();
                        page = 1;
                        getDatas(1);

                        adapter.notifyDataSetChanged();
                        mRecyclerView.setPullLoadMoreCompleted();
                    }
                }, 500);
            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page++;
                        if (page <= totalPage) {
                            getDatas(page);
                            adapter.notifyDataSetChanged();
                            mRecyclerView.setPullLoadMoreCompleted();
                        } else {
                            Toast.makeText(BcActivity.this, "没有数据了哦", Toast.LENGTH_SHORT).show();
                            mRecyclerView.setPullLoadMoreCompleted();
                        }

                    }
                }, 500);
            }
        });
    }

    private List<Park.ContentBean.DateBean> getDatas(int page) {
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
        Log.d("getData的经度", startLongitude + "");
        Map<String, String> map = new HashMap<>();
        map.put("uid", uid);
        map.put("token", token);
        map.put("longitude", String.valueOf(startLongitude));
        map.put("latitude", String.valueOf(startLatitude));
        map.put("pageNo", String.valueOf(page));

        HttpUtils.post(Param.CARPARTLIST, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        RecyclerViewRefreshUtils.refreshCompleted(mRecyclerView, ll);
                    }
                });
                Log.e("onFailure", "onFailure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonDatas = response.body().string();
                Log.e("附近车场", jsonDatas);
                try {
                    try {
                        JSONObject jsonObject = new JSONObject(jsonDatas);
                        String content = jsonObject.optString("content");
                        Gson gson = new Gson();
                        Park.ContentBean park = gson.fromJson(content, Park.ContentBean.class);
                        //获取页数
                        totalPage = park.getTotalPage();
                        pageNo = park.getPageNo();
                        List<Park.ContentBean.DateBean> newList = park.getDate();
                        //parkList.clear();
                        parkList.addAll(newList);
                        Log.e("Size", parkList.size() + "");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ll.setVisibility(View.GONE);
                                adapter.notifyDataSetChanged();
                                mRecyclerView.setPullLoadMoreCompleted();
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                RecyclerViewRefreshUtils.refreshCompleted(mRecyclerView, ll);
                            }
                        });

                    }
                } catch (Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            RecyclerViewRefreshUtils.refreshCompleted(mRecyclerView, ll);
                            Toast.makeText(BcActivity.this, "服务器异常", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }, map);

        return parkList;
    }

    @Override
    public void initView() {
        setTitle("泊车");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
        unregisterReceiver(myReceiver);
        unbinder.unbind();
    }

    private static final String ACTION = "com.bolong.bochetong.activity.STARTLOCATIONCLIENT";


    public class MyLocationListener implements BDLocationListener {


        @Override
        public void onReceiveLocation(final BDLocation location) {


            startLongitude = location.getLongitude();
            startLatitude = location.getLatitude();

            sendBroadcast(new Intent(ACTION));
            Log.e("定位经度", startLongitude + "");
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }

    }


    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");
//        int span = 1000;
//        option.setScanSpan(span);
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setLocationNotify(true);
        option.setIsNeedLocationDescribe(true);
        option.setIsNeedLocationPoiList(true);
        option.setIgnoreKillProcess(false);
        option.SetIgnoreCacheException(false);
        option.setEnableSimulateGps(false);
        mLocationClient.setLocOption(option);
    }

    public static List<Activity> activityList = new LinkedList<Activity>();

    public class DemoRoutePlanListener implements BaiduNaviManager.RoutePlanListener {

        private BNRoutePlanNode mBNRoutePlanNode = null;

        public DemoRoutePlanListener(BNRoutePlanNode node) {
            mBNRoutePlanNode = node;
        }

        @Override
        public void onJumpToNavigator() {
            /*
             * 设置途径点以及resetEndNode会回调该接口
             */
            for (Activity ac : activityList) {
                if (ac.getClass().getName().endsWith("BNDemoGuideActivity")) {
                    return;
                }
            }
            Intent intent = new Intent(BcActivity.this, BNDemoGuideActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(ROUTE_PLAN_NODE, (BNRoutePlanNode) mBNRoutePlanNode);
            intent.putExtras(bundle);
            startActivity(intent);
        }

        @Override
        public void onRoutePlanFailed() {
            // TODO Auto-generated method stub
            Toast.makeText(BcActivity.this, "算路失败", Toast.LENGTH_SHORT).show();
        }
    }


    private BroadcastReceiver myReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            adapter = new BcAdapter(getDatas(1));
            adapter.setOnItemClickLitener(new BcAdapter.OnItemClickLitener() {
                @Override
                public void onItemClick(View view, final int position) {

                    TextView bt = (TextView) view.findViewById(R.id.tv_bc_dh);

                    bt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //导航相关
//                            BNOuterLogUtil.setLogSwitcher(true);
//                            if (NaviUtils.initDirs()) {
//                                NaviUtils.initNavi(BcActivity.this);
//                            }
                            stopLongitude = Double.valueOf(parkList.get(position).getPartLongitude());
                            stopLatitude = Double.valueOf(parkList.get(position).getPartLatitude());

                            BNRoutePlanNode.CoordinateType coType = BNRoutePlanNode.CoordinateType.BD09LL;
                            BNRoutePlanNode sNode = null;

                            sNode = new BNRoutePlanNode(startLongitude, stopLatitude, "起点", null, coType);
                            NaviUtils.routeplanToNavi(BcActivity.this, startLongitude, stopLatitude, stopLongitude, stopLatitude, new DemoRoutePlanListener(sNode));

                        }
                    });


                    Intent intent = new Intent();
                    intent.setClass(BcActivity.this, BcxqActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("parkInfo", parkList.get(position));
                    intent.putExtras(bundle);

                    intent.putExtra("startLongitude", startLongitude);
                    intent.putExtra("stopLongitude", stopLongitude);

                    startActivity(intent);
                }
            });
            mRecyclerView.setAdapter(adapter);
        }

    };
}
