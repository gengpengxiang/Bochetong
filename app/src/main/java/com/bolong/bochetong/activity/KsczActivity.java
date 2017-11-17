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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bolong.bochetong.bean.User;
import com.bolong.bochetong.bean2.MsgEvent;
import com.bolong.bochetong.bean2.Wallet;
import com.bolong.bochetong.pay.WxEntity;
import com.bolong.bochetong.utils.HttpUtils;
import com.bolong.bochetong.utils.Param;
import com.bolong.bochetong.utils.PayUtils;
import com.bolong.bochetong.utils.SharedPreferenceUtil;
import com.bolong.bochetong.utils.SoftKeyboardStateHelperUtil;
import com.bolong.bochetong.utils.ToastUtil;
import com.bolong.bochetong.view.CustomPopDialog;
import com.google.gson.Gson;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
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
import static com.bolong.bochetong.activity.wxapi.WXPayEntryActivity.ACTION_WXPAY_SUCCESS;
import static com.bolong.bochetong.utils.PayUtils.ACTION_ALIPAY_FAILURE;

public class KsczActivity extends BaseActivity {

    @BindView(R.id.tv_else)
    TextView tvElse;
    @BindView(R.id.layout_else)
    RelativeLayout layoutElse;
    @BindView(R.id.tv_zhifubao)
    TextView tvZhifubao;
    @BindView(R.id.tv_weixin)
    TextView tvWeixin;
    @BindView(R.id.iv_zhifubao)
    ImageView ivZhifubao;
    @BindView(R.id.iv_weixin)
    ImageView ivWeixin;
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
    private String renewalFee;
    private String orderid;
    private String yuE;
    public static final int ACTION_WXSENDREQ = 333;
    public static final int ACTION_ALIPAY_SUCCESS = 3333;
    private WxEntity wxEntity;
    private String symbol;

    @Override
    public void onBaseCreate(Bundle bundle) {
        setContentViewId(R.layout.activity_kscz);
        EventBus.getDefault().register(this);
        flagZhifubao = false;
        flagWeixin = false;
        unbinder = ButterKnife.bind(this);
        SharedPreferenceUtil.putString("jine", "20");
        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                    hint = new SpannableString("请输入整数金额（1-1000）");
                    et.setHint(hint);
                    et.setCursorVisible(true);
                    SharedPreferenceUtil.removeString("jine");

                } else {

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

        //getYuE();
        setContent();
    }

