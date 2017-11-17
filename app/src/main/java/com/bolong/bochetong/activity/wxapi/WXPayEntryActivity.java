package com.bolong.bochetong.activity.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bolong.bochetong.activity.KsczActivity;
import com.bolong.bochetong.activity.R;
import com.bolong.bochetong.bean2.MsgEvent;
import com.bolong.bochetong.utils.Param;
import com.bolong.bochetong.utils.ToastUtil;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Unbinder;

import static com.bolong.bochetong.activity.KsczActivity.ACTION_WXSENDREQ;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
//    @BindView(R.id.tv_success)
//    TextView tvSuccess;

    private IWXAPI api;
    public static final int ACTION_WXPAY_SUCCESS = 2001;
    public static final int ACTION_WXPAY_FAILURE = 2002;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_fpkp);
        //       unbind = ButterKnife.bind(this);

        api = WXAPIFactory.createWXAPI(this, Param.WX_APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp resp) {
        Log.e("onResp", "==" + resp.errCode);
        Log.e("onResp2", "==" + resp.errStr);
        Log.e("onResp3", "==" + resp.getType() + "");

        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (resp.errCode == 0) {
                Toast.makeText(WXPayEntryActivity.this, "支付成功", Toast.LENGTH_LONG).show();
//                tvSuccess.setVisibility(View.VISIBLE);
//                tvSuccess.setText("支付成功");
                EventBus.getDefault().post(new MsgEvent(ACTION_WXPAY_SUCCESS));
            } else {
//                tvSuccess.setVisibility(View.VISIBLE);
//                tvSuccess.setText("支付失败");
                Toast.makeText(WXPayEntryActivity.this, "支付失败", Toast.LENGTH_LONG).show();
                EventBus.getDefault().post(new MsgEvent(ACTION_WXPAY_FAILURE));
            }
            finish();
        }

       /* switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                //分享成功
                ToastUtil.showShort(WXPayEntryActivity.this,"success");
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                //分享取消
                ToastUtil.showShort(WXPayEntryActivity.this,"cancel");
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                //分享拒绝
                break;
        }*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //       unbind.unbind();

    }

   /* @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateUI(MsgEvent event) {
        if (event.getAction() == ACTION_WXSENDREQ) {
            appId = event.getStr();
            Log.e("回调的appid", appId);
        }
    }*/
}