package com.bolong.bochetong.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviParaOption;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.PoiResult;
import com.bolong.bochetong.activity.MapPathActivity;
import com.bolong.bochetong.activity.PoiActivity;
import com.bolong.bochetong.activity.R;
import com.bolong.bochetong.bean2.City;
import com.bolong.bochetong.bean2.MsgEvent;
import com.bolong.bochetong.bean2.NearbyPark;
import com.bolong.bochetong.maputils.AMapUtil;
import com.bolong.bochetong.utils.CoordinateUtils;
import com.bolong.bochetong.utils.LocationUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.bolong.bochetong.activity.CityActivity.ACTION_CITY_SELECTED;
import static com.bolong.bochetong.activity.NewMainActivity.ACTION_LOCATION;
import static com.bolong.bochetong.activity.NewMainActivity.ACTION_LOCATION_MAP;
import static com.bolong.bochetong.activity.NewMainActivity.ACTION_LOCATION_SUCCESS;
import static com.bolong.bochetong.fragment.MainFragment.ACTION_OVERLAY;
import static com.bolong.bochetong.utils.LocationUtil.ACTION_LOCATION_NEW;
import static com.bolong.bochetong.utils.LocationUtil.ACTION_LOCATION_NEWMAP;


public class MapFragment extends Fragment {

    //    @BindView(R.id.mMapView)
//    MapView mMapView;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.layout_park_info)
    PercentRelativeLayout layoutParkInfo;
    @BindView(R.id.et_park)
    EditText etPark;
    Unbinder unbinder;
    public static final int ACTION_BACK = -1;
    public static final int ACTION_POI = 110;
    @BindView(R.id.mMapView)
    TextureMapView mMapView;

    private double startLongitude;
    private double startLatitude;
    private float radius;
    private BaiduMap baiduMap;
    private boolean isFirstLoc = true;// 是否首次定位
    //    private List<NearbyPark.ZonesBean> zones = new ArrayList<>();
//    private List<NearbyPark.ParksBean> parks = new ArrayList<>();
    private BitmapDescriptor mMarker;
    private List<NearbyPark.ParksBean> parks;
    private float zoomLevel;
    private boolean hasMarker = false;
    private LatLng latlng;
    private PoiResult result;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        //LocationUtil.start(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_viewpager_map, container, false);
        unbinder = ButterKnife.bind(this, view);
//        initMap();

