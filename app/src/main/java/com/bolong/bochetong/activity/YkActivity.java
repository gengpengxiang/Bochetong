package com.bolong.bochetong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bolong.bochetong.adapter.YkAdapter;
import com.bolong.bochetong.bean.User;
import com.bolong.bochetong.bean2.MonthCard;
import com.bolong.bochetong.bean2.MsgEvent;
import com.bolong.bochetong.fragment.YkFragment;
import com.bolong.bochetong.utils.HttpUtils;
import com.bolong.bochetong.utils.Param;
import com.bolong.bochetong.utils.SharedPreferenceUtil;
import com.bolong.bochetong.utils.ToastUtil;
import com.bolong.bochetong.view.MyViewPager;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
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
import static com.bolong.bochetong.activity.BdcpActivity2.ACTION_ADDMONTHCAR_SUCCESS;
import static com.bolong.bochetong.activity.BdcpActivity2.ACTION_ADDYK_SUCCESS;
import static com.bolong.bochetong.activity.YkOrderActivity.ACTION_BUYMONTHCARD_SUCCESS;
import static com.bolong.bochetong.activity.YkxfActivity.ACTION_MONTHCARD_XUFEI;
import static com.bolong.bochetong.fragment.NkFragment.ACTION_MONTHCARD_UPDATE;

public class YkActivity extends BaseActivity {


    @BindView(R.id.viewPager)
    MyViewPager viewPager;
    @BindView(R.id.tv_indicator)
    TextView tvIndicator;
    @BindView(R.id.bt_kaika)
    Button btKaika;
    private Unbinder unbind;
    private int ACTION_REALMONTHCARDS = 79856547;
    private int ACTION_REALMONTHCARDS_FAILURE = 796956547;
    List<Fragment> list = new ArrayList<>();
    List<MonthCard.MonthCardBean> cardList = new ArrayList<>();
    List<MonthCard.MonthCardBean> newList = new ArrayList<>();
    private MonthCard monthCard;

    private int position=1000;

    @Override
    public void onBaseCreate(Bundle bundle) {
        setContentViewId(R.layout.activity_yk);
        unbind = ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        getRealmonthCards();

        //viewPager.setVisibility(View.GONE);
        //tvIndicator.setVisibility(View.GONE);
    }

    @Override
    public void initView() {
        setTitle("月卡");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbind.unbind();
        EventBus.getDefault().unregister(this);
    }

    public String toNewStr(int n) {
        String str = String.valueOf(n);
        DecimalFormat df = new DecimalFormat("00");
        String newStr = df.format(Integer.parseInt(str));
        return newStr;
    }


