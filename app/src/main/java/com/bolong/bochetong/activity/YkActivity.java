package com.bolong.bochetong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bolong.bochetong.bean.MonthCard;
import com.bolong.bochetong.bean.User;
import com.bolong.bochetong.utils.HttpUtils;
import com.bolong.bochetong.utils.Param;
import com.bolong.bochetong.utils.SharedPreferenceUtil;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class YkActivity extends BaseActivity {

    private TextView tvPark;
    private TextView tvPhoneNumber;
    private TextView tvCarport;
    private TextView tvCarplate;
    private TextView tvStartTime;
    private TextView tvEndTime;
    private RelativeLayout layoutYk;
    private Button bt_qpbd;
    private TextView bt_bdcl;
    private String cardId;
    private String monthlyPrice;

    @Override
    public void onBaseCreate(Bundle bundle) {
        setContentViewId(R.layout.activity_yk);
        doPost();
        layoutYk = (RelativeLayout) findViewById(R.id.layout_yk);

    }

    @Override
    public void initView() {
        setTitle("月卡");
    }

    private void doPost() {
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
        HttpUtils.post(Param.MONTHCARDLIST, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LinearLayout ll = (LinearLayout) findViewById(R.id.layout_json_failure);
                        ll.setVisibility(View.VISIBLE);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonDatas = response.body().string();
                Log.e("月卡", jsonDatas);
                try {
                    try {
                        JSONObject jsonObject = new JSONObject(jsonDatas);
                        String content = jsonObject.optString("content");
                        JSONObject jb = new JSONObject(content);
                        String status = jb.optString("status");
                        String monthCardInfo = jb.optString("monthCard");
                        Log.e("Info", monthCardInfo);
                        //已绑定车辆
                        if (status.equals("1")) {

                            //存在月卡信息
                            if (!monthCardInfo.equals("null")) {
                                Gson gson = new Gson();
                                MonthCard card = gson.fromJson(content, MonthCard.class);
                                List<MonthCard.MonthCardBean> cardList = card.getMonthCard();
                                final MonthCard.MonthCardBean monthCard = cardList.get(0);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        layoutYk.removeAllViews();
                                        LayoutInflater layoutInflater = LayoutInflater.from(YkActivity.this);
                                        View view = layoutInflater.inflate(R.layout.layout_yk_card, null);
                                        tvPark = (TextView) view.findViewById(R.id.tv_park);
                                        tvPhoneNumber = (TextView) view.findViewById(R.id.tv_phoneNumber);
                                        tvCarport = (TextView) view.findViewById(R.id.tv_carport);
                                        tvCarplate = (TextView) view.findViewById(R.id.tv_carplate);
                                        tvStartTime = (TextView) view.findViewById(R.id.tv_startTime);
                                        tvEndTime = (TextView) view.findViewById(R.id.tv_endTime);
                                        bt_bdcl = (TextView) view.findViewById(R.id.bt_bdcl);
                                        layoutYk.addView(view);
                                        view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

                                        tvPark.setText(monthCard.getParkName());
                                        tvPhoneNumber.setText(monthCard.getUserPhoneNumber());
                                        tvCarport.setText(monthCard.getCarportNum());
                                        tvCarplate.setText(monthCard.getCarNumber());
                                        tvStartTime.setText(monthCard.getCreateTime());
                                        tvEndTime.setText(monthCard.getCardTermofvalidity());

                                        cardId = monthCard.getCardId();
                                        monthlyPrice = monthCard.getMonthlyPrice();
                                    }
                                });

                            }
                            //无月卡信息
                            if (monthCardInfo.equals("null")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        layoutYk.removeAllViews();
                                        LayoutInflater layoutInflater = LayoutInflater.from(YkActivity.this);
                                        View view = layoutInflater.inflate(R.layout.layout_yk_nocard, null);
                                        layoutYk.addView(view);
                                        view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
                                    }
                                });

                            }
                        }
                        //未绑定车辆
                        if (status.equals("0")) {
                            if (monthCardInfo.equals("null")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        layoutYk.removeAllViews();
                                        LayoutInflater layoutInflater = LayoutInflater.from(YkActivity.this);
                                        View view = layoutInflater.inflate(R.layout.layout_yk_bdcp, null);
                                        bt_qpbd = (Button) view.findViewById(R.id.bt_qqbd);

                                        layoutYk.addView(view);
                                        view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
                                    }
                                });
                            }

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {

                }
            }
        }, map);
    }

    public void doClick(View view) {
        switch (view.getId()) {
            case R.id.bt_xufei:
                Intent intent0 = new Intent();
                intent0.setClass(YkActivity.this, KsczActivity.class);
//                intent0.putExtra("monthlyPrice", monthlyPrice);
                intent0.putExtra("cardId", cardId);
                startActivityForResult(intent0, 1);

                //               startActivity(intent0);
                break;
//            case R.id.bt_bdcl:
//                Intent intent1 = new Intent();
//                intent1.setClass(YkActivity.this, BdcpActivity2.class);
//                startActivityForResult(intent1,1);
//                break;
            case R.id.bt_qqbd:

                Intent intent2 = new Intent();
                intent2.setClass(YkActivity.this, BdcpActivity2.class);
                startActivityForResult(intent2, 1);
                //layoutYk.removeAllViews();
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {
            layoutYk.removeAllViews();
            doPost();
            Log.e("onActivityResult", "onActivityResult");
        }
    }


}
