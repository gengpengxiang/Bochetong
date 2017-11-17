package com.bolong.bochetong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bolong.bochetong.adapter.CityAdapter;
import com.bolong.bochetong.bean2.City;
import com.bolong.bochetong.bean2.MsgEvent;
import com.bolong.bochetong.utils.HttpUtils;
import com.bolong.bochetong.utils.LocationUtil;
import com.bolong.bochetong.utils.Param;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.bolong.bochetong.utils.LocationUtil.ACTION_LOCATION_NEW;
import static com.bolong.bochetong.utils.LocationUtil.ACTION_LOCATION_SECOND;

public class CityActivity extends BaseActivity {


    @BindView(R.id.tv_current_city)
    TextView tvCurrentCity;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.iv_location)
    ImageView ivLocation;
    private Unbinder unbind;
    private int ACTION_CITYS = 236592;
    public static final int ACTION_CITY_SELECTED = 236603;
    private CityAdapter adapter;
    private City city;
    private String currentCityName;

    @Override
    public void onBaseCreate(Bundle bundle) {
        setContentViewId(R.layout.activity_city);
        unbind = ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        getCitys();

        Intent intent = getIntent();
        if(intent!=null){
            currentCityName = intent.getStringExtra("currentCity");
            tvCurrentCity.setText(currentCityName);
            ivLocation.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void initView() {

        setTitle("选择城市");
        //LocationUtil.start(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbind.unbind();
        //LocationUtil.stop();
        EventBus.getDefault().unregister(this);
    }

    @OnClick(R.id.tv_location)
    public void onViewClicked() {
        LocationUtil.start(this);
    }

    public void getCitys() {
        HttpUtils.post(Param.GETCITYS, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("失败", "onFailure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonDatas = response.body().string();
                Log.e("城市", jsonDatas);
                EventBus.getDefault().post(new MsgEvent(ACTION_CITYS, jsonDatas));
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateUI(MsgEvent event) {
        if (event.getAction() == ACTION_CITYS) {
            String content = event.getStr();
            Gson gson = new Gson();
            city = gson.fromJson(content, City.class);
            adapter = new CityAdapter(city.getContent());
            adapter.setOnItemClickLitener(new CityAdapter.OnItemClickLitener() {
                @Override
                public void onItemClick(View view, int position) {
                    EventBus.getDefault().post(new MsgEvent(ACTION_CITY_SELECTED,city.getContent().get(position)));
                    finish();
                }
            });
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
            mRecyclerView.setAdapter(adapter);

        }
        if (event.getAction() == ACTION_LOCATION_NEW) {
            String cityName = event.getStr();
            tvCurrentCity.setText(cityName);
            ivLocation.setVisibility(View.VISIBLE);
            //LocationUtil.stop();
        }
    }

}
