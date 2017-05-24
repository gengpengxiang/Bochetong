package com.bolong.bochetong.activity;


import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bolong.bochetong.adapter.TcjlAdapter;
import com.bolong.bochetong.bean.RecordList;
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
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class TcjlActivity extends BaseActivity {
    private Unbinder unbinder;
    public List<RecordList.ContentBean.DateBean> date = new ArrayList<>();
    @BindView(R.id.mRecyclerView)
    PullLoadMoreRecyclerView mRecyclerView;
    @BindView(R.id.iv_failure)
    ImageView ivFailure;
    private static TcjlAdapter adapter;
    private LinearLayout ll;

    private int totalPage;
    private int pageNo;
    private int page = 1;

    @Override
    public void onBaseCreate(Bundle bundle) {

        setContentViewId(R.layout.activity_tcjl);

        ll = (LinearLayout) findViewById(R.id.layout_json_failure);

        unbinder = ButterKnife.bind(this);

        adapter = new TcjlAdapter(this, getDatas(1));
        mRecyclerView.setAdapter(adapter);

        //关闭下拉刷新功能
//        mRecyclerView.setPullRefreshEnable(false);
        //首次进入显示顶部加载动画
        mRecyclerView.setRefreshing(true);
        mRecyclerView.setColorSchemeResources(R.color.blue);
        mRecyclerView.setLinearLayout();
        refreshRecyclerView();

    }

    private void refreshRecyclerView() {
        mRecyclerView.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        date.clear();
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
                            Toast.makeText(TcjlActivity.this, "没有数据了哦", Toast.LENGTH_SHORT).show();
                            mRecyclerView.setPullLoadMoreCompleted();
                        }
                    }
                }, 500);
            }
        });

    }


    public List<RecordList.ContentBean.DateBean> getDatas(int page) {

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
        HttpUtils.post(Param.CARRECORDLIST, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        RecyclerViewRefreshUtils.refreshCompleted(mRecyclerView, ll);
                    }
                });
                Log.e("TAG", "onFailure: ", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //date.clear();
                String jsonDatas = response.body().string();
                Log.e("停车记录", jsonDatas);
                try {
                    try {
                        JSONObject jsonObject = new JSONObject(jsonDatas);
                        String errCode = jsonObject.optString("errCode");
//                        if (errCode.equals("1000")) {
                        String content = jsonObject.optString("content");
                        Gson gson = new Gson();
                        RecordList.ContentBean bean = gson.fromJson(content, RecordList.ContentBean.class);
                        //获取页数
                        totalPage = bean.getTotalPage();
                        pageNo = bean.getPageNo();
                        List<RecordList.ContentBean.DateBean> newList = bean.getDate();
                        Log.e("NewList", newList.size() + "");
                        //date.clear();
                        date.addAll(newList);
                        //ADD
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //progressBar.setVisibility(View.GONE);
                                adapter.notifyDataSetChanged();
                                mRecyclerView.setPullLoadMoreCompleted();
                            }
                        });
//                        }


                    } catch (JSONException e) {
                        e.printStackTrace();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                RecyclerViewRefreshUtils.refreshCompleted(mRecyclerView, ll);
                            }
                        });
                    }
                } catch (Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            RecyclerViewRefreshUtils.refreshCompleted(mRecyclerView, ll);
                        }
                    });


                }

            }
        }, map);
        return date;

    }


    @Override
    public void initView() {
        setTitle("停车记录");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }


}
