package com.bolong.bochetong.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bolong.bochetong.bean.User;
import com.bolong.bochetong.bean2.MsgEvent;
import com.bolong.bochetong.utils.Param;
import com.bolong.bochetong.utils.SharedPreferenceUtil;
import com.bolong.bochetong.utils.ToastUtil;
import com.bolong.bochetong.view.CustomPopDialog;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.images.ImageManager;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.open.utils.Util;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.bolong.bochetong.activity.ChangeActivity.ACTION_EDITPHONE_SUCCESS;

public class WdActivity extends BaseActivity {

    @BindView(R.id.tv_phoneNumber)
    TextView tvPhoneNumber;
    @BindView(R.id.bt_gywm)
    TextView btGywm;
    @BindView(R.id.bt_tsjy)
    TextView btTsjy;
    @BindView(R.id.bt_cjwt)
    TextView btCjwt;
    @BindView(R.id.bt_wdqb)
    TextView btWdqb;
    @BindView(R.id.bt_zssj)
    TextView btZssj;
    @BindView(R.id.bt_qfbj)
    TextView btQfbj;
    @BindView(R.id.bt_wdcl)
    TextView btWdcl;
    @BindView(R.id.bt_tcjl)
    TextView btTcjl;
    @BindView(R.id.bt_wyfp)
    TextView btWyfp;
    @BindView(R.id.bt_tz)
    TextView btTz;
    @BindView(R.id.bt_ps)
    TextView btPs;
    @BindView(R.id.bt_xc)
    TextView btXc;
    @BindView(R.id.bt_nk)
    TextView btNk;
    @BindView(R.id.bt_cx)
    TextView btCx;
    @BindView(R.id.bt_fx)
    TextView btFx;
    private Unbinder unbind;
    private CustomPopDialog dialog, dialogShare;
    private Button btnConfirm;
    private Button btnCancel;
    public static final int ACTION_QUIT = 4444;
    private Tencent mTencent;
    private IWXAPI api;

    private GoogleApiClient client;

    @Override
    public void onBaseCreate(Bundle bundle) {
        setContentViewId(R.layout.activity_wd);
        unbind = ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        showUserInfos();
    }

    @Override
    public void initView() {
        showRight();
        setTitle("我的");
        api = WXAPIFactory.createWXAPI(this, Param.WX_APP_ID, false);
        api.registerApp(Param.WX_APP_ID);
    }


    private void share() {
        CustomPopDialog.Builder dialogBuild = new CustomPopDialog.Builder(this);
        dialogShare = dialogBuild.create(R.layout.layout_dialog_share, 0.1, 0.85, Gravity.BOTTOM);
        dialogShare.setCanceledOnTouchOutside(false);
        dialogShare.show();
        dialogShare.setCanceledOnTouchOutside(true);
    }

