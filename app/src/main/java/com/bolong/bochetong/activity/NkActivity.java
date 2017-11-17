package com.bolong.bochetong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bolong.bochetong.adapter.NkAdapter;
import com.bolong.bochetong.bean.User;
import com.bolong.bochetong.bean2.MsgEvent;
import com.bolong.bochetong.bean2.YearCard;
import com.bolong.bochetong.fragment.NkFragment;
import com.bolong.bochetong.utils.HttpUtils;
import com.bolong.bochetong.utils.Param;
import com.bolong.bochetong.utils.SharedPreferenceUtil;
import com.bolong.bochetong.utils.ToastUtil;
import com.bolong.bochetong.view.CustomPopDialog;
import com.bolong.bochetong.view.PageIndicatorView;
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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.bolong.bochetong.activity.BdcpActivity2.ACTION_ADDMONTHCAR_SUCCESS;
import static com.bolong.bochetong.activity.ChangeActivity.ACTION_EDITPHONE_SUCCESS;
import static com.bolong.bochetong.activity.R.id.viewPager;
import static com.bolong.bochetong.adapter.NkAdapter.ACTION_ADDCAR;
import static com.bolong.bochetong.adapter.NkAdapter.ACTION_CARPLATE;
import static com.bolong.bochetong.fragment.NkFragment.ACTION_MONTHCARD_UPDATE;

public class NkActivity extends BaseActivity {


    private RelativeLayout layoutYk;

    private List<YearCard.MonthCardBean> monthCardList = new ArrayList<>();
    private int ACTION_YEARCARD = 7766;
    public static int ACTION_ADDCARDCARPLATE = 7451;
    private PageIndicatorView pageIndicatorView;
    List<Fragment> list = new ArrayList<>();
    private ViewPager viewPager;
    private int position;

    @Override
    public void onBaseCreate(Bundle bundle) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentViewId(R.layout.activity_nk);
        EventBus.getDefault().register(this);

        doPost();
        layoutYk = (RelativeLayout) findViewById(R.id.activity_nk);
    }


    @Override
    public void initView() {
        setTitle("年卡");
    }

    private void doPost() {
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
        HttpUtils.post(Param.MONTHCARDS, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setContentViewId(R.layout.layout_nonet);
                    }
                });
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonDatas = response.body().string();
                Log.e("月卡", jsonDatas);
                try {
                    JSONObject jsonObject = new JSONObject(jsonDatas);
                    String content = jsonObject.optString("content");
                    EventBus.getDefault().post(new MsgEvent(ACTION_YEARCARD, content));
                } catch (JSONException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setContentViewId(R.layout.layout_noinfo);
                        }
                    });
                }
            }
        }, map);
    }

    public void doClick(View view) {
        switch (view.getId()) {
            case R.id.bt_qqbd:
                Intent intent2 = new Intent();
                intent2.setClass(NkActivity.this, BdcpActivity2.class);
                startActivityForResult(intent2, 1);
                break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {
            layoutYk.removeAllViews();
            doPost();
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateUI(MsgEvent event) {
        if (event.getAction() == ACTION_YEARCARD) {
            String cardInfo = event.getStr();
            Gson gson = new Gson();
            YearCard yearCard = gson.fromJson(cardInfo, YearCard.class);
            String status = yearCard.getStatus();
            if (status.equals("0")) {
                layoutYk.removeAllViews();
                LayoutInflater layoutInflater = LayoutInflater.from(NkActivity.this);
                View view = layoutInflater.inflate(R.layout.layout_yk_nocard, null);
                layoutYk.addView(view);
                view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
//                layoutYk.removeAllViews();
//                LayoutInflater layoutInflater = LayoutInflater.from(NkActivity.this);
//                View view = layoutInflater.inflate(R.layout.layout_yk_bdcp, null);
//                bt_qpbd = (Button) view.findViewById(R.id.bt_qqbd);
//                layoutYk.addView(view);
//                view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            }
            if (status.equals("1")) {
                List<YearCard.MonthCardBean> cardBean = yearCard.getMonthCard();
                if (cardBean.size() != 0) {
                    monthCardList.clear();
                    monthCardList.addAll(cardBean);
                    layoutYk.removeAllViews();
                    LayoutInflater layoutInflater = LayoutInflater.from(NkActivity.this);
                    View view = layoutInflater.inflate(R.layout.layout_yk_card, null);
                    //mRecyclerView = (RecyclerView) view.findViewById(R.id.mRecyclerView);

                    //add
                    pageIndicatorView = (PageIndicatorView) view.findViewById(R.id.pageIndicatorView);
                    pageIndicatorView.initIndicator(cardBean.size());
                    viewPager = (ViewPager) view.findViewById(R.id.viewPager);

                    list.clear();
                    for(int i=0;i<cardBean.size();i++){
                        NkFragment fragment = new NkFragment(cardBean.get(i),i);
                        list.add(fragment);
                        Log.e("循环",i+"");
                    }

                    FragmentStatePagerAdapter adapter2 = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
                        @Override
                        public Fragment getItem(int position) {
                            return list.get(position);
                        }

                        @Override
                        public int getCount() {
                            return list.size();
                        }
                    };

                    viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                        }

                        @Override
                        public void onPageSelected(int position) {
                            pageIndicatorView.setSelectedPage(position);
                        }

                        @Override
                        public void onPageScrollStateChanged(int state) {
                        }
                    });
                    viewPager.setAdapter(adapter2);

                    viewPager.setCurrentItem(position);

                    layoutYk.addView(view);
                    view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
                }
                if (cardBean.size() == 0) {
                    layoutYk.removeAllViews();
                    LayoutInflater layoutInflater = LayoutInflater.from(NkActivity.this);
                    View view = layoutInflater.inflate(R.layout.layout_yk_nocard, null);
                    layoutYk.addView(view);
                    view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
                }
            }
        }

        if (event.getAction() == ACTION_ADDMONTHCAR_SUCCESS) {
            layoutYk.removeAllViews();
            doPost();
            Log.e("添加位置",event.getPosition()+"");
            position = event.getPosition();
        }
        if(event.getAction() == ACTION_EDITPHONE_SUCCESS){
            finish();
        }
        if(event.getAction() == ACTION_MONTHCARD_UPDATE){
            layoutYk.removeAllViews();
            doPost();
            position = event.getPosition();
        }
    }

}
