package com.bolong.bochetong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bolong.bochetong.bean2.Fee;
import com.bolong.bochetong.bean.User;
import com.bolong.bochetong.utils.HttpUtils;
import com.bolong.bochetong.utils.Param;
import com.bolong.bochetong.utils.SharedPreferenceUtil;
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

public class QfbjActivity extends BaseActivity {

    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_carplate)
    TextView tvCarplate;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.bt_pay)
    Button btPay;
    @BindView(R.id.qfbj_list)
    PercentRelativeLayout qfbjList;
    private Unbinder unbinder;
    private LinearLayout ll;
    private String shoudPay;
    private String orderid;
    private SwipeRefreshLayout swiperereshlayout;

    @Override
    public void onBaseCreate(Bundle bundle) {
        setContentViewId(R.layout.activity_qfbj);
        unbinder = ButterKnife.bind(this);

        getBill();
    }

    private void getBill() {
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
        HttpUtils.post(Param.ARREARAGETABLE, new Callback() {
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
                Log.e("欠费补缴", jsonDatas);
                try {
                    try {
                        JSONObject jsonObject = new JSONObject(jsonDatas);
                        String errMessage = jsonObject.optString("errMessage");
                        String content = jsonObject.optString("content");
                        Log.e("errMessage", errMessage);
                        Log.e("content", content);

                        if (!errMessage.equals("没有欠费单")) {
                            Gson gson = new Gson();
                            final Fee fee = gson.fromJson(content, Fee.class);
                            double price = fee.getPrice();
                            shoudPay = String.valueOf(price);
                            orderid = fee.getId();
                            //设置数据
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    swiperereshlayout.setRefreshing(false);

                                    qfbjList.setVisibility(View.VISIBLE);
                                    btPay.setVisibility(View.VISIBLE);

                                    tvAddress.setText(fee.getParkPlace());
                                    tvCarplate.setText(fee.getCarNumber());
                                    tvTime.setText(fee.getTimeSlot());
                                    tvPrice.setText(fee.getPrice() + "");


                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    swiperereshlayout.setRefreshing(false);
                                    setContentViewId(R.layout.layout_noinfo);
                                    Log.e("aa","aa");
                                }
                            });
                        }

                    } catch (JSONException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.e("bb","bb");
                                setContentViewId(R.layout.layout_noinfo);
                            }
                        });
                    }
                } catch (Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.e("cc","cc");
                            setContentViewId(R.layout.layout_noinfo);
                        }
                    });
                }
            }
        }, map);

    }

    @Override
    public void initView() {
        setTitle("欠费补缴");
        swiperereshlayout = (SwipeRefreshLayout) findViewById(R.id.refreshLayout);
        swiperereshlayout.setColorSchemeResources(R.color.blue);
        swiperereshlayout.setRefreshing(true);
        swiperereshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getBill();
                        swiperereshlayout.setRefreshing(false);
                    }
                }, 500);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {
            getBill();
        }
    }

    @OnClick(R.id.bt_pay)
    public void onViewClicked() {
        Intent intent = new Intent();
        intent.setClass(QfbjActivity.this, KsczActivity.class);
        intent.putExtra("symbol","arrearage");
        intent.putExtra("shoudPay", shoudPay);
        intent.putExtra("orderid", orderid);
        startActivityForResult(intent, 1);
    }
}
