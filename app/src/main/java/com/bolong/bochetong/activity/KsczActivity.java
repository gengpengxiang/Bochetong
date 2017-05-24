package com.bolong.bochetong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.percent.PercentRelativeLayout;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.bolong.bochetong.bean.User;
import com.bolong.bochetong.pay.AuthResult;
import com.bolong.bochetong.pay.H5PayDemoActivity;
import com.bolong.bochetong.pay.PayResult;
import com.bolong.bochetong.utils.HttpUtils;
import com.bolong.bochetong.utils.Param;
import com.bolong.bochetong.utils.SharedPreferenceUtil;
import com.bolong.bochetong.utils.SoftKeyboardStateHelperUtil;
import com.bolong.bochetong.view.CustomPopDialog;

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
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class KsczActivity extends BaseActivity {

    private Unbinder unbinder;
    @BindView(R.id.frame_kscz_yue)
    FrameLayout frameKsczYue;
    @BindView(R.id.bt_kscz_twenty)
    Button btKsczTwenty;
    @BindView(R.id.bt_kscz_fifth)
    Button btKsczFifth;
    @BindView(R.id.bt_kscz_hundred)
    Button btKsczHundred;
    @BindView(R.id.relative_kscz_jine)
    PercentRelativeLayout relativeKsczJine;
    @BindView(R.id.et)
    EditText et;
    @BindView(R.id.bt_zhifubao)
    Button btZhifubao;
    @BindView(R.id.bt_weixin)
    Button btWeixin;
    @BindView(R.id.bt_chongzhi)
    Button btChongzhi;
    @BindView(R.id.tv_yue)
    TextView tvYue;
    @BindView(R.id.linear_kscz_edittext)
    LinearLayout linearKsczEdittext;
    private SpannableString hint;
    private boolean flagZhifubao = false;
    private boolean flagWeixin = false;
    private String shoudPay;
    private String monthlyPrice;
    private String cardId;

    @Override
    public void onBaseCreate(Bundle bundle) {
        setContentViewId(R.layout.activity_kscz);
        //
        flagZhifubao = false;
        flagWeixin = false;

        unbinder = ButterKnife.bind(this);
        SharedPreferenceUtil.putString("jine", "20");
        //EditText焦点改变时执行的方法.不宜用onClick()
        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                    hint = new SpannableString("请输入整数金额（1-1000）");
                    et.setHint(hint);
                    et.setCursorVisible(true);
                    SharedPreferenceUtil.removeString("jine");

                } else {
                    Log.e("Tag", "+++");
                }
            }
        });

        //监听软键盘打开和关闭
        SoftKeyboardStateHelperUtil softKeyboardStateHelper = new SoftKeyboardStateHelperUtil(findViewById(R.id.linear_kscz));
        softKeyboardStateHelper.addSoftKeyboardStateListener(new SoftKeyboardStateHelperUtil.SoftKeyboardStateListener() {
            @Override
            public void onSoftKeyboardOpened(int keyboardHeightInPx) {
                frameKsczYue.setVisibility(View.GONE);
                relativeKsczJine.setVisibility(View.GONE);
                btKsczTwenty.setBackgroundResource(R.mipmap.bg_kscz_jine_normal);
                btKsczFifth.setBackgroundResource(R.mipmap.bg_kscz_jine_normal);
                btKsczHundred.setBackgroundResource(R.mipmap.bg_kscz_jine_normal);
            }

            @Override
            public void onSoftKeyboardClosed() {
                frameKsczYue.setVisibility(View.VISIBLE);
                relativeKsczJine.setVisibility(View.VISIBLE);
            }
        });


        btZhifubao.setBackgroundResource(R.mipmap.bg_zhifubao_checked);
        btWeixin.setBackgroundResource(R.mipmap.bg_weixin_normal);
        flagZhifubao = true;
        flagWeixin = false;

        getYuE();

        setContent();
    }

    private void setContent() {
        Intent intent = getIntent();
        shoudPay = intent.getStringExtra("shoudPay");
//        monthlyPrice = intent.getStringExtra("monthlyPrice");
        cardId = intent.getStringExtra("cardId");
        // Log.e("月卡",cardId);
        if (shoudPay != null) {

            TextView tv_qfbj = (TextView) findViewById(R.id.tv_qfbj);
            tv_qfbj.setText("补缴金额为" + shoudPay + "元");
            tv_qfbj.setVisibility(View.VISIBLE);
            frameKsczYue.setVisibility(View.INVISIBLE);
            relativeKsczJine.setVisibility(View.INVISIBLE);
            linearKsczEdittext.setVisibility(View.INVISIBLE);
        }
        if (cardId != null) {

            TextView tv_qfbj = (TextView) findViewById(R.id.tv_qfbj);
            tv_qfbj.setText("月卡id是" + cardId+"续费金额为150元");
            tv_qfbj.setVisibility(View.VISIBLE);
            frameKsczYue.setVisibility(View.INVISIBLE);
            relativeKsczJine.setVisibility(View.INVISIBLE);
            linearKsczEdittext.setVisibility(View.INVISIBLE);
        }

    }


    private void getYuE() {
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
        HttpUtils.post(Param.NOTECASE, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonDatas = response.body().string();
                Log.e("钱包明细", jsonDatas);
                try {
                    try {
                        JSONObject jsonObject = new JSONObject(jsonDatas);
                        String content = jsonObject.optString("content");
                        JSONObject jb = new JSONObject(content);
                        //余额
                        final String accountBalance = jb.optString("accountBalance");
                        SharedPreferenceUtil.putString("yue", accountBalance);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvYue.setText(accountBalance);
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tvYue.setText("- -");
                                    }
                                });
                            }
                        });
                    }
                } catch (Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tvYue.setText("- -");
                                }
                            });
                        }
                    });
                }
            }
        }, map);
    }

    @Override
    public void initView() {
        setTitle("快速充值");
    }


    @OnClick({R.id.bt_kscz_twenty, R.id.bt_kscz_fifth, R.id.bt_kscz_hundred, R.id.bt_zhifubao, R.id.bt_weixin, R.id.bt_chongzhi})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_kscz_twenty:
                clearEditText();
                btKsczTwenty.setBackgroundResource(R.mipmap.bg_kscz_jine_selected);
                btKsczFifth.setBackgroundResource(R.mipmap.bg_kscz_jine_normal);
                btKsczHundred.setBackgroundResource(R.mipmap.bg_kscz_jine_normal);
                SharedPreferenceUtil.putString("jine", "20");
                break;
            case R.id.bt_kscz_fifth:
                clearEditText();
                btKsczTwenty.setBackgroundResource(R.mipmap.bg_kscz_jine_normal);
                btKsczFifth.setBackgroundResource(R.mipmap.bg_kscz_jine_selected);
                btKsczHundred.setBackgroundResource(R.mipmap.bg_kscz_jine_normal);
                SharedPreferenceUtil.putString("jine", "50");

                //pay("1","50");

                break;
            case R.id.bt_kscz_hundred:
                clearEditText();
                btKsczTwenty.setBackgroundResource(R.mipmap.bg_kscz_jine_normal);
                btKsczFifth.setBackgroundResource(R.mipmap.bg_kscz_jine_normal);
                btKsczHundred.setBackgroundResource(R.mipmap.bg_kscz_jine_selected);
                SharedPreferenceUtil.putString("jine", "100");

                //pay("1","100");

                break;
            case R.id.bt_zhifubao:
                btZhifubao.setBackgroundResource(R.mipmap.bg_zhifubao_checked);
                btWeixin.setBackgroundResource(R.mipmap.bg_weixin_normal);
                flagZhifubao = true;
                flagWeixin = false;
                break;
            case R.id.bt_weixin:
                btWeixin.setBackgroundResource(R.mipmap.bg_weixin_checked);
                btZhifubao.setBackgroundResource(R.mipmap.bg_zhifubao_normal);
                flagWeixin = true;
                flagZhifubao = false;
                break;
            case R.id.bt_chongzhi:
                setResult(1);
                recharge();

                break;
        }
    }

    private void recharge() {
        if (!flagZhifubao && !flagWeixin) {
            Toast.makeText(KsczActivity.this, "请选择充值方式", Toast.LENGTH_SHORT).show();
        }
        if (flagZhifubao) {
            if (TextUtils.isEmpty(et.getText()) == false) {
                SharedPreferenceUtil.putString("jine", et.getText().toString());
            }
            String price = SharedPreferenceUtil.getString("jine", "0");
            if (price != null && shoudPay == null && cardId == null) {
                pay("1", price);
                //Toast.makeText(KsczActivity.this, price + "支付宝", Toast.LENGTH_SHORT).show();
                /*Intent intent = new Intent(KsczActivity.this,ToastActivity.class);
                startActivity(intent);*/
                //showDialog();
            }
            if (price == null) {
                Toast.makeText(KsczActivity.this, "充值数额不能为0", Toast.LENGTH_SHORT).show();
            }
            if (shoudPay != null) {
                Log.e("充值界面的金额",shoudPay);
                pay("3", shoudPay);
            }
            if (cardId != null) {
                payYk("1", cardId);

            }
        }
        //
//        if (flagWeixin) {
//            if (TextUtils.isEmpty(et.getText()) == false) {
//                SharedPreferenceUtil.putString("jine", et.getText().toString());
//            }
//            String price = SharedPreferenceUtil.getString("jine", null);
//            if (price != null) {
//                pay("1", price);
//                Toast.makeText(KsczActivity.this, price + "微信", Toast.LENGTH_SHORT).show();
//                //Toast.makeText(KsczActivity.this, str+"支付宝", Toast.LENGTH_SHORT).show();
//                //showDialog();
//            } else {
//                Toast.makeText(KsczActivity.this, "充值数额不能为0", Toast.LENGTH_SHORT).show();
//            }
//        }

    }


    private void clearEditText() {
        et.clearFocus();
        et.setText("");
        et.setHint("请输入其他金额");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    public void showDialog() {
        CustomPopDialog.Builder dialogBuild = new CustomPopDialog.Builder(this);
        CustomPopDialog dialog = dialogBuild.create(R.layout.layout_dialog_kscz);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
//                        Toast.makeText(KsczActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        showDialog();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(KsczActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        Toast.makeText(KsczActivity.this,
                                "授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        // 其他状态值则为授权失败
                        Toast.makeText(KsczActivity.this,
                                "授权失败" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();

                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };

    private void pay(String orderType, String rmb) {
        //获取订单
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
        map.put("orderType", orderType);
        map.put("price", rmb);
        Log.e("新支付金额", rmb);
        HttpUtils.post(Param.ALIPAY, new Callback() {
            public String orderInfo;

            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("onFailure","onFailure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonDatas = response.body().string();
                Log.e("数据", jsonDatas);
                try {
                    try {
                        JSONObject jsonObject = new JSONObject(jsonDatas);
                        String content = jsonObject.optString("content");
                        Log.e("content", content);
                        JSONObject jb = new JSONObject(content);
                        orderInfo = jb.optString("orderStr");
                        Log.e("Info", orderInfo);

                        Runnable payRunnable = new Runnable() {

                            @Override
                            public void run() {
                                PayTask alipay = new PayTask(KsczActivity.this);
                                Map<String, String> result = alipay.payV2(orderInfo, true);
                                Log.i("msp", result.toString());

                                Message msg = new Message();
                                msg.what = SDK_PAY_FLAG;
                                msg.obj = result;
                                mHandler.sendMessage(msg);
                            }
                        };

                        Thread payThread = new Thread(payRunnable);
                        payThread.start();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {

                }

            }
        }, map);


    }


    /**
     * get the sdk version. 获取SDK版本号
     */
    public void getSDKVersion() {
        PayTask payTask = new PayTask(this);
        String version = payTask.getVersion();
        Toast.makeText(this, version, Toast.LENGTH_SHORT).show();
    }

    /**
     * 原生的H5（手机网页版支付切natvie支付） 【对应页面网页支付按钮】
     *
     * @param v
     */
    public void h5Pay(View v) {
        Intent intent = new Intent(this, H5PayDemoActivity.class);
        Bundle extras = new Bundle();
        /**
         * url是测试的网站，在app内部打开页面是基于webview打开的，demo中的webview是H5PayDemoActivity，
         * demo中拦截url进行支付的逻辑是在H5PayDemoActivity中shouldOverrideUrlLoading方法实现，
         * 商户可以根据自己的需求来实现
         */
        String url = "http://m.taobao.com";
        // url可以是一号店或者淘宝等第三方的购物wap站点，在该网站的支付过程中，支付宝sdk完成拦截支付
        extras.putString("url", url);
        intent.putExtras(extras);
        startActivity(intent);
    }

    private String urlPayYk = Param.IP + "/app/alipay/extendedPay";

    private void payYk(String s, String cardId) {
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
        map.put("orderType", s);
        map.put("submitNo", cardId);
        Log.e("月卡1", cardId);
        HttpUtils.post(urlPayYk, new Callback() {
            public String orderInfo;

            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonDatas = response.body().string();
                Log.e("数据", jsonDatas);
                try {
                    try {
                        JSONObject jsonObject = new JSONObject(jsonDatas);
                        String content = jsonObject.optString("content");
                        Log.e("content", content);


                        JSONObject jb = new JSONObject(content);
                        orderInfo = jb.optString("orderStr");
                        Log.e("月卡2", orderInfo);

                        Runnable payRunnable = new Runnable() {

                            @Override
                            public void run() {
                                PayTask alipay = new PayTask(KsczActivity.this);
                                Map<String, String> result = alipay.payV2(orderInfo, true);
                                Log.i("msp", result.toString());
                                Message msg = new Message();
                                msg.what = SDK_PAY_FLAG;
                                msg.obj = result;
                                mHandler.sendMessage(msg);
                            }
                        };

                        Thread payThread = new Thread(payRunnable);
                        payThread.start();

                        setResult(1);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("catch1", "1111");
                    }
                } catch (Exception e) {
                    Log.e("catch2", "2222");
                }

            }
        }, map);



    }
//    private void payYk(int i, String cardId) {
//        String uid = null;
//        String token = null;
//        if (SharedPreferenceUtil.getBean(getApplicationContext(), "userInfo") != null) {
//            User user = (User) SharedPreferenceUtil.getBean(getApplicationContext(), "userInfo");
//            uid = user.getUserId();
//            token = user.getToken();
//        } else {
//            uid = Param.UID;
//            token = Param.TOKEN;
//        }
//        OkHttpClient client = new OkHttpClient();
//        RequestBody body = new FormBody.Builder()
//                .add("uid", uid)
//                .add("token", token)
//                .add("orderType", String.valueOf(i))
//                .add("submitNo", cardId)
//                .build();
//
//        Request request = new Request.Builder()
//                .url(urlPayYk)
//                .post(body)
//                .build();
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String jsonDatas = response.body().string();
//                Log.e("数据", jsonDatas);
//                try {
//                    try {
//                        JSONObject jsonObject = new JSONObject(jsonDatas);
//                        String content = jsonObject.optString("content");
//                        Log.e("content", content);
//
//
//                        JSONObject jb = new JSONObject(content);
//                        orderInfo = jb.optString("orderStr");
//                        Log.e("Info", orderInfo);
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                } catch (Exception e) {
//
//                }
//            }
//        });
//        Runnable payRunnable = new Runnable() {
//
//            @Override
//            public void run() {
//                PayTask alipay = new PayTask(KsczActivity.this);
//                Map<String, String> result = alipay.payV2(orderInfo, true);
//                Log.i("msp", result.toString());
//
//                Message msg = new Message();
//                msg.what = SDK_PAY_FLAG;
//                msg.obj = result;
//                mHandler.sendMessage(msg);
//            }
//        };
//
//        Thread payThread = new Thread(payRunnable);
//        payThread.start();
//    }
}