    private void skip(Class c) {
        Intent intent = new Intent();
        intent.setClass(WdActivity.this, c);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        unbind.unbind();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == LoginActivity.RESULT_CODE) {
                showUserInfos();
            }
        }

        if (null != mTencent)
            mTencent.onActivityResult(requestCode, resultCode, data);
    }


    private void quit() {
        CustomPopDialog.Builder dialogBuild = new CustomPopDialog.Builder(this);
        //存在登录用户
        if (SharedPreferenceUtil.getBean(getApplicationContext(), "userInfo") != null) {
            dialog = dialogBuild.create(R.layout.layout_dialog_quit);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            btnConfirm = (Button) findViewById(R.id.btn_confirm);
            btnCancel = (Button) findViewById(R.id.btn_cancel);
        } else {
            dialog = dialogBuild.create(R.layout.layout_dialog_quiterror);
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    dialog.dismiss();
                }
            }, 1000);
        }

    }


    private void qqShare() {
        mTencent = Tencent.createInstance("1106110467", this.getApplicationContext());
        Bundle bundle = new Bundle();
        bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, "http://www.blpark.com/bolongshare");
        bundle.putString(QQShare.SHARE_TO_QQ_TITLE, "智慧停车-铂车通");
        bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,"http://api.open.qq.com/tfs/show_img.php?appid=1106110467&uuid=512x512.png%7C1048576%7C1495683250.6404");
        bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, "自动缴费，告别找零，注册即享好礼，马上领取属于你的停车时间吧!");
        bundle.putString(QQShare.SHARE_TO_QQ_APP_NAME, "铂车通" + "1106110467");

        mTencent.shareToQQ(this, bundle, new IUiListener() {
            @Override
            public void onComplete(Object o) {
            }
            @Override
            public void onError(UiError uiError) {
            }
            @Override
            public void onCancel() {
            }
        });
    }

    private void weiXinShare(int type) {

        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = "http://www.blpark.com/bolongshare";
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = "智慧停车-铂车通";
        msg.description = "自动缴费，告别找零，注册即享好礼，马上领取属于你的停车时间吧！";

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_logo);
        msg.thumbData = com.bolong.bochetong.activity.wxapi.Util.bmpToByteArray(bitmap,true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = type;
        api.sendReq(req);
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    private void showUserInfos() {
        if (SharedPreferenceUtil.getBean(getApplicationContext(), "userInfo") != null) {
            User user = (User) SharedPreferenceUtil.getBean(getApplicationContext(), "userInfo");
            tvPhoneNumber.setText(user.getUserPhone());

            Log.e("用户数据设置成功", "uid="+user.getUserId() + "======token=" + user.getToken());
        } else {

        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateUI(MsgEvent event) {
        if (event.getAction() == ACTION_QUIT) {
            finish();
        }
        if (event.getAction() == ACTION_EDITPHONE_SUCCESS) {
            finish();
        }
        if (event.getAction() == ACTION_BASE_QUIT) {
            quit();
        }
    }

    public void doClick(View view) {
        switch (view.getId()) {
            case R.id.btn_confirm:
                //退出登录
                SharedPreferenceUtil.removeString("userInfo");
                dialog.dismiss();
                //setResult(1);
                finish();
                EventBus.getDefault().post(new MsgEvent(ACTION_QUIT));
                break;
            case R.id.btn_cancel:
                dialog.dismiss();
                break;
            //分享添加
            case R.id.bt_weixin:
                if (api.isWXAppInstalled()) {
                    weiXinShare(0);
                    dialogShare.dismiss();
                } else {
                    ToastUtil.showShort(this, "您未安装微信客户端");
                }
                break;
            case R.id.bt_qq:
                qqShare();
                dialogShare.dismiss();
                break;
            case R.id.bt_friend:
                if (api.isWXAppInstalled()) {
                    weiXinShare(1);
                    dialogShare.dismiss();
                } else {
                    ToastUtil.showShort(this, "您未安装微信客户端");
                }
                break;
        }
    }


    @OnClick({R.id.bt_gywm, R.id.bt_tsjy, R.id.bt_cjwt, R.id.bt_wdqb, R.id.bt_zssj, R.id.bt_qfbj, R.id.bt_wdcl, R.id.bt_tcjl, R.id.bt_wyfp, R.id.bt_tz, R.id.bt_ps, R.id.bt_xc, R.id.bt_nk, R.id.bt_cx, R.id.bt_fx})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_gywm:
                skip(GywmActivity.class);
                break;
            case R.id.bt_tsjy:
                skip(TsjyActivity.class);
                break;
            case R.id.bt_cjwt:
                skip(CjwtActivity.class);
                break;
            case R.id.bt_wdqb:
                skip(WdqbActivity.class);
                break;
            case R.id.bt_zssj:
                skip(ZssjActivity.class);
                break;
            case R.id.bt_qfbj:
                skip(QfbjActivity.class);
                break;
            case R.id.bt_wdcl:
                skip(BdcpActivity.class);
                break;
            case R.id.bt_tcjl:
                skip(TcjlActivity.class);
                break;
            case R.id.bt_wyfp:
                skip(WyfpActivity.class);
                break;
            case R.id.bt_tz:
                skip(XxtzActivity.class);
                break;
            case R.id.bt_ps:
                ToastUtil.showShort(WdActivity.this, "敬请期待");
                break;
            case R.id.bt_xc:
                ToastUtil.showShort(WdActivity.this, "敬请期待");
                break;
            case R.id.bt_nk:
                skip(NkActivity.class);
                break;
            case R.id.bt_cx:
//                Intent intent = new Intent();
//                intent.setClass(WdActivity.this,WebActivity.class);
//                intent.putExtra("url",Param.BAOXIAN);
//                startActivity(intent);
//                ToastUtil.showShort(WdActivity.this, "敬请期待");
                skip(YkActivity.class);
                break;
            case R.id.bt_fx:
                share();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Wd Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
