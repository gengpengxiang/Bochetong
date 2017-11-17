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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bolong.bochetong.activity.BdcpActivity2;
import com.bolong.bochetong.activity.ChangeActivity;
import com.bolong.bochetong.activity.KsczActivity;
import com.bolong.bochetong.activity.R;
import com.bolong.bochetong.bean.User;
import com.bolong.bochetong.bean2.MsgEvent;
import com.bolong.bochetong.bean2.YearCard;
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

public class NkFragment extends Fragment {

    @BindView(R.id.tv_parkName)
    TextView tvParkName;
    @BindView(R.id.et_phoneNumber)
    TextView etPhoneNumber;
    @BindView(R.id.bt_change)
    TextView btChange;
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.bt_add)
    ImageView btAdd;
    @BindView(R.id.tv_carPort)
    TextView tvCarPort;
    @BindView(R.id.tv_startTime)
    TextView tvStartTime;
    @BindView(R.id.tv_stopTime)
    TextView tvStopTime;
    @BindView(R.id.layout_date)
    LinearLayout layoutDate;
    Unbinder unbinder;
    YearCard.MonthCardBean monthCardBean;
    @BindView(R.id.layout_xf)
    RelativeLayout layoutXf;
    private CustomPopDialog dialog;
    private String carPlate;
    private Button btnCancel,btnConfirm;
    public static int ACTION_UNBIND_CARPLATE = 25223;
    public static final int ACTION_MONTHCARD_UPDATE = 25224;
    private int position;

    public NkFragment(YearCard.MonthCardBean monthCardBean, int position) {
        this.monthCardBean = monthCardBean;
        this.position = position;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_item_nk, container, false);
        unbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        tvParkName.setText(monthCardBean.getParkName());
        etPhoneNumber.setText(monthCardBean.getUserPhoneNumber());
        tvCarPort.setText(monthCardBean.getCarportNum());
        tvStartTime.setText(monthCardBean.getCreateTime());
        tvStopTime.setText(monthCardBean.getCardTermofvalidity());

        NkCarAdapter adapter = new NkCarAdapter(monthCardBean.getCarNumbers());
        listView.setAdapter(adapter);
        adapter.setListViewHeight(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String cardId = monthCardBean.getCardId();
                carPlate = monthCardBean.getCarNumbers().get(position);
                int size = monthCardBean.getCarNumbers().size();
                showMenuDialog(R.layout.layout_dialog_menu, carPlate);
            }
        });

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
    }

    @OnClick({R.id.bt_change, R.id.bt_add, R.id.layout_xf})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_change:
                Intent intent = new Intent();
                intent.setClass(getActivity(),ChangeActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_add:
                int size = monthCardBean.getCarNumbers().size();
                if(size==2){
                    ToastUtil.showShort(getActivity(),"您最多可以绑定两辆年卡车");
                }else {
                    String cardId = monthCardBean.getCardId();
                    Intent intent2 = new Intent();
                    intent2.setClass(getActivity(), BdcpActivity2.class);
                    intent2.putExtra("symbol", "yearCard");
                    intent2.putExtra("yearCardCar", cardId);
                    intent2.putExtra("position",position);
                    startActivity(intent2);

                }
                break;
            case R.id.layout_xf:
                Intent intent0 = new Intent();
                intent0.setClass(getActivity(), KsczActivity.class);
                intent0.putExtra("symbol", "yearCard");
                intent0.putExtra("cardId", monthCardBean.getCardId());
                intent0.putExtra("renewalFee", String.valueOf(monthCardBean.getRenewalFee()));
                startActivityForResult(intent0, 1);
                Log.e("月卡ID",monthCardBean.getCardId()+"");
                break;
        }
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
        map.put("cardId", monthCardBean.getCardId());
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

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {

                }
            }
        }, map);
    }


    class NkCarAdapter extends BaseAdapter {

        private List<String> newList;

        public NkCarAdapter(List<String> newList) {
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
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_nkcarplate, parent, false);
                holder = new ViewHolder2();
                holder.tv = (TextView) convertView.findViewById(R.id.tv_carPlate);
                holder.layout = (LinearLayout) convertView.findViewById(R.id.layout_menu_carplate);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder2) convertView.getTag();
            }
            holder.tv.setText(newList.get(position));

            return convertView;
        }

        class ViewHolder2 {
            TextView tv;
            LinearLayout layout;
        }

        public void setListViewHeight(ListView listView) {
            NkCarAdapter adapter = (NkCarAdapter) listView.getAdapter();
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
}
