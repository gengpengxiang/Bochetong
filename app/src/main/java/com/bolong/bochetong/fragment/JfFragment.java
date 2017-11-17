package com.bolong.bochetong.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bolong.bochetong.activity.R;
import com.bolong.bochetong.bean.User;
import com.bolong.bochetong.bean2.MsgEvent;
import com.bolong.bochetong.bean2.ParkTimer;
import com.bolong.bochetong.utils.HttpUtils;
import com.bolong.bochetong.utils.Param;
import com.bolong.bochetong.utils.SharedPreferenceUtil;
import com.bolong.bochetong.view.CustomPopDialog;
import com.google.gson.Gson;

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


public class JfFragment extends Fragment {


    @BindView(R.id.tv_carplate)
    TextView tvCarplate;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_entertime)
    TextView tvEntertime;
    @BindView(R.id.switch_lock)
    ImageView switchLock;
    @BindView(R.id.activity_jf)
    PercentRelativeLayout activityJf;
    private Unbinder unbinder;

    private CustomPopDialog dialog;
    private CustomPopDialog.Builder dialogBuild;
    private Button btnConfirm;
    private Button btnCancel;
    private int ACTION_TIMERONE = 65648;
    private int ACTION_LOCKCAR_SUCCESS = 65649;
    private String carRecordId;
    private String locakar;
    private boolean isCkecked = true;
    private String status;
    private String statusOfLockCar;
    private ParkTimer.TimerBean mTimer;
    private int position;
    private int n=0;


    public JfFragment(ParkTimer.TimerBean mTimer,int position) {
        this.mTimer = mTimer;
        this.position = position;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_jf, container, false);
        unbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);

        setDatas();

        dialogBuild = new CustomPopDialog.Builder(getActivity());
        dialog = dialogBuild.create(R.layout.layout_dialog_lock);
        dialog.setCanceledOnTouchOutside(false);
        //dialog.show();

        btnConfirm = (Button) dialog.findViewById(R.id.btn_confirm);
        btnCancel = (Button) dialog.findViewById(R.id.btn_cancel);
        //解决dialog屏蔽返回键
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lockCar();
                dialog.dismiss();
            }
        });

        return view;
    }

    private void setDatas() {
        n++;
        Log.e("n的数值",n+"");
        tvCarplate.setText(mTimer.getCarCard());
        tvAddress.setText(mTimer.getParkName());
        //tvTime.setText(mTimer.getNowTime());
        tvPrice.setText(mTimer.getNowPrice());
        tvEntertime.setText("入场时间: " + mTimer.getEnterTimeStr());
        carRecordId = mTimer.getCarRecordId();
        locakar = mTimer.getLockcar();
        //计算停车时间

        long enterTime = Long.valueOf(mTimer.getEnterTime());
        //获取当前时间
        final long nowTime = Long.valueOf(mTimer.getNowTime());
        long diffTime = (nowTime - enterTime) / 60;
        long diffMinute = diffTime % 60;
        long diffHour = diffTime / (60) % 24;
        long diffDay = diffTime / (24 * 60);
        tvTime.setText(diffDay + "天" + diffHour + "小时" + diffMinute + "分钟");

        //timeThread.start();
        if (locakar.equals("0")) {
            isCkecked = false;
            switchLock.setImageResource(R.mipmap.icon_switch_normal);
            //Toast.makeText(getActivity(), "当前未锁车", Toast.LENGTH_SHORT).show();
        }
        if(locakar.equals("1")){
            isCkecked = true;
            switchLock.setImageResource(R.mipmap.icon_switch_checked);
            //Toast.makeText(getActivity(), "当前已锁车", Toast.LENGTH_SHORT).show();
        }
        if(n==1) {
            handler.postDelayed(runnable, 1000*60);//每两秒执行一次runnable.
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
        handler.removeCallbacks(runnable);
    }

    @OnClick(R.id.switch_lock)
    public void onViewClicked() {
        if (locakar.equals("0")) {
            dialog.show();
        } else {
            lockCar();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateUI(MsgEvent event) {
        if (event.getAction() == ACTION_TIMERONE) {
            n++;
            String content = event.getStr();
            Gson gson = new Gson();
            ParkTimer parkTimer = gson.fromJson(content, ParkTimer.class);
            List<ParkTimer.TimerBean> timers = parkTimer.getTimer();
            mTimer = timers.get(0);
            tvCarplate.setText(mTimer.getCarCard());
            tvAddress.setText(mTimer.getParkName());
            //tvTime.setText(mTimer.getNowTime());
            tvPrice.setText(mTimer.getNowPrice());
            tvEntertime.setText("入场时间: " + mTimer.getEnterTimeStr());
            carRecordId = mTimer.getCarRecordId();
            locakar = mTimer.getLockcar();
            //计算停车时间

            long enterTime = Long.valueOf(mTimer.getEnterTime());
            //获取当前时间
            final long nowTime = Long.valueOf(mTimer.getNowTime());
            long diffTime = (nowTime - enterTime) / 60;
            long diffMinute = diffTime % 60;
            long diffHour = diffTime / (60) % 24;
            long diffDay = diffTime / (24 * 60);
            tvTime.setText(diffDay + "天" + diffHour + "小时" + diffMinute + "分钟");

            //timeThread.start();
            if (locakar.equals("0")) {
                isCkecked = false;
                switchLock.setImageResource(R.mipmap.icon_switch_normal);
                //Toast.makeText(getActivity(), "当前未锁车", Toast.LENGTH_SHORT).show();
            }
            if(locakar.equals("1")){
                isCkecked = true;
                switchLock.setImageResource(R.mipmap.icon_switch_checked);
                //Toast.makeText(getActivity(), "当前已锁车", Toast.LENGTH_SHORT).show();
            }
            if(n==1) {
                handler.postDelayed(runnable, 1000*60);//每两秒执行一次runnable.
            }
        }
        if (event.getAction() == ACTION_LOCKCAR_SUCCESS) {
            String status = event.getStr();
            if(status.equals("1")){
                getParkTime();
            }

        }

    }

    private void getParkTime() {
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
        HttpUtils.post(Param.HOMETIMERS, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("计费Fragment", "failure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonDatas = response.body().string();
                Log.e("计费数据", jsonDatas);
                try {
                    JSONObject jsonObject = new JSONObject(jsonDatas);
                    String content = jsonObject.optString("content");
                    EventBus.getDefault().post(new MsgEvent(ACTION_TIMERONE, content));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, map);
    }

    private void lockCar() {
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
        map.put("carRecordId", carRecordId);
        HttpUtils.post(Param.LOCKCAR, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonDatas = response.body().string();
                Log.e("锁车",jsonDatas);
                try {
                    JSONObject jsonObject = new JSONObject(jsonDatas);
                    String content = jsonObject.optString("content");
                    JSONObject jb = new JSONObject(content);
                    String status = jb.optString("status");
                    EventBus.getDefault().post(new MsgEvent(ACTION_LOCKCAR_SUCCESS,status));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, map);
    }

    Handler handler=new Handler();
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            getParkTime();
            handler.postDelayed(this, 1000*60);
        }
    };

}
