package com.bolong.bochetong.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bolong.bochetong.utils.StatusBarUtil;

import java.util.Timer;
import java.util.TimerTask;

public class GuideActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_guide);
        StatusBarUtil.compat(this, Color.parseColor("#2c7bc0"));

        final Intent intent = new Intent(this, MainActivity.class); //下一步转向Mainctivity

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                startActivityForResult(intent,1); //执行意图
            }
        };
        timer.schedule(task, 1000 * 1); //3秒后跳转，这里根据自己需要设定时间
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {
            GuideActivity.this.finish();
        }
    }
}
