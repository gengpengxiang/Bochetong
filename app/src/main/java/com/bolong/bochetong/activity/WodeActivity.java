package com.bolong.bochetong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bolong.bochetong.bean.User;
import com.bolong.bochetong.utils.SharedPreferenceUtil;
import com.bolong.bochetong.view.CustomPopDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class WodeActivity extends BaseActivity {

    private Unbinder unbinder;
    @BindView(R.id.tv_wode_login)
    TextView tvWodeLogin;
    @BindView(R.id.bt_wode_touxiang)
    ImageView btWodeTouxiang;
    @BindView(R.id.item_xxtz)
    RelativeLayout itemXxtz;
    @BindView(R.id.item_tsjy)
    RelativeLayout itemTsjy;
    @BindView(R.id.item_gywm)
    RelativeLayout itemGywm;
    @BindView(R.id.item_tcdl)
    RelativeLayout itemTcdl;
    private final static int REQUEST_CODE = 1;
    private CustomPopDialog dialog;
    private Button btnConfirm;

    private Button btnCancel;

    @Override
    public void onBaseCreate(Bundle bundle) {
        setContentViewId(R.layout.activity_wode);

        unbinder = ButterKnife.bind(this);
        showUserInfos();

    }

    @Override
    public void initView() {

        setTitle("我的");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @OnClick({R.id.bt_wode_touxiang, R.id.item_xxtz, R.id.item_tsjy, R.id.item_gywm, R.id.item_tcdl})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_wode_touxiang:
                Intent intent = new Intent();
                intent.setClass(WodeActivity.this, LoginActivity.class);
                //startActivity(intent);
                startActivityForResult(intent, REQUEST_CODE);
                break;
            case R.id.item_xxtz:
                Intent intent1 = new Intent();
                intent1.setClass(WodeActivity.this, XxtzActivity.class);
                startActivity(intent1);
                break;
            case R.id.item_tsjy:
                Intent intent2 = new Intent();
                intent2.setClass(WodeActivity.this, TsjyActivity.class);
                startActivity(intent2);
                break;
            case R.id.item_gywm:
                Intent intent3 = new Intent();
                intent3.setClass(WodeActivity.this, GywmActivity.class);
                startActivity(intent3);
                break;
            case R.id.item_tcdl:
                quit();
                break;
        }
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

    public void doClick(View view) {
        switch (view.getId()) {
            case R.id.btn_confirm:
                //退出登录
                tvWodeLogin.setText("点击登录");
                btWodeTouxiang.setEnabled(true);
                SharedPreferenceUtil.removeString("userInfo");
                dialog.dismiss();
                break;
            case R.id.btn_cancel:
                dialog.dismiss();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == LoginActivity.RESULT_CODE) {
                Log.e("onActivityResult", "onActivityResult");
                showUserInfos();
            }
        }
    }

    private void showUserInfos() {
        if (SharedPreferenceUtil.getBean(getApplicationContext(), "userInfo") != null) {
            User user = (User) SharedPreferenceUtil.getBean(getApplicationContext(), "userInfo");
            tvWodeLogin.setText(user.getUserPhone());
            btWodeTouxiang.setEnabled(false);
            Log.e("用户数据设置成功", user.getUserPhone());
        } else {
            tvWodeLogin.setText("点击登录");
            btWodeTouxiang.setEnabled(true);
        }
    }

}
