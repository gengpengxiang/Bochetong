package com.bolong.bochetong.activity;

import android.content.Context;
import android.content.Intent;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bolong.bochetong.bean.CarPlate;
import com.bolong.bochetong.bean.CarPlateDao;
import com.bolong.bochetong.bean.User;
import com.bolong.bochetong.utils.HttpUtils;
import com.bolong.bochetong.utils.JsonValidatorUtils;
import com.bolong.bochetong.utils.ListDataSave;
import com.bolong.bochetong.utils.NetworkAvailableUtils;
import com.bolong.bochetong.utils.Param;
import com.bolong.bochetong.utils.SharedPreferenceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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


public class BdcpActivity2 extends BaseActivity implements View.OnFocusChangeListener, View.OnTouchListener {
    private Unbinder unbinder;
    @BindView(R.id.edit_province)
    EditText editProvince;
    @BindView(R.id.edit_1)
    EditText edit1;
    @BindView(R.id.edit_2)
    EditText edit2;
    @BindView(R.id.edit_3)
    EditText edit3;
    @BindView(R.id.edit_4)
    EditText edit4;
    @BindView(R.id.edit_5)
    EditText edit5;
    @BindView(R.id.edit_6)
    EditText edit6;
    @BindView(R.id.bt_bind)
    Button btBind;
    @BindView(R.id.keyboard_view)
    KeyboardView keyboardView;

    String license = "";


    private EditText[] mArray;
    private Context mContext;
    private KeyboardView mKeyboardView;
    /**
     * 省份简称键盘
     */
    private Keyboard province_keyboard;
    /**
     * 数字与大写字母键盘
     */
    private Keyboard number_keyboar;

    /**
     * 判定是否是中文的正则表达式 [\\u4e00-\\u9fa5]判断一个中文 [\\u4e00-\\u9fa5]+多个中文
     * //
     */
//    private String reg = "[\\u4e00-\\u9fa5]";
    private EditText currentEdit;
    private int currentIndex;
    //保存车牌相关
    private CarPlateDao mCardPlateDao;
    private CarPlate carPlate;

    @Override
    public void onBaseCreate(Bundle bundle) {
        mCardPlateDao = MyApplication.getInstance().getDaoSession().getCarPlateDao();
        setContentViewId(R.layout.activity_bdcp2);

        unbinder = ButterKnife.bind(this);

        btBind.setEnabled(false);
        //add


        mContext = this;

        //dataSave = new ListDataSave(getApplicationContext(), "baiyu");
        province_keyboard = new Keyboard(mContext, R.xml.province_abbreviation);
        number_keyboar = new Keyboard(mContext, R.xml.number_or_letters);
        mKeyboardView = (KeyboardView)
                findViewById(R.id.keyboard_view);
        mKeyboardView.setKeyboard(province_keyboard);
        mKeyboardView.setEnabled(true);
        mKeyboardView.setPreviewEnabled(false);
        mKeyboardView.setOnKeyboardActionListener(listener);
        editProvince = (EditText) findViewById(R.id.edit_province);
        edit1 = (EditText) findViewById(R.id.edit_1);
        edit2 = (EditText) findViewById(R.id.edit_2);
        edit3 = (EditText) findViewById(R.id.edit_3);
        edit4 = (EditText) findViewById(R.id.edit_4);
        edit5 = (EditText) findViewById(R.id.edit_5);
        edit6 = (EditText) findViewById(R.id.edit_6);
        mArray = new EditText[]{editProvince, edit1,
                edit2, edit3, edit4, edit5, edit6};

        editProvince.setOnFocusChangeListener(this);
        edit1.setOnFocusChangeListener(this);
        edit2.setOnFocusChangeListener(this);
        edit3.setOnFocusChangeListener(this);
        edit4.setOnFocusChangeListener(this);
        edit5.setOnFocusChangeListener(this);
        edit6.setOnFocusChangeListener(this);

        editProvince.setOnTouchListener(this);
        edit1.setOnTouchListener(this);
        edit2.setOnTouchListener(this);
        edit3.setOnTouchListener(this);
        edit4.setOnTouchListener(this);
        edit5.setOnTouchListener(this);
        edit6.setOnTouchListener(this);
        //add


        for (int i = 0; i < mArray.length; i++) {
            final int j = i;
            mArray[j].addTextChangedListener(new TextWatcher() {

                private CharSequence temp;
                private int sStart;
                private int sEnd;

                @Override
                public void onTextChanged(CharSequence s, int start,
                                          int before, int count) {
                    temp = s;
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start,
                                              int count, int after) {


                }

                @Override
                public void afterTextChanged(Editable s) {
                    sStart = mArray[j].getSelectionStart();
                    sEnd = mArray[j].getSelectionEnd();
                    //焦点后移
                    if (temp.length() == 1 && j < mArray.length - 1) {
                        mArray[j + 1].setFocusable(true);
                        mArray[j + 1].setFocusableInTouchMode(true);
                        mArray[j + 1].requestFocus();

                    }
                    //新内容替换旧内容
                    if (temp.length() > 1) {
                        s.delete(0, 1);
                        int tSelection = sStart;
                        mArray[j].setText(s);
                        mArray[j].setSelection(tSelection);
                        mArray[j].setFocusable(true);

                    }

                    if (editProvince.getText().length() == 1 && edit1.getText().length() == 1 && edit2.getText().length() == 1 && edit3.getText().length() == 1
                            && edit4.getText().length() == 1 && edit5.getText().length() == 1 && edit6.getText().length() == 1) {
                        btBind.setEnabled(true);
                       /* for (int i = 0; i < mArray.length; i++) {
                            license += mArray[i].getText().toString();
                        }*/
                        license = editProvince.getText().toString() + "-" + edit1.getText().toString() + edit2.getText().toString() + edit3.getText().toString()
                                + edit4.getText().toString() + edit5.getText().toString() + edit6.getText().toString();

                    } else {
                        btBind.setEnabled(false);
                    }


                }

            });


        }


        editProvince.setFocusable(true);
        editProvince.requestFocus();
    }

