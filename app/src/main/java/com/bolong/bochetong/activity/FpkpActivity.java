package com.bolong.bochetong.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.bolong.bochetong.bean.User;
import com.bolong.bochetong.bean2.MsgEvent;
import com.bolong.bochetong.utils.HttpUtils;
import com.bolong.bochetong.utils.Param;
import com.bolong.bochetong.utils.SharedPreferenceUtil;
import com.bolong.bochetong.view.CustomPopDialog;
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
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class FpkpActivity extends BaseActivity {

    @BindView(R.id.tv_success)
    TextView tvSuccess;
    private TextView tvCouponType, tvCouponHeader, tvCouponPrice;
    private String type, header, price, remark, ids;
    private Button btnConfirm, btnCancel;
    private CustomPopDialog dialog;
    private CustomPopDialog.Builder dialogBuild;
    private Unbinder unbind;
    public static final int ACTION_BUILDINVOICE = 1000;
    public static final int ACTION_SJKPSUCCESS = 1005;
    private String status;

    @Override
    public void onBaseCreate(Bundle bundle) {
        setContentViewId(R.layout.activity_fpkp);
        unbind = ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        Intent intent = getIntent();
        if (intent != null) {
            type = intent.getStringExtra("type");
            header = intent.getStringExtra("header");
            price = intent.getStringExtra("price");
            remark = intent.getStringExtra("remark");
            ids = intent.getStringExtra("ids");
            Log.e("ids===", ids);
            dialogBuild = new CustomPopDialog.Builder(this);
            dialog = dialogBuild.create(R.layout.layout_dialog_fp, 0.4, 0.7);
            dialog.setCanceledOnTouchOutside(false);

            tvCouponHeader = (TextView) dialog.findViewById(R.id.tv_couponHeader);
            tvCouponPrice = (TextView) dialog.findViewById(R.id.tv_couponPrice);
            tvCouponHeader.setText(header);
            tvCouponPrice.setText(price+"元");
            btnConfirm = (Button) findViewById(R.id.btn_confirm);
            btnCancel = (Button) findViewById(R.id.btn_cancel);
            //解决dialog屏蔽返回键
            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        FpkpActivity.this.finish();
                        return true;
                    } else {
                        return false;
                    }
                }
            });

            dialog.show();


        }

    }

    @Override
    public void initView() {
        setTitle("我要发票");

    }

    public void doClick(View view) {
        switch (view.getId()) {
            case R.id.btn_confirm:
                buildInvoice();
                dialog.dismiss();
                break;
            case R.id.btn_cancel:
                dialog.dismiss();
                finish();
                break;
        }

    }

    /**
     * 申请开票
     */
    private void buildInvoice() {
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
        map.put("flag", type);
        map.put("header", header);
        map.put("amount", price);
        map.put("recordIds", ids);
        map.put("remarks", remark);
        HttpUtils.post(Param.BUILDINVOICE, new Callback() {
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
                        JSONObject jb = new JSONObject(content);
                        status = jb.optString("status");
                        EventBus.getDefault().post(new MsgEvent(ACTION_BUILDINVOICE, status));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {

                }
            }
        }, map);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        unbind.unbind();
        dialog.cancel();
        dialog = null;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateUI(MsgEvent event) {
        if (event.getAction() == ACTION_BUILDINVOICE) {
            if (event.getStr().equals("1")) {
                tvSuccess.setVisibility(View.VISIBLE);
                EventBus.getDefault().post(new MsgEvent(ACTION_SJKPSUCCESS));
            } else {
                Toast.makeText(FpkpActivity.this, "开票失败", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
