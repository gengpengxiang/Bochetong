package com.bolong.bochetong.activity;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.bolong.bochetong.utils.NetBroadcastReceiver;
import com.bolong.bochetong.utils.NetUtil;
import com.bolong.bochetong.utils.StatusBarUtil;


public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener{
    TextView titlebarTitle;
    LinearLayout baseContentLayout;
    public View contentView;
    ImageButton titlbarArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_base);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
//            StatusBarUtil.compat(this, Color.parseColor("#2081d1"));
//        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            StatusBarUtil.compat(this, Color.parseColor("#2081d1"));
        }


        titlebarTitle = (TextView) findViewById(R.id.titlebar_title);
        baseContentLayout = (LinearLayout) findViewById(R.id.base_content_layout);
        titlbarArrow = (ImageButton) findViewById(R.id.titlbar_arrow);
        titlbarArrow.setOnClickListener(this);
        onBaseCreate(savedInstanceState);
        initView();



    }

    //初始化界面
    public abstract void onBaseCreate(Bundle bundle);

    //初始化数据
    public abstract void initView();

    public void setTitle(String title) {
        if (null != titlebarTitle) {
            titlebarTitle.setText(title);
        }
    }

    public void setContentViewId(int layoutId) {
        contentView = getLayoutInflater().inflate(layoutId, null);
        if (baseContentLayout.getChildCount() > 0) {
            baseContentLayout.removeAllViews();
        }
        if (contentView != null) {
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT);
            baseContentLayout.addView(contentView, params);
        }
    }

    @Override
    public void onClick(View v) {
        finish();
    }

}

