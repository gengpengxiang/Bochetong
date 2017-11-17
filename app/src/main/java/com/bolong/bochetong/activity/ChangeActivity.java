package com.bolong.bochetong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.bolong.bochetong.bean.User;
import com.bolong.bochetong.bean2.MsgEvent;
import com.bolong.bochetong.utils.HttpUtils;
import com.bolong.bochetong.utils.Param;
import com.bolong.bochetong.utils.SharedPreferenceUtil;
import com.bolong.bochetong.utils.TelNumMatch;
import com.bolong.bochetong.utils.ToastUtil;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ChangeActivity extends BaseActivity {

    @BindView(R.id.tv_phoneNumber)
    TextView tvPhoneNumber;
    @BindView(R.id.et_change)
    EditText etChange;
    @BindView(R.id.bt_confirm)
    TextView btConfirm;
    private Unbinder unbind;
    private String userPhone;
    private int ACTION_EDITPHOPNE = 5546;
    public static final int ACTION_EDITPHONE_SUCCESS = 5445;
    @Override
    public void onBaseCreate(Bundle bundle) {
        setContentViewId(R.layout.activity_change);
        unbind = ButterKnife.bind(this);
        EventBus.getDefault().register(this);
    }

    @Override
    public void initView() {
        setTitle("更换手机号");
        if (SharedPreferenceUtil.getBean(getApplicationContext(), "userInfo") != null) {
            User user = (User) SharedPreferenceUtil.getBean(getApplicationContext(), "userInfo");
            userPhone = user.getUserPhone();
        }
        tvPhoneNumber.setText(getResources().getString(R.string.change_info)+userPhone);
        etChange.setText(userPhone);
        etChange.setSelection(etChange.getText().length());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        unbind.unbind();
    }

    @OnClick(R.id.bt_confirm)
    public void onViewClicked() {
        changePhone();
    }

    private void changePhone() {
        String newPhone = etChange.getText().toString();
        if (!TelNumMatch.isValidPhoneNumber(newPhone)) {
            Toast.makeText(ChangeActivity.this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
        }else {
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
            map.put("phone",newPhone);
            HttpUtils.post(Param.EDITPHONE, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String jsonDatas = response.body().string();
                    Log.e("修改手机号", jsonDatas);

                    try {
                        JSONObject jsonObject = new JSONObject(jsonDatas);
                        String errCode = jsonObject.optString("errCode");
                        String errMessage = jsonObject.optString("errMessage");
                        if(errCode.equals("1000")){
                            String content = jsonObject.optString("content");
                            JSONObject jb = new JSONObject(content);
                            String status = jb.optString("status");
                            EventBus.getDefault().post(new MsgEvent(ACTION_EDITPHOPNE,status,errMessage));
                        }else {

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, map);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateUI(MsgEvent event) {
        if(event.getAction() == ACTION_EDITPHOPNE){
            String status = event.getStr();
            if(status.equals("1")){
                Intent intent = new Intent();
                intent.setClass(ChangeActivity.this,LoginActivity.class);
                startActivity(intent);
                EventBus.getDefault().post(new MsgEvent(ACTION_EDITPHONE_SUCCESS));
                SharedPreferenceUtil.removeString("userInfo");
                finish();
            }
            if(status.equals("0")){
                String errMessage = event.getStr2();
                ToastUtil.showShort(ChangeActivity.this,errMessage);
            }
        }
    }
}
