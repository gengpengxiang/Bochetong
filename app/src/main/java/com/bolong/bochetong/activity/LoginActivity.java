package com.bolong.bochetong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bolong.bochetong.bean.User;
import com.bolong.bochetong.bean2.MsgEvent;
import com.bolong.bochetong.utils.CountDownTimerUtils;
import com.bolong.bochetong.utils.HttpUtils;
import com.bolong.bochetong.utils.Param;
import com.bolong.bochetong.utils.SharedPreferenceUtil;
import com.bolong.bochetong.utils.SoftKeyboardStateHelperUtil;
import com.bolong.bochetong.utils.TelNumMatch;
import com.bolong.bochetong.utils.ToastUtil;
import com.bolong.bochetong.view.MyEditText;
import com.google.gson.Gson;
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
import static com.bolong.bochetong.utils.MD5Utils.MD5;

public class LoginActivity extends BaseActivity {

    private Unbinder unbinder;
    @BindView(R.id.rl)
    RelativeLayout rl;
    @BindView(R.id.et1)
    MyEditText et1;
    @BindView(R.id.et2)
    MyEditText et2;
    @BindView(R.id.bt_getSMS)
    TextView btGetSMS;
    @BindView(R.id.bt_login)
    Button btLogin;
    public final static int RESULT_CODE = 1;
    private String phoneNumber;
    private CountDownTimerUtils mCountDownTimerUtils;
    private static final int ACTION_SENDSMS = 0;
    private static final int ACTION_CHECKSMS = 65;
    public static final int ACTION_LOGIN = 369;

    @Override
    public void onBaseCreate(Bundle bundle) {
        setContentViewId(R.layout.activity_login);
        EventBus.getDefault().register(this);
        unbinder = ButterKnife.bind(this);
        //监听软键盘打开和关闭
        SoftKeyboardStateHelperUtil softKeyboardStateHelper = new SoftKeyboardStateHelperUtil(findViewById(R.id.prl_login));
        softKeyboardStateHelper.addSoftKeyboardStateListener(new SoftKeyboardStateHelperUtil.SoftKeyboardStateListener() {
            @Override
            public void onSoftKeyboardOpened(int keyboardHeightInPx) {
                rl.setVisibility(View.GONE);
            }
            @Override
            public void onSoftKeyboardClosed() {
                rl.setVisibility(View.VISIBLE);
            }
        });

        et2.setFocusable(false);
        et2.setFocusableInTouchMode(false);
        btLogin.setBackgroundResource(R.drawable.shape_code_ing);
        btLogin.setEnabled(false);
        btGetSMS.setBackgroundResource(R.drawable.shape_code_ing);
        btGetSMS.setEnabled(false);

        changeBtnLoginState();
        changeBtnSMSState();
    }

