package com.bolong.bochetong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DividerItemDecoration;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bolong.bochetong.adapter.FpAdapter;
import com.bolong.bochetong.bean.User;
import com.bolong.bochetong.bean2.Invoice;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.bolong.bochetong.activity.FpkpActivity.ACTION_SJKPSUCCESS;

public class WyfpActivity extends BaseActivity {


    @BindView(R.id.mRecyclerView)
    PullLoadMoreRecyclerView mRecyclerView;
    @BindView(R.id.bt_next)
    TextView btNext;
    @BindView(R.id.bt_checkall)
    TextView btCheckall;
    @BindView(R.id.checkBox)
    CheckBox checkBox;
    @BindView(R.id.tv_history)
    TextView tvHistory;
    @BindView(R.id.tv_explain)
    TextView tvExplain;
    @BindView(R.id.layout_all)
    RelativeLayout layoutAll;
    @BindView(R.id.iv_View)
    ImageView ivView;
    private Unbinder unbind;
    private FpAdapter adapter;

    private boolean isSelectAll = false;
    private int index = 0;
    //记录选择的Item
    private HashSet<Integer> positionSet;
    private List<Invoice.ContentBean> invoices = new ArrayList<>();
    public static final int ACTION_STAYOPENRECORD = 666;

    @Override
    public void onBaseCreate(Bundle bundle) {
        setContentViewId(R.layout.activity_wyfp);
        unbind = ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        getInvoices();
        initViews();
    }

