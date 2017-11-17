package com.bolong.bochetong.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bolong.bochetong.bean2.MsgEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.bolong.bochetong.push.MyReceiver.ACTION_ARREARAGE;

public class JfxqActivity extends BaseActivity {


    @BindView(R.id.tv_prompt)
    TextView tvPrompt;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    private Unbinder unbind;

    @Override
    public void onBaseCreate(Bundle bundle) {
        setContentViewId(R.layout.activity_jfxq);
        EventBus.getDefault().register(this);
        unbind = ButterKnife.bind(this);
//        tvPrompt.setVisibility(View.INVISIBLE);
    }

    @Override
    public void initView() {
        setTitle("计费");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        unbind.unbind();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateUI(MsgEvent event) {
        if (event.getAction() == ACTION_ARREARAGE) {
            tvPrompt.setVisibility(View.VISIBLE);
        }
    }
}