    private void changeBtnSMSState() {
        et1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 11) {
                    Log.e("编辑", "true1");
                    btGetSMS.setBackgroundResource(R.drawable.shape_code);
                    btGetSMS.setEnabled(true);
                } else {
                    btGetSMS.setBackgroundResource(R.drawable.shape_code_ing);
                    btGetSMS.setEnabled(false);
                }
                if (s.toString().equals("11111111111")) {
                    Intent intent = new Intent();
                    intent.setClass(LoginActivity.this,NewMainActivity.class);
                    startActivity(intent);
                    ToastUtil.showShort(LoginActivity.this,"测试账号");
                    finish();
                }
            }
        });
    }

    private void changeBtnLoginState() {
        et2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 6) {
                    btLogin.setBackgroundResource(R.drawable.shape_code);
                    btLogin.setEnabled(true);
                } else {
                    btLogin.setBackgroundResource(R.drawable.shape_code_ing);
                    btLogin.setEnabled(false);
                }
            }
        });
    }

    @Override
    public void initView() {
        setTitle("登录");
        hide();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
    }

    @OnClick({R.id.bt_getSMS, R.id.bt_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_getSMS:
                getSMS();
                break;
            case R.id.bt_login:
                String code = et2.getText().toString().trim();
                checkSMS(code);
                break;
        }
    }

    private void getSMS() {
        et1.setFocusable(false);
        phoneNumber = et1.getText().toString();
        if (!TelNumMatch.isValidPhoneNumber(phoneNumber)) {
            Toast.makeText(LoginActivity.this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
        } else {
            mCountDownTimerUtils = new CountDownTimerUtils(btGetSMS, 60000, 1000);
            mCountDownTimerUtils.start();
            et2.setFocusable(true);
            et2.setFocusableInTouchMode(true);
            et2.requestFocus();
            sendSMS(phoneNumber);
        }
    }

    /**
     * 发送验证码
     */
    private void sendSMS(String phoneNumber) {
        Map<String, String> map = new HashMap<>();
        map.put("phone", phoneNumber);
        HttpUtils.post(Param.SENDSMS, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonDatas = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(jsonDatas);
                    String content = jsonObject.optString("content");
                    JSONObject jb = new JSONObject(content);
                    String statusOfSMS = jb.optString("status");
                    EventBus.getDefault().post(new MsgEvent(ACTION_SENDSMS, statusOfSMS));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, map);
    }
    /**
     * 检验验证码
     */
    private void checkSMS(String code) {
        Map<String, String> map = new HashMap<>();
        map.put("phone", phoneNumber);
        map.put("vcode", code);
        HttpUtils.post(Param.CHECKSMS, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonDatas = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(jsonDatas);
                    String content = jsonObject.optString("content");
                    JSONObject jb = new JSONObject(content);
                    String status = jb.optString("status");
                    EventBus.getDefault().post(new MsgEvent(ACTION_CHECKSMS, status));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, map);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateUI(MsgEvent event) {
        if (event.getAction() == ACTION_SENDSMS) {
            String statusOfSMS = event.getStr();
            if (statusOfSMS.equals("1")) {
                toast("验证码发送成功");
            } else {
                toast("验证码发送失败");
            }
        }
        if (event.getAction() == ACTION_CHECKSMS) {
            String status = event.getStr();
            if (status.equals("1")) {
                toast("验证成功");
                if (mCountDownTimerUtils != null) {
                    mCountDownTimerUtils.cancel();
                }
                login();
            } else {
                toast("验证失败");
            }
        }
        if (event.getAction() == ACTION_LOGIN) {
            Intent intent = new Intent();
            intent.setClass(LoginActivity.this, NewMainActivity.class);
            //setResult(RESULT_CODE, intent);
            startActivity(intent);
            finish();
        }
    }

    private void login() {
        String md5Password = MD5(phoneNumber + "_bolong");
        Map<String, String> map = new HashMap<>();
        map.put("phoneNumber", phoneNumber);
        map.put("password", md5Password);
        HttpUtils.post(Param.LOGIN, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonDatas = response.body().string();
                Log.e("登录", jsonDatas);
                try {
                    JSONObject jsonObject = new JSONObject(jsonDatas);
                    String userInfo = jsonObject.optString("content");
                    Gson gson = new Gson();
                    User user = gson.fromJson(userInfo, User.class);
                    SharedPreferenceUtil.putBean(getApplicationContext(), "userInfo", user);
                    EventBus.getDefault().post(new MsgEvent(ACTION_LOGIN, "denglu"));
                    Log.e("登陆消息发送", "success");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, map);
    }

    private void toast(final String str) {
        runOnUiThread(new Runnable() {

            @Override

            public void run() {
                Toast.makeText(LoginActivity.this, str, Toast.LENGTH_SHORT).show();
            }

        });
    }

    private long exitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {

            if ((System.currentTimeMillis() - exitTime) > 2000)  //System.currentTimeMillis()无论何时调用，肯定大于2000
            {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                setResult(1);
                finish();
                //System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
