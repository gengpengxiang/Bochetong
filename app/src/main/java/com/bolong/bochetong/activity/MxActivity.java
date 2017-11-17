package com.bolong.bochetong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DividerItemDecoration;
import android.util.Log;
import com.bolong.bochetong.adapter.MxAdapter;
import com.bolong.bochetong.bean.User;
import com.bolong.bochetong.bean2.Bill;
import com.bolong.bochetong.bean2.MsgEvent;
import com.bolong.bochetong.utils.HttpUtils;
import com.bolong.bochetong.utils.Param;
import com.bolong.bochetong.utils.SharedPreferenceUtil;
import com.bolong.bochetong.utils.ToastUtil;
import com.google.gson.Gson;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import static com.bolong.bochetong.activity.WdqbActivity.ACTION_NOTECASE;

public class MxActivity extends BaseActivity {


    @BindView(R.id.mRecyclerView)
    PullLoadMoreRecyclerView mRecyclerView;
    private MxAdapter adapter;
    private Unbinder unbind;
    private List<Bill.PagenationBean.DataBean> billsList = new ArrayList<>();

    @Override
    public void onBaseCreate(Bundle bundle) {
        setContentViewId(R.layout.activity_mx);
        EventBus.getDefault().register(this);
        unbind = ButterKnife.bind(this);
        getNoteCase();
    }

    @Override
    public void initView() {
        setTitle("明细");
        mRecyclerView.setLinearLayout();
        mRecyclerView.setColorSchemeResources(R.color.blue);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
        refreshRecyclerView();
    }

    private void refreshRecyclerView() {
        mRecyclerView.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getNoteCase();
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
                        ToastUtil.showShort(MxActivity.this,"没有更多数据了");
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
                Log.e("账单明细", jsonDatas);
                try {
                    try {
                        JSONObject jsonObject = new JSONObject(jsonDatas);
                        String content = jsonObject.optString("content");
                        JSONObject jb = new JSONObject(content);
                        //余额
                        String accountBalance = jb.optString("accountBalance");
                        String pagenation = jb.optString("pagenation");
                        Log.e("pagenation", pagenation);
                        Gson gson = new Gson();
                        Bill.PagenationBean pagenationBean = gson.fromJson(pagenation, Bill.PagenationBean.class);
                        List<Bill.PagenationBean.DataBean> newList = pagenationBean.getData();
                        billsList.clear();
                        billsList.addAll(newList);
                        EventBus.getDefault().post(new MsgEvent(ACTION_NOTECASE));
                    } catch (JSONException e) {
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateUI(MsgEvent event) {
        if (event.getAction() == ACTION_NOTECASE) {
            //Collections.reverse(billsList);
            adapter = new MxAdapter(billsList);
            mRecyclerView.setAdapter(adapter);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {

        }
    }
}
