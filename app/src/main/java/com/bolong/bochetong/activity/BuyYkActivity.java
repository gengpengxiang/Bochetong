package com.bolong.bochetong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bolong.bochetong.adapter.PopuCarplateAdapter;
import com.bolong.bochetong.adapter.PopuCityAdapter;
import com.bolong.bochetong.adapter.PopuMonthAdapter;
import com.bolong.bochetong.adapter.PopuParkAdapter;
import com.bolong.bochetong.bean.User;
import com.bolong.bochetong.bean2.CarCard;
import com.bolong.bochetong.bean2.City;
import com.bolong.bochetong.bean2.MonthCardCharge;
import com.bolong.bochetong.bean2.MsgEvent;
import com.bolong.bochetong.bean2.PopuPark;
import com.bolong.bochetong.utils.HttpUtils;
import com.bolong.bochetong.utils.Param;
import com.bolong.bochetong.utils.SharedPreferenceUtil;
import com.bolong.bochetong.utils.ToastUtil;
import com.bolong.bochetong.view.CustomPopDialog;
import com.bolong.bochetong.view.CustomPopWindow;
import com.bolong.bochetong.view.MyDividerItemDecoration;
import com.google.gson.Gson;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
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
import static com.bolong.bochetong.activity.BdcpActivity2.ACTION_BUY_BIND_SUCCESS;
import static com.bolong.bochetong.activity.YkOrderActivity.ACTION_BUYMONTHCARD_SUCCESS;
public class BuyYkActivity extends BaseActivity {