        return view;
    }

    private void initMap() {
        mMapView.setMapCustomEnable(false);
        baiduMap = mMapView.getMap();
        baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        baiduMap.setMyLocationEnabled(true);// 开启定位图层
        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                ViewHolder viewHolder = null;
                Bundle extraInfo = marker.getExtraInfo();
                if (extraInfo != null) {
                    if (extraInfo != null) {
                        NearbyPark.ParksBean info = (NearbyPark.ParksBean) extraInfo.getSerializable("info");

                        //获取点击目的地坐标
                        final Double stopLongitude = Double.valueOf(info.getPart_longitude());
                        final Double stopLatitude = Double.valueOf(info.getPart_latitude());

                        if (layoutParkInfo.getTag() == null) {
                            viewHolder = new ViewHolder();
                            viewHolder.tvParkName = (TextView) layoutParkInfo.findViewById(R.id.tv_parkName);
                            viewHolder.tvDistance = (TextView) layoutParkInfo.findViewById(R.id.tv_distance);
                            viewHolder.tvEmpty = (TextView) layoutParkInfo.findViewById(R.id.tv_empty2);
                            viewHolder.tvPrice = (TextView) layoutParkInfo.findViewById(R.id.tv_price);
                            viewHolder.btPath = (TextView) layoutParkInfo.findViewById(R.id.bt_path);
                            viewHolder.btNavi = (TextView) layoutParkInfo.findViewById(R.id.bt_navi);
                            layoutParkInfo.setTag(viewHolder);
                        } else {
                            viewHolder = (ViewHolder) layoutParkInfo.getTag();
                        }
                        viewHolder.tvParkName.setText(info.getPark_name());
                        viewHolder.tvDistance.setText(info.getDistance() + "km");
                        viewHolder.tvEmpty.setText(info.getEmptyPosition() + "");
                        viewHolder.tvPrice.setText(info.getStandard());
                        viewHolder.btPath.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                intent.setClass(getActivity(), MapPathActivity.class);
                                intent.putExtra("startLongitude", startLongitude);
                                intent.putExtra("startLatitude", startLatitude);
                                intent.putExtra("stopLongitude", stopLongitude);
                                intent.putExtra("stopLatitude", stopLatitude);
                                startActivity(intent);
                                layoutParkInfo.setVisibility(View.INVISIBLE);
                                Log.e("start",startLongitude+"="+startLatitude+"=stop"+stopLongitude+stopLatitude);
                            }
                        });
                        viewHolder.btNavi.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //EventBus.getDefault().post(new MsgEvent(ACTION_NAVI, startLongitude, startLatitude, stopLongitude, stopLatitude));

                                NaviParaOption naviParaOption = new NaviParaOption();
                                naviParaOption.startPoint(new LatLng(startLatitude, startLongitude));
                                naviParaOption.endPoint(new LatLng(stopLatitude, stopLongitude));
                                if (AMapUtil.isInstallByRead("com.autonavi.minimap") && !AMapUtil.isInstallByRead("com.baidu.BaiduMap")) {
                                    double[] endPoint = CoordinateUtils.bdToGaoDe(stopLatitude, stopLongitude);
                                    Intent intent = new Intent("android.intent.action.VIEW", android.net.Uri.parse("androidamap://navi?sourceApplication=铂车通&lat=" + endPoint[0] + "&lon=" + endPoint[1] + "&dev=0"));
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
                                layoutParkInfo.setVisibility(View.INVISIBLE);

                            }
                        });
                        layoutParkInfo.setVisibility(View.VISIBLE);
                    }

                }

                return true;
            }
        });
        baiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                layoutParkInfo.setVisibility(View.GONE);
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });

        //add
        baiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                zoomLevel = mapStatus.zoom;

                if (zoomLevel > 12.9) {
                    if (hasMarker == false&&parks!=null) {
                        addOverLays(parks);
                        hasMarker = true;
                    }
                } else {
                    baiduMap.clear();
                    hasMarker = false;
                }

            }
        });
    }

    /**
     * 添加覆盖物
     */
    private void addOverLays(List<NearbyPark.ParksBean> parkInfos) {
        mMarker = BitmapDescriptorFactory.fromResource(R.mipmap.icon_park);
        baiduMap.clear();
        LatLng latLng = null;
//        OverlayOptions overlayOptions = null;
        MarkerOptions overlayOptions = null;
        Marker marker = null;
        for (NearbyPark.ParksBean info : parkInfos) {
            // 位置
            latLng = new LatLng(Double.valueOf(info.getPart_latitude()), Double.valueOf(info.getPart_longitude()));
            // 图标
            overlayOptions = new MarkerOptions().position(latLng)
                    .icon(mMarker).zIndex(0).period(10);

            overlayOptions.animateType(MarkerOptions.MarkerAnimateType.grow);
            marker = (Marker) (baiduMap.addOverlay(overlayOptions));
            Bundle bundle = new Bundle();
            bundle.putSerializable("info", info);
            marker.setExtraInfo(bundle);

        }
    }


    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mMapView.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("fragment销毁","onDestroy");
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateUI(MsgEvent event) {
        if (event.getAction() == ACTION_LOCATION_MAP) {

            startLongitude = event.getArg1();
            startLatitude = event.getArg2();
            radius = event.getRadius();
            if (mMapView == null) return;
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(radius)
                    .direction(0).latitude(startLatitude)
                    .longitude(startLongitude).build();
            baiduMap.setMyLocationData(locData);

            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng LL = new LatLng(startLatitude,
                        startLongitude);
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(LL);
                baiduMap.animateMapStatus(u);
            }


        }
        /*if (event.getAction() == ACTION_OVERLAY) {
            parks = event.getList();
//            if(zoomLevel>13.0){
//                baiduMap.clear();
//                addOverLays(parks);
//            }

            Log.e("OverLay","overlay");

        }*/
        if (event.getAction() == ACTION_POI) {

            LatLng lls = event.getLatLng();
            MapStatus mMapStatus = new MapStatus.Builder()
                    .target(lls)
                    .zoom(18)
                    .build();
            MapStatusUpdate u = MapStatusUpdateFactory.newMapStatus(mMapStatus);
            baiduMap.setMapStatus(u);

            float zoomLevel2 = baiduMap.getMapStatus().zoom;
            if (zoomLevel2 > 12.9) {
                if (hasMarker == false&&parks!=null) {
                    addOverLays(parks);
                    hasMarker = true;
                }
            } else {
                baiduMap.clear();
                hasMarker = false;
            }
        }

        if(event.getAction() == ACTION_CITY_SELECTED){
            City.ContentBean city = event.getCity();
            LatLng LL = new LatLng(city.getLatitude(), city.getLongtitude());
            MapStatus mMapStatus = new MapStatus.Builder()
                    .target(LL)
                    .zoom(12)
                    .build();
            MapStatusUpdate u = MapStatusUpdateFactory.newMapStatus(mMapStatus);
            baiduMap.animateMapStatus(u);

        }
        if(event.getAction() == ACTION_LOCATION_SUCCESS){
            startLongitude = event.getArg1();
            startLatitude = event.getArg2();
            initMap();
            Log.e("地图页接收广播","true");
            if (mMapView == null) return;
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(event.getRadius())
                    .direction(0).latitude(event.getArg2())
                    .longitude(event.getArg1()).build();
            baiduMap.setMyLocationData(locData);

            LatLng LL = new LatLng(event.getArg2(), event.getArg1());
            MapStatus mMapStatus = new MapStatus.Builder()
                    .target(LL)
                    .zoom(12)
                    .build();
            MapStatusUpdate u = MapStatusUpdateFactory.newMapStatus(mMapStatus);
            baiduMap.animateMapStatus(u);

            LocationUtil.stop();
        }
        if (event.getAction() == ACTION_OVERLAY) {
            parks = event.getList();
            Log.e("OverLay","overlay");

        }

    }

    @OnClick({R.id.iv_back, R.id.et_park})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                EventBus.getDefault().post(new MsgEvent(ACTION_BACK));
                break;
            case R.id.et_park:
                Intent intent = new Intent();
                intent.setClass(getActivity(), PoiActivity.class);
                startActivity(intent);
                break;
        }
    }

    private class ViewHolder {
        TextView tvParkName, tvDistance, tvEmpty, tvPrice, btPath, btNavi;
    }

}
