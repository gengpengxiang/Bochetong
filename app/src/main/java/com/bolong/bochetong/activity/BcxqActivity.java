package com.bolong.bochetong.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.percent.PercentRelativeLayout;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviParaOption;
import com.bolong.bochetong.bean2.ParkDetail;
import com.bolong.bochetong.bean2.ZoneDetail;
import com.bolong.bochetong.maputils.AMapUtil;
import com.bolong.bochetong.utils.CoordinateUtils;
import com.bolong.bochetong.utils.HttpUtils;
import com.bolong.bochetong.utils.Param;
import com.bolong.bochetong.view.CustomPopDialog;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class BcxqActivity extends BaseActivity {


    @BindView(R.id.iv_parkphoto)
    ImageView ivParkphoto;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_carPort)
    TextView tvCarPort;
    @BindView(R.id.tv_distance)
    TextView tvDistance;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_reference)
    TextView tvReference;
    @BindView(R.id.bt_navi)
    ImageView btNavi;
    @BindView(R.id.activity_bcxq)
    PercentRelativeLayout activityBcxq;
    private Unbinder unbinder;

    private double startLongitude;
    private double startLatitude;
    private double stopLongitude;
    private double stopLatitude;

    private String action;
    private String parkName;
    private String parkId;
    private String distance;

    private CustomPopDialog dialog;
    private String zonePicture="";
    private String parkPicture="";

    @Override
    public void onBaseCreate(Bundle bundle) {
        setContentViewId(R.layout.activity_bcxq);

        unbinder = ButterKnife.bind(this);
        Intent intent = this.getIntent();
        action = intent.getStringExtra("action");

        parkName = intent.getStringExtra("parkName");
        parkId = intent.getStringExtra("parkId");
        distance = intent.getStringExtra("distance");
        startLongitude = intent.getDoubleExtra("startLongitude", 0.1);
        startLatitude = intent.getDoubleExtra("startLatitude", 0.1);

        if (action.equals("MainFragment")) {
            getParkInfos(parkId, distance);
        }
        if (action.equals("LntcActivity")) {
            getZoneInfos(parkId, distance);
        }
    }

    private void getZoneInfos(String parkId, String distance) {
        Map<String, String> map = new HashMap<>();
        map.put("zoneId", parkId);
        map.put("distance", distance);
        HttpUtils.post(Param.ZONEDETAIL, new Callback() {
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
                Log.e("datas", jsonDatas);
                try {
                    try {
                        JSONObject jsonObject = new JSONObject(jsonDatas);
                        String content = jsonObject.optString("content");
                        Gson gson = new Gson();
                        final ZoneDetail zoneDetail = gson.fromJson(content, ZoneDetail.class);

                        stopLongitude = zoneDetail.getPartLongitude();
                        stopLatitude = zoneDetail.getPartLatitude();
                        zonePicture = zoneDetail.getParkPicture();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                activityBcxq.setVisibility(View.VISIBLE);
                                Log.e("zone", zonePicture+"=-=-=");
                                Glide.with(getApplicationContext()).load(zoneDetail.getParkPicture()).error(R.mipmap.bg_bcxq_nophoto).centerCrop().into(ivParkphoto);
                                tvAddress.setText(zoneDetail.getParkAddress());
                                tvDistance.setText("距我" + zoneDetail.getParkDistance() + "km");
                                tvPrice.setText(zoneDetail.getChargeDetail());
                                tvReference.setText(zoneDetail.getParkingProfile());
                                tvCarPort.setText(zoneDetail.getEmptyPosition() + "个");
                                tvAddress.setSelected(true);
                                tvReference.setMovementMethod(ScrollingMovementMethod.getInstance());
                            }
                        });

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

    private void getParkInfos(String parkId, String distance) {
        Map<String, String> map = new HashMap<>();
        map.put("parkId", parkId);
        map.put("distance", distance);
        HttpUtils.post(Param.PARKDETAIL, new Callback() {
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
                Log.e("datas", jsonDatas);
                try {
                    try {
                        JSONObject jsonObject = new JSONObject(jsonDatas);
                        String content = jsonObject.optString("content");
                        Gson gson = new Gson();
                        final ParkDetail parkDetail = gson.fromJson(content, ParkDetail.class);

                        stopLongitude = parkDetail.getPartLongitude();
                        stopLatitude = parkDetail.getPartLatitude();
                        parkPicture = parkDetail.getParkPicture();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                activityBcxq.setVisibility(View.VISIBLE);
                                Log.e("park", parkPicture+"====");

                                Glide.with(getApplicationContext()).load(parkDetail.getParkPicture()).error(R.mipmap.bg_bcxq_nophoto).centerCrop().into(ivParkphoto);

                                tvAddress.setText(parkDetail.getParkAddress());
                                tvDistance.setText("距我" + parkDetail.getParkDistance() + "km");
                                tvPrice.setText(parkDetail.getChargeDetail());
                                tvReference.setText(parkDetail.getParkingProfile());
                                tvCarPort.setText(parkDetail.getEmptyPosition() + "个");
                                tvAddress.setSelected(true);
                                tvReference.setMovementMethod(ScrollingMovementMethod.getInstance());
                            }
                        });

                    } catch (JSONException e) {
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
        setTitle(parkName);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @OnClick({R.id.iv_parkphoto, R.id.bt_navi})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_parkphoto:
                CustomPopDialog.Builder dialogBuild = new CustomPopDialog.Builder(this);
                dialog = dialogBuild.create(R.layout.layout_dialog_photo,1.0,1.0);
                dialog.setCanceledOnTouchOutside(true);

                if(parkPicture==null){
                    return;
                }
                if(zonePicture.equals("http://101.201.145.238/null")){
                    return;
                }else {
                    dialog.show();
                }

                ImageView iv = (ImageView) dialog.findViewById(R.id.iv_photo);

                if (action.equals("MainFragment")) {
                        Glide.with(getApplicationContext()).load(parkPicture).error(R.mipmap.bg_bcxq_nophoto).into(iv);
                }
                if (action.equals("LntcActivity")) {
                        Glide.with(getApplicationContext()).load(zonePicture).error(R.mipmap.bg_bcxq_nophoto).into(iv);
                }

                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(dialog.isShowing()){
                            dialog.dismiss();
                        }

                    }
                });

                dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                            if(dialog!=null){
                                dialog.dismiss();
                            }
                            return true;
                        } else {
                            return false;
                        }
                    }
                });

                break;
            case R.id.bt_navi:
                NaviParaOption naviParaOption = new NaviParaOption();
                naviParaOption.startPoint(new LatLng(startLatitude, startLongitude));
                naviParaOption.endPoint(new LatLng(stopLatitude, stopLongitude));
                if (AMapUtil.isInstallByRead("com.autonavi.minimap") && !AMapUtil.isInstallByRead("com.baidu.BaiduMap")) {
                    double[] endPoint = CoordinateUtils.bdToGaoDe(stopLatitude, stopLongitude);
                    Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("androidamap://navi?sourceApplication=铂车通&lat=" + endPoint[0] + "&lon=" + endPoint[1] + "&dev=0"));
                    intent.setPackage("com.autonavi.minimap");
                    startActivity(intent);
                }
                if (AMapUtil.isInstallByRead("com.baidu.BaiduMap") && !AMapUtil.isInstallByRead("com.autonavi.minimap")) {

                    BaiduMapNavigation.openBaiduMapNavi(naviParaOption, BcxqActivity.this);
                }
                if (!AMapUtil.isInstallByRead("com.baidu.BaiduMap") && !AMapUtil.isInstallByRead("com.autonavi.minimap")) {
                    BaiduMapNavigation.openWebBaiduMapNavi(naviParaOption, BcxqActivity.this);
                } else {

                    BaiduMapNavigation.openBaiduMapNavi(naviParaOption, BcxqActivity.this);
                }
                break;
        }
    }
}
