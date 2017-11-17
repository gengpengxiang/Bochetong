package com.bolong.bochetong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import com.bolong.bochetong.adapter.KpHistoryAdapter;
import com.bolong.bochetong.bean.User;
import com.bolong.bochetong.bean2.FpHistory;
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

public class KplsActivity extends BaseActivity {


    @BindView(R.id.mRecyclerView)
    PullLoadMoreRecyclerView mRecyclerView;
    private Unbinder unbind;
    private KpHistoryAdapter adapter;
    private List<FpHistory.ContentBean> historyList = new ArrayList<>();
    public static final int ACTION_GETINVOICELIST = 888;

    @Override
    public void onBaseCreate(Bundle bundle) {
        setContentViewId(R.layout.activity_kpls);
        unbind = ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        getKpHistoryList();

    }

    /**
     * 获取开票历史列表
     */
    private void getKpHistoryList() {
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
        HttpUtils.post(Param.GETINVOICELIST, new Callback() {
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
                Log.e("开票历史", jsonDatas);
                try {
                    try {
                        Gson gson = new Gson();
                        FpHistory fpHistory = gson.fromJson(jsonDatas, FpHistory.class);
                        List<FpHistory.ContentBean> newList = fpHistory.getContent();
                        historyList.clear();
                        historyList.addAll(newList);
                        EventBus.getDefault().post(new MsgEvent(ACTION_GETINVOICELIST));
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

    private void refreshRecyclerView() {
        mRecyclerView.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getKpHistoryList();
                        //adapter.notifyDataSetChanged();
                        mRecyclerView.setPullLoadMoreCompleted();
                    }
                }, 500);
            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //adapter.notifyDataSetChanged();
                        mRecyclerView.setPullLoadMoreCompleted();
                    }
                }, 500);
            }
        });
    }

    @Override
    public void initView() {

        setTitle("开票历史");
        mRecyclerView.setColorSchemeResources(R.color.blue);
        refreshRecyclerView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        unbind.unbind();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateUI(MsgEvent event) {
        if (event.getAction() == ACTION_GETINVOICELIST) {
            adapter = new KpHistoryAdapter(historyList);
            adapter.setOnItemClickLitener(new KpHistoryAdapter.OnItemClickLitener() {
                @Override
                public void onItemClick(View view, int position) {
                    //String invoiceId = historyList.get(position).getOrderNumber();
                    Intent intent = new Intent();
                    intent.setClass(KplsActivity.this, KpxqActivity.class);
                    //intent.putExtra("invoiceId", invoiceId);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("invoiceInfo",historyList.get(position));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            mRecyclerView.setLinearLayout();
            mRecyclerView.setAdapter(adapter);
        }
    }
}