    @OnClick(R.id.bt_kaika)
    public void onViewClicked() {
        Intent intent = new Intent();
        intent.setClass(YkActivity.this,BuyYkActivity.class);
//        intent.setClass(YkActivity.this,YkxfActivity.class);
        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateUI(MsgEvent event) {
        if(event.getAction()==ACTION_REALMONTHCARDS){
            String content = event.getStr();
            Gson gson = new Gson();
            monthCard = gson.fromJson(content,MonthCard.class);
            cardList.addAll(monthCard.getMonthCard());
            //Log.e("设置月卡数据",monthCard.getMonthCard().get(1).getCarNumbers().size()+"");

            if(monthCard.getMonthCard().size()==0){
                tvIndicator.setVisibility(View.GONE);
            }else {
                tvIndicator.setVisibility(View.VISIBLE);
            }

            setDatas(cardList);
            btKaika.setVisibility(View.VISIBLE);
        }
        if (event.getAction() == ACTION_ADDMONTHCAR_SUCCESS) {

            ToastUtil.showShort(YkActivity.this,event.getPosition()+"");
            list.clear();
            cardList.clear();
            newList.clear();
            getRealmonthCards();
            position = event.getPosition();
            viewPager.setCurrentItem(position);
        }
        if(event.getAction() == ACTION_MONTHCARD_UPDATE){
            position = event.getPosition();
            //ToastUtil.showShort(YkActivity.this,"解绑返回的位置"+event.getPosition());
            list.clear();
            cardList.clear();
            newList.clear();
            getRealmonthCards();
        }
        if(event.getAction() == ACTION_ADDYK_SUCCESS){
            //ToastUtil.showShort(YkActivity.this,"添加返回的位置"+event.getPosition());
            position = event.getPosition();
            list.clear();
            cardList.clear();
            newList.clear();
            getRealmonthCards();
        }
        if (event.getAction() == ACTION_BUYMONTHCARD_SUCCESS) {
            position = 1000;
            list.clear();
            cardList.clear();
            newList.clear();
            getRealmonthCards();
        }
        if(event.getAction() == ACTION_MONTHCARD_XUFEI){
            position = event.getPosition();
            list.clear();
            cardList.clear();
            newList.clear();
            getRealmonthCards();
        }
        if(event.getAction() == ACTION_REALMONTHCARDS_FAILURE){
            viewPager.setVisibility(View.GONE);
            tvIndicator.setVisibility(View.GONE);
            btKaika.setVisibility(View.VISIBLE);
        }


    }

    private void setDatas(final List<MonthCard.MonthCardBean> cardList) {
        if (cardList.size() > 1) {

            newList.addAll(cardList);
            newList.addAll(cardList);
            newList.addAll(cardList);
        } else {
            newList.addAll(cardList);
        }
        if(position!=1000){

            if (position == list.size() / 3 - 1) {
                tvIndicator.setText(toNewStr(list.size() / 3) + "/" + toNewStr(cardList.size()));
            }
            if (position == list.size() / 3 * 2) {
                tvIndicator.setText("01" + "/" + toNewStr(cardList.size()));
            }
            if (position > list.size() / 3 - 1 && position < list.size() / 3 * 2) {
                tvIndicator.setText(toNewStr(position - cardList.size() + 1) + "/" + toNewStr(cardList.size()));
            }
        }else {
            tvIndicator.setText("01/" + toNewStr(cardList.size()));
        }
//        tvIndicator.setText("01/" + toNewStr(cardList.size()));

        for (int i = 0; i < newList.size(); i++) {
            YkFragment fragment = new YkFragment(newList.get(i), i);
            list.add(fragment);
        }

        YkAdapter myAdapter = new YkAdapter(getSupportFragmentManager(),list);
        myAdapter.notifyDataSetChanged();
        viewPager.setAdapter(myAdapter);
        //viewPager.setAdapter(new YkAdapter(getSupportFragmentManager(), list));

        if(position!=1000){
            viewPager.setCurrentItem(position,false);
        }else {
            viewPager.setCurrentItem(list.size() / 3, false);
        }

//        viewPager.setCurrentItem(list.size() / 3, false);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {

            }
            @Override
            public void onPageScrollStateChanged(int state) {
                int position = viewPager.getCurrentItem();
                if (ViewPager.SCROLL_STATE_IDLE == state) {
                    if (position == list.size() / 3 - 1) {
                        viewPager.setCurrentItem(position + list.size() / 3, false);
//                        tv.setText(list.size() / 3+"/"+s11);
                        tvIndicator.setText(toNewStr(list.size() / 3) + "/" + toNewStr(cardList.size()));
                    }
                    if (position == list.size() / 3 * 2) {
                        viewPager.setCurrentItem(list.size() / 3, false);
                        tvIndicator.setText("01" + "/" + toNewStr(cardList.size()));
                    }
                    if (position > list.size() / 3 - 1 && position < list.size() / 3 * 2) {
//                        tv.setText(position-size+1+"/"+s11);
                        tvIndicator.setText(toNewStr(position - cardList.size() + 1) + "/" + toNewStr(cardList.size()));
                    }
                }
            }
        });

        viewPager.setPageMargin(getResources().getDimensionPixelSize(R.dimen.vp_yk));
        viewPager.setOffscreenPageLimit(list.size());
    }


    private void getRealmonthCards(){
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
        HttpUtils.post(Param.REALMONTHCARDS, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("月卡失败","fail");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonDatas = response.body().string();
                Log.e("返回月卡数据", jsonDatas);
                try {
                    JSONObject jsonObject = new JSONObject(jsonDatas);
                    String errCode = jsonObject.optString("errCode");
                    if(errCode.equals("1000")){
                        String content = jsonObject.optString("content");
                        EventBus.getDefault().post(new MsgEvent(ACTION_REALMONTHCARDS,content));
                    }
                    if(errCode.equals("4000")){
                        EventBus.getDefault().post(new MsgEvent(ACTION_REALMONTHCARDS_FAILURE));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, map);
    }


}
