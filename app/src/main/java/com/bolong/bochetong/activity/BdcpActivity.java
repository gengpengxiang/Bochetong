package com.bolong.bochetong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bolong.bochetong.adapter.BdcpAdapter;
import com.bolong.bochetong.bean.CarPlate;
import com.bolong.bochetong.bean.CarPlateDao;
import com.bolong.bochetong.bean.Card;
import com.bolong.bochetong.bean.User;
import com.bolong.bochetong.utils.HttpUtils;
import com.bolong.bochetong.utils.NetworkAvailableUtils;
import com.bolong.bochetong.utils.Param;
import com.bolong.bochetong.utils.SharedPreferenceUtil;
import com.bolong.bochetong.view.CustomPopDialog;
import com.google.gson.Gson;

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
    private CarPlateDao mCardPlateDao;
    private LinearLayoutManager mLayoutManager;
    private BdcpAdapter adapter;
    private List<CarPlate> list = null;
    private CustomPopDialog dialog;
    private Button btnConfirm;
    private Button btnCancel;

    @Override
    public void onBaseCreate(Bundle bundle) {
        mCardPlateDao = MyApplication.getInstance().getDaoSession().getCarPlateDao();
        setContentViewId(R.layout.activity_bdcp);

        unbinder = ButterKnife.bind(this);
        mCardPlateDao = MyApplication.getInstance().getDaoSession().getCarPlateDao();
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        //mRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));//添加分割线
        adapter = new BdcpAdapter(this, getDatas());
        //
        adapter.buttonSetOnclick(new BdcpAdapter.ButtonInterface() {
            @Override
            public void onclick(View view, final int position) {
                //Toast.makeText(BdcpActivity.this, "id=" + list.get(position).getId() + "position=" + position, Toast.LENGTH_LONG).show();

                CustomPopDialog.Builder dialogBuild = new CustomPopDialog.Builder(BdcpActivity.this);


                dialog = dialogBuild.create(R.layout.layout_dialog_unbind);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

                btnConfirm = (Button) dialog.findViewById(R.id.btn_confirm);
                btnCancel = (Button) dialog.findViewById(R.id.btn_cancel);

                btnConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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

                        final String s = list.get(position).getCarPlate().replaceAll("-", "");

                        Map<String, String> map = new HashMap<>();
                        map.put("uid", uid);
                        map.put("token", token);
                        map.put("carCard", s);
                        Log.e("要解绑的车牌", s);
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
                                        JSONObject jb = new JSONObject(content);
                                        String status = jb.optString("status");

                                        if (status.equals("1")) {
                                            mCardPlateDao.deleteByKey(list.get(position).getId());
                                            dialog.dismiss();
                                            adapter.removeData(position);

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(BdcpActivity.this, "解绑成功", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                        }

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

    public List<CarPlate> getDatas() {
        list = mCardPlateDao.loadAll();
        return list;
    }

    @OnClick(R.id.btn_bdcp_add)
    public void onViewClicked() {
        if(list.size()<=4){
            Intent intent = new Intent();
            intent.setClass(BdcpActivity.this, BdcpActivity2.class);
            startActivityForResult(intent, REQUEST_CODE);
        }else {
            Toast.makeText(BdcpActivity.this,"您绑定的车辆数已达上限",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == BdcpActivity2.RESULT_CODE) {
                refresh();
            }
        }
    }

    private void refresh() {
        List<CarPlate> lt = mCardPlateDao.loadAll();
        list.clear();
        list.addAll(lt);
        adapter.notifyDataSetChanged();


    }


}