    private void initViews() {
        positionSet = new HashSet<>();
        mRecyclerView.setAdapter(adapter);
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
                        //adapter.notifyDataSetChanged();

                        getInvoices();
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
                        ToastUtil.showShort(WyfpActivity.this,"没有更多数据了");
                        mRecyclerView.setPullLoadMoreCompleted();

                    }
                }, 300);
            }
        });
    }

    @Override
    public void initView() {

        setTitle("我要发票");
        titlebarRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(WyfpActivity.this, KpsmActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        unbind.unbind();
    }


    @OnClick({R.id.tv_history, R.id.tv_explain, R.id.checkBox, R.id.bt_next})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.tv_history:
                Intent intent = new Intent();
                intent.setClass(WyfpActivity.this, KplsActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_explain:
                Intent intent1 = new Intent();
                intent1.setClass(WyfpActivity.this, KpsmActivity.class);
                startActivity(intent1);
                break;
            case R.id.checkBox:
                checkAllBox();
                break;
            case R.id.bt_next:
                int totalPrice = 0;
                //获取选中记录的id
                StringBuffer stringBuffer = new StringBuffer();
                HashSet<Invoice.ContentBean> valueSet = new HashSet<>();
                for (Integer integer : positionSet) {
                    valueSet.add(adapter.getItem(integer));
                }
                for (Invoice.ContentBean itemModel : valueSet) {
                    String str = itemModel.getId();
                    if (str != null) {
                        if (stringBuffer.length() > 0) {
                            stringBuffer.append(",").append(str);
                        } else {
                            stringBuffer.append(str);
                        }
                    }

                }
                Log.e("字符串", stringBuffer.toString());
                String money = String.valueOf(getTotalMoney());
                Intent intent2 = new Intent();
                intent2.setClass(WyfpActivity.this, FpxqActivity.class);
                intent2.putExtra("price", money);
                intent2.putExtra("ids", stringBuffer.toString());
                startActivity(intent2);
                break;
        }
    }

    private void checkAllBox() {
        if (adapter == null) {
            return;
        } else {
            if (!isSelectAll) {
                //add
                btNext.setEnabled(true);
                btNext.setBackgroundResource(R.drawable.shape_code);

                for (int i = 0; i < invoices.size(); i++) {
                    invoices.get(i).setSelect(true);
                    positionSet.add(i);
                }
                index = invoices.size();
                adapter.notifyDataSetChanged();
                isSelectAll = true;
                //add
                checkBox.setBackgroundResource(R.mipmap.icon_select_blue);

                //add
            } else {
                btNext.setEnabled(false);
                btNext.setBackgroundResource(R.drawable.shape_code_ing);

                for (int i = 0; i < invoices.size(); i++) {
                    invoices.get(i).setSelect(false);
                    positionSet.remove(i);
                }
                index = 0;
                adapter.notifyDataSetChanged();
                isSelectAll = false;

                //add
                checkBox.setBackgroundResource(R.mipmap.icon_select_gray);

                //add
            }
        }
    }

    private void setBtnBackground(int size) {
        if (size != 0) {
            btNext.setEnabled(true);
            btNext.setBackgroundResource(R.drawable.shape_code);
        }
        if(size == invoices.size()){
            checkBox.setBackgroundResource(R.mipmap.icon_select_blue);
        }
        if(size != invoices.size()){
            checkBox.setBackgroundResource(R.mipmap.icon_select_gray);
        }

        if(size == 0) {
            btNext.setEnabled(false);
            btNext.setBackgroundResource(R.drawable.shape_code_ing);
        }
    }

    private double getTotalMoney() {
        double totalPrice = 0;
        double hasMoney = 0;
        HashSet<Invoice.ContentBean> valueSet = new HashSet<>();
        for (Integer integer : positionSet) {
            valueSet.add(adapter.getItem(integer));
        }
        for (Invoice.ContentBean itemModel : valueSet) {
            totalPrice += itemModel.getPrice();
        }
        adapter.notifyDataSetChanged();
        Log.e("金钱", "price" + totalPrice);

        return totalPrice;
    }

    /**
     * 操作Item记录集合
     */
    private void addOrRemove(int position) {
        if (positionSet.contains(position)) {
            // 如果包含，则撤销选择
            positionSet.remove(position);
        } else {
            // 如果不包含，则添加
            positionSet.add(position);
        }

        if (positionSet.size() == 0) {

        }
    }

    /**
     * 获取可开发票列表
     */
    private void getInvoices() {
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
        HttpUtils.post(Param.STAYOPENRECORD, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ivView.setImageResource(R.mipmap.bg_nonet);
                        ivView.setVisibility(View.VISIBLE);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonDatas = response.body().string();
                Log.e("发票数据", jsonDatas);
                try {
                    try {
                        Gson gson = new Gson();
                        Invoice invoice = gson.fromJson(jsonDatas, Invoice.class);

                        if(invoice.getContent().size()!=0){
                            List<Invoice.ContentBean> newList = invoice.getContent();
                            invoices.clear();
                            invoices.addAll(newList);
                            EventBus.getDefault().post(new MsgEvent(ACTION_STAYOPENRECORD));
                        }
                    } catch (Exception e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ivView.setImageResource(R.mipmap.bg_noinfo);
                                ivView.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                } catch (Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ivView.setImageResource(R.mipmap.bg_noinfo);
                            ivView.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        }, map);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateUI(MsgEvent event) {
        if (event.getAction() == ACTION_STAYOPENRECORD) {
            layoutAll.setVisibility(View.VISIBLE);
            adapter = new FpAdapter(invoices);

            mRecyclerView.setAdapter(adapter);
            positionSet = new HashSet<>();
            mRecyclerView.setLinearLayout();
            mRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                    DividerItemDecoration.VERTICAL));
            adapter.setOnItemClickLitener(new FpAdapter.OnItemClickLitener() {
                @Override
                public void onItemClick(View view, int position) {
                    Invoice.ContentBean invoice = invoices.get(position);
                    boolean isSelect = invoice.isSelect();
                    //点击之前判断是否已有选中item
                    addOrRemove(position);
                    if (!isSelect) {
                        index++;
                        invoice.setSelect(true);
                        positionSet.add(position);

                    } else {
                        index--;
                        invoice.setSelect(false);
                        positionSet.remove(position);

                    }
                    //根据选中item数量设置下一步按钮状态
                    setBtnBackground(index);

                    List<Boolean> list = new ArrayList<Boolean>();
                    for(int i = 0; i < invoices.size(); i++){
                        boolean select = invoices.get(i).getSelect();
                        Log.e("发票",select+"");
                        list.add(select);
                    }
                    if(list.contains(false)){
                        //checkBox.setBackgroundResource(R.mipmap.icon_select_gray);
                        //checkBox.setChecked(false);
                        checkBox.setPressed(false);
                        isSelectAll = false;
                    }else {
                        checkBox.setPressed(true);
                        isSelectAll = true;
                        //checkBox.setBackgroundResource(R.mipmap.icon_select_blue);
                    }


                }

            });
            mRecyclerView.setAdapter(adapter);
            refreshRecyclerView();
        }
        if (event.getAction() == ACTION_SJKPSUCCESS) {
            finish();
        }
    }

}