    @Override
    public void initView() {
        setTitle("绑定车牌");
    }


    public final static int RESULT_CODE = 1;

    @OnClick(R.id.bt_bind)
    public void onViewClicked() {

        String s = license.replaceAll("-", "");
        //Toast.makeText(BdcpActivity2.this, s, Toast.LENGTH_SHORT).show();
        postRequest();
        /*if (NetworkAvailableUtils.isNetworkAvailable(getApplicationContext())) {
            postRequest();
        } else {
            Toast.makeText(BdcpActivity2.this, "无网络连接", Toast.LENGTH_SHORT).show();
        }*/


//        Intent intent = new Intent();
//        setResult(RESULT_CODE,intent);
//        finish();
//        //保存绑定车牌数据
////        carPlate = new CarPlate(license);
////        mCardPlateDao.insert(carPlate);
        setResult(1);
    }

    private void postRequest() {

        String uid = null;
        String token = null;
        if (SharedPreferenceUtil.getBean(getApplicationContext(),"userInfo") != null){
            User user = (User)SharedPreferenceUtil.getBean(getApplicationContext(), "userInfo");
            uid = user.getUserId();
            token = user.getToken();
        }else {
            uid = Param.UID;
            token = Param.TOKEN;
        }


        final String s = license.replaceAll("-", "");

        Map<String, String> map = new HashMap<>();
        map.put("uid", uid);
        map.put("token", token);
        map.put("carCard", s);
        HttpUtils.post(Param.ADDCARCARD, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(BdcpActivity2.this, "绑定失败", Toast.LENGTH_SHORT).show();
                    }
                });
                Log.e("onFailure", "onFailure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonDatas = response.body().string();
                Log.e("绑定车牌", jsonDatas);
                try {
                    try {
                        JSONObject jsonObject = new JSONObject(jsonDatas);
                        String content = jsonObject.optString("content");
                        JSONObject jb = new JSONObject(content);
                        String status = jb.optString("status");
                        Log.e("Status", status + "===" + s);
                        if (status.equals("1")) {
                            carPlate = new CarPlate(license);
                            mCardPlateDao.insert(carPlate);

                            Intent intent = new Intent();
                            setResult(RESULT_CODE, intent);
                            finish();

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(BdcpActivity2.this, "绑定成功", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(BdcpActivity2.this, "绑定失败", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(BdcpActivity2.this, "绑定失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (Exception e) {
                    Toast.makeText(BdcpActivity2.this, "服务器异常", Toast.LENGTH_SHORT).show();
                }
            }


        }, map);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    //add
    private KeyboardView.OnKeyboardActionListener listener = new KeyboardView.OnKeyboardActionListener() {

        @Override
        public void swipeUp() {
        }

        @Override
        public void swipeRight() {
        }

        @Override
        public void swipeLeft() {
        }

        @Override
        public void swipeDown() {
        }

        @Override
        public void onText(CharSequence text) {
        }

        @Override
        public void onRelease(int primaryCode) {
        }

        @Override
        public void onPress(int primaryCode) {
        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            Editable editable = currentEdit.getText();
            int start = currentEdit.getSelectionStart();
            if (primaryCode == -3) {// 回退

                if (editable.length() == 0) {
                    if (currentIndex > 0) {
                        currentIndex = currentIndex - 1;
                        mArray[currentIndex].setFocusable(true);
                        mArray[currentIndex].requestFocus();
                    }
                }
                if (editable.length() == 1) {
                    editable.clear();
                }

            } else {
                editable.insert(start, Character.toString((char) primaryCode));
            }


        }
    };


    //指定切换软键盘 isnumber false表示要切换为省份简称软键盘 true表示要切换为数字软键盘


    public void changeKeyboard(boolean isnumber) {
        if (isnumber) {
            mKeyboardView.setKeyboard(number_keyboar);
        } else {
            mKeyboardView.setKeyboard(province_keyboard);
        }
    }


    //软键盘展示

    public void showKeyboard() {
        int visibility = mKeyboardView.getVisibility();
        if (visibility == View.GONE || visibility == View.INVISIBLE) {
            mKeyboardView.setVisibility(View.VISIBLE);
        }
    }


    //软键盘隐藏

    public void hideKeyboard() {
        int visibility = mKeyboardView.getVisibility();
        if (visibility == View.VISIBLE) {
            mKeyboardView.setVisibility(View.INVISIBLE);
        }
    }

    //禁掉系统软键盘
    public void hideSoftInputMethod() {

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        int currentVersion = Build.VERSION.SDK_INT;
        String methodName = null;
        if (currentVersion >= 16) {
            // 4.2
            methodName = "setShowSoftInputOnFocus";
        } else if (currentVersion >= 14) {
            // 4.0
            methodName = "setSoftInputShownOnFocus";
        }
        if (methodName == null) {
            currentEdit.setInputType(InputType.TYPE_NULL);
        } else {
            Class<EditText> cls = EditText.class;
            Method setShowSoftInputOnFocus;
            try {
                setShowSoftInputOnFocus = cls.getMethod(methodName,
                        boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(currentEdit, false);
            } catch (NoSuchMethodException e) {
                currentEdit.setInputType(InputType.TYPE_NULL);
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    //软键盘展示状态
    public boolean isShow() {
        return mKeyboardView.getVisibility() == View.VISIBLE;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isShow()) {
                hideKeyboard();
            } else {
                finish();
            }
        }
        return false;
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        if (hasFocus) {

            currentEdit = (EditText) v;

            for (int i = 0; i < mArray.length; i++) {

                EditText temp = mArray[i];
                if (temp == (EditText) v) {
                    currentIndex = i;
                }
            }
//            Toast.makeText(mContext, "currentIndex" + currentIndex, Toast.LENGTH_SHORT).show();
//            Log.i("Tag1111", "onFocusChange currentIndex= " + currentIndex);
            if (currentIndex == 0) {
                changeKeyboard(false);
            } else {
                changeKeyboard(true);
            }
            hideSoftInputMethod();

            if (!isShow()) {
                showKeyboard();
            }


        }


    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {


        return super.onTouchEvent(event);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        //add
        EditText currentEdit2 = (EditText) view;
        Editable s = currentEdit2.getText();
        s.clear();
        if (!isShow()) {
            showKeyboard();

        }
        return false;

    }

}
