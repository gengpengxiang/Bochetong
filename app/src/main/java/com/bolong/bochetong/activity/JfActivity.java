package com.bolong.bochetong.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import com.bolong.bochetong.bean.User;
import com.bolong.bochetong.bean2.MsgEvent;
import com.bolong.bochetong.bean2.ParkTimer;
import com.bolong.bochetong.fragment.JfFragment;
import com.bolong.bochetong.utils.HttpUtils;
import com.bolong.bochetong.utils.Param;
import com.bolong.bochetong.utils.SharedPreferenceUtil;
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
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import static com.bolong.bochetong.push.MyReceiver.ACTION_EXIT;

public class JfActivity extends BaseActivity {



    private Unbinder unbinder;

    private PageIndicatorView pageIndicatorView;
    List<Fragment> list = new ArrayList<>();
    private ViewPager viewPager;
    public static final int ACTION_TIMERSNUM = 6698;
    private int position;
    @Override
    public void onBaseCreate(Bundle bundle) {
        setContentViewId(R.layout.activity_jf);
        unbinder = ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        getParkTime();
    }

    @Override
    public void initView() {
        setTitle("计费");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
    }

    private void getParkTime() {
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
        Log.e("uid==", uid);
        Log.e("token===", token);
        HttpUtils.post(Param.HOMETIMERS, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("计费Fragment", "failure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonDatas = response.body().string();
                Log.e("计费数据", jsonDatas);
                try {
                    try {
                        JSONObject jsonObject = new JSONObject(jsonDatas);
                        String content = jsonObject.optString("content");

                        EventBus.getDefault().post(new MsgEvent(ACTION_TIMERSNUM, content));

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
        if ((event.getAction() == ACTION_TIMERSNUM)) {
            String content = event.getStr();
            Gson gson = new Gson();
            ParkTimer parkTimer = gson.fromJson(content, ParkTimer.class);

            if(parkTimer.getStatus()==2) {

                //虚拟数据
                //List<ParkTimer.TimerBean> timers = new ArrayList<>();
                //timers.add(new ParkTimer.TimerBean("1500513480","2017.06.09 06:19","1501571379","冀G98888","id1","地上停车场","id=1","0","147.0"));
                //timers.add(new ParkTimer.TimerBean("1500513480","1996.06.09 06:19","1501581379","京AAAAAA","id1","地下停车场","id=1","0","1.0"));

                List<ParkTimer.TimerBean> timers = parkTimer.getTimer();
                if (timers.size() != 0) {
                    pageIndicatorView = (PageIndicatorView) findViewById(R.id.pageIndicatorView);
                    pageIndicatorView.initIndicator(timers.size());
                    viewPager = (ViewPager) findViewById(R.id.viewPager);
                    list.clear();
                    for (int i = 0; i < timers.size(); i++) {
                        JfFragment fragment = new JfFragment(timers.get(i), i);
                        list.add(fragment);
                        Log.e("循环", i + "");
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
                }
            }
        }
        if(event.getAction() == ACTION_EXIT){
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
