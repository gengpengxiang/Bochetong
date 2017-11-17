package com.bolong.bochetong.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.bolong.bochetong.utils.DrivingRouteOverlay;
import com.bolong.bochetong.utils.OverlayManager;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MapPathActivity extends BaseActivity implements OnGetRoutePlanResultListener {

    @BindView(R.id.mMapView)
    MapView mMapView;
    private Unbinder unbind;
    private BaiduMap baiduMap;
    private RoutePlanSearch mSearch=null;
    private double startLongitude;
    private double startLatitude;
    private double stopLongitude;
    private double stopLatitude;

    @Override
    public void onBaseCreate(Bundle bundle) {
        setContentViewId(R.layout.activity_map_path);
        unbind = ButterKnife.bind(this);

        showPath();
    }

    private void showPath() {

        Intent intent = getIntent();
        startLongitude = intent.getDoubleExtra("startLongitude",0.1);
        startLatitude = intent.getDoubleExtra("startLatitude",0.1);
        stopLongitude = intent.getDoubleExtra("stopLongitude",0.1);
        stopLatitude = intent.getDoubleExtra("stopLatitude",0.1);

        baiduMap = mMapView.getMap();
        baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        baiduMap.setMyLocationEnabled(true);// 开启定位图层

        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(MapPathActivity.this);
        //baiduMap.clear();
        PlanNode startNode = PlanNode.withLocation(new LatLng(startLatitude,startLongitude));
        PlanNode endNode = PlanNode.withLocation(new LatLng(stopLatitude,stopLongitude));
        mSearch.drivingSearch((new DrivingRoutePlanOption())
                .from(startNode)
                .to(endNode));
    }

    @Override
    public void initView() {
        setTitle("路线查询");
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        mSearch.destroy();
        unbind.unbind();
    }

    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {
    }
    @Override
    public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {
    }
    @Override
    public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {
    }
    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult result) {
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            //result.getSuggestAddrInfo()
            Log.d("baiduMap", "起终点或途经点地址有岐义");
            return;
        }
        if (result.error == SearchResult.ERRORNO.PERMISSION_UNFINISHED) {
            //权限鉴定未完成则再次尝试
            Log.d("baiduMap", "权限鉴定未完成,再次尝试");
            //startSearch(loc_start,loc_end);
            return;
        }
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {

            MyDrivingRouteOverlay overlay = new MyDrivingRouteOverlay(baiduMap);
            baiduMap.setOnMarkerClickListener(overlay);
            overlay.setData(result.getRouteLines().get(0));
            overlay.addToMap();
            overlay.zoomToSpan();// 缩放地图，使所有overlay都在合适的视野范围内


        }
    }
    @Override
    public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {
    }
    @Override
    public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {
    }

    private class MyDrivingRouteOverlay extends DrivingRouteOverlay {

        public MyDrivingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            return BitmapDescriptorFactory.fromResource(R.mipmap.icon_qi);
        }

        @Override
        public int getLineColor() {
            return super.getLineColor();
        }
        @Override
        public BitmapDescriptor getTerminalMarker() {
            return BitmapDescriptorFactory.fromResource(R.mipmap.icon_zhong);
        }

        @Override
        public boolean onRouteNodeClick(int i) {
            return true;
        }


    }

    private void test(DrivingRouteLine drivingRouteLine) {
        final ArrayList<OverlayOptions> list = new ArrayList<OverlayOptions>();
        PolylineOptions object = new PolylineOptions();
        List<LatLng> arg0=new ArrayList<LatLng>();
        List<DrivingRouteLine.DrivingStep> allStep = drivingRouteLine.getAllStep();
        for (int i = 0; i < allStep.size(); i++) {
            DrivingRouteLine.DrivingStep drivingStep = allStep.get(i);
            List<LatLng> wayPoints = drivingStep.getWayPoints();
            arg0.addAll(wayPoints);
        }
        object.color(Color.BLUE).width(15).points(arg0);

        list.add(object);
        OverlayManager overlayManager = new OverlayManager(baiduMap) {

            @Override
            public boolean onPolylineClick(Polyline arg0) {
                return false;
            }

            @Override
            public boolean onMarkerClick(Marker arg0) {
                return false;
            }

            @Override
            public List<OverlayOptions> getOverlayOptions() {
                return list;
            }
        };
        overlayManager.addToMap();

    }
}
