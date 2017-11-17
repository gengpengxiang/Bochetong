package com.bolong.bochetong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.bolong.bochetong.adapter.BdcpAdapter;
import com.bolong.bochetong.bean.User;
import com.bolong.bochetong.bean2.CarCard;
import com.bolong.bochetong.bean2.MsgEvent;
import com.bolong.bochetong.utils.HttpUtils;
import com.bolong.bochetong.utils.Param;
import com.bolong.bochetong.utils.SharedPreferenceUtil;
import com.bolong.bochetong.utils.ToastUtil;
import com.bolong.bochetong.view.CustomPopDialog;
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

public class BdcpActivity extends BaseActivity {
    private Unbinder unbinder;
    @BindView(R.id.btn_bdcp_add)
    Button btnBdcpAdd;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    private final static int REQUEST_CODE = 1;
    //车牌相关
    private LinearLayoutManager mLayoutManager;
    private BdcpAdapter adapter;
    private List<CarCard.ContentBean> list = new ArrayList<>();
    private CustomPopDialog dialog;
    private Button btnConfirm;
    private Button btnCancel;
    private static final int ACTION_CARCARDLIST = 20;
    private static final int ACTION_UNBINDCARCARD = 21;

    @Override
    public void onBaseCreate(Bundle bundle) {
        setContentViewId(R.layout.activity_bdcp);

        EventBus.getDefault().register(this);
        unbinder = ButterKnife.bind(this);

        getCarCardList();
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        adapter = new BdcpAdapter(this, list);
        adapter.buttonSetOnclick(new BdcpAdapter.ButtonInterface() {
            @Override
            public void onclick(View view, final int position) {

                CustomPopDialog.Builder dialogBuild = new CustomPopDialog.Builder(BdcpActivity.this);
                dialog = dialogBuild.create(R.layout.layout_dialog_unbind);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                btnConfirm = (Button) dialog.findViewById(R.id.btn_confirm);
                btnCancel = (Button) dialog.findViewById(R.id.btn_cancel);
                btnConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            String carCard = list.get(position).getCarCard();
                            unbindCaeCard(carCard);
                        }catch (Exception e){

                        }
                    }
                });
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

            }
        });
        mRecyclerView.setAdapter(adapter);

    }

    @Override
    public void initView() {
        setTitle("绑定车牌");
    }

    @OnClick(R.id.btn_bdcp_add)
    public void onViewClicked() {
        if (list.size() <= 10) {
            Intent intent = new Intent();
            intent.putExtra("symbol","normal");
            intent.setClass(BdcpActivity.this, BdcpActivity2.class);
            startActivityForResult(intent, REQUEST_CODE);
        } else {
            Toast.makeText(BdcpActivity.this, "您绑定的车辆数已达上限", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == BdcpActivity2.RESULT_CODE) {
                list.clear();
                getCarCardList();
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateUI(MsgEvent event) {
        if (event.getAction() == ACTION_CARCARDLIST) {
            adapter.notifyDataSetChanged();
        }
        if (event.getAction() == ACTION_UNBINDCARCARD) {
            if (event.getStr2().equals("1")) {
                dialog.dismiss();
                getCarCardList();
                if (list.size() == 1) {
                    list.clear();
                    adapter.notifyDataSetChanged();
                }
                Toast.makeText(BdcpActivity.this, "解绑成功", Toast.LENGTH_SHORT).show();
            }
            if (event.getStr2().equals("0")){
                dialog.dismiss();
                //getCarCardList();
                ToastUtil.showShort(BdcpActivity.this,event.getStr());
            }
        }

    }

    /**
     * 获取已绑定车牌列表
     */
    private void getCarCardList() {
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
        Log.e("uid==",uid+"token=="+token);
        HttpUtils.post(Param.CARCARDLIST, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonDatas = response.body().string();
                Log.e("绑定车牌", jsonDatas);
                try {
                    try {
                        JSONObject jsonObject = new JSONObject(jsonDatas);
                        String content = jsonObject.optString("content");

                        Gson gson = new Gson();
                        CarCard carCard = gson.fromJson(jsonDatas, CarCard.class);
                        List<CarCard.ContentBean> newList = carCard.getContent();
                        list.clear();
                        list.addAll(newList);
                        EventBus.getDefault().post(new MsgEvent(ACTION_CARCARDLIST));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {

                }
            }
        }, map);
    }

    /**
     * 车牌解绑
     */
    private void unbindCaeCard(String carCard) {
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
        map.put("carCard", carCard);
        Log.e("要解绑的车牌", carCard);
        HttpUtils.post(Param.DELETECARCARD, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(BdcpActivity.this, "访问服务器失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonDatas = response.body().string();
                Log.e("解绑车牌", jsonDatas);
                try {
                    try {
                        JSONObject jsonObject = new JSONObject(jsonDatas);
                        String content = jsonObject.optString("content");
                        String errMessage = jsonObject.optString("errMessage");
                        JSONObject jb = new JSONObject(content);
                        String status = jb.optString("status");
                        EventBus.getDefault().post(new MsgEvent(ACTION_UNBINDCARCARD, errMessage,status));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(BdcpActivity.this, "解绑失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(BdcpActivity.this, "解绑失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }, map);
    }
}
