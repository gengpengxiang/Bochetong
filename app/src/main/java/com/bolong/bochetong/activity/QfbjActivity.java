package com.bolong.bochetong.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bolong.bochetong.bean.Fee;
import com.bolong.bochetong.bean.User;
import com.bolong.bochetong.utils.HttpUtils;
import com.bolong.bochetong.utils.Param;
import com.bolong.bochetong.utils.SharedPreferenceUtil;
import com.bolong.bochetong.view.CustomPopDialog;
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

    private Unbinder unbinder;
    @BindView(R.id.tv_chewei)
    TextView tvChewei;
    @BindView(R.id.tv_chehao)
    TextView tvChehao;
    @BindView(R.id.tv_tingcheshijian)
    TextView tvTingcheshijian;
    @BindView(R.id.tv_goumaishichang)
    TextView tvGoumaishichang;
    @BindView(R.id.tv_danhao)
    TextView tvDanhao;
    @BindView(R.id.tv_tuoqianshichang)
    TextView tvTuoqianshichang;
    @BindView(R.id.tv_yingbujiao)
    TextView tvYingbujiao;
    @BindView(R.id.bt_qfbj_qzf)
    Button btQfbjQzf;
    @BindView(R.id.qfbj_list)
    PercentRelativeLayout qfbjList;

    private Button btnConfirm;

    private Button btnCancel;
    private CustomPopDialog dialog;
    private CustomPopDialog.Builder dialogBuild;
    //add

    private LinearLayout ll;
    private String shoudPay;

    //
    private SwipeRefreshLayout swiperereshlayout;
    private boolean flag = true;

    @Override
    public void onBaseCreate(Bundle bundle) {
        setContentViewId(R.layout.activity_qfbj);

        ll = (LinearLayout) findViewById(R.id.layout_json_failure);

        unbinder = ButterKnife.bind(this);

        dialogBuild = new CustomPopDialog.Builder(this);

        dialog = dialogBuild.create(R.layout.layout_dialog_qfbj);
        dialog.setCanceledOnTouchOutside(false);
        //dialog.show();

        btnConfirm = (Button) findViewById(R.id.btn_confirm);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        //解决dialog屏蔽返回键
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    QfbjActivity.this.finish();
                    return true;
                } else {
                    return false;
                }
            }
        });

        getBill();

        swiperereshlayout = (SwipeRefreshLayout) findViewById(R.id.refreshLayout);
        swiperereshlayout.setColorSchemeResources(R.color.blue);
        swiperereshlayout.setRefreshing(true);
        swiperereshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                flag = false;

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
    ;

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

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                swiperereshlayout.setRefreshing(false);
                                ll.setVisibility(View.VISIBLE);
                                qfbjList.setVisibility(View.INVISIBLE);
                                btQfbjQzf.setVisibility(View.INVISIBLE);
                            }
                        }, 500);

                        //Toast.makeText(QfbjActivity.this, "数据获取失败", Toast.LENGTH_SHORT).show();

                    }
                });
                Log.e("onFailure", "fail");
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

                            //设置数据
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    swiperereshlayout.setRefreshing(false);

                                    qfbjList.setVisibility(View.VISIBLE);
                                    btQfbjQzf.setVisibility(View.VISIBLE);
                                    ll.setVisibility(View.INVISIBLE);

                                    tvChewei.setText(fee.getParkName());
                                    tvChehao.setText(fee.getCarNumber());
                                    tvTingcheshijian.setText(fee.getStartstopTime());
                                    tvGoumaishichang.setText(fee.getDurationTime());
                                    tvDanhao.setText(fee.getShoudPay() + "");
                                    tvTuoqianshichang.setText(fee.getDelivered() + "");
                                    tvYingbujiao.setText(fee.getArrearageCost() + "");

                                    shoudPay = String.valueOf(fee.getArrearageCost());
                                    Log.e("shoudPay", shoudPay);
                                    //dialog.show();
                                    if(flag){
                                        dialog.show();
                                    }

                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ll.setVisibility(View.VISIBLE);
                                    swiperereshlayout.setRefreshing(false);
                                }
                            });

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ll.setVisibility(View.VISIBLE);
                                Toast.makeText(QfbjActivity.this, "无欠费单", Toast.LENGTH_SHORT).show();
                                swiperereshlayout.setRefreshing(false);
                            }
                        });
                    }
                } catch (Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.e("333", "333");
                            ll.setVisibility(View.VISIBLE);
                            qfbjList.setVisibility(View.INVISIBLE);
                            btQfbjQzf.setVisibility(View.INVISIBLE);
                            Toast.makeText(QfbjActivity.this, "无欠费单", Toast.LENGTH_SHORT).show();
                            swiperereshlayout.setRefreshing(false);
                        }
                    });
                }

            }
        }, map);

    }

    @Override
    public void initView() {
        setTitle("欠费补缴");
    }


    @OnClick(R.id.bt_qfbj_qzf)
    public void onViewClicked() {
        Intent intent = new Intent();
        intent.setClass(QfbjActivity.this, KsczActivity.class);
        intent.putExtra("shoudPay", shoudPay);
        Log.e("补缴页面传递过去的的金额", shoudPay);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        dialog.dismiss();
    }

    public void doClick(View view) {
        switch (view.getId()) {
            case R.id.btn_confirm:

                dialog.dismiss();
                break;
            case R.id.btn_cancel:
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {
            getBill();
        }
    }
}
