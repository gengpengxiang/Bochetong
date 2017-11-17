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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bolong.bochetong.bean2.MsgEvent;
import com.bolong.bochetong.utils.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener{
    TextView titlebarTitle;
    LinearLayout baseContentLayout;
    public View contentView;
    ImageButton titlbarArrow;
    TextView titlebarRight;
    RelativeLayout layoutQuit;
    public static final int ACTION_BASE_QUIT = 11211;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_base);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            StatusBarUtil.compat(this, Color.parseColor("#000000"));
        }

        titlebarTitle = (TextView) findViewById(R.id.titlebar_title);
        baseContentLayout = (LinearLayout) findViewById(R.id.base_content_layout);
        titlbarArrow = (ImageButton) findViewById(R.id.titlbar_arrow);
        titlbarArrow.setOnClickListener(this);

        titlebarRight = (TextView) findViewById(R.id.titlebar_right);

        layoutQuit = (RelativeLayout) findViewById(R.id.layout_quit);
        layoutQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new MsgEvent(ACTION_BASE_QUIT));
            }
        });
        titlebarTitle.setSelected(true);
        onBaseCreate(savedInstanceState);
        initView();
    }

    public abstract void onBaseCreate(Bundle bundle);

    public abstract void initView();

    public void showRight(){
        if(null != layoutQuit){
            layoutQuit.setVisibility(View.VISIBLE);
        }
    }

    public void hide(){
        if(titlbarArrow!=null){
            titlbarArrow.setVisibility(View.INVISIBLE);
        }
    }

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