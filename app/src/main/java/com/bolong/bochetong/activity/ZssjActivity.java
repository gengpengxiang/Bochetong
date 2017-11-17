package com.bolong.bochetong.activity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.bolong.bochetong.adapter.FavorAdapter;
import com.bolong.bochetong.bean.User;
import com.bolong.bochetong.bean2.Favor;
import com.bolong.bochetong.bean2.MsgEvent;
import com.bolong.bochetong.utils.HttpUtils;
import com.bolong.bochetong.utils.Param;
import com.bolong.bochetong.utils.SharedPreferenceUtil;
import com.google.gson.Gson;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ZssjActivity extends BaseActivity {


    @BindView(R.id.mRecyclerView)
    PullLoadMoreRecyclerView mRecyclerView;
    private Unbinder unbind;
    private FavorAdapter adapter;
    private List<Favor.ContentBean> favorList = new ArrayList<>();
    private static final int ACTION_FAVOR = 1003;

    @Override
    public void onBaseCreate(Bundle bundle) {
        setContentViewId(R.layout.activity_zssj);
        unbind = ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        getFavor();
    }

    /**
     * 获取赠送时间
     */
    private void getFavor() {
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
        HttpUtils.post(Param.GETFREETIMELIST, new Callback() {
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
                Log.e("赠送时间", jsonDatas);
                try {
                    try {
                        Gson gson = new Gson();
                        Favor favor = gson.fromJson(jsonDatas, Favor.class);
                        List<Favor.ContentBean> contentBean = favor.getContent();
                        favorList.clear();
                        favorList.addAll(contentBean);
                        EventBus.getDefault().post(new MsgEvent(ACTION_FAVOR));
                    } catch (Exception e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setContentViewId(R.layout.layout_noinfo);
                            }
                        });
                    }
                } catch (Exception e) {
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


    @Override
    public void initView() {

        setTitle("赠送时间");
        mRecyclerView.setColorSchemeResources(R.color.blue);
        refreshRecyclerView();
    }

    private void refreshRecyclerView() {
        mRecyclerView.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getFavor();
                        //getSupportActionBar();
                        //adapter.notifyDataSetChanged();
                        mRecyclerView.setPullLoadMoreCompleted();
                    }
                }, 300);
            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //adapter.notifyDataSetChanged();
                        mRecyclerView.setPullLoadMoreCompleted();
                    }
                }, 300);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        unbind.unbind();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateUI(MsgEvent event) {
        if (event.getAction() == ACTION_FAVOR) {
            adapter = new FavorAdapter(favorList);
            mRecyclerView.setLinearLayout();
            mRecyclerView.setAdapter(adapter);

        }
    }
}
