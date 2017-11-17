package com.bolong.bochetong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.bolong.bochetong.adapter.PopuMonthAdapter;
import com.bolong.bochetong.bean2.MsgEvent;
import com.bolong.bochetong.utils.PayUtils;
import com.bolong.bochetong.utils.ToastUtil;
import com.bolong.bochetong.view.CustomPopWindow;
import com.bolong.bochetong.view.MyDividerItemDecoration;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import static com.bolong.bochetong.activity.KsczActivity.ACTION_ALIPAY_SUCCESS;
import static com.bolong.bochetong.activity.wxapi.WXPayEntryActivity.ACTION_WXPAY_SUCCESS;
import static com.bolong.bochetong.utils.PayUtils.ACTION_ALIPAY_FAILURE;

public class YkxfActivity extends BaseActivity {

    @BindView(R.id.tv_city)
    TextView tvCity;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.tv_park)
    TextView tvPark;
    @BindView(R.id.tv_carplate)
    TextView tvCarplate;
    @BindView(R.id.tv_date)
    TextView tvDate;
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
    @BindView(R.id.bt_xufei)
    Button btXufei;
    @BindView(R.id.tv_day)
    TextView tvDay;
    private Unbinder unbind;
    private CustomPopWindow mListPopWindow;
    private String standard;
    private boolean flagZhifubao = false;
    private boolean flagWeixin = false;
    private String monthCardId;
    private String monthAmount;
    private int position;
    public static int ACTION_MONTHCARD_XUFEI = 548745745;
    @Override
    public void onBaseCreate(Bundle bundle) {
        setContentViewId(R.layout.activity_ykxf);
        unbind = ButterKnife.bind(this);

        EventBus.getDefault().register(this);

        ivZhifubao.setBackgroundResource(R.mipmap.icon_zhifubao_checked);
        ivWeixin.setBackgroundResource(R.mipmap.icon_weixin_normal);
        flagZhifubao = true;
        flagWeixin = false;

        if (getIntent() != null) {
            Intent intent = getIntent();
            tvStatus.setText(intent.getStringExtra("cardStatus"));
            tvPark.setText(intent.getStringExtra("parkName"));
            tvCarplate.setText(intent.getStringExtra("carNumber"));
            tvDate.setText(intent.getStringExtra("date"));
            standard = intent.getStringExtra("standard");
            monthCardId = intent.getStringExtra("cardId");

            tvPrice.setText(Double.valueOf(standard) + "元");
            tvDay.setText("月卡剩余"+intent.getStringExtra("days"));

            position = intent.getIntExtra("position",0);
        }
    }

    @Override
    public void initView() {
        setTitle("月卡");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        unbind.unbind();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tv_month, R.id.bt_xufei, R.id.tv_zhifubao, R.id.tv_weixin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_month:
                View contentView = LayoutInflater.from(this).inflate(R.layout.layout_popu_city, null);
                RecyclerView recyclerView = (RecyclerView) contentView.findViewById(R.id.recyclerView);
                LinearLayoutManager manager = new LinearLayoutManager(this);
                manager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(manager);
                recyclerView.addItemDecoration(new MyDividerItemDecoration(this, MyDividerItemDecoration.VERTICAL));

                PopuMonthAdapter adapter4 = new PopuMonthAdapter(getResources().getStringArray(R.array.monthArray));

                adapter4.setOnItemClickLitener(new PopuMonthAdapter.OnItemClickLitener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        tvMonth.setText(getResources().getStringArray(R.array.monthArray)[position]);
                        mListPopWindow.dissmiss();

                        tvPrice.setText(Double.valueOf(standard) * (position + 1) + "元");
                    }
                });
                recyclerView.setAdapter(adapter4);
                mListPopWindow = new CustomPopWindow.PopupWindowBuilder(this)
                        .setView(contentView)
                        .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)//显示大小
                        .enableOutsideTouchableDissmiss(true)
                        .create()
                        .showAsDropDown(tvMonth, 0, 20);
                break;
            case R.id.bt_xufei:
                recharge();
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
        }
    }



    private void recharge() {
        if (!flagZhifubao && !flagWeixin) {
            //Toast.makeText(KsczActivity.this, "请选择充值方式", Toast.LENGTH_SHORT).show();
        }
        if (flagZhifubao) {
            Log.e("支付宝", "true");
            //aliPay();
            monthAmount = tvMonth.getText().toString().replace("个月", "");
            PayUtils.aliPay(YkxfActivity.this, "", "9", "", monthCardId, monthAmount);
        }

        if (flagWeixin) {
            //wxPay();
            monthAmount = tvMonth.getText().toString().replace("个月", "");
            PayUtils.wxPay(YkxfActivity.this, "", "9", "", monthCardId, monthAmount);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateUI(MsgEvent event) {
        if (event.getAction() == ACTION_ALIPAY_SUCCESS) {
            ToastUtil.showShort(YkxfActivity.this,"支付成功");
            EventBus.getDefault().post(new MsgEvent(ACTION_MONTHCARD_XUFEI,position));
            finish();
        }
        if (event.getAction() == ACTION_ALIPAY_FAILURE) {
            ToastUtil.showShort(YkxfActivity.this,"支付失败");
        }
        if (event.getAction() == ACTION_WXPAY_SUCCESS) {
            EventBus.getDefault().post(new MsgEvent(ACTION_MONTHCARD_XUFEI,position));
            finish();
        }
    }



}
