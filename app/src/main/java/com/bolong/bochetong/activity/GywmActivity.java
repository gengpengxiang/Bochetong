package com.bolong.bochetong.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class GywmActivity extends BaseActivity {


    @Override
    public void onBaseCreate(Bundle bundle) {
        setContentViewId(R.layout.activity_gywm);
    }

    @Override
    public void initView() {
        setTitle("关于我们");
    }
}
