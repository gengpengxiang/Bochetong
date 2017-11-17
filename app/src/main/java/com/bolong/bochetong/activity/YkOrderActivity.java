package com.bolong.bochetong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bolong.bochetong.bean2.MsgEvent;
import com.bolong.bochetong.utils.PayUtils;
import com.bolong.bochetong.utils.ToastUtil;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.bolong.bochetong.activity.KsczActivity.ACTION_ALIPAY_SUCCESS;
import static com.bolong.bochetong.activity.wxapi.WXPayEntryActivity.ACTION_WXPAY_FAILURE;
import static com.bolong.bochetong.activity.wxapi.WXPayEntryActivity.ACTION_WXPAY_SUCCESS;
import static com.bolong.bochetong.utils.PayUtils.ACTION_ALIPAY_FAILURE;

public class YkOrderActivity extends BaseActivity {

    @BindView(R.id.tv_cityName)
    TextView tvCityName;
    @BindView(R.id.tv_carPlate)
    TextView tvCarPlate;
    @BindView(R.id.tv_park)
    TextView tvPark;
    @BindView(R.id.tv_month)
    TextView tvMonth;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.iv_zhifubao)
    ImageView ivZhifubao;
    @BindView(R.id.tv_zhifubao)
    TextView tvZhifubao;
    @BindView(R.id.iv_weixin)
    ImageView ivWeixin;
    @BindView(R.id.tv_weixin)
    TextView tvWeixin;
    @BindView(R.id.bt_confirm)
    Button btConfirm;
    @BindView(R.id.bt_cancel)
    Button btCancel;
    private Unbinder unbind;
    private boolean flagZhifubao = false;
    private boolean flagWeixin = false;
    private String orderId;
    private String monthAmount;
    public static int ACTION_BUYMONTHCARD_SUCCESS = 658966542;

    @Override
    public void onBaseCreate(Bundle bundle) {
        setContentViewId(R.layout.activity_month_order);
        unbind = ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        ivZhifubao.setBackgroundResource(R.mipmap.icon_zhifubao_checked);
        ivWeixin.setBackgroundResource(R.mipmap.icon_weixin_normal);
        flagZhifubao = true;
        flagWeixin = false;
        if(getIntent()!=null){
            Intent intent = getIntent();
            tvCityName.setText(intent.getStringExtra("cityName"));
            tvPark.setText(intent.getStringExtra("parkName"));
            tvCarPlate.setText(intent.getStringExtra("carPlate"));
            tvMonth.setText(intent.getStringExtra("month"));
            tvPrice.setText(intent.getStringExtra("price"));

            orderId = intent.getStringExtra("orderId");
        }

    }

    @Override
    public void initView() {
        setTitle("订单");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        unbind.unbind();
    }

    @OnClick({R.id.tv_zhifubao, R.id.tv_weixin, R.id.bt_confirm, R.id.bt_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
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
            case R.id.bt_confirm:
                monthAmount = tvMonth.getText().toString().replace("个月","");
                //PayUtils.aliPay(YkOrderActivity.this,"","3",orderId,"",monthAmount);
                recharge();
                break;
            case R.id.bt_cancel:
                finish();
                break;
        }
    }

    private void recharge() {
        if (!flagZhifubao && !flagWeixin) {
            //Toast.makeText(KsczActivity.this, "请选择充值方式", Toast.LENGTH_SHORT).show();
        }
        if (flagZhifubao) {
            PayUtils.aliPay(YkOrderActivity.this,"","3",orderId,"",monthAmount);
        }

        if (flagWeixin) {
            PayUtils.wxPay(YkOrderActivity.this,"","3",orderId,"",monthAmount);
        }

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateUI(MsgEvent event) {
        if (event.getAction() == ACTION_ALIPAY_SUCCESS) {
            ToastUtil.showShort(YkOrderActivity.this,"支付成功");
            EventBus.getDefault().post(new MsgEvent(ACTION_BUYMONTHCARD_SUCCESS));
            finish();
        }
        if (event.getAction() == ACTION_ALIPAY_FAILURE) {
            ToastUtil.showShort(YkOrderActivity.this,"支付失败");
        }
        if (event.getAction() == ACTION_WXPAY_SUCCESS) {
            EventBus.getDefault().post(new MsgEvent(ACTION_BUYMONTHCARD_SUCCESS));
            finish();
        }

    }

}
