package com.bolong.bochetong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.bolong.bochetong.bean2.MsgEvent;
import com.bolong.bochetong.utils.SharedPreferenceUtil;
import com.bolong.bochetong.view.CustomPopDialog;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import static com.bolong.bochetong.activity.FpkpActivity.ACTION_SJKPSUCCESS;

public class FpxqActivity extends BaseActivity {

    @BindView(R.id.bt_submit)
    Button btSubmit;
    @BindView(R.id.et_header)
    EditText etHeader;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.et_sbh)
    EditText etSbh;
    @BindView(R.id.et_remark)
    EditText etRemark;
    @BindView(R.id.tv_couponType)
    TextView tvCouponType;

    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;
    @BindView(R.id.radioButton1)
    RadioButton radioButton1;
    @BindView(R.id.radioButton2)
    RadioButton radioButton2;
    private Unbinder unbind;
    private String price, ids, header, number, remark, type;
    private TextView tvKnow;

    @Override
    public void onBaseCreate(Bundle bundle) {
        setContentViewId(R.layout.activity_fpxq);
        unbind = ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        Intent intent = getIntent();
        if (intent != null) {
            ids = intent.getStringExtra("ids");
            price = intent.getStringExtra("price");
            tvPrice.setText(price + "元");
        }

        if(SharedPreferenceUtil.getBoolean("isFirst",true)){
            showDialog();
        }

    }

    /**
     * 开票须知
     */
    private void showDialog() {

        CustomPopDialog.Builder dialogBuild = new CustomPopDialog.Builder(this);
        final CustomPopDialog dialog = dialogBuild.create(R.layout.layout_dialog_kpxz, 0.35, 0.7);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        tvKnow = (TextView) dialog.findViewById(R.id.bt_know);
        tvKnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null) {
                    SharedPreferenceUtil.putBoolean("isFirst",false);
                    dialog.dismiss();
                }
            }
        });
    }

    @Override
    public void initView() {

        setTitle("我要发票");

        etHeader.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    etHeader.setCursorVisible(true);

                } else {
                    etHeader.setCursorVisible(false);
                }
            }
        });
        etSbh.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    etSbh.setCursorVisible(true);

                } else {
                    etSbh.setCursorVisible(false);
                }
            }
        });
        etRemark.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    etRemark.setCursorVisible(true);

                } else {
                    etRemark.setCursorVisible(false);
                }
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (radioButton1.getId() == checkedId) {
                    etHeader.setText("个人");
                }
                if (radioButton2.getId() == checkedId) {
                    etHeader.setText("公司");
                }

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        unbind.unbind();
    }

    @OnClick(R.id.bt_submit)
    public void onViewClicked() {

        header = etHeader.getText().toString();
        number = etSbh.getText().toString();
        remark = etRemark.getText().toString();
        if (radioButton1.isChecked()) {
            type = "0";
        }
        if (radioButton2.isChecked()) {
            type = "1";
            if (TextUtils.isEmpty(etSbh.getText())) {
                Toast.makeText(FpxqActivity.this, "纳税人识别号未填写", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        Intent intent = new Intent();
        intent.setClass(FpxqActivity.this, FpkpActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("header", header);
        intent.putExtra("price", price);
        intent.putExtra("number", number);
        intent.putExtra("remark", remark);
        intent.putExtra("ids", ids);
        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateUI(MsgEvent event) {
        if (event.getAction() == ACTION_SJKPSUCCESS) {
            finish();
        }
    }

}