    private void setContent() {
        Intent intent = getIntent();
        shoudPay = intent.getStringExtra("shoudPay");
        monthlyPrice = intent.getStringExtra("monthlyPrice");
        renewalFee = intent.getStringExtra("renewalFee");
        cardId = intent.getStringExtra("cardId");
        orderid = intent.getStringExtra("orderid");
        yuE = intent.getStringExtra("money");

        tvYue.setText(yuE);
        if (shoudPay != null) {
            tvElse.setText(shoudPay + "元");
            layoutElse.setVisibility(View.VISIBLE);
            frameKsczYue.setVisibility(View.INVISIBLE);
            relativeKsczJine.setVisibility(View.INVISIBLE);
            linearKsczEdittext.setVisibility(View.INVISIBLE);
        }
        if (cardId != null) {

            tvElse.setText(renewalFee + "元");
            layoutElse.setVisibility(View.VISIBLE);
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
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonDatas = response.body().string();
                Log.e("钱包明细", jsonDatas);
                try {
                    try {
                        Gson gson0 = new Gson();
                        Wallet wallet = gson0.fromJson(jsonDatas, Wallet.class);
                        Wallet.ContentBean content0 = wallet.getContent();
                        final String accountBalance = content0.getAccountBalance();

                        JSONObject jsonObject = new JSONObject(jsonDatas);
                        String content = jsonObject.optString("content");
                        JSONObject jb = new JSONObject(content);

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
        if (getIntent() == null) {
            setTitle("快速充值");
        }else {
            Intent intent = getIntent();
            symbol = intent.getStringExtra("symbol");
            if (symbol.equals("yearCard")) {
                setTitle("年卡续费");
            }
            if(symbol.equals("normal")){
                setTitle("快速充值");
            }
            if(symbol.equals("arrearage")){
                setTitle("欠费补缴");
            }
        }
    }


    @OnClick({R.id.bt_kscz_twenty, R.id.bt_kscz_fifth, R.id.bt_kscz_hundred, R.id.tv_zhifubao, R.id.tv_weixin, R.id.bt_chongzhi})
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

                break;
            case R.id.bt_kscz_hundred:
                clearEditText();
                btKsczTwenty.setBackgroundResource(R.mipmap.bg_kscz_jine_normal);
                btKsczFifth.setBackgroundResource(R.mipmap.bg_kscz_jine_normal);
                btKsczHundred.setBackgroundResource(R.mipmap.bg_kscz_jine_selected);
                SharedPreferenceUtil.putString("jine", "100");

                break;
            case R.id.tv_zhifubao:
                tvWeixin.setTextColor(getResources().getColor(R.color.deepgray));
                tvZhifubao.setTextColor(getResources().getColor(R.color.babyblack));

                ivZhifubao.setBackgroundResource(R.mipmap.icon_zhifubao_checked);
                ivWeixin.setBackgroundResource(R.mipmap.icon_weixin_normal);
                flagZhifubao = true;
                flagWeixin = false;
                break;
            case R.id.tv_weixin:
                if (PayUtils.isWXAppInstalledAndSupported(getApplicationContext())) {

                    tvZhifubao.setTextColor(getResources().getColor(R.color.deepgray));
                    tvWeixin.setTextColor(getResources().getColor(R.color.babyblack));

                    ivWeixin.setBackgroundResource(R.mipmap.icon_weixin_checked);
                    ivZhifubao.setBackgroundResource(R.mipmap.icon_zhifubao_normal);
                    flagWeixin = true;
                    flagZhifubao = false;
                } else {
                    ToastUtil.showShort(this, "您未安装微信客户端");
                }
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
            if (!TextUtils.isEmpty(et.getText())) {
                SharedPreferenceUtil.putString("jine", et.getText().toString());
            }
            //aliPay();
            String price = SharedPreferenceUtil.getString("jine", "0");

            if (price != null && shoudPay == null && cardId == null) {
                PayUtils.aliPay(KsczActivity.this,price,"1","","","");
            }
            if (cardId != null) {
                PayUtils.aliPay(KsczActivity.this,"","4","",cardId,"");
            }
            if (shoudPay != null) {
                PayUtils.aliPay(KsczActivity.this,"","2",orderid,"","");
            }


        }

        if (flagWeixin) {
            if (TextUtils.isEmpty(et.getText()) == false) {
                SharedPreferenceUtil.putString("jine", et.getText().toString());
            }
            //wxPay();

            String price = SharedPreferenceUtil.getString("jine", "0");
            if (price != null && shoudPay == null && cardId == null) {
                PayUtils.wxPay(KsczActivity.this,price,"1","","","");
            }
            if (cardId != null) {
                PayUtils.wxPay(KsczActivity.this,"","4","",cardId,"");
            }
            if (shoudPay != null) {
                PayUtils.wxPay(KsczActivity.this,"","2",orderid,"","");
            }
        }

    }


    private void clearEditText() {
        et.clearFocus();
        et.setText("");
        et.setHint("请输入其他金额");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
    }

    public void showDialog() {
        CustomPopDialog.Builder dialogBuild = new CustomPopDialog.Builder(this);
        CustomPopDialog dialog = dialogBuild.create(R.layout.layout_dialog_kscz);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateUI(MsgEvent event) {
        if (event.getAction() == ACTION_WXPAY_SUCCESS) {
            showDialog();
            getYuE();
        }
        if (event.getAction() == ACTION_WXSENDREQ) {
            String appId = event.getStr();
            Log.e("appid", appId);
            IWXAPI api = WXAPIFactory.createWXAPI(KsczActivity.this, appId, true);
            api.registerApp(appId);
            PayReq payReq = new PayReq();
            payReq.appId = wxEntity.getAppid();
            payReq.partnerId = wxEntity.getPartnerid();
            payReq.prepayId = wxEntity.getPrepayid();
            payReq.packageValue = wxEntity.getPackageX();
            payReq.nonceStr = wxEntity.getNoncestr();
            payReq.timeStamp = wxEntity.getTimestamp();
            payReq.sign = wxEntity.getSign();
            //
            if (api == null) {
                Log.e("空", "api");
                api = WXAPIFactory.createWXAPI(KsczActivity.this, wxEntity.getAppid());
                api.registerApp(wxEntity.getAppid());
                api.sendReq(payReq);
            } else {
                Log.e("不空", "api");
                api.sendReq(payReq);
                Log.e("不空2", "API");
            }
        }
        if (event.getAction() == ACTION_ALIPAY_SUCCESS) {
            showDialog();
            getYuE();
        }
        if (event.getAction() == ACTION_ALIPAY_FAILURE) {
            ToastUtil.showShort(KsczActivity.this,"支付失败");
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
