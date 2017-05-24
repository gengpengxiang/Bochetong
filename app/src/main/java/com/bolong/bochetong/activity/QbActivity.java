package com.bolong.bochetong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DividerItemDecoration;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bolong.bochetong.adapter.ZdmxAdapter;
import com.bolong.bochetong.bean.Bill;
import com.bolong.bochetong.bean.User;
import com.bolong.bochetong.utils.HttpUtils;
import com.bolong.bochetong.utils.Param;
import com.bolong.bochetong.utils.RecyclerViewRefreshUtils;
import com.bolong.bochetong.utils.SharedPreferenceUtil;
import com.google.gson.Gson;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

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

public class QbActivity extends BaseActivity {
    private Unbinder unbinder;
    @BindView(R.id.mRecyclerView)
    PullLoadMoreRecyclerView mRecyclerView;
    @BindView(R.id.tv_qianbao_yue)
    TextView tvQianbaoYue;
    @BindView(R.id.bt_qb_cz)
    Button btQbCz;

    private ZdmxAdapter adapter;
    private List<Bill.PagenationBean.DateBean> billsList = new ArrayList<>();
    private LinearLayout ll;

    private int totalPage;
    private int pageNo;
    private int page = 1;

    @Override
    public void onBaseCreate(Bundle bundle) {

        setContentViewId(R.layout.activity_qb);
        ll = (LinearLayout) findViewById(R.id.layout_json_failure);
        unbinder = ButterKnife.bind(this);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        adapter = new ZdmxAdapter(this, getDatas(1));
        mRecyclerView.setAdapter(adapter);

        mRecyclerView.setColorSchemeResources(R.color.blue);
        mRecyclerView.setRefreshing(true);
        mRecyclerView.setLinearLayout();
        mRecyclerView.setPushRefreshEnable(false);

        refreshRecyclerView();
        //mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void refreshRecyclerView() {
        mRecyclerView.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        billsList.clear();
                        page = 1;
                        getDatas(1);
                        adapter.notifyDataSetChanged();
                        mRecyclerView.setPullLoadMoreCompleted();
                    }
                }, 500);
            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page++;
                        if (page <= totalPage) {
                            getDatas(page);
                            adapter.notifyDataSetChanged();
                            mRecyclerView.setPullLoadMoreCompleted();
                        } else {
                            Toast.makeText(QbActivity.this, "没有数据了哦", Toast.LENGTH_SHORT).show();
                            mRecyclerView.setPullLoadMoreCompleted();
                        }
                    }
                }, 500);
            }
        });
    }


    private List<Bill.PagenationBean.DateBean> getDatas(int page) {

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
        map.put("pageNo", String.valueOf(page));
        HttpUtils.post(Param.NOTECASE, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        RecyclerViewRefreshUtils.refreshCompleted(mRecyclerView, ll);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonDatas = response.body().string();
                Log.e("钱包明细", jsonDatas);
                try {
                    try {
                        JSONObject jsonObject = new JSONObject(jsonDatas);
                        String content = jsonObject.optString("content");
                        JSONObject jb = new JSONObject(content);
                        //余额
                        final String accountBalance = jb.optString("accountBalance");
                        //SharedPreferenceUtil.putString("yue", accountBalance);

                        String pagenation = jb.optString("pagenation");
                        Gson gson = new Gson();
                        Bill.PagenationBean pagenationBean = gson.fromJson(pagenation, Bill.PagenationBean.class);
                        //获取页数
                        totalPage = pagenationBean.getTotalPage();
                        pageNo = pagenationBean.getPageNo();

                        List<Bill.PagenationBean.DateBean> newList = pagenationBean.getDate();
                        //billsList.clear();
                        billsList.addAll(newList);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                                Log.e("钱包余额",accountBalance);
                                tvQianbaoYue.setText(accountBalance);
                                mRecyclerView.setPullLoadMoreCompleted();
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvQianbaoYue.setText("- -");
                                RecyclerViewRefreshUtils.refreshCompleted(mRecyclerView, ll);
                            }
                        });
                    }
                } catch (Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvQianbaoYue.setText("- -");
                            RecyclerViewRefreshUtils.refreshCompleted(mRecyclerView, ll);
                        }
                    });
                }
            }
        }, map);
        return billsList;
    }

    @Override
    public void initView() {
        setTitle("钱包");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }


    @OnClick(R.id.bt_qb_cz)
    public void onViewClicked() {
        Intent intent = new Intent();
        intent.setClass(QbActivity.this, KsczActivity.class);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1){
            getDatas(1);
        }
    }
}
