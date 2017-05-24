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
import com.bolong.bochetong.utils.CountDownTimerUtils;
import com.bolong.bochetong.utils.HttpUtils;
import com.bolong.bochetong.utils.Param;
import com.bolong.bochetong.utils.SharedPreferenceUtil;
import com.bolong.bochetong.utils.SoftKeyboardStateHelperUtil;
import com.bolong.bochetong.utils.TelNumMatch;
import com.bolong.bochetong.view.MyEditText;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.smssdk.EventHandler;
import cn.smssdk.OnSendMessageHandler;
import cn.smssdk.SMSSDK;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.mob.tools.utils.Data.MD5;

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
    private String phone;
    private String code;
    private String uid;
    EventHandler eh = new EventHandler() {
        @Override
        public void afterEvent(int event, int result, Object data) {

            switch (event) {

                case SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE:

                    if (result == SMSSDK.RESULT_COMPLETE) {

                 //       toast("验证成功");
                        SharedPreferenceUtil.putString("phone", phone);
                        //add
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (mCountDownTimerUtils != null) {
                                    mCountDownTimerUtils.cancel();
                                }
                            }
                        });

                        //add
                        String md5Password = MD5(phone + "_bolong");
                        Map<String, String> map = new HashMap<>();
                        map.put("phoneNumber", phone);
                        map.put("password", md5Password);
                        HttpUtils.post(Param.USERLOGIN, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
//                                            Intent intent = new Intent();
//                                            setResult(RESULT_CODE, intent);
                                        finish();
                                        Toast.makeText(LoginActivity.this, "登录失败了", Toast.LENGTH_SHORT).show();
                                        Log.e("访问服务器失败", "访问服务器失败");
                                    }
                                });
                                Log.e("onFailure", "failure");
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {

                                String jsonDatas = response.body().string();
                                Log.e("onSuccess", jsonDatas);
                                try {
                                    try {
                                        JSONObject jsonObject = new JSONObject(jsonDatas);
                                        String userInfo = jsonObject.optString("content");
                                        Log.e("userInfo", userInfo);
                                        Gson gson = new Gson();
                                        User user = gson.fromJson(userInfo, User.class);
                                        Log.e("user.info", user.getUserPhone() + "");
                                        SharedPreferenceUtil.putBean(getApplicationContext(), "userInfo", user);
                                        Log.e("已保存的用户数据", SharedPreferenceUtil.getBean(getApplicationContext(), "userInfo").toString());

                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Intent intent = new Intent();
                                                setResult(RESULT_CODE, intent);
                                                finish();
                                                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
//                                            Intent intent = new Intent();
//                                            setResult(RESULT_CODE, intent);
                                                finish();
                                                //Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                                                Log.e("Log登录失败", "登录失败了");
                                            }
                                        });
                                    }
                                } catch (Exception e) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
//                                            Intent intent = new Intent();
//                                            setResult(RESULT_CODE, intent);
                                            //finish();
                                            //Toast.makeText(LoginActivity.this, "登录失败了", Toast.LENGTH_SHORT).show();
                                            Log.e("Log登录失败", "登录失败了哦");
                                        }
                                    });
                                }


                            }
                        }, map);

                    } else {

                     //   toast("验证失败");
                        finish();

                    }

                    break;

                case SMSSDK.EVENT_GET_VERIFICATION_CODE:

                    if (result == SMSSDK.RESULT_COMPLETE) {

                        toast("获取验证码成功");


                    } else {

                        toast("获取验证码失败");

                    }

                    break;

            }

        }
    };
    private CountDownTimerUtils mCountDownTimerUtils;

    @Override
    public void onBaseCreate(Bundle bundle) {
        setContentViewId(R.layout.activity_login);

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

        SMSSDK.initSDK(this, "1d5d780524030", "f84410a63a59aa5ca6b345f6f386b043");
        SMSSDK.registerEventHandler(eh);//注册短信回调

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
                    btGetSMS.setBackgroundResource(R.drawable.shape_code);
                    btGetSMS.setEnabled(true);
                } else {

                    btGetSMS.setBackgroundResource(R.drawable.shape_code_ing);
                    btGetSMS.setEnabled(false);
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
                if (s.length() == 4) {
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
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eh);
        unbinder.unbind();
    }


    @OnClick({R.id.bt_getSMS, R.id.bt_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_getSMS:
                getSMS();
                break;
            case R.id.bt_login:
                login();
                break;
        }
    }

    private void getSMS() {
        et1.setFocusable(false);
        String userPhoneNumber = et1.getText().toString();
        if (!TelNumMatch.isValidPhoneNumber(userPhoneNumber)) {
            Toast.makeText(LoginActivity.this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
        } else {
            mCountDownTimerUtils = new CountDownTimerUtils(btGetSMS, 60000, 1000);

            mCountDownTimerUtils.start();

            et2.setFocusable(true);
            et2.setFocusableInTouchMode(true);
            et2.requestFocus();

            phone = et1.getText().toString();
            SMSSDK.getVerificationCode("86", phone, new OnSendMessageHandler() {
                @Override
                public boolean onSendMessage(String s, String s1) {
                    return false;
                }
            });
        }


    }

    private void login() {

        String code = et2.getText().toString().trim();

        String phone = et1.getText().toString();

        if (code.length() == 4) {
            SMSSDK.submitVerificationCode("86", phone, code);
            //服务器交互
            //postRequest(phone, uid);

        }


//        Intent intent = new Intent();
//        setResult(RESULT_CODE, intent);
//        finish();
    }


    private void toast(final String str) {

        runOnUiThread(new Runnable() {

            @Override

            public void run() {

                Toast.makeText(LoginActivity.this, str, Toast.LENGTH_SHORT).show();

            }

        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            setResult(RESULT_CODE, intent);
            finish();
        }
        return true;
    }
}
