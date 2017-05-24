package com.bolong.bochetong.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.navisdk.adapter.BNCommonSettingParam;
import com.baidu.navisdk.adapter.BNOuterLogUtil;
import com.baidu.navisdk.adapter.BNOuterTTSPlayerCallback;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNaviSettingManager;
import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.bolong.bochetong.bean.ParkOfMap;
import com.bolong.bochetong.utils.NaviUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class LocationActivity extends AppCompatActivity {
    private Unbinder unbinder;
    @BindView(R.id.mMapView)
    TextureMapView mMapView;
    @BindView(R.id.layout_park_datails)
    LinearLayout layoutParkDatails;

    private BaiduMap baiduMap;
    private boolean isFirstLoc = true;// 是否首次定位

    private LocationClient mLocationClient = null;
    private MyLocationListener listener = new MyLocationListener();

    //覆盖物相关
    private BitmapDescriptor mMarker;

    //导航相关
    public static List<Activity> activityList = new LinkedList<Activity>();

    private static final String APP_FOLDER_NAME = "BNSDKSimpleDemo";

    private String mSDCardPath = null;

    public static final String ROUTE_PLAN_NODE = "routePlanNode";
    public static final String SHOW_CUSTOM_ITEM = "showCustomItem";
    public static final String RESET_END_NODE = "resetEndNode";
    public static final String VOID_MODE = "voidMode";

    private static final String[] authBaseArr = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION};
    private static final String[] authComArr = {Manifest.permission.READ_PHONE_STATE};
    private static final int authBaseRequestCode = 1;
    private static final int authComRequestCode = 2;

    private boolean hasInitSuccess = false;
    private boolean hasRequestComAuth = false;
    //出发点
    private double latitude;
    private double longitude;
    //目的地
    private double latitude2;
    private double longitude2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        //导航相关
        activityList.add(this);

        setContentView(R.layout.activity_loction);
        unbinder = ButterKnife.bind(this);


        //mMapView.setMapCustomEnable(true);
        baiduMap = mMapView.getMap();
        baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        baiduMap.setMyLocationEnabled(true);// 开启定位图层
        //定位相关
        mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
        mLocationClient.registerLocationListener(listener); // 注册监听函数
        initLocation();
        mLocationClient.start();

        //覆盖物相关
        initMarker();
        addOverlays(ParkOfMap.parkinfos);

        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                ViewHolder viewHolder = null;
                Bundle extraInfo = marker.getExtraInfo();
                ParkOfMap info = (ParkOfMap) extraInfo.getSerializable("info");

                //获取点击目的地坐标
                latitude2 = info.getLatitude();
                longitude2 = info.getLongitude();

                if (layoutParkDatails.getTag() == null) {
                    viewHolder = new ViewHolder();
                    viewHolder.tvPark = (TextView) layoutParkDatails.findViewById(R.id.tv_park);
                    viewHolder.tvDistance = (TextView) layoutParkDatails.findViewById(R.id.tv_distance);
                    viewHolder.btNavi = (Button) findViewById(R.id.bt_navi);
                    layoutParkDatails.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) layoutParkDatails.getTag();

                }
//                TextView tvPark = (TextView) layoutParkDatails.findViewById(R.id.tv_park);
//                TextView tvDistance = (TextView) layoutParkDatails.findViewById(R.id.tv_distance);
//                Button btNavi = (Button) findViewById(R.id.bt_navi);

                viewHolder.tvPark.setText(info.getName());
                viewHolder.tvDistance.setText(info.getDistance() + "");

                viewHolder.btNavi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (BaiduNaviManager.isNaviInited()) {

                            BNRoutePlanNode.CoordinateType coType = BNRoutePlanNode.CoordinateType.BD09LL;
                            BNRoutePlanNode sNode = null;
                            sNode = new BNRoutePlanNode(longitude, latitude, "起点", null, coType);
                            NaviUtils.routeplanToNavi(LocationActivity.this, longitude, latitude, longitude2, latitude2, new DemoRoutePlanListener(sNode));
                        }
                    }
                });

                layoutParkDatails.setVisibility(View.VISIBLE);

                return true;
            }
        });

        baiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                layoutParkDatails.setVisibility(View.GONE);
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });

        //导航相关
        BNOuterLogUtil.setLogSwitcher(true);
        if (NaviUtils.initDirs()) {
            NaviUtils.initNavi(this);
        }

    }

    //add
    private class ViewHolder {
        TextView tvPark;
        TextView tvDistance;
        Button btNavi;
    }

    private void addOverlays(List<ParkOfMap> parkinfos) {
        baiduMap.clear();
        LatLng latLng = null;
        OverlayOptions overlayOptions = null;
        Marker marker = null;
        for (ParkOfMap info : parkinfos) {
            // 位置
            latLng = new LatLng(info.getLatitude(), info.getLongitude());
            // 图标
            overlayOptions = new MarkerOptions().position(latLng)
                    .icon(mMarker).zIndex(5);
            marker = (Marker) (baiduMap.addOverlay(overlayOptions));
            Bundle bundle = new Bundle();
            bundle.putSerializable("info", info);
            marker.setExtraInfo(bundle);
        }
        // 将地图移到到最后一个经纬度位置
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(latLng);
        baiduMap.setMapStatus(u);
    }

    private void initMarker() {

        mMarker = BitmapDescriptorFactory.fromResource(R.mipmap.icon_park);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


        mMapView.onDestroy();
        mMapView = null;

        mLocationClient.stop();
        baiduMap.setMyLocationEnabled(false);
        unbinder.unbind();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        mMapView.onPause();
    }


    private class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null || mMapView == null) return;
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(0).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            baiduMap.setMyLocationData(locData);
            //自定义定位图标样式
            /**
             * 1.定位图层显示模式
             * 2.是否允许显示方向信息
             * 3.用户自定义定位图标
             * */
//            BitmapDescriptor bitmapMarker= BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher);
//            MyLocationConfiguration configuration = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL,false,bitmapMarker);
//            baiduMap.setMyLocationConfiguration(configuration);
            //判断是否为第一次定位,是的话需要定位到用户当前位置
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng LL = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(LL);
                baiduMap.animateMapStatus(u);
                //定位地址（门头沟永定）
                longitude = location.getLongitude();
                latitude = location.getLatitude();
                //Toast.makeText(LocationActivity.this, location.getAddrStr()+location.getLongitude()+location.getLatitude(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }

    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");
        int span = 1000;
        option.setScanSpan(span);
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
            Intent intent = new Intent(LocationActivity.this, BNDemoGuideActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(ROUTE_PLAN_NODE, (BNRoutePlanNode) mBNRoutePlanNode);
            intent.putExtras(bundle);
            startActivity(intent);
        }

        @Override
        public void onRoutePlanFailed() {
            // TODO Auto-generated method stub
            Toast.makeText(LocationActivity.this, "算路失败", Toast.LENGTH_SHORT).show();
        }
    }




}