    @BindView(R.id.bt_change_city)
    TextView btChangeCity;
    @BindView(R.id.layout_city)
    RelativeLayout layoutCity;
    @BindView(R.id.bt_add_car)
    TextView btAddCar;
    @BindView(R.id.layout_park)
    RelativeLayout layoutPark;
    @BindView(R.id.layout_carplate)
    RelativeLayout layoutCarplate;
    @BindView(R.id.tv_cityName)
    TextView tvCityName;
    @BindView(R.id.tv_parkName)
    TextView tvParkName;
    @BindView(R.id.tv_car)
    TextView tvCar;
    @BindView(R.id.tv_month)
    TextView tvMonth;
    @BindView(R.id.bt_buy)
    Button btBuy;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.bt_card_num)
    TextView btCardNum;
    @BindView(R.id.bt_change_month)
    TextView btChangeMonth;
    private Unbinder unbind;
    private List<City.ContentBean> cityList;
    private CustomPopWindow mListPopWindow;
    private List<PopuPark.ParkLisBean> parkLists;
    private List<CarCard.ContentBean> carplateList;

    private int ACTION_CREATEORDER_SUCCESS = 577487984;
    private int ACTION_GETMONTHCARDCHARGE = 7754877;
    private MonthCardCharge monthCardCharge;
    private String cityCode;
    private String cityName;
    private String parkId;
    private String monthAmount;
    private String carNumber;
    private String monthcardtypeId;
    private String orderPrice;
    private String orderId;
    private CustomPopDialog dialog;

    @Override
    public void onBaseCreate(Bundle bundle) {
        setContentViewId(R.layout.activity_buy_yk);
        unbind = ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        cityName = SharedPreferenceUtil.getString("currentCityName", "北京");
        tvCityName.setText(cityName.replace("市", ""));
        cityCode = SharedPreferenceUtil.getString("cityCode", "131");

        getCitys();
        getParksList(cityCode);
        getCarCardList();
    }

    @Override
    public void initView() {
        setTitle("月卡");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbind.unbind();
        EventBus.getDefault().unregister(this);
    }
    private void clear(){
        tvParkName.setText("");
        tvCar.setText("");
        tvMonth.setText("1个月");
        tvPrice.setText("");
        btCardNum.setText("添加");
    }

    @OnClick({R.id.bt_change_city, R.id.bt_card_num, R.id.bt_add_car, R.id.bt_change_month, R.id.bt_buy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_change_city:

                PopuCityAdapter adapter = new PopuCityAdapter(cityList);
                adapter.setOnItemClickLitener(new PopuCityAdapter.OnItemClickLitener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        if(!cityList.get(position).getCityName().equals(tvCityName.getText().toString())){
                            clear();
                            tvCityName.setText(cityList.get(position).getCityName());
                            mListPopWindow.dissmiss();
                            parkLists.clear();
                            getParksList(cityList.get(position).getId());
                        }else {
                            mListPopWindow.dissmiss();
                        }
                    }
                });
                showPopListView(R.layout.layout_popu_city, layoutCity, adapter, null, null, 1);
                break;
            case R.id.bt_card_num:

                if(parkLists.size()!=0){
                    PopuParkAdapter adapter2 = new PopuParkAdapter(parkLists);
                    adapter2.setOnItemClickLitener(new PopuParkAdapter.OnItemClickLitener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            parkId = parkLists.get(position).getId();
                            tvParkName.setText(parkLists.get(position).getParkName());
                            mListPopWindow.dissmiss();
                            getMonthCardCharge(parkId);
                        }
                    });
                    showPopListView(R.layout.layout_popu_city, layoutPark, null, adapter2, null, 2);
                }else {
                    ToastUtil.showShort(BuyYkActivity.this,"所选城市无可选停车场");
                }

                break;
            case R.id.bt_add_car:

                if(carplateList.size()!=0){
                    PopuCarplateAdapter adapter3 = new PopuCarplateAdapter(carplateList);
                    adapter3.setOnItemClickLitener(new PopuCarplateAdapter.OnItemClickLitener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            tvCar.setText(carplateList.get(position).getCarCard());
                            mListPopWindow.dissmiss();
                        }
                    });
                    showPopListView(R.layout.layout_popu_city, layoutCarplate, null, null, adapter3, 3);
                }else {

                    showBindDialog();
                }


                break;
            case R.id.bt_change_month:

                View contentView = LayoutInflater.from(this).inflate(R.layout.layout_popu_city, null);
                RecyclerView recyclerView = (RecyclerView) contentView.findViewById(R.id.recyclerView);
                LinearLayoutManager manager = new LinearLayoutManager(this);
                manager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(manager);
                recyclerView.addItemDecoration(new MyDividerItemDecoration(this, MyDividerItemDecoration.VERTICAL));

                PopuMonthAdapter adapter4 = new PopuMonthAdapter(getResources().getStringArray(R.array.monthArray));

                adapter4.setOnItemClickLitener(new PopuMonthAdapter.OnItemClickLitener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        tvMonth.setText(getResources().getStringArray(R.array.monthArray)[position]);
                        mListPopWindow.dissmiss();

                        if(!TextUtils.isEmpty(tvParkName.getText().toString())){
                            tvPrice.setText(monthCardCharge.getMonthcardtype().getUnitPrice() * (position + 1) + "元");
                        }else {
                            //ToastUtil.showShort(BuyYkActivity.this,"请先选择停车场");
                        }

                    }
                });
                recyclerView.setAdapter(adapter4);
                mListPopWindow = new CustomPopWindow.PopupWindowBuilder(this)
                        .setView(contentView)
                        .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)//显示大小
                        .enableOutsideTouchableDissmiss(true)
                        .create()
                        .showAsDropDown(tvMonth, 0, 20);
                break;
            case R.id.bt_buy:


                if (!TextUtils.isEmpty(tvParkName.getText()) && !TextUtils.isEmpty(tvCar.getText())&&!TextUtils.isEmpty(tvPrice.getText())) {
                    createMonthCardOrder();

                } else {
                    ToastUtil.showShort(BuyYkActivity.this, "填写信息不完整");
                }

                break;
        }
    }

    private void showBindDialog() {

        CustomPopDialog.Builder dialogBuild = new CustomPopDialog.Builder(this);
        dialog = dialogBuild.create(R.layout.layout_dialog_nocarplate);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Button btnConfirm = (Button) dialog.findViewById(R.id.btn_confirm);
        Button btnCancel = (Button) dialog.findViewById(R.id.btn_cancel);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(BuyYkActivity.this,BdcpActivity2.class);
                intent.putExtra("symbol","monthCardCar");
                startActivity(intent);
                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateUI(MsgEvent event) {
        if (event.getAction() == ACTION_GETMONTHCARDCHARGE) {
            String str = event.getStr();
            Gson gson = new Gson();
            monthCardCharge = gson.fromJson(str, MonthCardCharge.class);
            monthcardtypeId = monthCardCharge.getMonthcardtype().getId();
            btCardNum.setText("余" + monthCardCharge.getAmount() + "张");

            tvMonth.setText("1个月");

            tvPrice.setText(monthCardCharge.getMonthcardtype().getUnitPrice() + "元");

        }
        if (event.getAction() == ACTION_CREATEORDER_SUCCESS) {
            Intent intent = new Intent();
            intent.putExtra("cityName", tvCityName.getText());
            intent.putExtra("parkName", tvParkName.getText());
            intent.putExtra("carPlate", tvCar.getText());
            intent.putExtra("month", tvMonth.getText());
            intent.putExtra("price", tvPrice.getText());

            intent.putExtra("orderPrice", tvPrice.getText());
            intent.putExtra("orderId", orderId);

            intent.setClass(BuyYkActivity.this, YkOrderActivity.class);
            startActivity(intent);
        }
        if (event.getAction() == ACTION_BUYMONTHCARD_SUCCESS) {
            finish();
        }
        if(event.getAction() == ACTION_BUY_BIND_SUCCESS){
            tvCar.setText(event.getStr());
            getCarCardList();
        }
    }

    private void showPopListView(int resId, RelativeLayout layout, PopuCityAdapter adapter, PopuParkAdapter adapter2, PopuCarplateAdapter adapter3, int n) {
        View contentView = LayoutInflater.from(this).inflate(resId, null);
        RecyclerView recyclerView = (RecyclerView) contentView.findViewById(R.id.recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, MyDividerItemDecoration.VERTICAL));
        if (n == 1) {
            recyclerView.setAdapter(adapter);
        }
        if (n == 2) {
            recyclerView.setAdapter(adapter2);
        }
        if (n == 3) {
            recyclerView.setAdapter(adapter3);
        }
        mListPopWindow = new CustomPopWindow.PopupWindowBuilder(this)
                .setView(contentView)
                .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)//显示大小
                .enableOutsideTouchableDissmiss(true)
                .create()
                .showAsDropDown(layout, 0, 20);
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
                Gson gson = new Gson();
                City city = gson.fromJson(jsonDatas, City.class);
                cityList = city.getContent();
            }
        });
    }

    public void getParksList(String cityId) {
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
        Map<String, String> map = new HashMap<>();
        map.put("uid", uid);
        map.put("token", token);
        map.put("cityId", cityId);
//        map.put("keyword", );
        HttpUtils.post(Param.GETSTOPCARPARKLISTBYCITY, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonDatas = response.body().string();
                Log.e("返回数据", jsonDatas);
                try {
                    JSONObject jsonObject = new JSONObject(jsonDatas);
                    String errCode = jsonObject.optString("errCode");


                    if (errCode.equals("1000")) {
                        String content = jsonObject.optString("content");
                        Gson gson = new Gson();
                        PopuPark popPark = gson.fromJson(content, PopuPark.class);
                        parkLists = popPark.getParkLis();
                    } else {
                        final String errMessage = jsonObject.optString("errMessage");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showShort(BuyYkActivity.this, errMessage);
                            }
                        });
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, map);
    }

    private void getCarCardList() {
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
        Map<String, String> map = new HashMap<>();
        map.put("uid", uid);
        map.put("token", token);
        HttpUtils.post(Param.CARCARDLIST, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonDatas = response.body().string();
                Log.e("绑定车牌", jsonDatas);
                try {
                    try {
                        JSONObject jsonObject = new JSONObject(jsonDatas);
                        String content = jsonObject.optString("content");
                        Gson gson = new Gson();
                        CarCard carCard = gson.fromJson(jsonDatas, CarCard.class);
                        carplateList = carCard.getContent();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                }
            }
        }, map);
    }

    private void getMonthCardCharge(String parkId) {
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
        Map<String, String> map = new HashMap<>();
        map.put("uid", uid);
        map.put("token", token);
        map.put("parkId", parkId);
        HttpUtils.post(Param.GETMONTHCARDCHARGE, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonDatas = response.body().string();
                Log.e("标准",jsonDatas);
                try {
                    JSONObject jsonObject = new JSONObject(jsonDatas);
                    String errCode = jsonObject.optString("errCode");
                    if (errCode.equals("1000")) {
                        String content = jsonObject.optString("content");
                        EventBus.getDefault().post(new MsgEvent(ACTION_GETMONTHCARDCHARGE, content));
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, map);
    }

    private void createMonthCardOrder() {
        monthAmount = tvMonth.getText().toString().replace("个月", "");
        carNumber = tvCar.getText().toString();
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
        Map<String, String> map = new HashMap<>();
        map.put("uid", uid);
        map.put("token", token);
        map.put("monthcardtypeId", monthcardtypeId);
        map.put("amount", monthAmount);
        map.put("carNumber", carNumber);
        Log.e("创建订单所需参数", "月卡类型id==" + monthcardtypeId + "购买月数==" + monthAmount + "车牌号=" + carNumber);
        HttpUtils.post(Param.CREATEBUYMONTHCARDORDER, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonDatas = response.body().string();
                Log.e("创建订单", jsonDatas);
                try {
                    JSONObject jsonObject = new JSONObject(jsonDatas);
                    String errCode = jsonObject.optString("errCode");
                    final String errMessage = jsonObject.optString("errMessage");
                    if (errCode.equals("1000")) {
                        String content = jsonObject.optString("content");

                        JSONObject jb = new JSONObject(content);
                        orderPrice = jb.optString("orderPrice");
                        orderId = jb.optString("orderId");
                        EventBus.getDefault().post(new MsgEvent(ACTION_CREATEORDER_SUCCESS, content));
                    }else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showShort(BuyYkActivity.this,errMessage);
                            }
                        });
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, map);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

}
