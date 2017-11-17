package com.bolong.bochetong.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bolong.bochetong.activity.BdcpActivity2;
import com.bolong.bochetong.activity.ChangeActivity;
import com.bolong.bochetong.activity.R;
import com.bolong.bochetong.activity.YkxfActivity;
import com.bolong.bochetong.bean.User;
import com.bolong.bochetong.bean2.MonthCard;
import com.bolong.bochetong.bean2.MsgEvent;
import com.bolong.bochetong.utils.HttpUtils;
import com.bolong.bochetong.utils.Param;
import com.bolong.bochetong.utils.SharedPreferenceUtil;
import com.bolong.bochetong.utils.ToastUtil;
import com.bolong.bochetong.view.CustomPopDialog;

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

import static com.bolong.bochetong.fragment.NkFragment.ACTION_MONTHCARD_UPDATE;
import static com.bolong.bochetong.fragment.NkFragment.ACTION_UNBIND_CARPLATE;

public class YkFragment extends Fragment {


    Unbinder unbinder;
    @BindView(R.id.layout_yysysj)
    RelativeLayout layoutYysysj;
    @BindView(R.id.layout_bjcp)
    RelativeLayout layoutBjcp;
    @BindView(R.id.layout_cjsjh)
    RelativeLayout layoutCjsjh;
    @BindView(R.id.layout_card_info)
    LinearLayout layoutCardInfo;
    @BindView(R.id.tv_city)
    TextView tvCity;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.tv_park)
    TextView tvPark;
    @BindView(R.id.tv_carplate)
    TextView tvCarplate;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_day)
    TextView tvDay;
    @BindView(R.id.bt_xufei)
    TextView btXufei;
    @BindView(R.id.tv_phoneNumber)
    TextView tvPhoneNumber;
    @BindView(R.id.bt_bjsjh)
    TextView btBjsjh;
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.bt_tjcp)
    TextView btTicp;
    private MonthCard.MonthCardBean cards;
    private int position;
    private boolean isShow = false;
    private CustomPopDialog dialog;
    private Button btnCancel;
    private Button btnConfirm;
    private String carPlate;

    public YkFragment(MonthCard.MonthCardBean cards, int position) {
        this.cards = cards;
        this.position = position;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_item_monthcard, container, false);

        unbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        setDatas();
        return view;
    }

    private void setDatas() {
        int status = cards.getCardStatus();
        String cardStatus = null;
        switch (status) {
            case 1:
                cardStatus = "使用中";
                break;
            case 2:
                cardStatus = "待激活";
                break;
            case 3:
                cardStatus = "已封存";
                break;
            case 4:
                cardStatus = "过期";
                break;
        }
        tvStatus.setText("(" + cardStatus + ")");
        tvPark.setText(cards.getParkName());

        tvDay.setText(cards.getResidueDays()+"天");

        if(cards.getCarNumbers().size()!=0){
            if(cards.getCarNumbers().size()==1) {
                tvCarplate.setText("车牌号：" + cards.getCarNumbers().get(0));
            }
            if(cards.getCarNumbers().size()==2) {
                tvCarplate.setText("车牌号：" + cards.getCarNumbers().get(0)+" , "+cards.getCarNumbers().get(1));
            }
        }

       // tvCarplate.setText("车牌号：" + cards.getCarNumbers().get(0));


        tvDate.setText("使用期限：" + cards.getCreateTime().substring(0, 10) + " - " + cards.getCardTermofvalidity().substring(0, 10));

        //tvDay.setText();
        tvPhoneNumber.setText(cards.getUserPhoneNumber());

        YkCarAdapter adapter = new YkCarAdapter(cards.getCarNumbers());
        listView.setAdapter(adapter);
        adapter.setListViewHeight(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("abc","dfgg");
                String cardId = cards.getCardId();
                carPlate = cards.getCarNumbers().get(position);
                int size = cards.getCarNumbers().size();
                showMenuDialog(R.layout.layout_dialog_menu, carPlate);
            }
        });

    }

    private void showMenuDialog(int resId, String str) {
        CustomPopDialog.Builder dialogBuild = new CustomPopDialog.Builder(getActivity());
        dialog = dialogBuild.create(resId);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        TextView tv = (TextView) dialog.findViewById(R.id.tv_carPlate);
        tv.setText(str);
        btnCancel = (Button) dialog.findViewById(R.id.btn_cancel);
        btnConfirm = (Button) dialog.findViewById(R.id.btn_confirm);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dialog.isShowing()){
                    dialog.dismiss();
                }
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteMonthCardCar();
            }
        });
    }

    class YkCarAdapter extends BaseAdapter {

        private List<String> newList;

        public YkCarAdapter(List<String> newList) {
            this.newList = newList;
        }

        @Override
        public int getCount() {
            return newList.size();
        }

        @Override
        public Object getItem(int position) {
            return newList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder2 holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_ykcarplate, parent, false);
                holder = new ViewHolder2();
                holder.tv = (TextView) convertView.findViewById(R.id.tv_carPlate);
                //holder.layout = (LinearLayout) convertView.findViewById(R.id.layout_menu_carplate);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder2) convertView.getTag();
            }
            holder.tv.setText(newList.get(position));

            return convertView;
        }

        class ViewHolder2 {
            TextView tv;
            //LinearLayout layout;
        }

        public void setListViewHeight(ListView listView) {
            YkCarAdapter adapter = (YkCarAdapter) listView.getAdapter();
            if (adapter == null) {
                return;
            }
            int totalHeight = 0;
            for (int i = 0; i < adapter.getCount(); i++) {
                View listItem = adapter.getView(i, null, listView);
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
            listView.setLayoutParams(params);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

    @OnClick({R.id.layout_card_info, R.id.bt_xufei, R.id.bt_bjsjh,R.id.bt_tjcp})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_card_info:
               /* if (!isShow) {
                    layoutBjcp.setVisibility(View.VISIBLE);
                    layoutCjsjh.setVisibility(View.VISIBLE);
                    layoutYysysj.setVisibility(View.VISIBLE);
                    isShow = true;
                } else {
                    layoutBjcp.setVisibility(View.GONE);
                    layoutCjsjh.setVisibility(View.GONE);
                    layoutYysysj.setVisibility(View.GONE);
                    isShow = false;
                }*/
                break;
            case R.id.bt_xufei:
                Intent intent = new Intent();
                intent.setClass(getActivity(), YkxfActivity.class);
                intent.putExtra("cardStatus", tvStatus.getText().toString());
                intent.putExtra("parkName", tvPark.getText().toString());
                intent.putExtra("carNumber", tvCarplate.getText().toString());
                intent.putExtra("date", tvDate.getText().toString());
                intent.putExtra("standard", cards.getMonthlyPrice());
                intent.putExtra("cardId", cards.getCardId());
                intent.putExtra("days",cards.getResidueDays()+"天");
                intent.putExtra("position",position);
                startActivity(intent);
                break;
            case R.id.bt_bjsjh:
                Intent intent3 = new Intent();
                intent3.setClass(getActivity(),ChangeActivity.class);
                startActivity(intent3);
                break;
            case R.id.bt_tjcp:

                int size = cards.getCarNumbers().size();
                if(size==2){
                    ToastUtil.showShort(getActivity(),"您最多可以绑定两辆月卡车");
                }else {
                    String cardId = cards.getCardId();
                    Intent intent2 = new Intent();
                    intent2.setClass(getActivity(), BdcpActivity2.class);
                    intent2.putExtra("symbol", "monthCard");
                    intent2.putExtra("monthCardCar", cardId);
                    intent2.putExtra("position",position);
                    startActivity(intent2);
                    Log.e("传过去的position",position+"");
                }

                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateUI(MsgEvent event) {
        if ((event.getAction() == ACTION_UNBIND_CARPLATE)) {
            String status = event.getStr();
            if (status.equals("1")) {
                dialog.dismiss();
                EventBus.getDefault().post(new MsgEvent(ACTION_MONTHCARD_UPDATE,position));
                ToastUtil.showShort(getActivity(), "解绑成功");
            } else {
                ToastUtil.showShort(getActivity(), "解绑失败");
            }
        }
    }

    private void deleteMonthCardCar() {
        String uid = null;
        String token = null;
        if (SharedPreferenceUtil.getBean(getActivity(), "userInfo") != null) {
            User user = (User) SharedPreferenceUtil.getBean(getActivity(), "userInfo");
            uid = user.getUserId();
            token = user.getToken();
        } else {
            uid = Param.UID;
            token = Param.TOKEN;
        }
        Map<String, String> map = new HashMap<>();
        map.put("uid", uid);
        map.put("token", token);
        map.put("cardId", cards.getCardId());
        map.put("carNumber", carPlate);
        HttpUtils.post(Param.DELETEMONTHCARDCAR, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonDatas = response.body().string();
                Log.e("解绑月卡车", jsonDatas);
                try {
                    try {
                        JSONObject jsonObject = new JSONObject(jsonDatas);
                        String content = jsonObject.optString("content");
                        JSONObject jb = new JSONObject(content);
                        final String status = jb.optString("status");
                        EventBus.getDefault().post(new MsgEvent(ACTION_UNBIND_CARPLATE,status));
                        Log.e("解绑成功","true");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {

                }
            }
        }, map);
    }

}
