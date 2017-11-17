package com.bolong.bochetong.activity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.LinearLayout;
import com.bolong.bochetong.adapter.TcjlAdapter;
import com.bolong.bochetong.bean.User;
import com.bolong.bochetong.bean2.CarRecord;
import com.bolong.bochetong.bean2.MsgEvent;
import com.bolong.bochetong.utils.HttpUtils;
import com.bolong.bochetong.utils.Param;
import com.bolong.bochetong.utils.SharedPreferenceUtil;
import com.google.gson.Gson;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;
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
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class TcjlActivity extends BaseActivity {
    private Unbinder unbinder;
    public List<CarRecord.DataBean> dataList = new ArrayList<>();
    @BindView(R.id.mRecyclerView)
    PullLoadMoreRecyclerView mRecyclerView;

    private TcjlAdapter adapter;
    private LinearLayout ll;

    private int totalPage;
    private int pageNo;
    private int page = 1;
    public static final int ACTION_CARRECORELIST = 400;
    public static final int ACTION_FAILURE = 444;

    @Override
    public void onBaseCreate(Bundle bundle) {
        setContentViewId(R.layout.activity_tcjl);

        EventBus.getDefault().register(this);
        unbinder = ButterKnife.bind(this);
        getCarRecords();
    }

    private void refreshRecyclerView() {
        mRecyclerView.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dataList.clear();
                        page = 1;
                        getCarRecords();
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
//                            getDatas(page);
                            getCarRecords();
                            adapter.notifyDataSetChanged();
                            mRecyclerView.setPullLoadMoreCompleted();
                        } else {
                            //Toast.makeText(TcjlActivity.this, "没有数据了哦", Toast.LENGTH_SHORT).show();
                            mRecyclerView.setPullLoadMoreCompleted();
                        }
                    }
                }, 500);
            }
        });

    }


    public void getCarRecords() {

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
        //map.put("pageNo", String.valueOf(page));
        HttpUtils.post(Param.CARRECORDLIST, new Callback() {
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
                Log.e("停车记录", jsonDatas);
                try {
                    try {
                        JSONObject jsonObject = new JSONObject(jsonDatas);
                        String content = jsonObject.optString("content");
                        Gson gson = new Gson();
                        CarRecord bean = gson.fromJson(content, CarRecord.class);
                        List<CarRecord.DataBean> newList = bean.getData();
                        dataList.clear();
                        dataList.addAll(newList);
                        EventBus.getDefault().post(new MsgEvent(ACTION_CARRECORELIST));

                    } catch (JSONException e) {
                        EventBus.getDefault().post(new MsgEvent(ACTION_FAILURE));
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
        setTitle("停车记录");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateUI(MsgEvent event) {
        if (event.getAction() == ACTION_CARRECORELIST) {
            adapter = new TcjlAdapter(dataList);
            mRecyclerView.setAdapter(adapter);
            mRecyclerView.setColorSchemeResources(R.color.blue);
            mRecyclerView.setLinearLayout();
            refreshRecyclerView();
        }
        if (event.getAction() == ACTION_FAILURE) {

        }
    }


}
