package com.bolong.bochetong.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class XxtzActivity extends BaseActivity {



    @Override
    public void onBaseCreate(Bundle bundle) {
        setContentViewId(R.layout.activity_xxtz);
    }

    @Override
    public void initView() {
        setTitle("消息通知");
    }
}
