package com.bolong.bochetong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bolong.bochetong.bean.User;
import com.bolong.bochetong.bean2.Bill;
import com.bolong.bochetong.bean2.MsgEvent;
import com.bolong.bochetong.bean2.Wallet;
import com.bolong.bochetong.utils.HttpUtils;
import com.bolong.bochetong.utils.Param;
import com.bolong.bochetong.utils.SharedPreferenceUtil;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.bolong.bochetong.activity.KsczActivity.ACTION_ALIPAY_SUCCESS;
import static com.bolong.bochetong.activity.wxapi.WXPayEntryActivity.ACTION_WXPAY_SUCCESS;

public class WdqbActivity extends BaseActivity {


    @BindView(R.id.tv_yue)
    TextView tvYue;
    @BindView(R.id.bt_cz)
    TextView btCz;
    @BindView(R.id.bt_mx)
    TextView btMx;
    private Unbinder unbind;
    public static final int ACTION_NOTECASE = 200;
    private String accountBalance;
    private List<Bill.PagenationBean.DataBean> billsList = new ArrayList<>();

    @Override
    public void onBaseCreate(Bundle bundle) {
        setContentViewId(R.layout.activity_wdqb);
        EventBus.getDefault().register(this);
        unbind = ButterKnife.bind(this);
        getNoteCase();
    }

    @Override
    public void initView() {
        setTitle("我的钱包");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        unbind.unbind();
    }

    @OnClick({R.id.bt_cz, R.id.bt_mx})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_cz:
                skip(KsczActivity.class);
                break;
            case R.id.bt_mx:
                skip(MxActivity.class);
                break;
        }
    }

    /**
     * 获取账单明细列表
     */
    private void getNoteCase() {
        String uid = null;
        String token = null;
        if (SharedPreferenceUtil.getBean(getApplicationContext(), "userInfo") != null) {
            User user = (User) SharedPreferenceUtil.getBean(getApplicationContext(), "userInfo");
            uid = user.getUserId();
            token = user.getToken();
        } else {
            uid = Param.UID;
            token = Param.TOKEN;
        }
        Map<String, String> map = new HashMap<>();
        map.put("uid", uid);
        map.put("token", token);
        //map.put("pageNo", "2");
        HttpUtils.post(Param.NOTECASE, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonDatas = response.body().string();
                Log.e("账单明细", jsonDatas);
                try {
                    try {
                        //
                        Gson gson0 = new Gson();
                        Wallet wallet = gson0.fromJson(jsonDatas, Wallet.class);
                        Wallet.ContentBean content0 = wallet.getContent();
                        accountBalance = content0.getAccountBalance();
                        //
                        JSONObject jsonObject = new JSONObject(jsonDatas);
                        String content = jsonObject.optString("content");
                        Log.e("content",content);
                        JSONObject jb = new JSONObject(content);
                        //余额
                        //accountBalance = jb.optString("accountBalance");
                        Log.e("accountBalance",content0.getAccountBalance());
                        String pagenation = jb.optString("pagenation");

                        Gson gson = new Gson();
                        Bill.PagenationBean pagenationBean = gson.fromJson(pagenation, Bill.PagenationBean.class);
                        List<Bill.PagenationBean.DataBean> billsList = pagenationBean.getData();
                        EventBus.getDefault().post(new MsgEvent(ACTION_NOTECASE,accountBalance,billsList));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {

                }
            }
        }, map);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateUI(MsgEvent event) {
        if (event.getAction() == ACTION_NOTECASE) {
            tvYue.setText(accountBalance);
        }
        if (event.getAction() == ACTION_WXPAY_SUCCESS) {
            getNoteCase();
            tvYue.setText(accountBalance);
        }
        if(event.getAction() == ACTION_ALIPAY_SUCCESS){
            getNoteCase();
            tvYue.setText(accountBalance);
        }
    }


    private void skip(Class c) {
        Intent intent = new Intent();
        intent.setClass(WdqbActivity.this, c);
        intent.putExtra("symbol", "normal");
        intent.putExtra("money",tvYue.getText().toString());
        startActivity(intent);
    }
}
