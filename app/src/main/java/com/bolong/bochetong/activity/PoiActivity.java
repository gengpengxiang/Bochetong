package com.bolong.bochetong.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.bolong.bochetong.adapter.PoiAdapter;
import com.bolong.bochetong.bean2.MsgEvent;
import com.bolong.bochetong.utils.StatusBarUtil;
import com.bolong.bochetong.utils.ToastUtil;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;
import org.greenrobot.eventbus.EventBus;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import static com.bolong.bochetong.fragment.MapFragment.ACTION_POI;

public class PoiActivity extends AppCompatActivity implements
        OnGetPoiSearchResultListener, OnGetSuggestionResultListener {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.et_park)
    EditText etPark;
    @BindView(R.id.mRecyclerView)
    PullLoadMoreRecyclerView mRecyclerView;
    private Unbinder unbind;

    private PoiSearch mPoiSearch = null;
    private SuggestionSearch mSuggestionSearch = null;

    private int loadIndex = 0;

    int searchType = 0;  // 搜索的类型，在显示时区

    //添加
    private List<PoiInfo> poiInfos = new ArrayList<>();
    private static List<PoiInfo> poiInfosHistory = new ArrayList<>();
    private PoiAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            StatusBarUtil.compat(this, Color.parseColor("#000000"));
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_poi);
        unbind = ButterKnife.bind(this);

        // 初始化搜索模块，注册搜索事件监听
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);
        // 初始化建议搜索模块，注册建议搜索事件监听
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(this);

        mRecyclerView.setPullRefreshEnable(false);
        mRecyclerView.setPushRefreshEnable(false);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        etPark.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                poiInfos.clear();
                if (s.length() == 0) {
                    poiInfos.clear();
                    adapter.notifyDataSetChanged();
                    //return;
                }else {
                    /**
                     * 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
                     */
                    mSuggestionSearch.requestSuggestion((new SuggestionSearchOption()).keyword(s.toString()).city("北京"));
                    searchButtonProcess(s.toString());
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()==0){
                    poiInfos.clear();
                    adapter.notifyDataSetChanged();
                }

            }
        });

        adapter = new PoiAdapter(poiInfos);
        mRecyclerView.setLinearLayout();
        adapter.setOnItemClickLitener(new PoiAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                LatLng lls = poiInfos.get(position).location;
                EventBus.getDefault().post(new MsgEvent(ACTION_POI, lls));
                finish();
            }
        });
        mRecyclerView.setAdapter(adapter);


        etPark.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    if(poiInfos.size()!=0){
                        LatLng lls = poiInfos.get(0).location;
                        EventBus.getDefault().post(new MsgEvent(ACTION_POI, lls));
                        finish();
                    }else {
                        ToastUtil.showShort(PoiActivity.this,"无结果");
                    }
                }
                return false;
            }
        });
    }

    /**
     * 获取POI搜索结果，包括searchInCity，searchNearby，searchInBound返回的搜索结果
     */
    @Override
    public void onGetPoiResult(final PoiResult result) {
        if (result == null || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            /*Toast.makeText(PoiActivity.this, "未找到结果", Toast.LENGTH_LONG)
                    .show();*/
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            poiInfos.clear();
            for (PoiInfo poiInfo : result.getAllPoi()) {
                poiInfos.add(poiInfo);
            }
            adapter.notifyDataSetChanged();
            return;
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {
            // 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
            /*String strInfo = "在";
            for (CityInfo cityInfo : result.getSuggestCityList()) {
                strInfo += cityInfo.city;
                strInfo += ",";
            }
            strInfo += "找到结果";
            Toast.makeText(PoiActivity.this, strInfo, Toast.LENGTH_LONG)
                    .show();*/
        }
    }

    /**
     * 获取POI详情搜索结果，得到searchPoiDetail返回的搜索结果
     */
    @Override
    public void onGetPoiDetailResult(PoiDetailResult result) {
        /*if (result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(PoiActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT)
                    .show();
        } else {
            Toast.makeText(PoiActivity.this, result.getName() + ": " + result.getAddress(), Toast.LENGTH_SHORT)
                    .show();
        }*/
    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

    }

    /**
     * 获取在线建议搜索结果，得到requestSuggestion返回的搜索结果
     */
    @Override
    public void onGetSuggestionResult(SuggestionResult res) {
       /* if (res == null || res.getAllSuggestions() == null) {
            return;
        }
        suggest = new ArrayList<String>();

        for (SuggestionResult.SuggestionInfo info : res.getAllSuggestions()) {
            if (info.key != null) {
                suggest.add(info.key);
                //suggest.add(latlngToAddress(info.pt));总是为null
            }
        }
        sugAdapter = new ArrayAdapter<String>(PoiActivity.this, android.R.layout.simple_dropdown_item_1line, suggest);
        //etPark.setAdapter(sugAdapter);
        sugAdapter.notifyDataSetChanged();
        //ToastUtil.showShort(PoiActivity.this,"sou");*/

    }




    public void searchButtonProcess(String poiInfo) {
        searchType = 1;
//        String citystr = editCity.getText().toString();
//        String keystr = etPark.getText().toString();
        mPoiSearch.searchInCity((new PoiCitySearchOption()).city("北京").keyword(poiInfo).pageNum(loadIndex));
    }


    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPoiSearch.destroy();
        mSuggestionSearch.destroy();
        unbind.unbind();
    }

}
