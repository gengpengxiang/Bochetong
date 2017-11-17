package com.bolong.bochetong.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.bolong.bochetong.activity.KsczActivity;
import com.bolong.bochetong.activity.YkxfActivity;
import com.bolong.bochetong.bean.User;
import com.bolong.bochetong.bean2.MsgEvent;
import com.bolong.bochetong.pay.AuthResult;
import com.bolong.bochetong.pay.PayResult;
import com.bolong.bochetong.pay.WxEntity;
import com.google.gson.Gson;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.bolong.bochetong.activity.KsczActivity.ACTION_ALIPAY_SUCCESS;

public class PayUtils {

    public static final int SDK_PAY_FLAG = 1;
    public static final int SDK_AUTH_FLAG = 2;

    public static void aliPay(final Activity activity, String price, String orderType, String orderid, String monthCardId, String amount) {
        String uid = null;
        String token = null;
        if (SharedPreferenceUtil.getBean(activity, "userInfo") != null) {
            User user = (User) SharedPreferenceUtil.getBean(activity, "userInfo");
            uid = user.getUserId();
            token = user.getToken();
        } else {
            uid = Param.UID;
            token = Param.TOKEN;
        }
        Map<String, String> map = new HashMap<>();
        map.put("uid", uid);
        map.put("token", token);
        map.put("price",price);
        map.put("orderType",orderType);
        map.put("orderid",orderid);
        map.put("monthCardId",monthCardId);
        map.put("amount",amount);
        HttpUtils.post(Param.ALIPAY, new Callback() {
            public String orderInfo;

            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("onFailure", "onFailure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonDatas = response.body().string();
                Log.e("支付宝返回数据", jsonDatas);
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
                                PayTask alipay = new PayTask(activity);
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

    public static final int ACTION_ALIPAY_FAILURE = 3333354;

    static Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    if (TextUtils.equals(resultStatus, "9000")) {
                        EventBus.getDefault().post(new MsgEvent(ACTION_ALIPAY_SUCCESS));
                    } else {
                        EventBus.getDefault().post(new MsgEvent(ACTION_ALIPAY_FAILURE));
                        Log.e("支付失败","fail");
                    }
                    break;
                }
                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {

//                        Toast.makeText(YkxfActivity.this,
//                                "授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT)
//                                .show();
                    } else {
//                        Toast.makeText(YkxfActivity.this,
//                                "授权失败" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();

                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };

    public static void wxPay(final Activity activity, String price, String orderType, String orderid, String monthCardId, String amount) {
        String uid = null;
        String token = null;
        if (SharedPreferenceUtil.getBean(activity, "userInfo") != null) {
            User user = (User) SharedPreferenceUtil.getBean(activity, "userInfo");
            uid = user.getUserId();
            token = user.getToken();
        } else {
            uid = Param.UID;
            token = Param.TOKEN;
        }
        Map<String, String> map = new HashMap<>();
        map.put("uid", uid);
        map.put("token", token);
        map.put("price",price);
        map.put("orderType",orderType);
        map.put("orderid",orderid);
        map.put("monthCardId",monthCardId);
        map.put("amount",amount);
        HttpUtils.post(Param.WXPAY, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("失败", "onFailure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonDatas = response.body().string();
                Log.e("微信数据", jsonDatas);
                try {
                    JSONObject jsonObject = new JSONObject(jsonDatas);
                    String content = jsonObject.optString("content");
                    Gson gson = new Gson();

                    WxEntity wxEntity = gson.fromJson(content, WxEntity.class);
                    //EventBus.getDefault().post(new MsgEvent(ACTION_WXSENDREQ, wxEntity.getAppid()));

                    String appId = wxEntity.getAppid();
                    Log.e("appid", appId);
                    IWXAPI api = WXAPIFactory.createWXAPI(activity, appId, true);
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
                        api = WXAPIFactory.createWXAPI(activity, wxEntity.getAppid());
                        api.registerApp(wxEntity.getAppid());
                        api.sendReq(payReq);
                    } else {
                        Log.e("不空", "api");
                        api.sendReq(payReq);
                        Log.e("不空2", "API");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, map);
    }

    public static boolean isWXAppInstalledAndSupported(Context context) {
        IWXAPI msgApi = WXAPIFactory.createWXAPI(context, null);
        msgApi.registerApp("wx21388637886dba0b");

        boolean sIsWXAppInstalledAndSupported = msgApi.isWXAppInstalled()
                && msgApi.isWXAppSupportAPI();

        return sIsWXAppInstalledAndSupported;
    }

}
